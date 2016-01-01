package prak.travelerapp;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.TripDatabase.TripDBAdapter;

public class SettingsFragment extends Fragment {
    private Button button;
    private ImageButton hamburger_button;
    private CheckBox del_history,del_items;
    private Switch aSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        aSwitch = (Switch) view.findViewById(R.id.switchNotifications);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int notification_var = sharedPref.getInt(String.valueOf(R.integer.saved_notification_var), -1);
        // 0 means notification was pushed already, 1 not pushed and enabled, 2 notification disabled
        switch (notification_var){
            case 0:
            case 1:
                aSwitch.setChecked(true);
                break;
            case 2:
                aSwitch.setChecked(false);
                break;
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                int notification_var = sharedPref.getInt(String.valueOf(R.integer.saved_notification_var), -1);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (isChecked) {
                    if (notification_var == 0){
                        editor.putInt(String.valueOf(R.integer.saved_notification_var),0);
                    } else {
                        editor.putInt(String.valueOf(R.integer.saved_notification_var),1);
                    }
                } else {
                    editor.putInt(String.valueOf(R.integer.saved_notification_var),2);
                }
                editor.apply();
            }
        });

        hamburger_button = (ImageButton) view.findViewById(R.id.button_hamburger);
        hamburger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        del_history = (CheckBox) view.findViewById(R.id.checkBoxVergangene);
        del_items = (CheckBox) view.findViewById(R.id.checkBoxCustomItems);

        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (del_history.isChecked()){
                    deleteHistory();
                }
                if (del_items.isChecked()){
                    deleteCustomItems();
                }
            }
        });
        return view;
    }

    private void deleteCustomItems() {
        ItemDBAdapter itemDB = new ItemDBAdapter(getActivity());
        itemDB.open();
        itemDB.createDatabase();
        itemDB.close();
        Toast.makeText(getActivity(),"Deleted Custom Items",Toast.LENGTH_SHORT).show();
        del_items.setChecked(false);
    }

    private void deleteHistory() {
        TripDBAdapter tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        tripDBAdapter.removeAllNonActiveTrips();
        tripDBAdapter.close();
        Toast.makeText(getActivity(),"Deleted History",Toast.LENGTH_SHORT).show();
        del_history.setChecked(false);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

}
