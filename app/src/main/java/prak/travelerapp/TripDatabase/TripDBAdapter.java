package prak.travelerapp.TripDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import prak.travelerapp.ItemDatabase.Dataset;
import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.TripItems;
import prak.travelerapp.Utils;

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
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_ID)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_NAME)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_STARTDATE)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_ENDDATE)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_TYPE1)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_TYPE2)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_ACTIVE)));
                        Log.d("DBTEST", cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_ITEMS)));

                    } while (cursor.moveToNext());
                    cursor.close();
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


        String startDate = Utils.dateTimeToString(trip.getStartdate());
        String endDate = Utils.dateTimeToString(trip.getEnddate());
        String items = trip.getTripItems().makeString();

        System.out.println(items);

        ContentValues contentValue = new ContentValues();
        contentValue.put(TripDBHelper.COLUMN_NAME, trip.getName());
        contentValue.put(TripDBHelper.COLUMN_STARTDATE, startDate);
        contentValue.put(TripDBHelper.COLUMN_ENDDATE, endDate);
        contentValue.put(TripDBHelper.COLUMN_TYPE1, trip.getType1().ordinal());
        contentValue.put(TripDBHelper.COLUMN_TYPE2, trip.getType2().ordinal());
        contentValue.put(TripDBHelper.COLUMN_ACTIVE, trip.isActive());
        contentValue.put(TripDBHelper.COLUMN_ITEMS, items);
        tripDB.insert(TripDBHelper.TABLE_NAME, null, contentValue);
    }

    public Trip getActiveTrip() {
        Cursor cursor = tripDB.query(TripDBHelper.TABLE_NAME, null,TripDBHelper.COLUMN_ACTIVE + "=" + 1, null, null, null, null);
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(TripDBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_NAME));
                String startDateString = cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_STARTDATE));
                DateTime startDate = Utils.stringToDatetime(startDateString);
                String endDateString = cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_ENDDATE));
                DateTime endDate = Utils.stringToDatetime(endDateString);
                int type1 = cursor.getInt(cursor.getColumnIndex(TripDBHelper.COLUMN_TYPE1));
                TravelType travelType1 = TravelType.values()[type1];
                int type2 = cursor.getInt(cursor.getColumnIndex(TripDBHelper.COLUMN_TYPE2));
                TravelType travelType2 = TravelType.values()[type2];
                int active = cursor.getInt(cursor.getColumnIndex(TripDBHelper.COLUMN_ACTIVE));
                boolean isActive = (active == 1);
                String tripItemsString = cursor.getString(cursor.getColumnIndex(TripDBHelper.COLUMN_ITEMS));
                TripItems items = new TripItems(tripItemsString);

                Trip trip = new Trip(id,items,name,startDate,endDate,travelType1,travelType2,isActive);
                cursor.close();
                return trip;
            }
        }
        return null;
    }
}
