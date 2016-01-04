package prak.travelerapp;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import prak.travelerapp.Notifications.NotificationReceiver;
import prak.travelerapp.PlaceApi.PlacePickerFragment;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.Tupel;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MenueApdapter menueApdapter;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] menue_links;
    private TripDBAdapter tripDBAdapter;
    private Trip active_trip;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Utils.overrideFont(getApplicationContext(), "SERIF", "fonts/Avenir-Book.ttf");
        prepareViews();

        active_trip = checkActiveTrip();

        menueApdapter = new MenueApdapter(this);
        if(active_trip != null){
            updateMenueRemainingItems(active_trip);
        }
        listView.setAdapter(menueApdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.performItemClick(listView.getChildAt(1), 1, listView.getItemIdAtPosition(1));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (active_trip != null) {
            setUpNotificationService(active_trip.getStartdate());
        }
    }

    public Trip checkActiveTrip(){
        TripDBAdapter tripDBAdapter = new TripDBAdapter(this);
        tripDBAdapter.open();
        active_trip = tripDBAdapter.getActiveTrip();


        if (active_trip != null) {
            boolean tripIsOver = active_trip.getEnddate().isBeforeNow();
            if(tripIsOver){
                Toast.makeText(this, "Aktive Reise is beendet", Toast.LENGTH_LONG).show();
                tripDBAdapter.setAllTripsInactive();
                // TODO Reset Shared Notifications Shared Preferences
                return null;
            }
        }
        return active_trip;
    }

    private void prepareViews() {
        menue_links = getResources().getStringArray(R.array.menue_links);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);

        // Prepare youre views
    }

    public void setUpMenu(int checkedItem){
        menue_links = getResources().getStringArray(R.array.menue_links);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        menueApdapter = new MenueApdapter(this);
        if(active_trip != null){
            updateMenueRemainingItems(active_trip);
        }
        listView.setAdapter(menueApdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.performItemClick(listView.getChildAt(checkedItem), checkedItem, listView.getItemIdAtPosition(checkedItem));
    }

    public void openDrawer(){
        drawerLayout.openDrawer(listView);
    }

    public void closeDrawer(){
        drawerLayout.closeDrawer(listView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment;
        switch(position) {
            case 1:
                if (active_trip != null) {
                    fragment = new LandingFragment();
                    setUpFragment(fragment,true);
                } else {
                    fragment = new StartFragment();
                    setUpFragment(fragment,true);
                }
                break;
            case 2:
                if (active_trip != null) {
                    fragment = new ItemViewFragment();
                    setUpFragment(fragment,true);
                } else {
                    Toast.makeText(this, "Du besitzt keine Aktive Reise", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                fragment = new TripHistoryFragment();
                setUpFragment(fragment,true);
                break;
            case 4:
                fragment = new SettingsFragment();
                setUpFragment(fragment,true);
                break;
        }
        closeDrawer();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
            String fragmentTag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2).getName();
            System.out.println(fragmentTag);
            switch (fragmentTag){
                case "LandingFragment":
                    setUpMenu(1);
                    break;
                case "ItemViewFragment":
                    setUpMenu(2);
                    break;
                case "TripHistoryFragment":
                    setUpMenu(3);
                    break;
                case "TripHistoryListFragment":
                    setUpMenu(3);
                    break;
                case "SettingsFragment":
                    setUpMenu(4);
            }
        } else {
            super.onBackPressed();
        }
    }

    public void setUpFragment(Fragment fragment,boolean takeFromBackstack) {
        FragmentManager fragmentManager = getFragmentManager();

        String frag_name = fragment.getClass().getSimpleName();
        if (frag_name.equals("NewTripFragment")){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        boolean isFragmentInStack = isFragmentInBackstack(fragmentManager,fragment.getClass().getSimpleName());

        if (isFragmentInStack && takeFromBackstack){
            //Log.d("Main", "Load Fragment with Tag " + fragment.getClass().getSimpleName() + " from Backstack");
            fragmentManager.popBackStackImmediate(fragment.getClass().getSimpleName(), 0);
        } else {
            //Log.d("Main", "Load Fragment" + fragment.getClass().getSimpleName() + " not from Backstack" );
            fragmentTransaction.replace(R.id.mainContent, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }

    }

    public static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return true;
            }
        }
        return false;
    }

    public void clearBackstack(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Trip getActive_trip() {
        return active_trip;
    }

    public void menueClick(int pos) {
        if (listView != null){
            listView.performItemClick(listView.getAdapter().getView(pos, null, null), pos, listView.getAdapter().getItemId(pos));
            listView.clearChoices();
            menueApdapter.notifyDataSetChanged();
        }
    }


    //Update die anzahl der verbleibenden Items im Menü
    public void updateMenueRemainingItems(Trip trip){
        int remainingItems = 0;
        for (Tupel t : trip.getTripItems().getItems()) {
            if (t.getY() == 0) {
                remainingItems++;
            }
        }
        menueApdapter.setRemainingItems(remainingItems);
    }

    public void resetRemainingItems(){
        menueApdapter.setRemainingItems(0);
    }

    public void setUpNotificationService(DateTime date) {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean push_enabled = sharedPref.getBoolean(String.valueOf(R.bool.push_notifications), true);
        boolean day_before_pushed = sharedPref.getBoolean(String.valueOf(R.bool.day_before_notification),false);

        date = date.minusDays(1);
        date = date.plusHours(13);
        date = date.plusMinutes(50);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");

        Log.d("Main","Push: " + push_enabled +" ,"+ " day before pushed " + day_before_pushed);
        Log.d("Main", " Push Date: " + date.toString(fmt) + " " + active_trip.getCity() + " start date: " + active_trip.getStartdate().toString(fmt));
        if (push_enabled){
            if (!day_before_pushed){
                Intent myIntent = new Intent(MainActivity.this, NotificationReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //alarmManager.cancel(pendingIntent);
                alarmManager.set(AlarmManager.RTC, date.getMillis(), pendingIntent);
            }
        }
    }
}