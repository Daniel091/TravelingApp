package prak.travelerapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class MainFragment extends Fragment implements AsyncWeatherResponse {
    private Button itemList, autocomplete,flickrButton;
    private Intent listIntent,autocompleteIntent,flickrIntent;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareViews();

        //weatherAPItest();
        flickrAPItest();

        //Wechselt zur View der Packliste
        changeToItemList();

        changeToAutoComplete();
    }

    private void prepareViews() {

    }

    private void changeToItemList() {

        itemList = (Button) getView().findViewById(R.id.itemList);
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listIntent = new Intent(getActivity(), ItemViewActivity.class);
                Log.d("MainActivity", "Click started item list");

                startActivity(listIntent);
            }
        });
    }

    private void changeToAutoComplete() {

        autocomplete = (Button) getView().findViewById(R.id.button_autocomplete);
        autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                autocompleteIntent = new Intent(getActivity(), CityAutocompleteFragment.class);
                Log.d("MainActivity", "Click started autocomplete view");

                startActivity(autocompleteIntent);
            }
        });
    }

    private void flickrAPItest() {
        flickrButton = (Button) getView().findViewById(R.id.flickrTest);
        flickrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flickrIntent = new Intent(getActivity(), LandingFragment.class);
                Log.d("MainActivity", "Flickr Test Button clicked");

                startActivity(flickrIntent);
            }
        });
    }

    public void weatherAPItest(){
        boolean isOnline = Utils.isOnline(getActivity());
        if(isOnline) {

            WeatherTask weathertask = new WeatherTask();
            weathertask.delegate = this;
            weathertask.execute(new String[]{"Berlin"});

        }else{
            //TODO tell user to get internet connection
        }
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
