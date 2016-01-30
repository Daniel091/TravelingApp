package prak.travelerapp;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class StartFragment extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    private ImageButton button_hamburger;
    private RadioGroup radioGroup_gender;
    private Button button_newTrip;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        Context context = getActivity();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        prepareViews(view);
        prepareListeners();
        return view;
    }

    private void prepareListeners() {
        button_newTrip.setOnClickListener(this);
        button_hamburger.setOnClickListener(this);
        String gender_fromPref = sharedPref.getString(getString(R.string.saved_gender),"not_selected");
        if (gender_fromPref == "not_selected") {
            radioGroup_gender.setOnCheckedChangeListener(this);
        } else {
            if (gender_fromPref.equals("male")){
                radioGroup_gender.check(R.id.radio_male);
            } else {
                radioGroup_gender.check(R.id.radio_female);
            }
            radioGroup_gender.setOnCheckedChangeListener(this);
            Logger.getInstance().log("StartFrag","Got gender from SharedPref " + gender_fromPref);
        }
    }

    private void prepareViews(View view) {
        button_newTrip = (Button) view.findViewById(R.id.button_newTrip);
        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        radioGroup_gender = (RadioGroup) view.findViewById(R.id.radioGroup_gender);
        button_newTrip = (Button) view.findViewById(R.id.button_newTrip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_hamburger:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.button_newTrip:
                if (radioGroup_gender.getCheckedRadioButtonId() != -1) {
                    Fragment newTripFragment = new NewTripFragment();
                    ((MainActivity) getActivity()).setUpFragment(newTripFragment,false);
                }else
                {
                    Toast.makeText(getActivity(), "Bitte wähle ein Geschlecht", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        switch(checkedId){
            case R.id.radio_male:
                editor.putString(getString(R.string.saved_gender), "male");
                editor.apply();
                break;
            case R.id.radio_female:
                editor.putString(getString(R.string.saved_gender), "female");
                editor.apply();
                break;
        }
    }
}
