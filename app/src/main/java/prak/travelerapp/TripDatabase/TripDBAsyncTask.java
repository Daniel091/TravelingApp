package prak.travelerapp.TripDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import prak.travelerapp.TripDatabase.model.TravelType;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.TripDatabase.model.TripItems;

public class TripDBAsyncTask extends AsyncTask {
    private final Context context;
    private TripDBAdapter tripDBAdapter;

    public TripDBAsyncTask(Context context){
        super();
        this.context = context;
    }

    @Override
    protected Trip doInBackground(Object[] params) {
        String s = "(3,0);(4,0)";
        TripItems items = new TripItems(s);
        DateTime startDate = new DateTime(2016,01,12,0,0);
        DateTime endDate = new DateTime(2016,01,15,0,0);


        Trip trip = new Trip(0,items, "Miami", startDate,endDate, TravelType.WANDERN, TravelType.SKIFAHREN, true);
        tripDBAdapter = new TripDBAdapter(context);
        tripDBAdapter.open();

        tripDBAdapter.insert(trip);
        Trip activeTrip = tripDBAdapter.getActiveTrip();
        Log.d("Database", activeTrip.getName() + " " + activeTrip.getType1());

        return activeTrip;
    }

    protected void onPostExecute(Trip activeTrip) {
        // http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    }

}
