package prak.travelerapp.ItemDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 05.12.15.
 */
public class ItemDBAdapter {

    protected static final String TAG = "DBAdapter";

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


    public List<Dataset> getItems()
    {
        try
        {
            String sql ="SELECT * FROM item_table";

            Cursor cursor = mDb.rawQuery(sql, null);
            List<Dataset> dataSetList = new ArrayList<>();
            if (cursor!=null)
            {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        Dataset itemSet = cursorToDataset(cursor);
                        dataSetList.add(itemSet);

                    } while (cursor.moveToNext());
                }

            }
            return dataSetList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Dataset createDataset(String itemName, int basic, int geschlecht, int trocken,
                                 int strandurlaub, int staedtetrip, int skifahren, int wandern,
                                 int geschaeftsreise, int partyurlaub, int camping, int festival,
                                 int kategorie) {

        ContentValues values = new ContentValues();
        values.put(ItemDBHelper.COLUMN_NAME, itemName);
        values.put(ItemDBHelper.COLUMN_SEX, geschlecht);
        values.put(ItemDBHelper.COLUMN_TROCKEN, trocken);
        values.put(ItemDBHelper.COLUMN_STRANDURLAUB, strandurlaub);
        values.put(ItemDBHelper.COLUMN_STAEDTETRIP, staedtetrip);
        values.put(ItemDBHelper.COLUMN_SKIFAHREN, skifahren);
        values.put(ItemDBHelper.COLUMN_WANDERN, wandern);
        values.put(ItemDBHelper.COLUMN_GESCHAEFTSREISE, geschaeftsreise);
        values.put(ItemDBHelper.COLUMN_PARTYURLAUB, partyurlaub);
        values.put(ItemDBHelper.COLUMN_CAMPING, camping);
        values.put(ItemDBHelper.COLUMN_FESTIVAL, festival);
        values.put(ItemDBHelper.COLUMN_KATEGORIE, kategorie);

        long insertId = mDb.insert(ItemDBHelper.TABLE_ITEM_LIST, null, values);

        Cursor cursor = mDb.query(ItemDBHelper.TABLE_ITEM_LIST,
                ItemDBHelper.columns, ItemDBHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Dataset dataSet = cursorToDataset(cursor);
        cursor.close();

        return dataSet;
    }

    public Dataset cursorToDataset(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ItemDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(ItemDBHelper.COLUMN_NAME);
        int idSex = cursor.getColumnIndex(ItemDBHelper.COLUMN_SEX);
        int idTrocken = cursor.getColumnIndex(ItemDBHelper.COLUMN_TROCKEN);
        int idStrandurlaub = cursor.getColumnIndex(ItemDBHelper.COLUMN_STRANDURLAUB);
        int idStaedtetrip = cursor.getColumnIndex(ItemDBHelper.COLUMN_STAEDTETRIP);
        int idSkifahren = cursor.getColumnIndex(ItemDBHelper.COLUMN_SKIFAHREN);
        int idWandern = cursor.getColumnIndex(ItemDBHelper.COLUMN_WANDERN);
        int idGeschaeftsreise = cursor.getColumnIndex(ItemDBHelper.COLUMN_GESCHAEFTSREISE);
        int idPartyurlaub = cursor.getColumnIndex(ItemDBHelper.COLUMN_PARTYURLAUB);
        int idCamping = cursor.getColumnIndex(ItemDBHelper.COLUMN_CAMPING);
        int idFestival = cursor.getColumnIndex(ItemDBHelper.COLUMN_FESTIVAL);
        int idKategorie = cursor.getColumnIndex(ItemDBHelper.COLUMN_KATEGORIE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        int geschlecht = cursor.getInt(idSex);
        int trocken = cursor.getInt(idTrocken);
        int strandurlaub = cursor.getInt(idStrandurlaub);
        int staedtetrip = cursor.getInt(idStaedtetrip);
        int skifahren = cursor.getInt(idSkifahren);
        int wandern = cursor.getInt(idWandern);
        int geschaeftsreise = cursor.getInt(idGeschaeftsreise);
        int partyurlaub = cursor.getInt(idPartyurlaub);
        int camping = cursor.getInt(idCamping);
        int festival = cursor.getInt(idFestival);
        int kategorie = cursor.getInt(idKategorie);

        Dataset dataSet = new Dataset(id, name, geschlecht, trocken, strandurlaub,
                staedtetrip, skifahren, wandern, geschaeftsreise,
                partyurlaub, camping, festival, kategorie);

        return dataSet;
    }
}
