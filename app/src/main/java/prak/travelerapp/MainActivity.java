package prak.travelerapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

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

    }

    @Override
    public void weatherProcessFinish(Weather output) {

        Log.d("dcw",output.toString());

    }
}
