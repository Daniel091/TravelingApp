package prak.travelerapp.WeatherAPI.model;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;

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

    public String getTemperatureOnDate(DateTime dayRequest){

        String temperature = "--";

        Log.d("Weather API. Date Temp", dayRequest.toDateMidnight().toString());

        for (Day day: days) {
            if (day.getDate().toDateMidnight().equals(dayRequest.toDateMidnight())) {
                Log.d("Weather API. Temp", String.valueOf(day.temperature.getDayTemp()));
                float temp = Math.round(day.temperature.getDayTemp());
                temperature = new DecimalFormat("#").format(temp);
            }
        }

        return temperature;
    }

    public String getIconOnDate(DateTime dayRequest){

        String iconSrc = "--";

        Log.d("Weather API. Date Cond", dayRequest.toDateMidnight().toString());

        for (Day day: days) {
            if (day.getDate().toDateMidnight().equals(dayRequest.toDateMidnight())) {
                Log.d("Weather API. Cond Main", String.valueOf(day.condition.getMain()));
                Log.d("Weather API. Cond Desc", String.valueOf(day.condition.getDescription()));
                Log.d("Weather API. Cond Icon", String.valueOf(day.condition.getIcon()));
                iconSrc = "weather_" + String.valueOf(day.condition.getIcon());
            }
        }

        return iconSrc;
    }

    public Integer getAverageTemp(DateTime startDate, DateTime endDate) {
        int travellingdays = Days.daysBetween(startDate.toLocalDate(), endDate.toLocalDate()).getDays() + 1;

        int factor = 0;
        int tempSum = 0;
        for (int i = 0; i < travellingdays; i++) {
            if (getTemperatureOnDate(startDate.plusDays(i)) != "--") {
                Log.d("Tag " + i, getTemperatureOnDate(startDate.plusDays(i)));
                tempSum += Integer.valueOf(getTemperatureOnDate(startDate.plusDays(i)));
                factor++;
            }
        }
        int averageTemp = tempSum / factor;
        Log.d("Temperatur Durchschnitt", String.valueOf(averageTemp));

        return averageTemp;
    }

}