package prak.travelerapp.Autocompleter;

/**
 * Created by Michael on 05.12.15.
 */

    import android.content.Context;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.widget.ArrayAdapter;

    public class CityAutocompleteTextChangedListener implements TextWatcher{

        public static final String TAG = "CityAutocompleteTextChangedListener.java";
        Context context;

        public CityAutocompleteTextChangedListener(Context context){
            this.context = context;
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence userInput, int start, int before, int count) {

            // if you want to see in the logcat what the user types
            //Log.d("m", "User input: " + userInput);

            CityAutocompleteActivity autocompleteActivity = ((CityAutocompleteActivity) context);

            // query the database based on the user input
            autocompleteActivity.item = autocompleteActivity.getItemsFromDb(userInput.toString());

            // update the adapater
            autocompleteActivity.myAdapter.notifyDataSetChanged();
            autocompleteActivity.myAdapter = new ArrayAdapter<String>(autocompleteActivity, android.R.layout.simple_dropdown_item_1line, autocompleteActivity.item);
            autocompleteActivity.myAutoComplete.setAdapter(autocompleteActivity.myAdapter);

        }

    }
