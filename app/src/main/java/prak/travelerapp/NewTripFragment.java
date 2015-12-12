package prak.travelerapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by Michael on 12.12.15.
 */
public class NewTripFragment extends Fragment implements View.OnClickListener {

    ImageButton button_hamburger;
    Spinner spinner_category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_trip, container, false);

        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.setOnClickListener(this);

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
        }
    }
}
