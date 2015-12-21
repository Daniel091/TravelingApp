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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import prak.travelerapp.PictureAPI.AsyncPictureResponse;
import prak.travelerapp.PictureAPI.GetImageFromURLTask;
import prak.travelerapp.PictureAPI.GetImageURLTask;

public class LandingFragment extends Fragment implements AsyncPictureResponse {

    private ImageButton button_hamburger;
    private Button testQuery;       // Photo Test
    private ImageView imageView;    // ImageView
    private EditText editText;
    private TextView city;
    private TextView temperature;
    private RelativeLayout relativeLayout;
    private String path;
    private SharedPreferences sharedPref;
    private String defaultCity = "München";

    private int screenheight;
    private int screenwidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        prepareViews(view);
        prepareListeners();
        sharedPref = getActivity().getBaseContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        return view;
    }

    private void prepareViews(View view) {
        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.bringToFront();
        city = (TextView) view.findViewById(R.id.city);
        temperature = (TextView) view.findViewById(R.id.temperature);
        editText = (EditText) view.findViewById(R.id.edit_text);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        testQuery = (Button) view.findViewById(R.id.button);
    }

    private void prepareListeners() {
        button_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        testQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                city.setText(editText.getText().toString());
                //searchterm and tag given
                if (input.contains(",")) {
                    String[] inputs = input.split(",");
                    String searchTerm = inputs[0];
                    String tag = inputs[1];
                    GetImageURLTask getImageURLTask = new GetImageURLTask();
                    getImageURLTask.delegate = LandingFragment.this;
                    getImageURLTask.execute(searchTerm, tag);
                    //only searchterm given
                } else {
                    GetImageURLTask getImageURLTask = new GetImageURLTask();
                    getImageURLTask.delegate = LandingFragment.this;
                    getImageURLTask.execute(input);

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;

        city.setText(defaultCity);
        temperature.setText("14°");

        String path_fromPref = sharedPref.getString(getString(R.string.saved_image_path),"");
        if (!loadImageFromStorage(path_fromPref)) {
            GetImageURLTask getImageURLTask = new GetImageURLTask();
            getImageURLTask.delegate = this;
            getImageURLTask.execute(defaultCity);
        } else {
            Log.d("LandingFrag","Image file is there, no need to make http request");
        }
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
        Bitmap resizedImage = getResizedBitmap(image, screenheight, screenheight);
        try {
            saveToInternalStorage(resizedImage);
        } catch (Exception e){
            e.printStackTrace();
        }
        imageView.setImageBitmap(resizedImage);
    }

    @Override
    public void getImageFromURLProcessFailed() {
        Log.d("mw", "Image Process Failed");

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
}
