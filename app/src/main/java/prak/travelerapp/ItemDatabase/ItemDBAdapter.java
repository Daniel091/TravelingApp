package prak.travelerapp.ItemDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Michael on 05.12.15.
 */
public class ItemDBAdapter {

    protected static final String TAG = "DataAdapter";

    public String tableName = "items_table";


    private final Context mContext;
    private SQLiteDatabase mDb;
    private ItemDBHelper mDbHelper;

    public ItemDBAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new ItemDBHelper(mContext);
    }

    public ItemDBAdapter createDatabase() throws SQLException
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

    public ItemDBAdapter open() throws SQLException
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


    public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM item_table";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // move cursor to first row
                if (mCur.moveToFirst()) {
                    do {
                        // Get version from Cursor
                        String itemname = mCur.getString(mCur.getColumnIndex("NAME"));
                        // add the bookName into the bookTitles ArrayList
                        Log.d("mw", itemname);
                        // move to next row
                    } while (mCur.moveToNext());
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

    /*
    public void insertData(String name, int ){

        ContentValues values = new ContentValues();
        //values.put("StudentName", queryValues.get("StudentName"));
        //mdb.insert("Students", null, values);
        mDb.insert("items_table", null,values);

    }*/


}
