package prak.travelerapp.WeatherAPI.model;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Weather{

    public Location location;
    public ArrayList<Day> days = new ArrayList<Day>();

    /**
     * Funktion prüft, ob es an einem der Reisetage regnet
     *
     * @return
     */
    public boolean isRaining(){

        for (Day day: days) {
            if(day.condition.getMain().equals("Rain")){
                return true;
            }

        }
        return false;
    }

    /**
     * Funktion liefert die Tagestemperatur, die die Wetter API zurückgibt, für ein bestimmtes Datum
     *
     * @param dayRequest
     * @return
     */
    public String getTemperatureOnDate(DateTime dayRequest){

        String temperature = "--";  // wenn keine Temp verfügbar ist bleibt Temperatur "--"

        for (Day day: days) {
            if (day.getDate().toDateMidnight().equals(dayRequest.toDateMidnight())) {
                float temp = Math.round(day.temperature.getDayTemp());
                temperature = new DecimalFormat("#").format(temp);
            }
        }

        return temperature;
    }

    /**
     * Funktion liefert Wetter Icon für ein bestimmtes Datum. Der Rückgabe-String ist
     * gleich dem Dateinamen
     *
     * @param dayRequest
     * @return
     */
    public String getIconOnDate(DateTime dayRequest){

        String iconSrc = "--";

        for (Day day: days) {
            if (day.getDate().toDateMidnight().equals(dayRequest.toDateMidnight())) {
                iconSrc = "weather_" + String.valueOf(day.condition.getIcon());
            }
        }

        return iconSrc;
    }

    /**
     * Funktion liefert die Durchschnittstemperatur von allen Tagen zwischen zwei eingegebenen
     * Daten. Dabei werden fehlende Wetterdaten ignoriert
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getAverageTemp(DateTime startDate, DateTime endDate) {
        int travellingdays = Days.daysBetween(startDate.toLocalDate(), endDate.toLocalDate()).getDays() + 1;

        int factor = 0;
        int tempSum = 0;
        for (int i = 0; i < travellingdays; i++) {
            if (getTemperatureOnDate(startDate.plusDays(i)) != "--") {
                tempSum += Integer.valueOf(getTemperatureOnDate(startDate.plusDays(i)));
                factor++;
            }
        }
        int averageTemp = tempSum / factor;

        return averageTemp;
    }

}