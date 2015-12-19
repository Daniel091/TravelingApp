package prak.travelerapp;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import prak.travelerapp.Autocompleter.CityAutoCompleteView;
import prak.travelerapp.Autocompleter.database.CityDBAdapter;
import prak.travelerapp.Autocompleter.model.City;
import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.TripItems;
import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Weather;

public class NewTripFragment extends Fragment implements View.OnClickListener,TextWatcher,AdapterView.OnItemSelectedListener {
    private LinearLayout secondTripType;
    private ImageButton button_hamburger;
    private Spinner spinner_category,spinner_category2;
    private CityAutoCompleteView autocompleter;
    private ArrayAdapter<String> autocompleteAdapter;
    private CityDBAdapter cityDB;
    private TextView editText_arrival, editText_departure;
    private DatePickerDialog arrivalDatePickerDialog,departureDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private FloatingActionButton button_submit;
    private TripDBAdapter tripDBAdapter;

    private String[] traveltypeStrings;
    String[] items = new String[] {};


    private String city;
    private String country;
    private DateTime startDate;
    private DateTime endDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_trip, container, false);

        prepareViews(view);
        setOnClicksListeners();

        try{
            // instantiate database handler
            cityDB = new CityDBAdapter(getActivity());
            cityDB.createDatabase();
            cityDB.open();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // add the listener so it will tries to suggest while the user types
        autocompleter.addTextChangedListener(this);
        // set our adapter
        autocompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autocompleter.setAdapter(autocompleteAdapter);

        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

        setUpArrivalDatePicker();
        setUpDepartureDatePicker();

        //Convert Kategorie enum to String values but skip NoType
        traveltypeStrings = new String[TravelType.values().length];
        for(int i = 0; i < TravelType.values().length; i++){
            TravelType type = TravelType.values()[i];
            traveltypeStrings[i] = type.getStringValue();
        }
        String[] onlyRealCategoryStrings = Arrays.copyOfRange(traveltypeStrings,1,traveltypeStrings.length);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,onlyRealCategoryStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter);
        spinner_category.setOnItemSelectedListener(this);

        return view;
    }

    private void setUpDepartureDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        departureDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_departure.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        departureDatePickerDialog.getDatePicker().setMinDate(new Date().getTime());
    }

    private void setUpArrivalDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        arrivalDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_arrival.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        arrivalDatePickerDialog.getDatePicker().setMinDate(new Date().getTime());
    }

    private void prepareViews(View view) {
        // autocompletetextview is in activity_main.xml
        autocompleter = (CityAutoCompleteView) view.findViewById(R.id.autocomplete_destination);

        editText_arrival = (TextView) view.findViewById(R.id.edittext_arrival);
        editText_departure = (TextView) view.findViewById(R.id.edittext_departure);
        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_submit = (FloatingActionButton)view.findViewById(R.id.button_submit);
        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);
    }

    private void setOnClicksListeners() {
        button_hamburger.setOnClickListener(this);
        editText_arrival.setOnClickListener(this);
        editText_departure.setOnClickListener(this);
        button_submit.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void submitTrip(){
        String autocompleterText = autocompleter.getText().toString();
        String[] separated = autocompleterText.split(",");
        //City was set and is correct format -> City, Country
        if(!autocompleterText.equals(getResources().getString(R.string.city_default)) && separated.length == 2){
            final String city = separated[0].trim();
            final String country= separated[1].trim();

            //two dates were selected
            if(!editText_arrival.getText().toString().equals(getResources().getString(R.string.arrival_default)) && !editText_departure.getText().toString().equals(getResources().getString(R.string.departure_default))){
                final DateTime startDate = Utils.stringToDatetime(editText_arrival.getText().toString());
                final DateTime endDate = Utils.stringToDatetime(editText_departure.getText().toString());
                    //Check which category has been selected
                    TravelType type1 = TravelType.NO_TYPE;
                    TravelType type2 = TravelType.NO_TYPE;
                    for (TravelType type : TravelType.values()){
                        if(type.getStringValue().equals(spinner_category.getSelectedItem().toString())){
                            type1 = type;
                        }
                        if (spinner_category2 != null){
                            if(type.getStringValue().equals(spinner_category2.getSelectedItem().toString())){
                                type2 = type;
                            }
                        }
                    }
                final TravelType type_one = type1;
                final TravelType type_two = type2;
                    /*
                    String s = "(3,0);(4,0)";
                    TripItems items = new TripItems(s);

                    Log.d("NewTrip","Inserted "+ city + " " + type1 + " " + type2);
                    */

                WeatherTask weathertask = new WeatherTask();
                weathertask.delegate = new AsyncWeatherResponse() {
                    @Override
                    public void weatherProcessFinish(Weather output) {

                        Weather weather = output;

                        int relevantDayStartIndex = -1;
                        int relevantDayEndIndex = -1;
                        for(int i = 0;i < weather.days.length;i++){
                            DateTime date = weather.days[i].getDate();
                            if(DateTimeComparator.getDateOnlyInstance().compare(startDate, date) == 0){
                                System.out.println("Startdate is " + date.toString("dd.MM.yyyy"));
                                relevantDayStartIndex = i;
                                relevantDayEndIndex = weather.days.length-1;
                            }
                        }

                        if(relevantDayStartIndex != -1){
                            for(int z = 0;z < weather.days.length;z++){
                                DateTime date = weather.days[z].getDate();
                                if(DateTimeComparator.getDateOnlyInstance().compare(endDate, date) == 0){
                                    System.out.println("Enddate is " + date.toString("dd.MM.yyyy"));
                                    relevantDayEndIndex = z;
                                }
                            }

                            weather.days = Arrays.copyOfRange(weather.days, relevantDayStartIndex,relevantDayEndIndex);
                            //keine relevanten wetterdaten verfügbar
                        }else{

                            weather = null;

                        }
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        String gender = sharedPref.getString(getString(R.string.saved_gender), "not_selected");

                        TripItems tripItems = configureTripItems(gender, weather, type_one, type_two);

                        tripDBAdapter = new TripDBAdapter(getActivity());
                        tripDBAdapter.open();
                        tripDBAdapter.insertTrip(tripItems, city, country, startDate, endDate, type_one, type_one, true);

                        Fragment ItemViewFragment = new ItemViewFragment();
                        ((MainActivity) getActivity()).setUpFragement(ItemViewFragment);

                    }

                    @Override
                    public void weatherProcessFailed() {

                    }
                };
                weathertask.execute(new String[]{city,country});
            }else{
                Toast.makeText(getActivity(), "Wähle einen Reisezeitraum", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getActivity(), "Wähle ein Reiseziel", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getActivity(),"Added youre Trip",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_hamburger:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.edittext_arrival:
                arrivalDatePickerDialog.show();
                break;
            case R.id.edittext_departure:
                departureDatePickerDialog.show();
                break;
            case R.id.button_submit:
                submitTrip();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        // query the database based on the user input
        items = getItemsFromDb(userInput.toString());

        // update the adapater
        autocompleteAdapter.notifyDataSetChanged();
        autocompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autocompleter.setAdapter(autocompleteAdapter);
    }

    @Override
    public void afterTextChanged(Editable s) {

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (position != 0 && spinner.getId() == R.id.spinner_category){
            secondTripType = (LinearLayout) getView().findViewById(R.id.fourthSection);
            secondTripType.setVisibility(LinearLayout.VISIBLE);
            setUpSecondTripType();
        } else {
            if (secondTripType != null && spinner.getId() == R.id.spinner_category){
                secondTripType.setVisibility(LinearLayout.GONE);
            }
        }
    }

    private void setUpSecondTripType() {
        spinner_category2 = (Spinner) getView().findViewById(R.id.spinner_category2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,traveltypeStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category2.setAdapter(adapter);
        spinner_category2.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public TripItems configureTripItems(String gender, Weather weather, TravelType type1, TravelType type2){
        int genderValue = 0;
        switch (gender){
            case "male":
                genderValue=1;
                break;
            case "female":
                genderValue=2;
                break;
            case "not_selected":
                Log.e("ERROR", "Kein Gender festgelegt");
                break;
        }

        boolean isRaining = weather.isRaining();

        ItemDBAdapter itemDB = new ItemDBAdapter(getActivity());
        itemDB.createDatabase();
        itemDB.open();
        ArrayList<Integer> itemIDs  = itemDB.findItemIDs(genderValue,isRaining,type1,type2);
        TripItems items = new TripItems();

        for(Integer id : itemIDs){
            items.addItem(id);
        }

        String itemString = items.makeString();
        System.out.println(itemString);
        return items;
    }

}
