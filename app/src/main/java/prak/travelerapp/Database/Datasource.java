package prak.travelerapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcel on 24.11.15.
 *
 * Verantwortlich für alle Zugriffe auf die Datenbank
 * Schreiben von Datensätzen in die Tabelle und auslesen dergleichen
 */
public class Datasource {

    private static final String LOG_TAG = Datasource.class.getSimpleName();

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    private String[] columns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_SEX,
            DatabaseHelper.COLUMN_TROCKEN,
            DatabaseHelper.COLUMN_STAEDTETRIP,
            DatabaseHelper.COLUMN_STRANDURLAUB,
            DatabaseHelper.COLUMN_SKIFAHREN,
            DatabaseHelper.COLUMN_WANDERN,
            DatabaseHelper.COLUMN_GESCHAEFTSREISE,
            DatabaseHelper.COLUMN_PARTYURLAUB,
            DatabaseHelper.COLUMN_CAMPING,
            DatabaseHelper.COLUMN_FESTIVAL
    };

    public Datasource(Context context) {
        Log.d(LOG_TAG, "Datasource erzeugt DatabaseHelper");
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird angefragt.");
        database = databaseHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        databaseHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DatabaseHelpers geschlossen.");
    }

    public Dataset createDataset(String itemName, int basic, int geschlecht, int trocken,
                                 int strandurlaub, int staedtetrip, int skifahren, int wandern, int geschaeftsreise, int partyurlaub, int camping, int festival) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, itemName);
        //values.put(DatabaseHelper.COLUMN_BASIC, basic);
        //values.put(DatabaseHelper.COLUMN_SEX, geschlecht);
        //values.put(DatabaseHelper.COLUMN_TROCKEN, trocken);
        //values.put(DatabaseHelper.COLUMN_STRANDURLAUB, strandurlaub);
        //values.put(DatabaseHelper.COLUMN_STAEDTETRIP, staedtetrip);
        //values.put(DatabaseHelper.COLUMN_SKIFAHREN, skifahren);
        //values.put(DatabaseHelper.COLUMN_WANDERN, wandern);
        //values.put(DatabaseHelper.COLUMN_GESCHAEFTSREISE, geschaeftsreise);
        //values.put(DatabaseHelper.COLUMN_PARTYURLAUB, partyurlaub);
        //values.put(DatabaseHelper.COLUMN_CAMPING, camping);
        //values.put(DatabaseHelper.COLUMN_FESTIVAL, festival);

        long insertId = database.insert(DatabaseHelper.TABLE_ITEM_LIST, null, values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEM_LIST,
                columns, DatabaseHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Dataset dataSet = cursorToDataset(cursor);
        cursor.close();

        return dataSet;
    }

    private Dataset cursorToDataset(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);;
        int idSex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SEX);
        int idTrocken = cursor.getColumnIndex(DatabaseHelper.COLUMN_TROCKEN);
        int idStrandurlaub = cursor.getColumnIndex(DatabaseHelper.COLUMN_STRANDURLAUB);
        int idStaedtetrip = cursor.getColumnIndex(DatabaseHelper.COLUMN_STAEDTETRIP);
        int idSkifahren = cursor.getColumnIndex(DatabaseHelper.COLUMN_SKIFAHREN);
        int idWandern = cursor.getColumnIndex(DatabaseHelper.COLUMN_WANDERN);
        int idGeschaeftsreise = cursor.getColumnIndex(DatabaseHelper.COLUMN_GESCHAEFTSREISE);
        int idPartyurlaub = cursor.getColumnIndex(DatabaseHelper.COLUMN_PARTYURLAUB);
        int idCamping = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAMPING);
        int idFestival = cursor.getColumnIndex(DatabaseHelper.COLUMN_FESTIVAL);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        int geschlecht = cursor.getInt(idSex);
        int trocken = cursor.getInt(idTrocken);
        int strandurlaub = cursor.getInt(idStrandurlaub);
        int staedtetrip = cursor.getInt(idStaedtetrip);
        int skifahren = cursor.getInt(idSkifahren);
        int wandern = cursor.getInt(idWandern);
        int geschaeftsreise = cursor.getInt(idGeschaeftsreise);
        int partyurlaub = cursor.getInt(idPartyurlaub);
        int camping = cursor.getInt(idCamping);
        int festival = cursor.getInt(idFestival);

        Dataset dataSet = new Dataset(id, name, geschlecht, trocken, strandurlaub,
                staedtetrip, skifahren, wandern, geschaeftsreise, partyurlaub, camping, festival);

        return dataSet;
    }

    public List<Dataset> getAllDatasets() {
        List<Dataset> dataSetList = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEM_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Dataset dataSet;

        while(!cursor.isAfterLast()) {
            dataSet = cursorToDataset(cursor);
            dataSetList.add(dataSet);
            Log.d(LOG_TAG, "ID: " + dataSet.getItemID() + ", Inhalt: " + dataSet.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return dataSetList;
    }
}
