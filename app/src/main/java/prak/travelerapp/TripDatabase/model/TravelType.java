package prak.travelerapp.TripDatabase.model;

public enum TravelType {
    NO_TYPE("Keine Kategorie"),STRANDURLAUB("Strandurlaub"), STAEDTETRIP("Städtetrip"), SKIFAHREN("Skifahren"),WANDERN("Wandern"),
    GESCHAEFTSREISE("Geschäftsreise"),PARTYURLAUB("Partyurlaub"),CAMPING("Camping"),FESTIVAL("Festival");

    private final String stringValue;

    private TravelType(String value) {
        stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }
}
