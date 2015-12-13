package prak.travelerapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by marcel on 13.12.15.
 * Popup zum Eingeben eines neuen Items und Auswählen der Kategorie
 */
public class AddItemPopUp extends Activity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private static final String[]paths = {"Kleidung", "Hygiene", "Equipment", "Dokumente"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_item_popup);

        // Gibt uns die Dimensionen des Screens
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Größe des Screens
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*0.80), (int) (height*0.80));

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddItemPopUp.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Was passiert wenn "Kleidung" ausgewählt wird
                break;
            case 1:
                // Was passiert wenn "Hygiene" ausgewählt wird
                break;
            case 2:
                // Was passiert wenn "Equipment" ausgewählt wird
                break;
            case 3:
                // Was passiert wenn "Dokumente" ausgewählt wird
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO
    }
}
