package prak.travelerapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import prak.travelerapp.ItemDatabase.Dataset;
import prak.travelerapp.ItemDatabase.ItemDBAdapter;
import prak.travelerapp.ItemDatabase.ItemDBHelper;


/**
 * Created by marcel on 26.11.15.
 */
public class ItemViewActivity extends AppCompatActivity {

    // Log Tag
    public static final String LOG_TAG = ItemViewActivity.class.getSimpleName();

    // Instanz vom ItemDBAdapter
    ItemDBAdapter itemDB;

    // Holt Items aus der DB
    List<Dataset> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        activateAddButton();

    }

    protected void onResume() {
        super.onResume();
        itemDB = new ItemDBAdapter(this);
        itemDB.createDatabase();
        itemDB.open();
        itemList = itemDB.getItems();
        showAllListEntries(itemList);
    }

    @Override
    protected void onPause() {
        super.onPause();
       itemDB.close();
    }

    private void showAllListEntries (List<Dataset> items) {
       // List<Dataset> dataSetList = dataSource.getAllDatasets();

        ArrayAdapter<Dataset> dataSetArrayAdapter = new ArrayAdapter<> (
                this,
                R.layout.list_item,
                items);

        ListView dataSetsListView = (ListView) findViewById(R.id.item_list_view);
        dataSetsListView.setAdapter(dataSetArrayAdapter);
    }

    private void activateAddButton() {
        FloatingActionButton buttonAddItem = (FloatingActionButton) findViewById(R.id.button_add_item);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dataset dataSet = itemDB.createDataset("Testitem", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                itemList.add(dataSet);
                showAllListEntries(itemList);
            }
        });

    }


}
