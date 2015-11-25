package prak.travelerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import prak.travelerapp.Database.Dataset;
import prak.travelerapp.Database.Datasource;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Datasource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("dsd", "dchih");
        Log.d("dsd","dchdjskdskjih");

        //WeatherAPI weather = new WeatherAPI();
        //weather.execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=ibiza&cnt=3&appid=5883d3f233f058145feca6dd84ec76f9");

        WeatherTask weathertask = new WeatherTask();
        weathertask.delegate = this;
        weathertask.execute(new String[]{"London,UK"});

        // Probeweise Erstellung eines Test Datasets

        Dataset testSet = new Dataset(0, "Testitem", 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        Log.d(LOG_TAG, "Inhalt des Testsets: " + testSet.toString());

        dataSource = new Datasource(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();

    }

    @Override
    public void weatherProcessFinish(Weather output) {

        Log.d("dcw",output.toString());

    }
}
