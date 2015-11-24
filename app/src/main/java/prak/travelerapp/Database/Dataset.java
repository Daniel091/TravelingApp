package prak.travelerapp.Database;

import java.util.Arrays;

/**
 * Created by marcel on 24.11.15.
 *
 * Entspricht dem Aufbau eines Datensatzes der Tabelle
 */
public class Dataset {

    private int itemID;
    private String itemName;
    private int basic; // 0 für normales Item, 1 für Basic Item
    private int geschlecht; // 0 für "Neutral" , 1 für "Mann", 2 für "Frau"
    private String[] kategorien;

    public Dataset(int itemID, String itemName, int basic, int geschlecht, String[] kategorien) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.basic = basic;
        this.geschlecht = geschlecht;
        this.kategorien = kategorien;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
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

    public String[] getKategorien() {
        return kategorien;
    }

    public void setKategorien(String[] kategorien) {
        this.kategorien = kategorien;
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

        String output = "ID: " + itemID + ", " + itemName + ", Basic: " + basicOutput + ", Geschlecht: " +
                geschlechtOutput + ", Kategorien: " + Arrays.toString(kategorien);

        return output;
    }

}
