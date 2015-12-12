package prak.travelerapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Michael on 12.12.15.
 */
public class StartFragment extends Fragment implements View.OnClickListener {

    ImageButton button_hamburger;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.setOnClickListener(this);


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
