package prak.travelerapp.FlickrAPI;

import org.json.JSONObject;

import prak.travelerapp.WeatherAPI.model.Weather;

/**
 * Created by Michael on 24.11.15.
 */
public interface AsyncFlickrResponse {
    void flickrProcessFinish(JSONObject output);
    void flickrProcessFailed();
}
