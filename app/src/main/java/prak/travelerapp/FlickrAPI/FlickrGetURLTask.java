package prak.travelerapp.FlickrAPI;

/**
 * Created by Michael on 07.12.15.
 */
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Michael on 24.11.15.
 */
public class FlickrGetURLTask extends AsyncTask<String, Void, JSONObject> {

    public AsyncFlickrResponse delegate = null;
    private Exception error;

    @Override
    protected JSONObject doInBackground(String... params) {

            String data = ( (new FlickrHTTPClient()).getFlickrImageURL(params[0],params[1]));
            JSONObject jObj;
             // We create out JSONObject from the data
            try {
                jObj = new JSONObject(data);
            } catch (JSONException e) {
                return null;
            }
            return jObj;

    }




    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        if(result != null) {
            delegate.flickrProcessFinish(result);
        }else{
            delegate.flickrProcessFailed();
        }
    }


}
