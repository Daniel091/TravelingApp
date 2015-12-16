package prak.travelerapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import prak.travelerapp.PlaceApi.PlacePickerFragment;
import prak.travelerapp.TripDatabase.TripDBAsyncTask;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MenueApdapter menueApdapter;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] menue_links;

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

        // Start with Home Screen
        Fragment fragment = new StartFragment();
        setUpFragement(fragment);
        listView.setItemChecked(1, true);


        testTripDB();
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
                fragment = new StartFragment();
                setUpFragement(fragment);
                break;
            case 2:
                fragment = new ItemViewActivity();
                setUpFragement(fragment);
                break;
            case 3:
                fragment = new NewTripFragment();
                setUpFragement(fragment);
                break;
            case 6:
                fragment = new PlacePickerFragment();
                setUpFragement(fragment);
                break;
            case 7:
                fragment = new LandingFragment();
                setUpFragement(fragment);
                break;
            case 8:
                fragment = new CityAutocompleteFragment();
                setUpFragement(fragment);
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

    public void setUpFragement(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContent, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public void testTripDB(){
         new TripDBAsyncTask(this.getApplicationContext()).execute();
    }
}