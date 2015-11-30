package prak.travelerapp.Database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import prak.travelerapp.R;

/**
 * Created by marcel on 26.11.15.
 */
public class ItemViewActivity extends AppCompatActivity {

    // Datenbank
    private Datasource dataSource;
    public static final String LOG_TAG = ItemViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        dataSource = new Datasource(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Dataset testSet = dataSource.createDataset("Testitem", 1, 2, 1, 1, 0, 0, 0, 0, 1, 0, 0);
        Log.d(LOG_TAG, "Inhalt der Testmemo: " + testSet.toString());

        Dataset dataSet = dataSource.createDataset("WeiteresTestitem", 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0);
        Log.d(LOG_TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(LOG_TAG, "ID: " + dataSet.getItemID() + ", Inhalt: " + dataSet.toString());

        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void showAllListEntries () {
        List<Dataset> dataSetList = dataSource.getAllDatasets();

        ArrayAdapter<Dataset> dataSetArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                dataSetList);

        ListView dataSetsListView = (ListView) findViewById(R.id.item_list_view);
        dataSetsListView.setAdapter(dataSetArrayAdapter);
    }
}
