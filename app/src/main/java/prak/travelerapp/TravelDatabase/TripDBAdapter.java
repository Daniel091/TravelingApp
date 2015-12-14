package prak.travelerapp.TravelDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import prak.travelerapp.ItemDatabase.Dataset;

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
            String sql ="SELECT * FROM journey";

            Cursor cursor = tripDB.rawQuery(sql, null);
            List<Dataset> dataSetList = new ArrayList<>();
            if (cursor!=null)
            {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        cursor.getString(cursor.getColumnIndex("name"));

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
}
