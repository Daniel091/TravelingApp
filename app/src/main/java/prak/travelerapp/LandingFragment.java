package prak.travelerapp;

import android.app.Fragment;
import android.graphics.Bitmap;
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

import prak.travelerapp.PictureAPI.AsyncPictureResponse;
import prak.travelerapp.PictureAPI.GetImageFromURLTask;
import prak.travelerapp.PictureAPI.GetImageURLTask;

public class LandingFragment extends Fragment implements AsyncPictureResponse {

    private ImageButton button_hamburger;
    private Button testQuery;       // Photo Test
    private ImageView imageView;    // ImageView
    private EditText editText;
    private RelativeLayout relativeLayout;

    private int screenheight;
    private int screenwidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.bringToFront();
        button_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;

        editText = (EditText)getView().findViewById(R.id.edit_text);
        imageView = (ImageView) getView().findViewById(R.id.imageView);
        GetImageURLTask getImageURLTask = new GetImageURLTask();
        getImageURLTask.delegate = this;
        getImageURLTask.execute("Muenchen");

        testQuery = (Button) getView().findViewById(R.id.button);
        testQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
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
}
