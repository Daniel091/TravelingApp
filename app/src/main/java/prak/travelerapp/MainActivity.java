package prak.travelerapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;

import org.joda.time.DateTime;

import java.util.Date;

import prak.travelerapp.PlaceApi.PlacePickerFragment;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.TripItems;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MenueApdapter menueApdapter;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] menue_links;
    private TripDBAdapter tripDBAdapter;
    private Trip active_trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Utils.overrideFont(getApplicationContext(), "SERIF", "fonts/Avenir-Book.ttf");
        prepareViews();
        menueApdapter = new MenueApdapter(this);
        listView.setAdapter(menueApdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //testTripDB();
    }

    @Override
    public void onStart() {
        super.onStart();
        active_trip = checkActiveTrip();

        if (active_trip != null) {
            Fragment fragment = new LandingFragment();
            setUpFragment(fragment);
        } else {
            Fragment fragment = new StartFragment();
            setUpFragment(fragment);
        }
    }

    public Trip checkActiveTrip(){
        TripDBAdapter tripDBAdapter = new TripDBAdapter(this);
        tripDBAdapter.open();
        active_trip = tripDBAdapter.getActiveTrip();

        if (active_trip != null) {
            active_trip.getEnddate().isBeforeNow();
            Toast.makeText(this, "Aktive Reise is beendet", Toast.LENGTH_LONG).show();
            tripDBAdapter.removeActiveFromTrip();
            return null;
        }
        return active_trip;
    }

    private void prepareViews() {
        menue_links = getResources().getStringArray(R.array.menue_links);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);

        // Prepare youre views
    }

    public void openDrawer(){
        drawerLayout.openDrawer(listView);
    }

    public void closeDrawer(){
        drawerLayout.closeDrawer(listView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG,"Selected: " + menue_links[position] + " at " + position);
        Fragment fragment;
        switch(position) {
            case 1:
                if (active_trip != null) {
                    fragment = new LandingFragment();
                    setUpFragment(fragment);
                } else {
                    fragment = new StartFragment();
                    setUpFragment(fragment);
                }
                break;
            case 2:
                if (active_trip != null) {
                    fragment = new ItemViewFragment();
                    setUpFragment(fragment);
                } else {
                    Toast.makeText(this, "Du besitzt keine Aktive Reise", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                fragment = new TripHistoryFragment();
                setUpFragment(fragment);
                break;
            case 5:
                fragment = new PlacePickerFragment();
                setUpFragment(fragment);
                break;
        }

        // Highlight the selected items, update the title, and close the drawer
        listView.setItemChecked(position, true);
        closeDrawer();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void setUpFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();

        String frag_name = fragment.getClass().getSimpleName();
        if (frag_name.equals("NewTripFragment")){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragment.getClass().getName(), 0);
        Log.d("Main",fragment.getClass().getName() + " is in BackStack " + fragmentPopped);
        if (!fragmentPopped) {
            Log.d("Main","New Add to Back Stack");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContent, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.commit();
        }
    }

    public void testTripDB(){

        String s = "(3,0);(4,0)";
        TripItems items = new TripItems(s);
        DateTime startDate = new DateTime(2016,01,12,0,0);
        DateTime endDate = new DateTime(2016,01,15,0,0);


        tripDBAdapter = new TripDBAdapter(this);
        tripDBAdapter.open();

        tripDBAdapter.insertTrip(items, "Berlin", "DE" ,startDate,endDate, TravelType.WANDERN, TravelType.SKIFAHREN, false);
        Trip activeTrip = tripDBAdapter.getActiveTrip();
        System.out.println(activeTrip.getCity());
    }
}