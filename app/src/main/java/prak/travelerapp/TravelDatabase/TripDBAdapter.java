package prak.travelerapp.TravelDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import prak.travelerapp.ItemDatabase.Dataset;
import prak.travelerapp.TravelDatabase.model.Trip;

public class TripDBAdapter {

    private TripDBHelper tripDBHelper;
    private Context context;
    private SQLiteDatabase tripDB;

    public TripDBAdapter(Context c) {
        context = c;
    }

    public TripDBAdapter open() throws SQLException {
        tripDBHelper = new TripDBHelper(context);
        tripDB = tripDBHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        tripDBHelper.close();
    }

    public Cursor fetch() {
        try
        {
            String sql ="SELECT * FROM " + TripDBHelper.TABLE_NAME;

            Cursor cursor = tripDB.rawQuery(sql, null);
            List<Dataset> dataSetList = new ArrayList<>();
            if (cursor!=null)
            {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        Log.d("DBTEST",cursor.getString(cursor.getColumnIndex("name")));

                    } while (cursor.moveToNext());
                }

            }
            return null;
        }
        catch (SQLException mSQLException)
        {
            Log.e("TripDBAdapter", "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }


    public void insert(Trip trip) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(TripDBHelper.COLUMN_NAME, trip.getName());
        contentValue.put(TripDBHelper.COLUMN_STARTDATE, "11.10.2018");
        contentValue.put(TripDBHelper.COLUMN_ENDDATE, "12.10.2018");
        contentValue.put(TripDBHelper.COLUMN_TYPE1, trip.getType1().ordinal());
        contentValue.put(TripDBHelper.COLUMN_TYPE2, trip.getType2().ordinal());
        contentValue.put(TripDBHelper.COLUMN_ACTIVE, 0);
        tripDB.insert(TripDBHelper.TABLE_NAME, null, contentValue);
    }
}
