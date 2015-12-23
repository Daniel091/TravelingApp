package prak.travelerapp.PictureAPI;

/**
 * Created by Michael on 07.12.15.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Michael on 24.11.15.
 */
public class PictureHTTPClient {

    private static String BASE_URL = "https://api.500px.com/v1/photos/search?";
    private static String TERM_PARAM = "term=";
    private static String TAG_PARAM = "&tag=";
    private static String SIZE_PARAM = "&image_size=600";
    private static String NUMBER_PARAM = "&rpp=2";
    private static String CATEGORY_PARAM = "&only=Travel";

    private static String API_KEY = "&consumer_key=qVFJEFeFoAIoW3yjCb697qJxuYvEbpl5H68lmSQ4";


    public String getImageURL(String term) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            String url = BASE_URL + TERM_PARAM + term + SIZE_PARAM + NUMBER_PARAM+  CATEGORY_PARAM + API_KEY;
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
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

    public String getImageURL(String term, String tag) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            String url = BASE_URL + TERM_PARAM + term + TAG_PARAM + tag + SIZE_PARAM + NUMBER_PARAM+ CATEGORY_PARAM +API_KEY;
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
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

    // Creates Bitmap from InputStream and returns it
    public Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }


    // Makes HttpURLConnection and returns InputStream
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

}
