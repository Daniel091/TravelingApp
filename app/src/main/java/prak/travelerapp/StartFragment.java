package prak.travelerapp;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class StartFragment extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    ImageButton button_hamburger;
    RadioGroup radioGroup_gender;
    Button button_newTrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        button_newTrip = (Button) view.findViewById(R.id.button_newTrip);
        button_newTrip.setOnClickListener(this);

        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.setOnClickListener(this);

        radioGroup_gender = (RadioGroup) view.findViewById(R.id.radioGroup_gender);
        radioGroup_gender.setOnCheckedChangeListener(this);

        button_newTrip = (Button) view.findViewById(R.id.button_newTrip);
        button_newTrip.setOnClickListener(this);


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
            case R.id.button_newTrip:
                Fragment newTripFragment = new NewTripFragment();
                ((MainActivity) getActivity()).setUpFragement(newTripFragment);
                break;

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.radio_male:
                Log.d("mw", "male");
                break;
            case R.id.radio_female:
                Log.d("mw", "female");
                break;
        }

    }
}
