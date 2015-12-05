package prak.travelerapp.Autocompleter.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import prak.travelerapp.Autocompleter.model.City;

/**
 * Created by Michael on 05.12.15.
 */
public class CityDBAdapter {

    protected static final String TAG = "DataAdapter";

    public String tableName = "CityList";
    public String fieldObjectId = "ID";
    public String fieldObjectName = "Name";
    public String fieldObjectCountry = "Country";
    public String fieldObjectLatitude = "X";
    public String fieldObjectLongitude = "Y";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private CityDBHelper mDbHelper;

    public CityDBAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new CityDBHelper(mContext);
    }

    public CityDBAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public CityDBAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }


    // Read records related to the search term
    public List<City> read(String searchTerm) {

        List<City> recordsList = new ArrayList<City>();

        String sql = "";

        //check for umlaute
        if(!(searchTerm.contains("ä") | searchTerm.contains("ü")|searchTerm.contains("ö"))) {

            // select query
            sql += "SELECT * FROM " + tableName;
            sql += " WHERE " + fieldObjectName + " LIKE '" + searchTerm + "%'";
            sql += " ORDER BY " + fieldObjectId + " DESC";
            sql += " LIMIT 0,5";
        }else{
            Log.d("mw", searchTerm);
            String searchTermWithoutUmlaute = searchTerm;
            searchTermWithoutUmlaute = searchTermWithoutUmlaute.replace("ö","oe");
            searchTermWithoutUmlaute =searchTermWithoutUmlaute.replace("ä","ae");
            searchTermWithoutUmlaute =searchTermWithoutUmlaute.replace("ü","ue");

            // select query
            sql += "SELECT * FROM " + tableName;
            sql += " WHERE " + fieldObjectName + " LIKE '" + searchTerm + "%' OR " + fieldObjectName + " LIKE '"  + searchTermWithoutUmlaute + "%'";
            sql += " ORDER BY " + fieldObjectId + " DESC";
            sql += " LIMIT 0,5";
        }

        // execute the query
        Cursor cursor = mDb.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                int cityID = cursor.getInt(cursor.getColumnIndex(fieldObjectId));
                String cityName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                String cityCountry = cursor.getString(cursor.getColumnIndex(fieldObjectCountry));
                double cityLatitude = cursor.getDouble(cursor.getColumnIndex(fieldObjectLatitude));
                double cityLongitude = cursor.getDouble(cursor.getColumnIndex(fieldObjectLongitude));

                City city = new City();
                city.setID(cityID);
                city.setName(cityName);
                city.setCountry(cityCountry);
                city.setLatitude((float)cityLatitude);
                city.setLongitude((float)cityLongitude);


                // add to list
                recordsList.add(city);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // return the list of records
        return recordsList;
    }

    public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM CityList WHERE name LIKE 'Berlin%'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // move cursor to first row
                if (mCur.moveToFirst()) {
                   // do {
                        // Get version from Cursor
                        String cityname = mCur.getString(mCur.getColumnIndex("Name"));
                        // add the bookName into the bookTitles ArrayList
                        Log.d("mw", cityname);
                        // move to next row
                   // } while (mCur.moveToNext());
                }

            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


}
