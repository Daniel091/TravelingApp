package prak.travelerapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public Datasource(Context context) {
        Log.d(LOG_TAG, "Datasource erzeugt den DatabaseHelper");
        databaseHelper = new DatabaseHelper(context);
    }
}
