package prak.travelerapp;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import prak.travelerapp.Autocompleter.CityAutoCompleteView;
import prak.travelerapp.Autocompleter.database.CityDBAdapter;
import prak.travelerapp.Autocompleter.model.City;

/**
 * Created by Michael on 12.12.15.
 */
public class NewTripFragment extends Fragment implements View.OnClickListener,TextWatcher {

    private ImageButton button_hamburger;
    private Spinner spinner_category;
    private CityAutoCompleteView autocompleter;
    // adapter for auto-complete
    private ArrayAdapter<String> autocompleteAdapter;
    // for database operations
    private CityDBAdapter cityDB;

    private EditText editText_arrival;
    private EditText editText_departure;
    private DatePickerDialog arrivalDatePickerDialog;
    private DatePickerDialog departureDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    String[] items = new String[] {};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_trip, container, false);

        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.setOnClickListener(this);

        // autocompletetextview is in activity_main.xml
        autocompleter = (CityAutoCompleteView) view.findViewById(R.id.autocomplete_destination);

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
        editText_arrival = (EditText) view.findViewById(R.id.edittext_arrival);
        editText_arrival.setInputType(InputType.TYPE_NULL);

        editText_departure = (EditText) view.findViewById(R.id.edittext_departure);
        editText_departure.setInputType(InputType.TYPE_NULL);

        editText_arrival.setOnClickListener(this);
        editText_departure.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        arrivalDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_arrival.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        departureDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_departure.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
}
