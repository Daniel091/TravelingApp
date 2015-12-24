package prak.travelerapp.ItemList;

/**
 * Created by Michael on 24.12.15.
 */
public class ListItem {

    private int id;
    private String name;
    private boolean checked;

    public ListItem(int id, String name, boolean checked){

        this.id = id;
        this.name = name;
        this.checked = checked;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setId(int id) {

        this.id = id;
    }
}
