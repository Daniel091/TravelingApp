package prak.travelerapp.WeatherAPI.model;

import android.util.Log;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Michael on 24.11.15.
 */

public class Weather{

    public Location location;
    public ArrayList<Day> days = new ArrayList<Day>();

    public boolean isRaining(){

        for (Day day: days) {
            if(day.condition.getMain().equals("Rain")){
                return true;
            }

        }
        return false;
    }

    public String getTemperature(DateTime dayRequest){

        String temperature = "--";

        Log.d("Weather API. Datum", dayRequest.toDateMidnight().toString());

        for (Day day: days) {
            if (day.getDate().toDateMidnight().equals(dayRequest.toDateMidnight())) {
                Log.d("Weather API. Temp", String.valueOf(day.temperature.getDayTemp()));
                Log.d("Weather API. Wetter", String.valueOf(day.condition.getMain()));
                float temp = Math.round(day.temperature.getDayTemp());
                temperature = new DecimalFormat("#").format(temp);
            }
        }

        return temperature;
    }

}