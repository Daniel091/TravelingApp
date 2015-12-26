package prak.travelerapp;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import prak.travelerapp.ItemDatabase.Dataset;
import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.ItemList.ExpandableListAdapter;
import prak.travelerapp.ItemList.ItemCheckedListener;
import prak.travelerapp.ItemList.ListItem;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.Trip;

/**
 * Fragment, dass uns die Liste anzeigt und verschiedene Funktionalitäten zur Verfügung stellt
 */
public class ItemViewFragment extends Fragment implements AdapterView.OnItemSelectedListener,ItemCheckedListener{

    // Log Tag
    public static final String LOG_TAG = ItemViewFragment.class.getSimpleName();

    // Instanzen
    ItemDBAdapter itemDBAdapter;
    TripDBAdapter tripDBAdapter;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    // Holt Items aus der DB
    List<Dataset> itemList;

    // Layout
    private LayoutInflater inflater;

    // Container
    ViewGroup container;

    // UI Elemente für das Popup Window
    private PopupWindow dummyPopup;
    private int windowWidth;
    private int windowHeight;
    private EditText userInput;
    private Spinner spinner;
    private static final String[]paths = {"Kleidung", "Hygiene", "Equipment", "Dokumente"};
    private Button finalAddButton;
    private ImageButton button_hamburger;
    private TextView tripCity;
    private Trip activeTrip;

    // Werte für das vom User hinzugefügte Item
    private String customItem; // Name des manuellen Icons
    private int customCat;  // Gewählte Kategorie des ausgewählten Icons

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_item_view, container, false);

        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        button_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        FloatingActionButton buttonAddItem = (FloatingActionButton) view.findViewById(R.id.button_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showDummyPopup(v);
                showPopup(v);
            }
        });
        tripCity = (TextView) view.findViewById(R.id.textview_tripCity);
        activeTrip = ((MainActivity) getActivity()).getActive_trip();
        tripCity.setText(activeTrip.getCity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (activeTrip == null) {
            tripDBAdapter = new TripDBAdapter(getActivity());
            tripDBAdapter.open();
            activeTrip = tripDBAdapter.getActiveTrip();
        }

        itemDBAdapter = new ItemDBAdapter(getActivity());
        itemDBAdapter.createDatabase();
        itemDBAdapter.open();
        itemList = itemDBAdapter.getItems(activeTrip.getTripItems());
        showAllListEntries(itemList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       itemDBAdapter.close();
    }

    /**
     * Methode zum anzeigen der Datenbankeinträge in der Liste
     * @param items
     */
    private void showAllListEntries (List<Dataset> items) {

        expListView = (ExpandableListView) getView().findViewById(R.id.item_list_view);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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

        // vorbereiten der Liste ----------------------------------
        List<String> listDataHeader  = new ArrayList<String>();
        listDataHeader.add("Kleidung");
        listDataHeader.add("Hygiene");
        listDataHeader.add("Equipment");
        listDataHeader.add("Dokumente");


        HashMap<String, List<ListItem>> listDataChild = new HashMap<String, List<ListItem>>();

        // Unterlisten-Kategorien
        List<ListItem> kleidung = new ArrayList<ListItem>();
        List<ListItem> hygiene = new ArrayList<ListItem>();
        List<ListItem> equipment = new ArrayList<ListItem>();
        List<ListItem> dokumente = new ArrayList<ListItem>();

        // selektiert die items aus dem Array und ordnet sie der passenden Unterliste ein
        for(int i = 0; i < items.size(); i++){
            Dataset dataSet = items.get(i);
            int id = dataSet.getItemID();
            String name = dataSet.getItemName();
            //TODO set checked value from TripItems
            boolean checked = false;

            ListItem item = new ListItem(id,name,checked);
            if (dataSet.getKategorie() == 0) {
                kleidung.add(item);
            } else if (dataSet.getKategorie() == 1) {
                hygiene.add(item);
            } else if (dataSet.getKategorie() == 2) {
                equipment.add(item);
            } else if (dataSet.getKategorie() == 3) {
                dokumente.add(item);
            }
        }

        listDataChild.put(listDataHeader.get(0), kleidung);
        listDataChild.put(listDataHeader.get(1), hygiene);
        listDataChild.put(listDataHeader.get(2), equipment);
        listDataChild.put(listDataHeader.get(3), dokumente);
        // ---------------------------------------------------

        // setting list adapter
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        listAdapter.listener = this;

        expListView.setAdapter(listAdapter);
        //expListView.setAdapter(dataSetArrayAdapter);
    }

    @Override
    public void itemClicked(ListItem item) {

        System.out.println(item.getId() + " " + item.getName() + " " + item.isChecked());

    }

    /**
     * Nur ein Dummy Popup zum dimmen des Backgrounds bei Aufruf des eigentlichen Popups
     */
    public void showDummyPopup(View anchorView) {

        final View popupDummyView = inflater.inflate(R.layout.dummy_popup, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;

        dummyPopup = new PopupWindow(popupDummyView,
               windowWidth, windowHeight, false);
        dummyPopup.showAtLocation(popupDummyView, Gravity.NO_GRAVITY, 0, 0);
    }

    // Popup zum eingeben eines neuen Items
    public void showPopup(final View anchorView) {

        final View popupView = inflater.inflate(R.layout.add_item_popup, container, false);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /**
         * Elemente des PopUp windows
         */

        // Eingabefeld
        userInput = (EditText) popupView.findViewById(R.id.userInput);

        // Spinner
        spinner = (Spinner) popupView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(popupView.getContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Button zum finalen hinzufügen eines Items
        finalAddButton = (Button) popupView.findViewById(R.id.button_final_add);
        finalAddButton.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                customItem = userInput.getText().toString();

                // Fängt leeren User Input ab und bringt einen Hinweis
                if (userInput.length() == 0) {
                    Toast.makeText(popupView.getContext(), "Bitte Namen des Items eingeben", Toast.LENGTH_SHORT).show();
                } else {
                    Dataset customDataSet = itemDBAdapter.createDataset(customItem, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, customCat);
                    itemList.add(customDataSet);

                    showAllListEntries(itemList);

                    popupWindow.dismiss();
                }
            }
        });

        // Listener, der abfängt sobald das popup window geschlossen wird und damit automatisch
        // das dummy popup mitschliesst
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dummyPopup.dismiss();
            }
        });

        popupWindow.setFocusable(true);

        // Bei Klick auf Bereich neben dem Popup schliesst es sich
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // holt die Location der View
        anchorView.getLocationOnScreen(location);

        // zeigt das popup window unter der anchor view an
        popupWindow.showAtLocation(anchorView, Gravity.CENTER,
                0, 0);

    }

    // Regelt was passiert, wenn eine Kategorie im Spinner gewählt wurde
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Was passiert wenn "Kleidung" ausgewählt wird
                customCat = 0;
                break;
            case 1:
                // Was passiert wenn "Hygiene" ausgewählt wird
                customCat = 1;
                break;
            case 2:
                // Was passiert wenn "Equipment" ausgewählt wird
                customCat = 2;
                break;
            case 3:
                // Was passiert wenn "Dokumente" ausgewählt wird
                customCat = 3;
                break;
        }
    }

    // Regelt was passiert, wenn keine Kategorie ausgewählt wurde
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Nicht benötigt. Methode muss aber overrided sein.
    }

}
