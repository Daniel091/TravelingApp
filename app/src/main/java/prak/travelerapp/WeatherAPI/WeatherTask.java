package prak.travelerapp.WeatherAPI;

import android.os.AsyncTask;

import org.json.JSONException;

import prak.travelerapp.AsyncResponse;
import prak.travelerapp.WeatherAPI.model.Weather;

/**
 * Created by Michael on 24.11.15.
 */
public class WeatherTask extends AsyncTask<String, Void, Weather> {

    public AsyncResponse delegate = null;
    @Override
    protected Weather doInBackground(String... params) {
        Weather weather = new Weather();
        String data = ( (new WeatherHTTPClient()).getWeatherData(params[0]));

        try {
            weather = JSONWeatherParser.getWeather(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;

    }




    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        delegate.weatherProcessFinish(weather);
    }


}
