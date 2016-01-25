package prak.travelerapp.ItemDatabase;

/**
 * Created by marcel on 24.11.15.
 *
 * Entspricht dem Aufbau eines Datensatzes der Tabelle
 */
public class Dataset implements Comparable<Dataset> {

    private int itemID;
    private String itemName;
    private int kategorie; // 0 für "Kleidung", 1 für "Hygiene", 2 für "Equipment", 3 für "Dokumente"

    public Dataset(int itemID, String itemName,int kategorie) {

        this.itemID = itemID;
        this.itemName = itemName;
        this.kategorie = kategorie;

    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public int getKategorie() { return kategorie; }

    @Override
    public String toString() {

        String output = itemName;
        return output;
    }

    @Override
    public int compareTo(Dataset another) {
        return Integer.compare(this.itemID,another.getItemID());
    }
}
