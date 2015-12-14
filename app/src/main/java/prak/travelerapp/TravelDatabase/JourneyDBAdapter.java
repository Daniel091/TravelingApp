package prak.travelerapp.TravelDatabase;

/**
 * Created by Michael on 14.12.15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class JourneyDBAdapter {

    private JourneyDBHelper journeyDBHelper;
    private Context context;
    private SQLiteDatabase database;

    public JourneyDBAdapter(Context c) {
        context = c;
    }

    public JourneyDBAdapter open() throws SQLException {
        journeyDBHelper = new JourneyDBHelper(context);
        database = journeyDBHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        journeyDBHelper.close();
    }

/*    public Cursor fetch() {
        String[] columns = new String[] { DBhelper._ID, DBhelper.TODO_SUBJECT,
                DBhelper.TODO_DESC };
        Cursor cursor = database.query(DBhelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBhelper.TODO_SUBJECT, name);
        contentValues.put(DBhelper.TODO_DESC, desc);
        int i = database.update(DBhelper.TABLE_NAME, contentValues,
                DBhelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBhelper.TABLE_NAME, DBhelper._ID + "=" + _id, null);
    }
    */

}
