package prak.travelerapp.TripDatabase.model;

public enum TravelType {
    NO_TYPE("no_type"),STAEDTETRIP("Städtetrip"), SKIFAHREN("Skifahren"),FESTIVAL("Festival"),WANDERN("Wandern");

    private final String stringValue;

    private TravelType(String value) {
        stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }
}
