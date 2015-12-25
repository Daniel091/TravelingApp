package prak.travelerapp.WeatherAPI;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import prak.travelerapp.WeatherAPI.model.Day;
import prak.travelerapp.WeatherAPI.model.Location;
import prak.travelerapp.WeatherAPI.model.Weather;


public class JSONWeatherParser {

    public static Weather getWeather(String data) throws JSONException  {

        if(data != null) {
            try {
                Weather weather = new Weather();

                // We create out JSONObject from the data
                JSONObject jObj = new JSONObject(data);

                // We start extracting the info
                Location loc = new Location();

                JSONObject cityObj = getObject("city", jObj);
                loc.setCity(getString("name", cityObj));
                loc.setCountry(getString("country", cityObj));


                JSONObject coordObj = getObject("coord", cityObj);
                loc.setLatitude(getFloat("lat", coordObj));
                loc.setLongitude(getFloat("lon", coordObj));
                weather.location = loc;


                JSONArray weatherList = getArray("list", jObj);

                for (int i = 0; i < weatherList.length(); i++) {
                    Day curDay = new Day();
                    JSONObject dayObj = weatherList.getJSONObject(i);

                    curDay.setDate(getLong("dt",dayObj));
                    curDay.setClouds(getInt("clouds", dayObj));
                    curDay.setDeg(getInt("deg", dayObj));

                    JSONObject tempObj = getObject("temp", dayObj);
                    curDay.temperature.setDayTemp(getFloat("day", tempObj));
                    curDay.temperature.setNightTemp(getFloat("night", tempObj));
                    curDay.temperature.setMinTemp(getFloat("min", tempObj));
                    curDay.temperature.setMaxTemp(getFloat("max", tempObj));
                    curDay.temperature.setMornTemp(getFloat("morn", tempObj));
                    curDay.temperature.setEveTemp(getFloat("eve", tempObj));

                    JSONObject weath = getArray("weather", dayObj).getJSONObject(0);
                    curDay.condition.setDescription(getString("description", weath));
                    curDay.condition.setIcon(getString("icon", weath));
                    curDay.condition.setMain(getString("main", weath));

                    weather.days.add(curDay);
                }

                return weather;
            }catch(JSONException e){
                return null;
            }

        }else{
            return null;
        }
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static JSONArray getArray(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getJSONArray(tagName);
    }

    private static long  getLong(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getLong(tagName);
    }

}