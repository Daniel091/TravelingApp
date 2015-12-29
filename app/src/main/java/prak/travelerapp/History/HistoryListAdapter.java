package prak.travelerapp.History;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import prak.travelerapp.R;
import prak.travelerapp.TripDatabase.model.Trip;
import prak.travelerapp.Utils;

public class HistoryListAdapter extends ArrayAdapter<Trip> {

    private final Context context;
    private final Trip[] trips;
    private final int layoutResourceID;

    public HistoryListAdapter(Context context,int resource, Trip[] values) {
        super(context, resource, values);
        this.context = context;
        this.trips = values;
        this.layoutResourceID = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layoutResourceID, parent, false);
        TextView cityTextView = (TextView) rowView.findViewById(R.id.textview_cityName);
        TextView arrivalDateTextView = (TextView) rowView.findViewById(R.id.textview_arrivalDate);
        TextView departureDateTextView = (TextView) rowView.findViewById(R.id.textview_departureDate);

        arrivalDateTextView.setText(Utils.dateTimeToString(trips[position].getStartdate()));
        departureDateTextView.setText(Utils.dateTimeToString(trips[position].getEnddate()));

        cityTextView.setText(trips[position].getCity());
        return rowView;
    }


}
