package prak.travelerapp.TravelDatabase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Michael on 14.12.15.
 */

public class JourneyDBHelper extends SQLiteOpenHelper{;


    // Table Name
    public static final String TABLE_NAME = "journeys";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STARTDATE = "start_date";
    public static final String COLUMN_ENDDATE = "end_date";
    public static final String COLUMN_TYPE1 = "type1";
    public static final String COLUMN_TYPE2 = "type2";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_ITEMS = "items";
    // Database Information
    static final String DB_NAME = "JOURNEYS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_STARTDATE + " TEXT NOT NULL"
            + COLUMN_ENDDATE + "TEXT NOT NULL"
            + COLUMN_TYPE1 + "INTEGER NOT NULL"
            + COLUMN_TYPE2 + "INTEGER"
            + COLUMN_ACTIVE + "INTEGER"
            + COLUMN_ITEMS + "TEXT"
            +");";

    public JourneyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
