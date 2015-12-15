package prak.travelerapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Typeface;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;

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

    /*
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     * edit by Max
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride, e.toString());
        }
    }

    public static DateTime stringToDatetime(String dateString){

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        DateTime dt = formatter.parseDateTime(dateString);
        return dt;
    }

    public static String dateTimeToString(DateTime date){

        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
        return date.toString(fmt);

    }
}
