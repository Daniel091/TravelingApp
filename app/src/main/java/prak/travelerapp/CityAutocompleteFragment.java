package prak.travelerapp;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

import prak.travelerapp.Autocompleter.CityAutoCompleteView;
import prak.travelerapp.Autocompleter.database.CityDBAdapter;
import prak.travelerapp.Autocompleter.model.City;
import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class CityAutocompleteFragment extends Fragment implements AsyncWeatherResponse {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_autocomplete, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareViews();

        try{

            // instantiate database handler
            cityDB = new CityDBAdapter(getActivity());
            cityDB.createDatabase();
            cityDB.open();

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence userInput, int start, int before, int count) {

                    // query the database based on the user input
                    item = getItemsFromDb(userInput.toString());

                    // update the adapater
                    myAdapter.notifyDataSetChanged();
                    myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);
                    myAutoComplete.setAdapter(myAdapter);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // set our adapter
            myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void prepareViews() {
        // autocompletetextview is in activity_main.xml
        myAutoComplete = (CityAutoCompleteView) getView().findViewById(R.id.myautocomplete);
        btn_getWeather = (Button) getView().findViewById(R.id.button_get_weather);
        btn_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = myAutoComplete.getText().toString();
                String[] separated = text.split(",");
                String city = separated[0];
                String country= separated[1];
                Log.d("mw", city);
                WeatherTask weathertask = new WeatherTask();
                weathertask.delegate = CityAutocompleteFragment.this;
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

    @Override
    public void onDestroy(){
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
