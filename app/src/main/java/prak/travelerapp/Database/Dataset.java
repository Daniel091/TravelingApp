package prak.travelerapp.Database;

import java.util.Arrays;

/**
 * Created by marcel on 24.11.15.
 *
 * Entspricht dem Aufbau eines Datensatzes der Tabelle
 */
public class Dataset {

    private long itemID;
    private String itemName;
    private int basic; // 0 für normales Item, 1 für Basic Item
    private int geschlecht; // 0 für "Neutral" , 1 für "Mann", 2 für "Frau"
    private int trocken; // 0 für "Nein", 1 für "Ja"
    private int strandurlaub;  // 0 für "Nein", 1 für "Ja"
    private int staedtetrip;  // 0 für "Nein", 1 für "Ja"
    private int skifahren;  // 0 für "Nein", 1 für "Ja"
    private int wandern;  // 0 für "Nein", 1 für "Ja"
    private int geschaeftsreise;  // 0 für "Nein", 1 für "Ja"
    private int partyurlaub;  // 0 für "Nein", 1 für "Ja"
    private int camping;  // 0 für "Nein", 1 für "Ja"
    private int festival;  // 0 für "Nein", 1 für "Ja"

    public Dataset(long itemID, String itemName, int basic, int geschlecht, int trocken,
                   int strandurlaub, int staedtetrip, int skifahren, int wandern, int geschaeftsreise, int partyurlaub, int camping, int festival) {

        this.itemID = itemID;
        this.itemName = itemName;
        this.basic = basic;
        this.geschlecht = geschlecht;
        this.trocken = trocken;
        this.strandurlaub = strandurlaub;
        this.staedtetrip = staedtetrip;
        this.skifahren = skifahren;
        this.wandern = wandern;
        this.geschaeftsreise = geschaeftsreise;
        this.partyurlaub = partyurlaub;
        this.camping = camping;
        this.festival = festival;

    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getBasic() {
        return basic;
    }

    public void setBasic(int basic) {
        this.basic = basic;
    }

    public int getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(int geschlecht) {
        this.geschlecht = geschlecht;
    }

    public int getTrocken() {
        return trocken;
    }

    public void setTrocken(int trocken) {
        this.trocken = trocken;
    }

    public int getStrandurlaub() {
        return strandurlaub;
    }

    public void setStrandurlaub(int strandurlaub) {
        this.strandurlaub = strandurlaub;
    }

    public int getStaedtetrip() {
        return staedtetrip;
    }

    public void setStaedtetrip(int staedtetrip) {
        this.staedtetrip = staedtetrip;
    }

    public int getSkifahren() {
        return skifahren;
    }

    public void setSkifahren(int skifahren) {
        this.skifahren = skifahren;
    }

    public int getWandern() {
        return wandern;
    }

    public void setWandern(int wandern) {
        this.wandern = wandern;
    }

    public int getGeschaeftsreise() {
        return geschaeftsreise;
    }

    public void setGeschaeftsreise(int geschaeftsreise) {
        this.geschaeftsreise = geschaeftsreise;
    }

    public int getPartyurlaub() {
        return partyurlaub;
    }

    public void setPartyurlaub(int partyurlaub) {
        this.partyurlaub = partyurlaub;
    }

    public int getFestival() {
        return festival;
    }

    public void setFestival(int festival) {
        this.festival = festival;
    }

    public int getCamping() {
        return camping;
    }

    public void setCamping(int camping) {
        this.camping = camping;
    }

    @Override
    public String toString() {

        String basicOutput;
        if (basic == 0) {
            basicOutput = "Nein";
        } else if (basic == 1) {
            basicOutput = "Ja";
        } else {
            basicOutput = "falscher Wert";
        }

        String geschlechtOutput;
        if (geschlecht == 0) {
            geschlechtOutput = "Neutral";
        } else if (geschlecht == 1) {
            geschlechtOutput = "Mann";
        } else if (geschlecht == 2) {
            geschlechtOutput = "Frau";
        } else {
            geschlechtOutput = "falscher Wert";
        }

        String trockenOutput;
        if (trocken == 0) {
            trockenOutput = "Nein";
        } else if (trocken == 1) {
            trockenOutput = "Ja";
        } else {
            trockenOutput = "falscher Wert";
        }

        String strandurlaubOutput;
        if (strandurlaub == 0) {
            strandurlaubOutput = "Nein";
        } else if (strandurlaub == 1) {
            strandurlaubOutput = "Ja";
        } else {
            strandurlaubOutput = "falscher Wert";
        }

        String staedtetripOutput;
        if (staedtetrip == 0) {
            staedtetripOutput = "Nein";
        } else if (staedtetrip == 1) {
            staedtetripOutput = "Ja";
        } else {
            staedtetripOutput = "falscher Wert";
        }

        String skifahrenOutput;
        if (skifahren == 0) {
            skifahrenOutput = "Nein";
        } else if (skifahren == 1) {
            skifahrenOutput = "Ja";
        } else {
            skifahrenOutput = "falscher Wert";
        }

        String wandernOutput;
        if (wandern == 0) {
            wandernOutput = "Nein";
        } else if (wandern == 1) {
            wandernOutput = "Ja";
        } else {
            wandernOutput = "falscher Wert";
        }

        String geschaeftsreiseOutput;
        if (trocken == 0) {
            geschaeftsreiseOutput = "Nein";
        } else if (trocken == 1) {
            geschaeftsreiseOutput = "Ja";
        } else {
            geschaeftsreiseOutput = "falscher Wert";
        }

        String partyurlaubOutput;
        if (partyurlaub == 0) {
            partyurlaubOutput = "Nein";
        } else if (partyurlaub == 1) {
            partyurlaubOutput = "Ja";
        } else {
            partyurlaubOutput = "falscher Wert";
        }

        String campingOutput;
        if (camping == 0) {
            campingOutput = "Nein";
        } else if (camping == 1) {
            campingOutput = "Ja";
        } else {
            campingOutput = "falscher Wert";
        }

        String festivalOutput;
        if (festival == 0) {
            festivalOutput = "Nein";
        } else if (festival == 1) {
            festivalOutput = "Ja";
        } else {
            festivalOutput = "falscher Wert";
        }
        String output = "ID: " + itemID + ", " + itemName + ", Basic: " + basicOutput + ", Geschlecht: " +
                geschlechtOutput + ", Trocken: " + trockenOutput + ", Strandurlaub: " + strandurlaubOutput
                + ", Städtetrip: " + staedtetripOutput + ", Skifahren: " + skifahrenOutput + ", Wandern: " + wandernOutput +
                ", Geschäftsreise: " + geschaeftsreiseOutput + ", Partyurlaub: " + partyurlaubOutput +
                ", Camping: " + campingOutput + ", Festival: " + festivalOutput;

        return output;
    }

}
