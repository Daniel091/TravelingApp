package prak.travelerapp.TravelDatabase.model;

import java.util.Calendar;

public class Trip {
    private TripItems tupel_list;
    private final String name;
    private final int id;
    private final TravelTypes type1;
    private final TravelTypes type2;
    private Calendar start_date;
    private Calendar end_date;
    private boolean active;

    public Trip(int id,TripItems list,String name,Calendar start_date,Calendar end_date, TravelTypes cat1, TravelTypes cat2, boolean active){
        this.id = id;
        this.name = name;
        this.tupel_list = list;
        this.start_date = start_date;
        this.end_date = end_date;
        this.type1 = cat1;
        if(cat2 != null){
            this.type2 = cat2;
        }else{
            this.type2 = TravelTypes.NO_TYPE;
        }
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public TripItems getTupel_list() {
        return tupel_list;
    }

    public int getId() {
        return id;
    }

    public TravelTypes getType1() {
        return type1;
    }

    public TravelTypes getType2() {
        return type2;
    }

    public Calendar getStart_date() {
        return start_date;
    }

    public Calendar getEnd_date() {
        return end_date;
    }

    public boolean isActive() {
        return active;
    }
}
