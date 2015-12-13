package prak.travelerapp.PlaceApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import prak.travelerapp.R;

public class PlacePickerFragment extends Fragment {
    /**
     * Request code passed to the PlacePicker intent to identify its result when it returns.
     */
    private static final int REQUEST_PLACE_PICKER = 1;

    private TextView nameView,adressView;
    private Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_picker, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        nameView = (TextView) getView().findViewById(R.id.name);
        adressView = (TextView) getView().findViewById(R.id.place);
        btn = (Button) getView().findViewById(R.id.placeButton);

        setListeners();
    }

    private void setListeners() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fragment Place Picker", "Button clicked");
                constructIntent();
            }
        });
    }

    public void constructIntent() {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(getActivity());
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(new Activity(), "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == getActivity().RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, getActivity());

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            nameView.setText(name);
            adressView.setText(address);
            //mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
