package prak.travelerapp.FlickrAPI;

/**
 * Created by Michael on 07.12.15.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import prak.travelerapp.MainActivity;
import prak.travelerapp.R;

/**
 * Created by Michael on 24.11.15.
 */
public class FlickrHTTPClient {

    private static String BASE_URL = "https://api.flickr.com/services/rest/?&method=flickr.photos.search&format=json&nojsoncallback=1";
    private static String IMAGES_PARAM = "&per_page=1";
    private static String LOCATION_PARAM = "&tags=";
    private static String API_KEY = "&api_key=7c4034aedc42e402d26421f9388e189f";

    public String getFlickrImageURL(String location) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            String url = BASE_URL + API_KEY + IMAGES_PARAM + LOCATION_PARAM + location;
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            int code = con.getResponseCode();

            if (code == 200) {
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
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
