package prak.travelerapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import prak.travelerapp.Database.Datasource;
import prak.travelerapp.Database.ItemListActivity;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private Button newTrip;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Datasource dataSource;
    private Button itemList; // startet die Packliste
    private Intent listIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googlePlacesTest();
        //weatherAPItest();

        //Wechselt zur View der Packliste
        changeToItemList();
    }

    // Wechselt in ItemListActivity
    private void changeToItemList() {

        itemList = (Button) findViewById(R.id.itemList);
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listIntent = new Intent(MainActivity.this, ItemListActivity.class);
                Log.d("MainActivity", "Koffer packen wurde gedrückt...");

                startActivity(listIntent);
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
