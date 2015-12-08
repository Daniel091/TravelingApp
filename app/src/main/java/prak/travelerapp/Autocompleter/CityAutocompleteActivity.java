package prak.travelerapp.Autocompleter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;

import prak.travelerapp.Autocompleter.database.CityDBAdapter;
import prak.travelerapp.Autocompleter.model.City;
import prak.travelerapp.PlaceApi.PlacesAutoCompleteAdapter;
import prak.travelerapp.R;
import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class CityAutocompleteActivity extends AppCompatActivity implements AsyncWeatherResponse {

    /*
   * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
   * since we are extending to customize the view and disable filter
   * The same with the XML view, type will be CustomAutoCompleteView
   */
    CityAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // for database operations
    CityDBAdapter cityDB;

    Button btn_getWeather;

    // just to add some initial value
    String[] item = new String[] {"Please search..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_autocomplete);

        prepareViews();

        try{

            // instantiate database handler
            cityDB = new CityDBAdapter(this);
            cityDB.createDatabase();
            cityDB.open();

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CityAutocompleteTextChangedListener(this));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareViews() {
        // autocompletetextview is in activity_main.xml
        myAutoComplete = (CityAutoCompleteView) findViewById(R.id.myautocomplete);
        btn_getWeather = (Button) findViewById(R.id.button_get_weather);
        btn_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = myAutoComplete.getText().toString();
                String[] separated = text.split(",");
                String city = separated[0];
                String country= separated[1];
                Log.d("mw", city);
                WeatherTask weathertask = new WeatherTask();
                weathertask.delegate = CityAutocompleteActivity.this;
                weathertask.execute(new String[]{city,country});

            }
        });
    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        List<City> products = cityDB.read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (City record : products) {

            item[x] = record.getName() + ", " +record.getCountry();
            x++;
        }

        return item;
    }

    protected void onDestroy(){
        super.onDestroy();
        cityDB.close();

    }

    @Override
    public void weatherProcessFinish(Weather output) {

        Log.d("mw", "Weather Success");
    }

    @Override
    public void weatherProcessFailed() {

    }
}
