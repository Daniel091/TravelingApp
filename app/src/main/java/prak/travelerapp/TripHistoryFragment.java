package prak.travelerapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import prak.travelerapp.History.HistoryListAdapter;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.Trip;

public class TripHistoryFragment extends Fragment {


    TripDBAdapter tripDBAdapter;
    private ListView listview;
    private TextView noOldTrips;
    private ImageButton hamburger_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_history, container, false);

        noOldTrips = (TextView) view.findViewById(R.id.history_list_noOldTrips);
        listview = (ListView) view.findViewById(R.id.historyList);
        hamburger_button = (ImageButton) view.findViewById(R.id.button_hamburger);
        hamburger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // get all old Trips
        tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        ArrayList<Trip> oldTrips = tripDBAdapter.getOldTrips();

        // show message if no old trips exist
        if (oldTrips.isEmpty()) {
            noOldTrips.setVisibility(View.VISIBLE);
        } else {
            noOldTrips.setVisibility(View.GONE);
        }

        // reverse array to have the latest trips on top
        Collections.reverse(oldTrips);
        final Trip[] tripArray = oldTrips.toArray(new Trip[oldTrips.size()]);

        HistoryListAdapter adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item,tripArray);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Logger.getInstance().log("TripHistory", tripArray[position].getCity());
                TripHistoryListFragment fragment = new TripHistoryListFragment();
                fragment.trip = tripArray[position];
                ((MainActivity) getActivity()).setUpFragment(fragment, false);

            }
        });
    }

}
