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


        weatherAPItest();

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
