package prak.travelerapp.TripDatabase.model;

import org.joda.time.DateTime;

public class Trip {
    private TripItems tripItems;
    private final String name;
    private final int id;
    private final TravelType type1;
    private final TravelType type2;
    private DateTime startDate;
    private DateTime endDate;
    private boolean active;

    public Trip(int id,TripItems list,String name,DateTime startDate,DateTime endDate, TravelType cat1, TravelType cat2, boolean active){
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
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
