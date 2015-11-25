package prak.travelerapp;

import prak.travelerapp.WeatherAPI.model.Weather;

/**
 * Created by Michael on 24.11.15.
 */
public interface AsyncResponse {
    void weatherProcessFinish(Weather output);
    void weatherProcessFailed();
}
