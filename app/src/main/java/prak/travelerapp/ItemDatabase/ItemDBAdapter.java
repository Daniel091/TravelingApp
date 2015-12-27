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

import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.TripItems;
import prak.travelerapp.TripDatabase.model.Tupel;

/**
 * Created by Michael on 05.12.15.
 */
public class ItemDBAdapter {

    protected static final String TAG = "DBAdapter";


    private Context mContext;
    private SQLiteDatabase itemDb;
    private ItemDBHelper itemDBHelper;

    public ItemDBAdapter(Context context)
    {
        this.mContext = context;
        itemDBHelper = new ItemDBHelper(mContext);
    }

    public ItemDBAdapter createDatabase() throws SQLException
    {
        try
        {
            itemDBHelper.createDataBase();
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
            itemDBHelper.openDataBase();
            itemDBHelper.close();
            itemDb = itemDBHelper.getReadableDatabase();
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
        itemDBHelper.close();
    }


    public ArrayList<Dataset> getItems(TripItems items)
    {
        String itemIDArrayString = tripItemsToIDArrayString(items);

        try
        {
            String sql ="SELECT * FROM item_table WHERE " + ItemDBHelper.COLUMN_ID + " IN " + itemIDArrayString;

            Cursor cursor = itemDb.rawQuery(sql, null);
            ArrayList<Dataset> dataSetList = new ArrayList<>();
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

    private String tripItemsToIDArrayString(TripItems items) {
        String idArrayString = "(";
        for (int i=0; i< items.getItems().size();i++) {
            Tupel item = items.getItems().get(i);
            if(i< items.getItems().size()-1){
                idArrayString += item.getX()+",";
            }else{
                idArrayString += item.getX()+"";
            }

        }
        idArrayString += ")";
        return idArrayString;
    }

    public Dataset createDataset(String itemName, int basic, int geschlecht, int trocken,
                                 int strandurlaub, int staedtetrip, int skifahren, int wandern,
                                 int geschaeftsreise, int partyurlaub, int camping, int festival,
                                 int kategorie) {

        ContentValues values = new ContentValues();
        values.put(ItemDBHelper.COLUMN_NAME, itemName);
        values.put(ItemDBHelper.COLUMN_GENDER, geschlecht);
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

        long insertId = itemDb.insert(ItemDBHelper.TABLE_NAME, null, values);

        Cursor cursor = itemDb.query(ItemDBHelper.TABLE_NAME,
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
        int idSex = cursor.getColumnIndex(ItemDBHelper.COLUMN_GENDER);
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

        int id = cursor.getInt(idIndex);
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


    //QUERY STRUKTUR --> VORLÄUFIG
    //SELECT * FROM item_table WHERE ((GESCHLECHT = 0 OR GESCHLECHT=1) AND (STAEDTETRIP=1 OR WANDERN=1)) OR TROCKEN=1;
    public ArrayList<Integer> findItemIDs(int gender,boolean isRaining,TravelType type1,TravelType type2){

        ArrayList<Integer> itemIDs = new ArrayList<Integer>();
        String COLUMN_TYPE1 = travelTypeToColumnName(type1);
        String COLUMN_TYPE2 = travelTypeToColumnName(type2);

        String selector = "";
        selector += "(" +ItemDBHelper.COLUMN_GENDER + "=" + 0 + " OR " + ItemDBHelper.COLUMN_GENDER + "=" + gender + ") ";
        //nur ein Reisetyp wurde festgelegt
        if(COLUMN_TYPE2 == ""){
            selector += "AND " + COLUMN_TYPE1 + "=" + 1;
        //es wurden zwei reisetypendefiniert
        }else{
            selector += "AND (" + COLUMN_TYPE1 + "=" + 1 + " OR " + COLUMN_TYPE2 + "=" + 1 + ")";
        }

        Cursor cursor = itemDb.query(ItemDBHelper.TABLE_NAME, new String[]{ItemDBHelper.COLUMN_ID},selector, null, null, null, null);
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(ItemDBHelper.COLUMN_ID);
                    int id = cursor.getInt(idIndex);
                    itemIDs.add(id);
                }while (cursor.moveToNext());
                cursor.close();
                return itemIDs;
            }
        }
        return null;
    }

    /**
     * Matche von TravelType auf die entsprechende Spalte in der DB
     * @param type Typ
     * @return columName
     */
    private String travelTypeToColumnName(TravelType type){
        String columName="";
        switch (type){
            case NO_TYPE:
                break;
            case STRANDURLAUB:
                columName = ItemDBHelper.COLUMN_STRANDURLAUB;
                break;
            case STAEDTETRIP:
                columName = ItemDBHelper.COLUMN_STAEDTETRIP;
                break;
            case SKIFAHREN:
                columName = ItemDBHelper.COLUMN_SKIFAHREN;
                break;
            case WANDERN:
                columName = ItemDBHelper.COLUMN_WANDERN;
                break;
            case GESCHAEFTSREISE:
                columName = ItemDBHelper.COLUMN_GESCHAEFTSREISE;
                break;
            case PARTYURLAUB:
                columName = ItemDBHelper.COLUMN_PARTYURLAUB;
                break;
            case CAMPING:
                columName = ItemDBHelper.COLUMN_CAMPING;
                break;
            case FESTIVAL:
                columName = ItemDBHelper.COLUMN_FESTIVAL;
                break;
        }
        return columName;

    }
}
