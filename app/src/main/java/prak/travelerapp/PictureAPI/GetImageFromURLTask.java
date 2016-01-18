package prak.travelerapp.PictureAPI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GetImageFromURLTask extends AsyncTask<String,Void,Bitmap> {

    public AsyncPictureResponse delegate = null;

    protected Bitmap doInBackground(String... params) {

        String url = params[0];
        return (new PictureHTTPClient()).downloadImage(url);

    }

    // Sets the Bitmap returned by doInBackground
    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null){
            delegate.getImageFromURLProcessFinish(result);
        }else{
            delegate.getURLProcessFailed();
        }
    }


}
