package prak.travelerapp.WeatherAPI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import prak.travelerapp.MainActivity;
import prak.travelerapp.R;

/**
 * Created by Michael on 24.11.15.
 */
public class WeatherHTTPClient {

        private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
        private static String COUNT_PARAM = "&cnt=16";
        private static String API_KEY = "&appid=5883d3f233f058145feca6dd84ec76f9";


        public String getWeatherData(String location) {
            HttpURLConnection con = null;
            InputStream is = null;

            try {
                String url = BASE_URL + location + COUNT_PARAM + API_KEY;
                con = (HttpURLConnection) (new URL(url)).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                int code = con.getResponseCode();

                if(code == 200){
                    // Let's read the response
                    StringBuffer buffer = new StringBuffer();
                    is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while ((line = br.readLine()) != null)
                        buffer.append(line + "\r\n");
                    is.close();
                    con.disconnect();
                    return buffer.toString();
                }else{
                    //TODO Handle request error
                    return "";
                }
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Throwable t) {
                }
                try {
                    con.disconnect();
                } catch (Throwable t) {
                }
            }

            return null;

        }
}
