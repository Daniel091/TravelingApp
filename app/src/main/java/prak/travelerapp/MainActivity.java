package prak.travelerapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import prak.travelerapp.PlaceApi.PlacePickerFragment;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] menue_links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.overrideFont(getApplicationContext(), "SERIF", "fonts/Avenir-Book.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareViews();
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menue_links));
        listView.setOnItemClickListener(this);

        // Start with Home Screen
        Fragment fragment = new MainFragment();
        setUpFragement(fragment);
        listView.setItemChecked(1, true);
        setTitle(menue_links[0]);
    }

    private void prepareViews() {
        menue_links = getResources().getStringArray(R.array.menue_links);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);

        // Prepare youre views
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG,"Selected: " + menue_links[position]);
        Fragment fragment;
        switch(position) {
            case 0:
                fragment = new MainFragment();
                setUpFragement(fragment);
                break;
            case 1:
                fragment = new PlacePickerFragment();
                setUpFragement(fragment);
                break;
            case 2:
                Toast.makeText(getBaseContext(), menue_links[position] + " To implement as Fragment",Toast.LENGTH_SHORT).show();
                // Add here Flicker Test Fragment
                break;
            case 3:
                Toast.makeText(getBaseContext(), menue_links[position] + " To implement as Fragment",Toast.LENGTH_SHORT).show();
                // Add here Autocompleter Test Fragment
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        listView.setItemChecked(position, true);
        setTitle(menue_links[position]);
        drawerLayout.closeDrawer(listView);
    }

    private void setUpFragement(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.commit();
    }
}
