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

    public static final String DB_NAME = "kofferitems.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_ITEM_LIST = "item_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "itemname";
    public static final String COLUMN_SEX = "geschlecht";
    public static final String COLUMN_TROCKEN = "trocken";
    public static final String COLUMN_STAEDTETRIP = "staedtetrip";
    public static final String COLUMN_STRANDURLAUB = "strandurlaub";
    public static final String COLUMN_SKIFAHREN = "skifahren";
    public static final String COLUMN_WANDERN = "wandern";
    public static final String COLUMN_GESCHAEFTSREISE = "geschaeftsreise";
    public static final String COLUMN_PARTYURLAUB = "partyurlaub";
    public static final String COLUMN_CAMPING = "camping";
    public static final String COLUMN_FESTIVAL = "festival";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEM_LIST +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SEX + " INTEGER, " +
                    COLUMN_TROCKEN + " INTEGER, " +
                    COLUMN_STAEDTETRIP + " INTEGER, " +
                    COLUMN_STRANDURLAUB + " INTEGER, " +
                    COLUMN_SKIFAHREN + " INTEGER, " +
                    COLUMN_WANDERN + " INTEGER, " +
                    COLUMN_GESCHAEFTSREISE + " INTEGER, " +
                    COLUMN_PARTYURLAUB + " INTEGER, " +
                    COLUMN_CAMPING + " INTEGER, " +
                    COLUMN_FESTIVAL + " INTEGER);";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION); // 1 == Version der Datenbank, Erhöhung um jeweils 1 bei DB Upgrade
        Log.d(LOG_TAG, "DatabaseHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }


    // Aufruf der onCreate nur, falls DB noch nicht existiert!
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
