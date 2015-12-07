package prak.travelerapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by marcel on 03.12.15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Log Tag
    private static final String LOG_TAG = DatabaseHandler.class.getSimpleName();

    // Context
    Context context;

    // Angaben zur Datenbank
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "";

    // Name der Tabelle
    private static final String ITEM_TABLE = "items";

    // Spalten der Tabelle
    private static final String ITEM_ID = "_id";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_SEX = "geschlecht";
    private static final String ITEM_TROCKEN = "trocken";
    private static final String ITEM_STRANDURLAUB = "strandurlaub";
    private static final String ITEM_STAEDTETRIP = "staedtetrip";
    private static final String ITEM_SKIFAHREN = "skifahren";
    private static final String ITEM_WANDERN = "wandern";
    private static final String ITEM_GESCHAEFTSREISE = "geschaeftsreise";
    private static final String ITEM_PARTYURLAUB = "partyurlaub";
    private static final String ITEM_CAMPING = "camping";
    private static final String ITEM_FESTIVAL = "festival";
    private static final String ITEM_KATEGORIE = "kategorie";

    // Befehl zum Anlegen der DB
    String createDB = "CREATE TABLE IF NOT EXISTS " + ITEM_TABLE + " (" +
            ITEM_ID + " INTEGER PRIMARY KEY, " +
            ITEM_NAME + " TEXT, " +
            ITEM_SEX + " INTEGER, " +
            ITEM_TROCKEN + " INTEGER, " +
            ITEM_STRANDURLAUB + " INTEGER, " +
            ITEM_STAEDTETRIP + " INTEGER, " +
            ITEM_SKIFAHREN + " INTEGER, " +
            ITEM_WANDERN + " INTEGER, " +
            ITEM_GESCHAEFTSREISE + " INTEGER, " +
            ITEM_PARTYURLAUB + " INTEGER, " +
            ITEM_CAMPING + " INTEGER, " +
            ITEM_FESTIVAL + " INTEGER, " +
            ITEM_KATEGORIE + " INTEGER)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        File dbFile = new File(DB_PATH + DATABASE_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        boolean dbExists = dbFile.exists();

        if(!dbExists){
            db.execSQL(createDB);
            Log.d(LOG_TAG, "Die Datenbank wird erstellt...");

            String file = "items.csv";
            AssetManager manager = context.getAssets();
            InputStream inStream = null;
            try {
                inStream = manager.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line = "";
            Log.d(LOG_TAG, "Die Tabelle wird in die DB geschrieben...");
            db.beginTransaction();
            try {
                while ((line = buffer.readLine()) != null) {
                    String[] columns = line.split(";");
                    if (columns.length != 13) {
                        Log.d("CSVParser", "Überspringt falsche CSV Zeile");
                        continue;
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(ITEM_ID, columns[0].trim());
                    cv.put(ITEM_NAME, columns[1].trim());
                    cv.put(ITEM_SEX, columns[2].trim());
                    cv.put(ITEM_TROCKEN, columns[3].trim());
                    cv.put(ITEM_STRANDURLAUB, columns[4].trim());
                    cv.put(ITEM_STAEDTETRIP, columns[5].trim());
                    cv.put(ITEM_SKIFAHREN, columns[6].trim());
                    cv.put(ITEM_WANDERN, columns[7].trim());
                    cv.put(ITEM_GESCHAEFTSREISE, columns[8].trim());
                    cv.put(ITEM_PARTYURLAUB, columns[9].trim());
                    cv.put(ITEM_CAMPING, columns[10].trim());
                    cv.put(ITEM_FESTIVAL, columns[11].trim());
                    cv.put(ITEM_KATEGORIE, columns[12].trim());
                    db.insert(ITEM_TABLE, null, cv);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            db.setTransactionSuccessful();
            db.endTransaction();

        }else{
            getTableAsString(db,ITEM_TABLE);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        onCreate(db);
    }


    public String getTableAsString(SQLiteDatabase db, String tableName) {

        Log.d(LOG_TAG, "getTableAsString wurde aufgerufen");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }


        return tableString;
    }

}
