package prak.travelerapp.Database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import prak.travelerapp.R;

/**
 * Created by marcel on 26.11.15.
 */
public class ItemListActivity extends AppCompatActivity {

    // Log Tag
    public static final String LOG_TAG = ItemListActivity.class.getSimpleName();

    // Instanz der Datenbank erzeugen
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        databaseHandler = new DatabaseHandler(this);
        db = databaseHandler.getWritableDatabase();
        databaseHandler.onCreate(db);

    }

    /**
    private void showAllListEntries () {
        List<Dataset> dataSetList = dataSource.getAllDatasets();

        ArrayAdapter<Dataset> dataSetArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                dataSetList);

        ListView dataSetsListView = (ListView) findViewById(R.id.item_list_view);
        dataSetsListView.setAdapter(dataSetArrayAdapter);
    }
     */

}
