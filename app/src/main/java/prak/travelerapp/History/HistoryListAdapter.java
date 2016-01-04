package prak.travelerapp.History;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView tripIcon = (ImageView) rowView.findViewById(R.id.trip_icon);

        arrivalDateTextView.setText(Utils.dateTimeToString(trips[position].getStartdate()));
        departureDateTextView.setText(Utils.dateTimeToString(trips[position].getEnddate()));
        cityTextView.setText(trips[position].getCity());

        switch (trips[position].getType1()) {
            case SKIFAHREN:
                tripIcon.setImageResource(R.mipmap.ic_history_ski);
                break;
            case WANDERN:
                tripIcon.setImageResource(R.mipmap.ic_history_hike);
                break;
            case PARTYURLAUB:
                tripIcon.setImageResource(R.mipmap.ic_history_party);
                break;
            case GESCHAEFTSREISE:
                tripIcon.setImageResource(R.mipmap.ic_history_business);
                break;
            case CAMPING:
                tripIcon.setImageResource(R.mipmap.ic_history_camping);
                break;
            case FESTIVAL:
                tripIcon.setImageResource(R.mipmap.ic_history_festival);
                break;
            case STAEDTETRIP:
                tripIcon.setImageResource(R.mipmap.ic_history_city);
                break;
            case STRANDURLAUB:
                tripIcon.setImageResource(R.mipmap.ic_history_beach);
                break;
            default:
                tripIcon.setImageResource(R.mipmap.ic_history_business);
                break;
        }

        return rowView;
    }


}
