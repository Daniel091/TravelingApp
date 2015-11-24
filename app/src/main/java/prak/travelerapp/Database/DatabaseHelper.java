package prak.travelerapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marcel on 24.11.15.
 *
 * Erzeugen und Updaten der Datenbank
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context){
        super(context, "KOFFERITEMS", null, 1); // 1 == Version der Datenbank, Erhöhung um jeweils 1 bei DB Upgrade
        Log.d(LOG_TAG, "DatabaseHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
