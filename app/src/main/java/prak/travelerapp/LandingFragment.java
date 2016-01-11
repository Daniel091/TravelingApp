package prak.travelerapp;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import org.joda.time.DateTime;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;
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
    private ImageView imageView;
    private TextView city,timeToJourney,missingThings;
    private ImageView weatherForecastIcon1, weatherForecastIcon2, weatherForecastIcon3, weatherForecastIcon4, weatherForecastIcon5;
    private TextView weatherForecastTemp1, weatherForecastTemp2, weatherForecastTemp3, weatherForecastTemp4, weatherForecastTemp5, weatherForecastDay1, weatherForecastDay2, weatherForecastDay3, weatherForecastDay4, weatherForecastDay5;
    private SharedPreferences sharedPref;
    private Trip active_trip;
    private Button cancel_button,cancel_popup,ok_cancel_button;
    private LinearLayout koffer_packen,forecastIcons,forecastTemperature,forecastDays;
    private PopupWindow dummyPopup;
    private LayoutInflater inflater;
    private ViewGroup container;
    DateTime currentDate = new DateTime();
    private int daysToTrip;

    public int getDaysToTrip() {
        return daysToTrip;
    }

    public void setDaysToTrip(int daysToTrip) {
        this.daysToTrip = daysToTrip;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        prepareViews(view);
        prepareListeners();
        active_trip = getActiveTrip();
        sharedPref = getActivity().getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Get remaining days to trip
        float difference = active_trip.getStartdate().getMillis() - currentDate.getMillis();
        int days = Math.round((difference / 1000 / 3600 / 24)) + 1;
        setDaysToTrip(days);
        String date = "(" + active_trip.getStartdate().dayOfWeek().getAsShortText(Locale.GERMAN) + ", " + active_trip.getStartdate().toString("dd. MMM yyyy", Locale.GERMAN) + ")";

        // Get remaining days until return
        float differenceReturn = active_trip.getEnddate().getMillis() - currentDate.getMillis();
        int returnDays = Math.round((differenceReturn / 1000 / 3600 / 24)) + 1;
        String returnDate = "(" + active_trip.getEnddate().dayOfWeek().getAsShortText(Locale.GERMAN) + ", " + active_trip.getEnddate().toString("dd. MMM yyyy", Locale.GERMAN) + ")";

        setUpUnderline(days,returnDays, date,returnDate);

        // Get Weather on departing date
        WeatherTask weathertask = new WeatherTask();
        weathertask.delegate = LandingFragment.this;
        weathertask.execute(new String[]{active_trip.getCity(), active_trip.getCountry()});

        city.setText(active_trip.getCity());

        // Get things that are not checked
        int number = 0;
        for(Tupel t : active_trip.getTripItems().getItems()){
            if (t.getY() == 0){
                number++;
            }
        }
        missingThings.setText(getActivity().getResources().getString(R.string.missingThings, String.valueOf(number)));
        setUpBackgroundImage();

        return view;
    }

    private void prepareViews(View view) {
        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.bringToFront();
        city = (TextView) view.findViewById(R.id.city);
        timeToJourney = (TextView) view.findViewById(R.id.city_subline);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        cancel_button = (Button) view.findViewById(R.id.cancel_button);
        missingThings = (TextView) view.findViewById(R.id.missingThings);
        koffer_packen = (LinearLayout) view.findViewById(R.id.koffer_packen);
        forecastDays = (LinearLayout) view.findViewById(R.id.forecastDays);
        forecastIcons = (LinearLayout) view.findViewById(R.id.forecastIcons);
        forecastTemperature = (LinearLayout) view.findViewById(R.id.forecastTemperature);
        weatherForecastIcon1 = (ImageView) view.findViewById(R.id.weatherForecastIcon1);
        weatherForecastIcon2 = (ImageView) view.findViewById(R.id.weatherForecastIcon2);
        weatherForecastIcon3 = (ImageView) view.findViewById(R.id.weatherForecastIcon3);
        weatherForecastIcon4 = (ImageView) view.findViewById(R.id.weatherForecastIcon4);
        weatherForecastIcon5 = (ImageView) view.findViewById(R.id.weatherForecastIcon5);
        weatherForecastTemp1 = (TextView) view.findViewById(R.id.weatherForecastTemp1);
        weatherForecastTemp2 = (TextView) view.findViewById(R.id.weatherForecastTemp2);
        weatherForecastTemp3 = (TextView) view.findViewById(R.id.weatherForecastTemp3);
        weatherForecastTemp4 = (TextView) view.findViewById(R.id.weatherForecastTemp4);
        weatherForecastTemp5 = (TextView) view.findViewById(R.id.weatherForecastTemp5);
        weatherForecastDay1 = (TextView) view.findViewById(R.id.weatherForecastDay1);
        weatherForecastDay2 = (TextView) view.findViewById(R.id.weatherForecastDay2);
        weatherForecastDay3 = (TextView) view.findViewById(R.id.weatherForecastDay3);
        weatherForecastDay4 = (TextView) view.findViewById(R.id.weatherForecastDay4);
        weatherForecastDay5 = (TextView) view.findViewById(R.id.weatherForecastDay5);
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
                showDummyPopup();
                showPopup(v);
            }
        });
        koffer_packen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setUpMenu(2);
            }
        });
    }

    private void setUpUnderline(int days,int returnDays,String date,String returnDate) {
        // Wenn die Reise noch nicht begonnen hat, werden die Tage bis zum Beginn angezeigt
        if (days >= 0) {
            switch (days) {
                case 0:
                    timeToJourney.setText("heute geht's los");
                    break;
                case 1:
                    timeToJourney.setText("morgen gehts's los");
                    break;
                default:
                    timeToJourney.setText(getActivity().getResources().getString(R.string.daysToTrip, String.valueOf(days)) + " " + date);
                    break;
            }
        }

        // Wenn die Reise bereits läuft, werden die Tage bis zur Rückkehr angezeigt
        else {
            switch (returnDays) {
                case 0:
                    timeToJourney.setText("heute geht's nach Hause");
                    break;
                case 1:
                    timeToJourney.setText("morgen geht's nach Hause");
                    break;
                default:
                    timeToJourney.setText(getActivity().getResources().getString(R.string.daysToTrip, String.valueOf(returnDays)) + " " + returnDate);
                    break;
            }
        }
    }

    // Sets Background Image, path is stored in shared preferences
    private void setUpBackgroundImage() {
        String path_fromPref = sharedPref.getString(getString(R.string.saved_image_path),"default");
        if (path_fromPref.equals("image_by_categorie")){
            int resID = Utils.getDefaultPicResID(active_trip.getType1());
            imageView.setImageResource(resID);
        } else {
            Bitmap image = Utils.loadImageFromStorage(path_fromPref);
            if (image == null) {
                GetImageURLTask getImageURLTask = new GetImageURLTask();
                getImageURLTask.delegate = this;

                getImageURLTask.execute(active_trip.getCity().replaceAll("\\s", "%20"));
                Log.d("500px loads new image ", active_trip.getCity().replaceAll("\\s","%20"));
            } else {
                imageView.setImageBitmap(image);
            }
        }
    }

    private Trip getActiveTrip() {
        TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        return tripDBAdapter.getActiveTrip();
    }

    private void showDummyPopup() {
        final View popupDummyView = inflater.inflate(R.layout.dummy_popup, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int windowHeight = displaymetrics.heightPixels;
        int windowWidth = displaymetrics.widthPixels;

        dummyPopup = new PopupWindow(popupDummyView, windowWidth, windowHeight, false);
        dummyPopup.showAtLocation(popupDummyView, Gravity.NO_GRAVITY, 0, 0);
    }

    private void showPopup(View anchorView) {
        final View popupView = inflater.inflate(R.layout.delete_active_popup, container, false);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ok_cancel_button = (Button) popupView.findViewById(R.id.button_remove);
        // when Active Trip gets canceled
        ok_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
                tripDBAdapter.open();
                tripDBAdapter.setAllTripsInactive();

                popupWindow.dismiss();
                dummyPopup.dismiss();

                // Remove path of loaded image (image gets overwritten when a new trip is started)
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_image_path), "default");
                editor.putBoolean(String.valueOf(R.bool.day_before_notification), false);
                editor.apply();

                ((MainActivity) getActivity()).resetRemainingItems();

                StartFragment startFragment = new StartFragment();
                ((MainActivity) getActivity()).checkActiveTrip();
                ((MainActivity) getActivity()).clearBackstack();
                ((MainActivity) getActivity()).setUpFragment(startFragment,false);
            }
        });
        cancel_popup = (Button) popupView.findViewById(R.id.button_cancel_popup);
        cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dummyPopup.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        int location[] = new int[2];
        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void getURLProcessFinish(String url) {
        GetImageFromURLTask getImageFromURLTask = new GetImageFromURLTask();
        getImageFromURLTask.delegate = this;
        getImageFromURLTask.execute(url);
    }

    @Override
    public void getURLProcessFailed() {
        // No internet connection here
        // Could no longer save image path in shared preferences here so url task would try again next time
        Log.d("mw", "URL Process failed, now Default Picture");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_image_path), "image_by_categorie");
        editor.apply();

        int resID = Utils.getDefaultPicResID(active_trip.getType1());
        imageView.setImageResource(resID);
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
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_image_path), "image_by_categorie");
        editor.apply();

        int resID = Utils.getDefaultPicResID(active_trip.getType1());
        imageView.setImageResource(resID);
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
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
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

    @Override
    public void weatherProcessFinish(Weather output) {
        forecastDays.setVisibility(View.VISIBLE);
        forecastTemperature.setVisibility(View.VISIBLE);
        forecastIcons.setVisibility(View.VISIBLE);

        // Wenn die Reise frühestens morgen beginnt wird das Wetter für die ersten fünf Reisetage dargestellt
        if (getDaysToTrip() > 0) {

            // Alle Wetterdaten ausblenden, wenn Reisedatum außerhalb Vorhersage liegt
            int missingData = 0;
            for (int i=0; i<5; i++) {
                if ((output.getTemperatureOnDate(active_trip.getStartdate().plusDays(i))) == "--") {
                    missingData++;
                }
                if (missingData == 5) {
                    forecastDays.setVisibility(View.GONE);
                    forecastTemperature.setVisibility(View.GONE);
                    forecastIcons.setVisibility(View.GONE);
                }
            }

            if ((output.getTemperatureOnDate(active_trip.getStartdate().plusDays(0))) != "--") {
                weatherForecastIcon1.setImageResource(getResources().getIdentifier(output.getIconOnDate(active_trip.getStartdate()), "mipmap", "prak.travelerapp"));
                weatherForecastTemp1.setText(output.getTemperatureOnDate(active_trip.getStartdate()) + "°");
                weatherForecastDay1.setText(active_trip.getStartdate().dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());
            }

            if ((output.getTemperatureOnDate(active_trip.getStartdate().plusDays(1))) != "--") {
                weatherForecastIcon2.setImageResource(getResources().getIdentifier(output.getIconOnDate(active_trip.getStartdate().plusDays(1)), "mipmap", "prak.travelerapp"));
                weatherForecastTemp2.setText(output.getTemperatureOnDate(active_trip.getStartdate().plusDays(1)) + "°");
                weatherForecastDay2.setText(active_trip.getStartdate().plusDays(1).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());
            }

            if ((output.getTemperatureOnDate(active_trip.getStartdate().plusDays(2))) != "--") {
                weatherForecastIcon3.setImageResource(getResources().getIdentifier(output.getIconOnDate(active_trip.getStartdate().plusDays(2)), "mipmap", "prak.travelerapp"));
                weatherForecastTemp3.setText(output.getTemperatureOnDate(active_trip.getStartdate().plusDays(2)) + "°");
                weatherForecastDay3.setText(active_trip.getStartdate().plusDays(2).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());
            }

            if ((output.getTemperatureOnDate(active_trip.getStartdate().plusDays(3))) != "--") {
                weatherForecastIcon4.setImageResource(getResources().getIdentifier(output.getIconOnDate(active_trip.getStartdate().plusDays(3)), "mipmap", "prak.travelerapp"));
                weatherForecastTemp4.setText(output.getTemperatureOnDate(active_trip.getStartdate().plusDays(3)) + "°");
                weatherForecastDay4.setText(active_trip.getStartdate().plusDays(3).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());
            }

            if ((output.getTemperatureOnDate(active_trip.getStartdate().plusDays(4))) != "--") {
                weatherForecastIcon5.setImageResource(getResources().getIdentifier(output.getIconOnDate(active_trip.getStartdate().plusDays(4)), "mipmap", "prak.travelerapp"));
                weatherForecastTemp5.setText(output.getTemperatureOnDate(active_trip.getStartdate().plusDays(4)) + "°");
                weatherForecastDay5.setText(active_trip.getStartdate().plusDays(4).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());
            }
        }

        // Wenn die Reise bereits läuft, wird das Wetter für den aktuellen und die vier weiteren Tage dargestellt
        else {
            weatherForecastIcon1.setImageResource(getResources().getIdentifier(output.getIconOnDate(currentDate), "mipmap", "prak.travelerapp"));
            weatherForecastTemp1.setText(output.getTemperatureOnDate(currentDate) + "°");
            weatherForecastDay1.setText(currentDate.dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());

            weatherForecastIcon2.setImageResource(getResources().getIdentifier(output.getIconOnDate(currentDate.plusDays(1)), "mipmap", "prak.travelerapp"));
            weatherForecastTemp2.setText(output.getTemperatureOnDate(currentDate.plusDays(1)) + "°");
            weatherForecastDay2.setText(currentDate.plusDays(1).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());

            weatherForecastIcon3.setImageResource(getResources().getIdentifier(output.getIconOnDate(currentDate.plusDays(2)), "mipmap", "prak.travelerapp"));
            weatherForecastTemp3.setText(output.getTemperatureOnDate(currentDate.plusDays(2)) + "°");
            weatherForecastDay3.setText(currentDate.plusDays(2).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());

            weatherForecastIcon4.setImageResource(getResources().getIdentifier(output.getIconOnDate(currentDate.plusDays(3)), "mipmap", "prak.travelerapp"));
            weatherForecastTemp4.setText(output.getTemperatureOnDate(currentDate.plusDays(3)) + "°");
            weatherForecastDay4.setText(currentDate.plusDays(3).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());

            weatherForecastIcon5.setImageResource(getResources().getIdentifier(output.getIconOnDate(currentDate.plusDays(4)), "mipmap", "prak.travelerapp"));
            weatherForecastTemp5.setText(output.getTemperatureOnDate(currentDate.plusDays(4)) + "°");
            weatherForecastDay5.setText(currentDate.plusDays(4).dayOfWeek().getAsShortText(Locale.GERMAN).toUpperCase());
        }

    }

    @Override
    public void weatherProcessFailed() {
        forecastDays.setVisibility(View.GONE);
        forecastTemperature.setVisibility(View.GONE);
        forecastIcons.setVisibility(View.GONE);
    }
}
