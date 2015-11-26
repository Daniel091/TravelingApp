package prak.travelerapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
