package prak.travelerapp.PictureAPI;

/**
 * Created by Michael on 07.12.15.
 */
import android.os.AsyncTask;

import org.json.JSONObject;

public class GetImageURLTask extends AsyncTask<String, Void, String> {

    public AsyncPictureResponse delegate = null;
    private Exception error;

    @Override
    protected String doInBackground(String... params) {
            String data = "";
            //only searchterm given
            if(params.length == 1){
                data = ( (new PictureHTTPClient()).getImageURL(params[0]));
            //searchterm and Tag given
            }else{
                data = ( (new PictureHTTPClient()).getImageURL(params[0], params[1]));
            }

            String url = "";
             // We create out JSONObject from the data
            try {
                JSONObject jObj = new JSONObject(data);
                JSONObject firstPhoto = jObj.getJSONArray("photos").getJSONObject(0);
                url = firstPhoto.getString("image_url");
            } catch (Exception e) {
                return null;
            }
            if(!url.isEmpty()){
                return url;
            }else{
                return null;
            }



    }

    @Override
    protected void onPostExecute(String url) {
        super.onPostExecute(url);
        if(url != null) {
            delegate.getURLProcessFinish(url);
        }else{
            delegate.getURLProcessFailed();
        }
    }


}
