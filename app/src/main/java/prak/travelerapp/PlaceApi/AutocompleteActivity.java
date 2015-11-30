package prak.travelerapp.PlaceApi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import prak.travelerapp.R;

public class AutocompleteActivity extends Activity {
    private AutoCompleteTextView autocompleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        prepareViews();
    }

    private void prepareViews() {
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getBaseContext(), R.layout.autocomplete_list_item));
    }
}
