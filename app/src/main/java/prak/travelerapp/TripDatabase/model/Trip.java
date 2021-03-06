package prak.travelerapp.TripDatabase.model;

import org.joda.time.DateTime;

public class Trip {
    private TripItems tripItems;
    private final String city;
    private final String country;
    private final int id;
    private final TravelType type1;
    private final TravelType type2;
    private DateTime startDate;
    private DateTime endDate;
    private boolean active;

    public Trip(int id,TripItems list,String city,String country, DateTime startDate,DateTime endDate, TravelType cat1, TravelType cat2, boolean active){
        this.id = id;
        this.city = city;
        this.country = country;
        this.tripItems = list;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type1 = cat1;
        if(cat2 != null){
            this.type2 = cat2;
        }else{
            this.type2 = TravelType.NO_TYPE;
        }
        this.active = active;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public TripItems getTripItems() {
        return tripItems;
    }

    public int getId() {
        return id;
    }

    public TravelType getType1() {
        return type1;
    }

    public TravelType getType2() {
        return type2;
    }

    public DateTime getStartdate() {
        return startDate;
    }

    public DateTime getEnddate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }


}
