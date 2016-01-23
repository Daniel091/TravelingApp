package prak.travelerapp.ItemDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Michael on 05.12.15.
 */
public class ItemDBHelper extends SQLiteOpenHelper {

    private static String TAG = "DBHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device

    private static String DB_PATH = "";
    private static String DB_NAME ="items.db";// Database name
    // database version
    static final int DB_VERSION = 1;
    private SQLiteDatabase mDataBase;
    private final Context mContext;


    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String TABLE_NAME = "item_table";
    public static final String COLUMN_GENDER = "GESCHLECHT";
    public static final String COLUMN_KATEGORIE = "KATEGORIE";
    public static final String COLUMN_NASS = "NASS";
    public static final String COLUMN_STAEDTETRIP = "STAEDTETRIP";
    public static final String COLUMN_STRANDURLAUB = "STRANDURLAUB";
    public static final String COLUMN_SKIFAHREN = "SKIFAHREN";
    public static final String COLUMN_WANDERN = "WANDERN";
    public static final String COLUMN_GESCHAEFTSREISE = "GESCHÄFTSREISE";
    public static final String COLUMN_PARTYURLAUB = "PARTYURLAUB";
    public static final String COLUMN_CAMPING = "CAMPING";
    public static final String COLUMN_FESTIVAL = "FESTIVAL";
    public static final String COLUMN_TEMP_STAEDTETRIP = "TEMP_STAEDTETRIP";
    public static final String COLUMN_TEMP_STRANDURLAUB = "TEMP_STRANDURLAUB";
    public static final String COLUMN_TEMP_SKIFAHREN = "TEMP_SKIFAHREN";
    public static final String COLUMN_TEMP_WANDERN = "TEMP_WANDERN";
    public static final String COLUMN_TEMP_GESCHAEFTSREISE = "TEMP_GESCHÄFTSREISE";
    public static final String COLUMN_TEMP_PARTYURLAUB = "TEMP_PARTYURLAUB";
    public static final String COLUMN_TEMP_CAMPING = "TEMP_CAMPING";
    public static final String COLUMN_TEMP_FESTIVAL = "TEMP_FESTIVAL";

    public ItemDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() throws IOException
    {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    public void resetDatabase() throws IOException{
        this.getReadableDatabase();
        this.close();
        try
        {
            //Copy the database from assests
            copyDataBase();
            Log.e(TAG, "resetDatabase database reseted");
        }
        catch (IOException mIOException)
        {
            throw new Error("ErrorCopyingDataBase");
        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*Log.d("Upgrade ItemDB", String.format("IansSQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        if(oldVersion < 2){

            ContentValues values = new ContentValues();
            values.put(ItemDBHelper.COLUMN_NAME, "UpdateTest");
            values.put(ItemDBHelper.COLUMN_GENDER, 1);
            values.put(ItemDBHelper.COLUMN_NASS, 0);
            values.put(ItemDBHelper.COLUMN_STRANDURLAUB, 1);
            values.put(ItemDBHelper.COLUMN_STAEDTETRIP, 1);
            values.put(ItemDBHelper.COLUMN_SKIFAHREN, 1);
            values.put(ItemDBHelper.COLUMN_WANDERN, 1);
            values.put(ItemDBHelper.COLUMN_GESCHAEFTSREISE, 1);
            values.put(ItemDBHelper.COLUMN_PARTYURLAUB, 1);
            values.put(ItemDBHelper.COLUMN_CAMPING, 1);
            values.put(ItemDBHelper.COLUMN_FESTIVAL, 1);
            values.put(ItemDBHelper.COLUMN_TEMP_STRANDURLAUB, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_STAEDTETRIP, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_SKIFAHREN, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_WANDERN, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_GESCHAEFTSREISE, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_PARTYURLAUB, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_CAMPING, 0);
            values.put(ItemDBHelper.COLUMN_TEMP_FESTIVAL, 0);
            values.put(ItemDBHelper.COLUMN_KATEGORIE, 1);
            db.insert(TABLE_NAME,null, values);

        }*/
    }


}
