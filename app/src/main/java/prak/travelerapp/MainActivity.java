package prak.travelerapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import prak.travelerapp.Autocompleter.CityAutocompleteActivity;
import prak.travelerapp.Autocompleter.database.CityDBAdapter;
import prak.travelerapp.Database.Datasource;
import prak.travelerapp.Database.ItemViewActivity;
import prak.travelerapp.PlaceApi.AutocompleteActivity;
import prak.travelerapp.PlaceApi.PlacePickerFragment;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Datasource dataSource;
    private Button newTrip;     // startet neue Reise
    private Button itemList; // startet die Packliste
    private Button autocomplete; //wechselt zur testview für die autocompletion
    private Intent listIntent,PlaceActivityIntent,autocompleteIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Neue Schriftart für alle Texte
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Avenir-Book.ttf");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //googlePlacesTest();
        //weatherAPItest();

        //Wechselt zur View der Packliste
        changeToItemList();

        changeToAutoComplete();
    }

    private void changeToItemList() {

        itemList = (Button) findViewById(R.id.itemList);
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listIntent = new Intent(MainActivity.this, ItemViewActivity.class);
                Log.d("MainActivity", "Click started item list");

                startActivity(listIntent);
            }
        });
    }

    private void changeToAutoComplete(){

        autocomplete = (Button) findViewById(R.id.button_autocomplete);
        autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                autocompleteIntent = new Intent(MainActivity.this, CityAutocompleteActivity.class);
                Log.d("MainActivity", "Click started autocomplete view");

                startActivity(autocompleteIntent);
            }
        });


    }

    private void googlePlacesTest(){
        //request latest google play services update
        int googleServiceStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, googleServiceStatus, 10);
        if(dialog!=null){
            dialog.show();
        }
        prepareViews();
        prepareListeners();
        setUpFragment();
    }

    private void prepareListeners() {
        newTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Click started new trip");
                PlaceActivityIntent = new Intent(MainActivity.this,AutocompleteActivity.class);
                startActivity(PlaceActivityIntent);
            }
        });
    }

    private void prepareViews() {
        newTrip = (Button) findViewById(R.id.newTrip);
    }

    private void setUpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PlacePickerFragment fragment = new PlacePickerFragment();
        fragmentTransaction.add(R.id.ll, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void weatherAPItest(){
        boolean isOnline = Utils.isOnline(this);
        if(isOnline) {

            WeatherTask weathertask = new WeatherTask();
            weathertask.delegate = this;
            weathertask.execute(new String[]{"Berlin"});

        }else{
            //TODO tell user to get internet connection
        }
    }

    @Override
    public void weatherProcessFinish(Weather output) {
        Log.d("dcw", output.toString());
    }

    @Override
    public void weatherProcessFailed() {
        Log.d("ERROR", "WeatherAPIcall failed");
        //TODO -> handle exception
    }
}
