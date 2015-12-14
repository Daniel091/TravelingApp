package prak.travelerapp.TravelDatabase;

import java.util.Calendar;
import java.util.List;

import prak.travelerapp.Categories;

public class Trip {
    private TripItems tupel_list;
    private final String name;
    private final int id;
    private final Categories categorie;
    private Calendar start_date;
    private Calendar end_date;
    private boolean active;

    public Trip(int id,TripItems list,String name,Calendar start_date,Calendar end_date, Categories cat, boolean active){
        this.id = id;
        this.name = name;
        this.tupel_list = list;
        this.start_date = start_date;
        this.end_date = end_date;
        this.categorie = cat;
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

    public Categories getCategorie() {
        return categorie;
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
