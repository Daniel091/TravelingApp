package prak.travelerapp.Database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import prak.travelerapp.R;


/**
 * Created by marcel on 26.11.15.
 */
public class ItemViewActivity extends AppCompatActivity {

    // Log Tag
    public static final String LOG_TAG = ItemViewActivity.class.getSimpleName();

    // Instanz der Datenbank erzeugen
    private Datasource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        dataSource = new Datasource(this);

        dataSource.open();
        // Nur zum testen
        Dataset dataSet = dataSource.createDataset("Testitem",0,0,0,0,0,0,0,0,0,0,0,0);

        // Zeigt alle Einträge in der ListView
        showAllListEntries();
        dataSource.close();

    }

    private void showAllListEntries () {
        List<Dataset> dataSetList = dataSource.getAllDatasets();

        ArrayAdapter<Dataset> dataSetArrayAdapter = new ArrayAdapter<> (
                this,
                R.layout.list_item,
                dataSetList);

        ListView dataSetsListView = (ListView) findViewById(R.id.item_list_view);
        dataSetsListView.setAdapter(dataSetArrayAdapter);
    }

}
