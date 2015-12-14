package prak.travelerapp;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import prak.travelerapp.PictureAPI.AsyncPictureResponse;
import prak.travelerapp.PictureAPI.GetImageFromURLTask;
import prak.travelerapp.PictureAPI.GetImageURLTask;

public class FlickrFragment extends Fragment implements AsyncPictureResponse {

    private Button sanFrancisco;    // Photo Test
    private ImageView imageView;// ImageView
    private EditText editText;

    private int screenheight;
    private int screenwidth;

    // Flickr Settings
    private static final String FLICKRKEY = "7c4034aedc42e402d26421f9388e189f";
    private static final String FLICKRSECRET = "746fc3e64519bcee";

    // REST Request with JSON Output
    private String group_id = "26328425@N00";
    private String location = "sanfrancisco";
    //private String images = "1";
    //private String restUrl = "https://api.flickr.com/services/rest/?&method=flickr.photos.search&format=json&nojsoncallback=1&api_key=" + FLICKRKEY + "&per_page=" + images + "&tags=" + location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;
       // Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
       // getActivity().setSupportActionBar(toolbar);

        editText = (EditText)getView().findViewById(R.id.edit_text);
        imageView = (ImageView) getView().findViewById(R.id.imageView);
        GetImageURLTask getImageURLTask = new GetImageURLTask();
        getImageURLTask.delegate = this;
        getImageURLTask.execute("Muenchen");

        sanFrancisco = (Button) getView().findViewById(R.id.button);
        sanFrancisco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                //searchterm and tag given
                if (input.contains(",")) {
                    String[] inputs = input.split(",");
                    String searchTerm = inputs[0];
                    String tag = inputs[1];
                    GetImageURLTask getImageURLTask = new GetImageURLTask();
                    getImageURLTask.delegate = FlickrFragment.this;
                    getImageURLTask.execute(searchTerm, tag);
                    //only searchterm given
                } else {
                    GetImageURLTask getImageURLTask = new GetImageURLTask();
                    getImageURLTask.delegate = FlickrFragment.this;
                    getImageURLTask.execute(input);

                }


            }
        });
    }

    @Override
    public void getURLProcessFinish(String url) {

        GetImageFromURLTask getImageFromURLTask = new GetImageFromURLTask();
        getImageFromURLTask.delegate = this;
        getImageFromURLTask.execute(url);

    }

    @Override
    public void getURLProcessFailed() {

        Log.d("mw", "URL Process failed");

    }

    @Override
    public void getImageFromURLProcessFinish(Bitmap image) {

        Bitmap resizedImage = getResizedBitmap(image,screenheight,screenheight);
        imageView.setImageBitmap(resizedImage);

    }

    @Override
    public void getImageFromURLProcessFailed() {
        Log.d("mw", "Image Process Failed");

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

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }
}
