package prak.travelerapp;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import prak.travelerapp.Autocompleter.CityAutoCompleteView;
import prak.travelerapp.Autocompleter.database.CityDBAdapter;
import prak.travelerapp.Autocompleter.model.City;
import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.TripItems;
import prak.travelerapp.WeatherAPI.AsyncWeatherResponse;
import prak.travelerapp.WeatherAPI.WeatherTask;
import prak.travelerapp.WeatherAPI.model.Day;
import prak.travelerapp.WeatherAPI.model.Weather;

public class NewTripFragment extends Fragment implements View.OnClickListener,TextWatcher,AdapterView.OnItemSelectedListener {

    public Trip reusedTrip;
    private Spinner spinner_category,spinner_category2;
    private CityAutoCompleteView autocompleter;
    private ArrayAdapter<String> autocompleteAdapter;
    private CityDBAdapter cityDB;
    private TextView editText_arrival, editText_departure;
    private DatePickerDialog arrivalDatePickerDialog,departureDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private boolean firstSelect = true;

    private String[] traveltypeStrings;
    String[] items = new String[] {};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_trip, container, false);
        prepareViews(view);
        setUpArrivalDatePicker();
        setUpDepartureDatePicker();

        return view;
    }

    private void prepareViews(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.newTripRelative);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (in.isAcceptingText()){
                    in.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        autocompleter = (CityAutoCompleteView) view.findViewById(R.id.autocomplete_destination);
        autocompleter.addTextChangedListener(this);

        // Listen for enter an then Close InputWindow
        autocompleter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }

                return false;
            }
        });

        autocompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autocompleter.setAdapter(autocompleteAdapter);

        editText_arrival = (TextView) view.findViewById(R.id.edittext_arrival);
        editText_arrival.setOnClickListener(this);
        editText_departure = (TextView) view.findViewById(R.id.edittext_departure);
        editText_departure.setOnClickListener(this);
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

        //Set default values of eddittext field for arrival and departure to currentdate and currentdate+5
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        editText_arrival.setText(dateFormatter.format(date.getTime()));
        date.add(Calendar.DATE, 5);
        editText_departure.setText(dateFormatter.format(date.getTime()));
        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);

        //if we have a reused trip we reuse a old itemlist and dont need to configure traveltypes
        if(reusedTrip == null){
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
        }else{
            spinner_category.setVisibility(View.GONE);
            View thirdDividerView = view.findViewById(R.id.thirdDivider);
            thirdDividerView.setVisibility(View.GONE);
            LinearLayout thirdSection = (LinearLayout)view.findViewById(R.id.thirdSection);
            thirdSection.setVisibility(View.GONE);

        }


        FloatingActionButton button_submit = (FloatingActionButton)view.findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);

    }


    private void setUpArrivalDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        arrivalDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                DatePicker departureDatepicker = departureDatePickerDialog.getDatePicker();

                //save the values of the currently set depdate
                int depDay = departureDatepicker.getDayOfMonth();
                int depMonth = departureDatepicker.getMonth();
                int depYear = departureDatepicker.getYear();
                GregorianCalendar currentDepartureDate = new GregorianCalendar(depYear, depMonth, depDay);


                //Set mindate of departure datepicker to new arrival date + 1
                GregorianCalendar newdepartureMinDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                newdepartureMinDate.add(Calendar.DATE,1);

                //SET to 0 first -> workaround to be able to update mindate
                departureDatepicker.setMinDate(0);
                departureDatepicker.setMinDate(newdepartureMinDate.getTimeInMillis());

                //if departure mindate is now after currently set depDate, the depDate has changed -> update textfield
                long depCurrentMillis = currentDepartureDate.getTimeInMillis();
                long depMinimumMillis = newdepartureMinDate.getTimeInMillis();
                if(depCurrentMillis < depMinimumMillis){
                    editText_departure.setText(dateFormatter.format(newdepartureMinDate.getTime()));
                }


                Calendar arrivalDate = Calendar.getInstance();
                arrivalDate.set(year, monthOfYear, dayOfMonth);
                editText_arrival.setText(dateFormatter.format(arrivalDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        arrivalDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
    }

    private void setUpDepartureDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.DATE, 5);
        departureDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_departure.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DATE, 1);
        departureDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
    }

    @Override
    public void onStart() {

        super.onStart();

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
    }


    /**
     * Nach klick auf den submitbutton werden alle daten des Formulars gesammelt, geprüft
     * und ein neuer Trip wird in die Datenbank eingefügt
     */
    public void submitTrip(){
        String autocompleterText = autocompleter.getText().toString();
        String[] separated = autocompleterText.split(",");
        //City was set and is correct format -> City, Country
        if(!autocompleterText.equals(getResources().getString(R.string.city_default)) && separated.length == 2){
            final String city = separated[0].trim();
            final String country= separated[1].trim();

            //two dates were selected
            final DateTime startDate = Utils.stringToDatetime(editText_arrival.getText().toString());
            final DateTime endDate = Utils.stringToDatetime(editText_departure.getText().toString());
            if (endDate.isAfter(startDate)) {

                //check if we Come from an OLD trip  -> reuse the categories as well as the listItems
                if(reusedTrip == null) {

                    //Check which category has been selected
                    TravelType type1 = TravelType.NO_TYPE;
                    TravelType type2 = TravelType.NO_TYPE;
                    for (TravelType type : TravelType.values()) {
                        if (type.getStringValue().equals(spinner_category.getSelectedItem().toString())) {
                            type1 = type;
                        }
                        if (spinner_category2 != null) {
                            if (type.getStringValue().equals(spinner_category2.getSelectedItem().toString())) {
                                type2 = type;
                            }
                        }
                    }
                    final TravelType type_one = type1;
                    final TravelType type_two = type2;

                    WeatherTask weathertask = new WeatherTask();
                    weathertask.delegate = new AsyncWeatherResponse() {
                        @Override
                        public void weatherProcessFinish(Weather output) {

                            Weather weather = output;

                            int relevantDayStartIndex = -1;
                            int relevantDayEndIndex = -1;
                            for (int i = 0; i < weather.days.size(); i++) {
                                DateTime date = weather.days.get(i).getDate();
                                if (DateTimeComparator.getDateOnlyInstance().compare(startDate, date) == 0) {
                                    System.out.println("Startdate is " + date.toString("dd.MM.yyyy"));
                                    relevantDayStartIndex = i;
                                    relevantDayEndIndex = weather.days.size() - 1;
                                }
                            }

                            if (relevantDayStartIndex != -1) {
                                for (int z = 0; z < weather.days.size(); z++) {
                                    DateTime date = weather.days.get(z).getDate();
                                    if (DateTimeComparator.getDateOnlyInstance().compare(endDate, date) == 0) {
                                        System.out.println("Enddate is " + date.toString("dd.MM.yyyy"));
                                        relevantDayEndIndex = z;
                                    }
                                }

                                weather.days = new ArrayList<Day>(weather.days.subList(relevantDayStartIndex, relevantDayEndIndex));
                                //keine relevanten wetterdaten verfügbar
                            } else {

                                weather = null;

                            }

                            putTripInDatabase(weather, type_one, type_two, city, country, startDate, endDate);
                        }


                        @Override
                        public void weatherProcessFailed() {
                            Log.d("New Trip Frag", "Weather Process Failed");
                            Toast.makeText(getActivity(), "Cant fetch weather data, no internet connection", Toast.LENGTH_SHORT).show();
                            putTripInDatabase(null, type_one, type_two, city, country, startDate, endDate);
                        }
                    };
                    weathertask.execute(new String[]{city, country});

                }else{
                    //refresh the old trip that will be reused
                    putReusedTripInDatabase(city, country, startDate, endDate);
                }
            }else{
                Toast.makeText(getActivity(), "Abreise Datum vor Anreise Datum", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getActivity(), "Wähle ein Reiseziel", Toast.LENGTH_SHORT).show();
        }
    }

    private void putTripInDatabase(Weather weather,TravelType type_one,TravelType type_two,String city, String country,DateTime startDate,DateTime endDate) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String gender = sharedPref.getString(getString(R.string.saved_gender), "not_selected");

        TripItems tripItems = configureTripItems(gender, weather, type_one, type_two);

        TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        tripDBAdapter.insertTrip(tripItems, city, country, startDate, endDate, type_one, type_two, true);
        tripDBAdapter.close();

        moveToLandingPage();
    }

    public void putReusedTripInDatabase(String city, String country,DateTime startDate,DateTime endDate){


        TravelType type_one = reusedTrip.getType1();
        TravelType type_two = reusedTrip.getType2();

        TripItems tripItems = reusedTrip.getTripItems();
        tripItems.cleanItems();


        TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        tripDBAdapter.insertTrip(tripItems, city, country, startDate, endDate, type_one, type_two, true);
        tripDBAdapter.close();

        moveToLandingPage();

    }

    public void moveToLandingPage(){
                /* Clear Backstack, so User cant go back after submission, reason to do this here
        otherwise fragment is no longer attached to activity, when weather async task finishs*/
        ((MainActivity) getActivity()).clearBackstack();

        // TODO CHECK this error, maybe better to just call a setActiveTrip Method in MainActivity
        //java.lang.NullPointerException: Attempt to invoke virtual method 'prak.travelerapp.TripDatabase.model.Trip prak.travelerapp.MainActivity.checkActiveTrip()' on a null object reference
        Trip activeTrip = ((MainActivity) getActivity()).checkActiveTrip();
        ((MainActivity)getActivity()).updateMenueRemainingItems(activeTrip);
        Fragment landingFragment = new LandingFragment();
        ((MainActivity) getActivity()).setUpFragment(landingFragment,false);
        ((MainActivity) getActivity()).setUpNotificationService(activeTrip.getStartdate());
        if(reusedTrip != null){
            ((MainActivity) getActivity()).setUpMenu(1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        if (spinner.getId() == R.id.spinner_category){
            LinearLayout secondTripType = (LinearLayout) getView().findViewById(R.id.fourthSection);
            secondTripType.setVisibility(LinearLayout.VISIBLE);
            setUpSecondTripType();
        } else {
            // User made first select
            firstSelect = false;
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


        DateTime startDate = Utils.stringToDatetime(editText_arrival.getText().toString());
        DateTime endDate = Utils.stringToDatetime(editText_departure.getText().toString());

        //value for temprature -> 0 if normal, 1 if warm, -1 if cold
        int tempValue = 0;  // default
        boolean isRaining = false;
        if (weather != null) {
            isRaining = weather.isRaining();
            int averageTemp = weather.getAverageTemp(startDate, endDate);
            // Setup list for cold temperature
            if (averageTemp < 6) {
                tempValue = -1;
            }
            // Setup list for warm temperature
            if (averageTemp > 19) {
                tempValue = 1;
            }
        }

        ItemDBAdapter itemDB = new ItemDBAdapter(getActivity());
        itemDB.createDatabase();
        itemDB.open();
        ArrayList<Integer> itemIDs  = itemDB.findItemIDs(genderValue,isRaining,tempValue,type1,type2);
        TripItems items = new TripItems();

        for(Integer id : itemIDs){
            items.addItem(id);
        }

        String itemString = items.makeString();
        System.out.println(itemString);
        return items;
    }

}
