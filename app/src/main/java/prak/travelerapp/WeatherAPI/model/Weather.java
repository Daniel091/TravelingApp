package prak.travelerapp.WeatherAPI.model;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Created by Michael on 24.11.15.
 */

public class Weather{

    public Location location;
    public Day[] days = new Day[16];

    public boolean isRaining(){

        for (Day day: days) {
            if(day.condition.getMain().equals("Rain")){
                return true;
            }

        }
        return false;
    }

}