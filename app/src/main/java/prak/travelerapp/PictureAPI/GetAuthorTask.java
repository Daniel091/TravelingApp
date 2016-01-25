package prak.travelerapp.PictureAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

/**
 * Class creates URL to background image loaded from API
 */
public class GetAuthorTask extends AsyncTask<String, Void, String> {

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

        String author = "";

        // We create out JSONObject from the data
        try {
            JSONObject jObj = new JSONObject(data);
            JSONObject firstPhoto = jObj.getJSONArray("photos").getJSONObject(0);
            JSONObject user = firstPhoto.getJSONObject("user");
            String name = user.getString("fullname");
            String source = firstPhoto.getString("url");
            if(name.length() > 20)
                name = name.substring(0,20) + "...";
            author = "<a href='http://www.500px.com" + source + "'>\u00A9 " + name + " / 500px </a>";
            //Log.d("Quelle",author);
        } catch (Exception e) {
            return null;
        }
        if(!author.isEmpty()){
            return author;
        }else{
            return null;
        }

    }

    @Override
    protected void onPostExecute(String author) {
        super.onPostExecute(author);
        if(author != null) {
            delegate.getAuthorProcessFinish(author);
        }else{
            delegate.getAuthorProcessFailed();
        }
    }


}
