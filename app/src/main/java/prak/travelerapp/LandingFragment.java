package prak.travelerapp;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import prak.travelerapp.PictureAPI.AsyncPictureResponse;
import prak.travelerapp.PictureAPI.GetImageFromURLTask;
import prak.travelerapp.PictureAPI.GetImageURLTask;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.Tupel;
import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class LandingFragment extends Fragment implements AsyncPictureResponse, AsyncWeatherResponse {

    private ImageButton button_hamburger;
    private ImageView imageView;    // ImageView
    private TextView city;
    private TextView temperature,timeToJourney,missingThings;
    private SharedPreferences sharedPref;
    private Trip active_trip;
    private Button cancel_button;

    private int screenheight;
    private int screenwidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        prepareViews(view);
        prepareListeners();

        return view;
    }

    private Trip getActiveTrip() {
        TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        return tripDBAdapter.getActiveTrip();
    }

    private void prepareViews(View view) {
        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.bringToFront();
        city = (TextView) view.findViewById(R.id.city);
        timeToJourney = (TextView) view.findViewById(R.id.city_subline);
        temperature = (TextView) view.findViewById(R.id.temperature);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        cancel_button = (Button) view.findViewById(R.id.cancel_button);
        missingThings = (TextView) view.findViewById(R.id.missingThings);
    }

    private void prepareListeners() {
        button_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LandingFrag", "Pressed Cancel trip");
                TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
                tripDBAdapter.open();
                tripDBAdapter.removeActiveFromTrip();

                StartFragment startFragment = new StartFragment();
                ((MainActivity) getActivity()).checkActiveTrip();
                ((MainActivity) getActivity()).setUpFragment(startFragment);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        active_trip = getActiveTrip();
        sharedPref = getActivity().getBaseContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;

        // Get current Date
        DateTime currentDate = new DateTime();
        float difference = active_trip.getStartdate().getMillis() - currentDate.getMillis();
        int days = Math.round((difference / 1000 / 3600 / 24)) + 1;

        // Get Weather on departing date
        WeatherTask weathertask = new WeatherTask();
        weathertask.delegate = LandingFragment.this;
        weathertask.execute(new String[]{active_trip.getCity(), active_trip.getCountry()});

        city.setText(active_trip.getCity());
        timeToJourney.setText(getActivity().getResources().getString(R.string.daysToTrip,String.valueOf(days)));

        int number = 0;
        for(Tupel t : active_trip.getTripItems().getItems()){
            if (t.getY() == 0){
                number++;
            }
        }
        missingThings.setText(getActivity().getResources().getString(R.string.missingThings,String.valueOf(number)));

        String path_fromPref = sharedPref.getString(getString(R.string.saved_image_path),"");
      //  if (!loadImageFromStorage(path_fromPref)) {
            GetImageURLTask getImageURLTask = new GetImageURLTask();
            getImageURLTask.delegate = this;

            getImageURLTask.execute(active_trip.getCity());
            Log.d("500px loads new image ",active_trip.getCity());
      /*  } else {
            Log.d("LandingFrag","Image file is there, no need to make http request");
        }*/
    }

    @Override
    public void getURLProcessFinish(String url) {

        GetImageFromURLTask getImageFromURLTask = new GetImageFromURLTask();
        getImageFromURLTask.delegate = this;
        getImageFromURLTask.execute(url);

    }

    @Override
    public void getURLProcessFailed() {
        Log.d("mw", "URL Process failed, now Default Picture");
        setDefaultPic();
    }

    @Override
    public void getImageFromURLProcessFinish(Bitmap image) {
        final Bitmap resizedImage = getResizedBitmap(image, 800, 800);
            try {
                imageView.setImageBitmap(resizedImage);
                saveToInternalStorage(resizedImage);
            } catch (Exception e){
                e.printStackTrace();
            }
    }

    @Override
    public void getImageFromURLProcessFailed() {
        Log.d("mw", "Image Process Failed, now Default Picture");
        setDefaultPic();
    }

    private void setDefaultPic() {
        // Sets Default Activity with Country
        Log.d("500px","Get image of city failed, now trying to get image of country, "+ active_trip.getCountry());
        GetImageURLTask getImageURLTask = new GetImageURLTask();
        getImageURLTask.delegate = this;
        getImageURLTask.execute(active_trip.getCountry());
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
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws Exception{
        ContextWrapper cw = new ContextWrapper(getActivity().getBaseContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"ActiveTrip.jpg");


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_image_path), directory.getAbsolutePath());
        editor.apply();

        Log.d("Landing","Image saved to " + directory.getAbsolutePath());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }

    private boolean loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "ActiveTrip.jpg");
            if (f.exists()) {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
                return true;
            } else {
                return false;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void weatherProcessFinish(Weather output) {
        temperature.setText(output.getTemperature(active_trip.getStartdate()) + "°");
    }

    @Override
    public void weatherProcessFailed() {

    }
}
