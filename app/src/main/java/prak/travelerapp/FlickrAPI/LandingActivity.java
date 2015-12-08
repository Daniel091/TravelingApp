package prak.travelerapp.FlickrAPI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import prak.travelerapp.R;

public class LandingActivity extends AppCompatActivity implements AsyncFlickrResponse {

    private Button sanFrancisco;    // Photo Test
    private ImageView imageView;    // ImageView

    // Flickr Settings
    private static final String FLICKRKEY = "7c4034aedc42e402d26421f9388e189f";
    private static final String FLICKRSECRET = "746fc3e64519bcee";

    /* REST Request with JSON Output
    private String location = "sanfrancisco";
    private String images = "1";
    private String restUrl = "https://api.flickr.com/services/rest/?&method=flickr.photos.search&format=json&nojsoncallback=1&api_key=" + FLICKRKEY + "&per_page=" + images + "&tags=" + location;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FlickrGetURLTask flickrAPI = new FlickrGetURLTask();
        flickrAPI.delegate = this;
        flickrAPI.execute("münchen");
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);

        // Set Image from static URL as Background
        DefaultBackground task = new DefaultBackground();
        // Execute the task
        task.execute(new String[] { URL });

        sanFrancisco = (Button) findViewById(R.id.button);
        sanFrancisco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Landing Activity", "JSON for San Francisco");
                //loadImageUrl();
            }
        });*/
    }

    @Override
    public void flickrProcessFinish(JSONObject output) {
        Log.d("mw", output.toString());
        try {
            JSONObject photosObj = output.getJSONObject("photos");
            JSONArray photoArray = photosObj.getJSONArray("photo");
            JSONObject photoObj = photoArray.getJSONObject(0);
            String photoid = photoObj.getString("id");
            String farmid = photoObj.getString("farm");
            String serverid = photoObj.getString("server");
            String secret = photoObj.getString("secret");
            String flickrURL = "https://farm" + farmid + ".staticflickr.com/" + serverid + "/" + photoid + "_" + secret + "_b.jpg";
            Log.d("Flickr Image URL", flickrURL);

            imageView = (ImageView) findViewById(R.id.imageView);

            // Set Image from static URL as Background
            SetBackground task = new SetBackground();
            // Execute the task
            task.execute(new String[]{ flickrURL });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void flickrProcessFailed() {

    }


    // Set Background from Flickr URL
    private class SetBackground extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
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
}
