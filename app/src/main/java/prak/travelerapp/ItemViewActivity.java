package prak.travelerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        //open items DB
        ItemDBAdapter itemDB = new ItemDBAdapter(this);
        itemDB.createDatabase();
        itemDB.open();

        //query all items
        List<Dataset> itemList = itemDB.getItems();

        // Nur zum testen
        Dataset dataSet = itemDB.createDataset("Testitem",0,0,0,0,0,0,0,0,0,0,0,0);

        itemList.add(dataSet);

        showAllListEntries(itemList);


        itemDB.close();


        // Zeigt alle Einträge in der ListView

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

}
