package prak.travelerapp.WeatherAPI;

import prak.travelerapp.WeatherAPI.model.Weather;

/**
 * Created by Michael on 24.11.15.
 */
public interface AsyncWeatherResponse {
    void weatherProcessFinish(Weather output);
    void weatherProcessFailed();
}
