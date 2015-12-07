package prak.travelerapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michael on 25.11.15.
 */
public class Utils {


    /**
     * Checks if internet connetion is active
     * @return bool
     */
    public static boolean isOnline(Context ctxt){
        ConnectivityManager cm = (ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Loads JSON FILE from Assets Folder
     * @return String
     */
    public static String loadJSONFromAsset(AssetManager assets, String filename) {
        String json = null;
        try {

            InputStream is = assets.open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
