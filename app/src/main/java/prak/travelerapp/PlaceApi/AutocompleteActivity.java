package prak.travelerapp.PlaceApi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.R;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class AutocompleteActivity extends Activity implements AsyncWeatherResponse {
    private AutoCompleteTextView autocompleteView;

    private Button btn_getWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        prepareViews();
    }

    private void prepareViews() {
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getBaseContext(), R.layout.autocomplete_list_item));
        btn_getWeather = (Button) findViewById(R.id.button_get_weather);
        btn_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = autocompleteView.getText().toString();
                String[] separated = text.split(",");
                String city = separated[0];
                Log.d("mw", city);
                WeatherTask weathertask = new WeatherTask();
                weathertask.delegate = AutocompleteActivity.this;
                weathertask.execute(new String[]{city});

            }
        });
    }

    @Override
    public void weatherProcessFinish(Weather output) {

        Log.d("dcw", output.toString());
    }

    @Override
    public void weatherProcessFailed() {
        Log.d("ERROR", "WeatherAPIcall failed");
        //TODO -> handle exception
    }
}
