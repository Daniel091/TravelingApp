package prak.travelerapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import prak.travelerapp.History.HistoryItemListAdapter;
import prak.travelerapp.ItemDatabase.Dataset;
import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.TripDatabase.model.Trip;

public class TripHistoryListFragment extends Fragment {

    private ExpandableListView listview;
    private ImageButton hamburger_button;
    public Trip trip;
    private ImageView imageView_traveltype;
    private FloatingActionButton reuseList_button;
    private LayoutInflater inflater;
    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_history_list, container, false);

        TextView tripCity = (TextView) view.findViewById(R.id.history_textview_tripCity);
        tripCity.setText(trip.getCity());

        listview = (ExpandableListView) view.findViewById(R.id.history_item_list_view);

        //prevent default scrolling action on Group toggle
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    boolean animateExpansion = false;
                    parent.expandGroup(groupPosition, animateExpansion);
                }
                //telling the listView we have handled the group click, and don't want the default actions.
                return true;
            }
        });

        hamburger_button = (ImageButton) view.findViewById(R.id.button_hamburger);
        hamburger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        reuseList_button = (FloatingActionButton)view.findViewById(R.id.button_reuse_list);
        //if there is an active trip hide the floating action Button
        if(((MainActivity) getActivity()).checkActiveTrip() != null){
            reuseList_button.setVisibility(View.GONE);
        }

        reuseList_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTripFragment newTripFragment = new NewTripFragment();
                newTripFragment.reusedTrip = trip;
                ((MainActivity) getActivity()).setUpFragment(newTripFragment, false);

                //showDummyPopup();
                //showPopup(v);
            }
        });

        imageView_traveltype = (ImageView) view.findViewById(R.id.traveltypeImage);
        int resID = Utils.getDefaultPicResID(trip.getType1());
        imageView_traveltype.setImageResource(resID);

        return view;
    }


    @Override
    public void onStart() {

        super.onStart();

        ItemDBAdapter itemDBAdapter = new ItemDBAdapter(getActivity());
        itemDBAdapter.createDatabase();
        itemDBAdapter.open();

        ArrayList<Dataset> itemList = itemDBAdapter.getItems(trip.getTripItems());

        showAllEntries(itemList);
    }

    public void showAllEntries(ArrayList<Dataset>  itemList){

        // vorbereiten der Liste ----------------------------------
        List<String> listDataHeader  = new ArrayList<String>();
        listDataHeader.add("Kleidung");
        listDataHeader.add("Hygiene");
        listDataHeader.add("Equipment");
        listDataHeader.add("Dokumente");
        listDataHeader.add("Sonstiges");

        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

        // Unterlisten-Kategorien
        List<String> kleidung = new ArrayList<String>();
        List<String> hygiene = new ArrayList<String>();
        List<String> equipment = new ArrayList<String>();
        List<String> dokumente = new ArrayList<String>();
        List<String> sonstiges = new ArrayList<String>();

        // befüllt die Kategorien
        for(int i = 0; i < itemList.size(); i++){
            Dataset dataSet = itemList.get(i);
            String name = dataSet.getItemName();

            if (dataSet.getKategorie() == 0) {
                kleidung.add(dataSet.getItemName());
            } else if (dataSet.getKategorie() == 1) {
                hygiene.add(dataSet.getItemName());
            } else if (dataSet.getKategorie() == 2) {
                equipment.add(dataSet.getItemName());
            } else if (dataSet.getKategorie() == 3) {
                dokumente.add(dataSet.getItemName());
            } else if (dataSet.getKategorie() == 4) {
                sonstiges.add(dataSet.getItemName());
            }
        }

        listDataChild.put(listDataHeader.get(0), kleidung);
        listDataChild.put(listDataHeader.get(1), hygiene);
        listDataChild.put(listDataHeader.get(2), equipment);
        listDataChild.put(listDataHeader.get(3), dokumente);
        listDataChild.put(listDataHeader.get(4), sonstiges);

        HistoryItemListAdapter listAdapter = new HistoryItemListAdapter(getActivity(), listDataHeader, listDataChild);
        listview.setAdapter(listAdapter);

    }

    private void showDummyPopup() {
        final View popupDummyView = inflater.inflate(R.layout.dummy_popup, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int windowHeight = displaymetrics.heightPixels;
        int windowWidth = displaymetrics.widthPixels;

        PopupWindow dummyPopup = new PopupWindow(popupDummyView, windowWidth, windowHeight, false);
        dummyPopup.showAtLocation(popupDummyView, Gravity.NO_GRAVITY, 0, 0);
    }

}
