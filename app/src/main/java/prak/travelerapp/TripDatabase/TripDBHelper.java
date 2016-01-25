package prak.travelerapp.TripDatabase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TripDBHelper extends SQLiteOpenHelper{


    // Table Name
    public static final String TABLE_NAME = "trips";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_STARTDATE = "start_date";
    public static final String COLUMN_ENDDATE = "end_date";
    public static final String COLUMN_TYPE1 = "type1";
    public static final String COLUMN_TYPE2 = "type2";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_ITEMS = "items";
    // Database Information
    static final String DB_NAME = "TRIP.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_COUNTRY + " TEXT NOT NULL, "
            + COLUMN_STARTDATE + " TEXT NOT NULL, "
            + COLUMN_ENDDATE + " TEXT NOT NULL, "
            + COLUMN_TYPE1 + " INTEGER NOT NULL, "
            + COLUMN_TYPE2 + " INTEGER, "
            + COLUMN_ACTIVE + " INTEGER, "
            + COLUMN_ITEMS + " TEXT"
            +");";

    public TripDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d("Upgrade TripDB", String.format("IansSQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
    }

}
