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
        String[] testKategorien = new String[11];

        testKategorien[0] = "Trocken"; // eher trockenes Wetter, Niederschlagswahrscheinlichkeit <= 50%
        testKategorien[1] = "Nass"; // eher nasses Wetter, Niederschlagswahrscheinlichkeit > 50%
        testKategorien[2] = "Strandurlaub";
        testKategorien[3] = "Städtetrip";
        testKategorien[4] = "Skifahren";
        testKategorien[5] = "Wandern";
        testKategorien[6] = "Geschäftsreise";
        testKategorien[7] = "Partyurlaub";
        testKategorien[8] = "Camping";
        testKategorien[9] = "Festival";
        testKategorien[10] = "Manuell"; // vom User manuell hinzugefügtes Item

        Dataset testSet = new Dataset(0, "Testitem", 0, 0, testKategorien);
        Log.d(LOG_TAG, "Inhalt des Testsets: " + testSet.toString());

        dataSource = new Datasource(this);

    }

    @Override
    public void weatherProcessFinish(Weather output) {

        Log.d("dcw",output.toString());

    }
}
