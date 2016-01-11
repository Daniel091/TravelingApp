package prak.travelerapp;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import prak.travelerapp.ItemDatabase.Dataset;
import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.ItemList.ExpandableListAdapter;
import prak.travelerapp.ItemList.ItemCheckedListener;
import prak.travelerapp.ItemList.ListItem;
import prak.travelerapp.TripDatabase.TripDBAdapter;
import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.Tupel;

/**
 * Fragment, das uns die Liste anzeigt und verschiedene Funktionalitäten zur Verfügung stellt
 */
public class ItemViewFragment extends Fragment implements AdapterView.OnItemSelectedListener,ItemCheckedListener{
    private ItemDBAdapter itemDBAdapter;
    private TripDBAdapter tripDBAdapter;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    int[] openedSections = new int[5];
    ArrayList<Dataset> itemList;
    private LayoutInflater inflater;
    private ViewGroup container;

    // UI Elemente für das Popup Window
    private PopupWindow dummyPopup;
    private EditText userInput;
    private static final String[]paths = {"Kleidung", "Hygiene", "Equipment", "Dokumente", "Sonstiges"};
    private ImageView imageView_location;
    private ImageButton button_hamburger;
    private TextView tripCity;
    private FloatingActionButton buttonAddItem;
    private Trip activeTrip;
    private CheckBox allTravelTypes;

    // Werte für das vom User hinzugefügte Item
    private String customItem;
    private int customCat;
    private int strandurlaub;
    private int staedtetrip;
    private int skifahren;
    private int wandern;
    private int geschaeftsreise;
    private int partyurlaub;
    private int camping;
    private int festival;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_item_view, container, false);

        tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        activeTrip = tripDBAdapter.getActiveTrip();

        prepareViews(view);
        prepareListeners();
        tripCity.setText(activeTrip.getCity());

        itemDBAdapter = new ItemDBAdapter(getActivity());
        itemDBAdapter.createDatabase();
        itemDBAdapter.open();

        //update die anzahl der verbleibenden Items im Menü
        ((MainActivity)getActivity()).updateMenueRemainingItems(activeTrip);

        //sort the tripitems with ID ascending
        ArrayList<Tupel> tripitems = activeTrip.getTripItems().getItems();
        Collections.sort(tripitems);
        //sortiere die items ebenfalls entsprechend der ID aufsteigend
        itemList = itemDBAdapter.getItems(activeTrip.getTripItems());
        Collections.sort(itemList);
        showAllListEntries(itemList, tripitems);
        return view;
    }

    private void prepareListeners() {
        button_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showDummyPopup();
                showPopup(v);
            }
        });
        expListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked on child, function longClick is executed
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    ListItem listItem = listAdapter.getChild(groupPosition, childPosition);

                    showDummyPopup();
                    showDeletePopup(view, listItem, groupPosition, childPosition);

                    return true;
                }
                return false;
            }

        });
        //prevent default scrolling action on Group toggle
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    openedSections[groupPosition] = 0;
                } else {
                    boolean animateExpansion = false;
                    parent.expandGroup(groupPosition, animateExpansion);
                    openedSections[groupPosition] = 1;
                }
                //telling the listView we have handled the group click, and don't want the default actions.
                return true;
            }
        });
    }

    private void prepareViews(View view) {
        expListView = (ExpandableListView) view.findViewById(R.id.item_list_view);
        button_hamburger = (ImageButton) view.findViewById(R.id.button_hamburger);
        buttonAddItem = (FloatingActionButton) view.findViewById(R.id.button_add_item);
        tripCity = (TextView) view.findViewById(R.id.textview_tripCity);
        imageView_location = (ImageView) view.findViewById(R.id.locationImage);
        setLocationImage();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        tripDBAdapter = new TripDBAdapter(getActivity());
        tripDBAdapter.open();
        itemDBAdapter = new ItemDBAdapter(getActivity());
        itemDBAdapter.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        tripDBAdapter.updateTripItems(activeTrip);
        itemDBAdapter.close();

        if(tripDBAdapter != null){
            tripDBAdapter.close();
        }
    }

    /**
     * Methode zum anzeigen der Datenbankeinträge in der Liste
     * @param items
     *
     */
    private void showAllListEntries (ArrayList<Dataset> items,ArrayList<Tupel> tripitems) {

        // vorbereiten der Liste ----------------------------------
        List<String> listDataHeader  = new ArrayList<String>();
        listDataHeader.add("Kleidung");
        listDataHeader.add("Hygiene");
        listDataHeader.add("Equipment");
        listDataHeader.add("Dokumente");
        listDataHeader.add("Sonstiges");


        HashMap<String, List<ListItem>> listDataChild = new HashMap<>();

        // Unterlisten-Kategorien
        List<ListItem> kleidung = new ArrayList<>();
        List<ListItem> hygiene = new ArrayList<>();
        List<ListItem> equipment = new ArrayList<>();
        List<ListItem> dokumente = new ArrayList<>();
        List<ListItem> sonstiges = new ArrayList<>();

        // erstellt Listitems aus den daten der ItemList und den tripitems
        for(int i = 0; i < items.size(); i++){
            Dataset dataSet = items.get(i);
            int id = dataSet.getItemID();
            String name = dataSet.getItemName();
            boolean checked = tripitems.get(i).getY() == 1;

            ListItem item = new ListItem(id,name,checked);
            if (dataSet.getKategorie() == 0) {
                kleidung.add(item);
            } else if (dataSet.getKategorie() == 1) {
                hygiene.add(item);
            } else if (dataSet.getKategorie() == 2) {
                equipment.add(item);
            } else if (dataSet.getKategorie() == 3) {
                dokumente.add(item);
            } else if (dataSet.getKategorie() == 4) {
                sonstiges.add(item);
            }
        }

        listDataChild.put(listDataHeader.get(0), kleidung);
        listDataChild.put(listDataHeader.get(1), hygiene);
        listDataChild.put(listDataHeader.get(2), equipment);
        listDataChild.put(listDataHeader.get(3), dokumente);
        listDataChild.put(listDataHeader.get(4), sonstiges);
        // ---------------------------------------------------

        // setting list adapter
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        listAdapter.listener = this;

        expListView.setAdapter(listAdapter);

        //open previous opened sections
        for(int i = 0;i < openedSections.length;i++){
            if(openedSections[i] == 1){
                expListView.expandGroup(i,false);
            }
        }
    }

    /**
     * Checkbox eines items wurde gecheckt oder unchecked
     * @param clickedItem
     */
    @Override
    public void itemClicked(ListItem clickedItem) {
        //update checked state of tripitems
        activeTrip.getTripItems().getItem(clickedItem.getId()).setY(clickedItem.isChecked() ? 1 : 0);
        //update die anzahl der verbleibenden Items im Menü
        ((MainActivity)getActivity()).updateMenueRemainingItems(activeTrip);
    }

    /**
     * Nur ein Dummy Popup zum dimmen des Backgrounds bei Aufruf des eigentlichen Popups
     */
    public void showDummyPopup() {
        final View popupDummyView = inflater.inflate(R.layout.dummy_popup, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int windowHeight = displaymetrics.heightPixels;
        int windowWidth = displaymetrics.widthPixels;

        dummyPopup = new PopupWindow(popupDummyView,
               windowWidth, windowHeight, false);
        dummyPopup.showAtLocation(popupDummyView, Gravity.NO_GRAVITY, 0, 0);
    }

    // Popup zum eingeben eines neuen Items
    public void showPopup(final View anchorView) {
        final View popupView = inflater.inflate(R.layout.add_item_popup, container, false);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Eingabefeld
        userInput = (EditText) popupView.findViewById(R.id.userInput);
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        // Spinner
        Spinner spinner = (Spinner) popupView.findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(popupView.getContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        strandurlaub = 0;
        staedtetrip = 0;
        skifahren = 0;
        wandern = 0;
        geschaeftsreise = 0;
        partyurlaub = 0;
        camping = 0;
        festival = 0;

        allTravelTypes = (CheckBox) popupView.findViewById(R.id.checkBoxTravelType);
        allTravelTypes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (allTravelTypes.isChecked()) {
                    setTravelType();
                } else {
                    strandurlaub = 0;
                    staedtetrip = 0;
                    skifahren = 0;
                    wandern = 0;
                    geschaeftsreise = 0;
                    partyurlaub = 0;
                    camping = 0;
                    festival = 0;

                    TravelType tripType1 = activeTrip.getType1();
                    String tripType1asString = tripType1.getStringValue();
                    getTravelType(tripType1asString);

                    TravelType tripType2 = activeTrip.getType2();
                    String tripType2asString = tripType2.getStringValue();
                    getTravelType(tripType2asString);
                }
            }
        });

        TravelType tripType1 = activeTrip.getType1();
        String tripType1asString = tripType1.getStringValue();
        getTravelType(tripType1asString);

        TravelType tripType2 = activeTrip.getType2();
        String tripType2asString = tripType2.getStringValue();
        getTravelType(tripType2asString);


        // Button zum finalen hinzufügen eines Items
        Button finalAddButton = (Button) popupView.findViewById(R.id.button_final_add);
        finalAddButton.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                customItem = userInput.getText().toString();

                // Fängt leeren User Input ab und bringt einen Hinweis
                if (userInput.length() == 0) {
                    Toast.makeText(popupView.getContext(), "Bitte Namen des Items eingeben", Toast.LENGTH_SHORT).show();
                } else {
                    Dataset customDataSet = itemDBAdapter.createDataset(customItem, 0, 0, strandurlaub, staedtetrip, skifahren,
                            wandern, geschaeftsreise, partyurlaub, camping, festival, customCat);
                    itemList.add(customDataSet);
                    activeTrip.getTripItems().addItem(customDataSet.getItemID());

                    ((MainActivity) getActivity()).updateMenueRemainingItems(activeTrip);

                    showAllListEntries(itemList, activeTrip.getTripItems().getItems());

                    //get position of the new item -> scroll to position
                    int groupPosition = listAdapter.getGroupPositionForItem(customDataSet.getItemID());
                    int childPosition = listAdapter.getChildPositionForItem(customDataSet.getItemID());
                    expListView.setSelectedChild(groupPosition,childPosition,true);

                    popupWindow.dismiss();
                    Toast.makeText(popupView.getContext(), "Gegenstand hinzugefügt", Toast.LENGTH_SHORT).show();
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
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

    // Popup zum löschen eines Items
    private void showDeletePopup(View anchorView,ListItem listItem, final int groupPosition, final int childPosition) {
        final ListItem item = listItem;
        final View popupView = inflater.inflate(R.layout.delete_item_popup, container, false);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button delete_for_trip = (Button) popupView.findViewById(R.id.button_delete_for_trip);
        delete_for_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove item from itemlist
                for (Iterator<Dataset> it = itemList.iterator(); it.hasNext(); ) {
                    Dataset dataset = it.next();
                    if (dataset.getItemID() == item.getId()) {
                        it.remove();
                        break;
                    }
                }
                //remove item from tripitems
                for (Iterator<Tupel> it = activeTrip.getTripItems().getItems().iterator(); it.hasNext(); ) {
                    Tupel tupel = it.next();
                    if (tupel.getX() == item.getId()) {
                        it.remove();
                        break;
                    }
                }
                ((MainActivity) getActivity()).updateMenueRemainingItems(activeTrip);

                showAllListEntries(itemList, activeTrip.getTripItems().getItems());
                expListView.setSelectedChild(groupPosition, childPosition - 1, false);
                popupWindow.dismiss();

                Toast.makeText(popupView.getContext(), "Gegenstand gelöscht", Toast.LENGTH_SHORT).show();
            }
        });

        Button delete_final_button = (Button) popupView.findViewById(R.id.button_delete_final);
        delete_final_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove item from itemlist
                for (Iterator<Dataset> it = itemList.iterator(); it.hasNext(); ) {
                    Dataset dataset = it.next();
                    if (dataset.getItemID() == item.getId()) {
                        it.remove();
                        break;
                    }
                }
                //remove item from tripitems
                for (Iterator<Tupel> it = activeTrip.getTripItems().getItems().iterator(); it.hasNext(); ) {
                    Tupel tupel = it.next();
                    if (tupel.getX() == item.getId()) {
                        it.remove();
                        break;
                    }
                }
                ((MainActivity)getActivity()).updateMenueRemainingItems(activeTrip);

                itemDBAdapter.deleteItem(item.getId());
                showAllListEntries(itemList, activeTrip.getTripItems().getItems());
                expListView.setSelectedChild(groupPosition,childPosition-1,false);
                popupWindow.dismiss();
                Toast.makeText(popupView.getContext(),"Gegenstand gelöscht", Toast.LENGTH_SHORT).show();
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
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        int location[] = new int[2];
        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

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
            case 4:
                // Was passiert wenn "Sonstiges" ausgewählt wird
                customCat = 4;
                break;
        }
    }

    // Regelt was passiert, wenn keine Kategorie ausgewählt wurde
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void setLocationImage(){
        SharedPreferences sharedPref = getActivity().getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String path_fromPref = sharedPref.getString(getString(R.string.saved_image_path), "default");

        //check if default pic or API picture is used for this trip
        if (path_fromPref.equals("image_by_categorie")){
            int resID = Utils.getDefaultPicResID(activeTrip.getType1());
            imageView_location.setImageResource(resID);
        } else {
            Bitmap image = Utils.loadImageFromStorage(path_fromPref);
            //if loading API image fails -> use default pic
            if (image == null) {
                int resID = Utils.getDefaultPicResID(activeTrip.getType1());
                imageView_location.setImageResource(resID);
            } else {
                imageView_location.setImageBitmap(image);
            }
        }
    }

    /**
     * Setzt den Reisetyp für alle auf Reisetypen
     */
    public void setTravelType() {
        strandurlaub = 1;
        staedtetrip = 1;
        skifahren = 1;
        wandern = 1;
        geschaeftsreise = 1;
        partyurlaub = 1;
        camping = 1;
        festival = 1;
    }
    /**
     * Weist einem Gegenstand den passenden Reisetyp zu
     * @param travelType
     */
    public void getTravelType(String travelType) {
        switch (travelType) {
            case "Keine Kategorie":
                break;
            case "Strandurlaub":
                strandurlaub = 1;
                break;
            case "Städtetrip":
                staedtetrip = 1;
                break;
            case "Skifahren":
                skifahren = 1;
                break;
            case "Wandern":
                wandern = 1;
                break;
            case "Geschäftsreise":
                geschaeftsreise = 1;
                break;
            case "Partyurlaub":
                partyurlaub = 1;
                break;
            case "Camping":
                camping = 1;
                break;
            case "Festival":
                festival = 1;
                break;
        }

    }
}
