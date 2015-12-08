package prak.travelerapp.WeatherAPI;

import android.os.AsyncTask;

import org.json.JSONException;

import prak.travelerapp.WeatherAPI.model.Weather;

/**
 * Created by Michael on 24.11.15.
 */
public class WeatherTask extends AsyncTask<String, Void, Weather> {

    public AsyncWeatherResponse delegate = null;
    private Exception error;

    @Override
    protected Weather doInBackground(String... params) {
        Weather weather = new Weather();

        try {
            String city = params[0].replace(" ", "%20");
            String country = params[1].trim();
            String data = ( (new WeatherHTTPClient()).getWeatherData(city +"," + country));
            weather = JSONWeatherParser.getWeather(data);

        } catch (JSONException e) {
            return null;
        }
        return weather;

    }




    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        if(weather != null) {
            delegate.weatherProcessFinish(weather);
        }else{
            delegate.weatherProcessFailed();
        }
    }


}
