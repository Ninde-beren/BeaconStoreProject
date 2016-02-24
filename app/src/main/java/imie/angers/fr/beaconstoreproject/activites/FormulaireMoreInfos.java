package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import imie.angers.fr.beaconstoreproject.R;

public class FormulaireMoreInfos extends Activity {


    Button retour;
    Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_more_infos);

        Spinner spinnerSexeType = (Spinner) findViewById(R.id.sexetype);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSexeType = ArrayAdapter.createFromResource(this,
                R.array.sexetype_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSexeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSexeType.setAdapter(adapterSexeType);

        Spinner spinnerSocialStatut = (Spinner) findViewById(R.id.socialstatut);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSocialStatut = ArrayAdapter.createFromResource(this,
                R.array.socialstatut_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSocialStatut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSocialStatut.setAdapter(adapterSocialStatut);

        retour = (Button) findViewById(R.id.buttonPreviousformulaireprofil);
        retour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);

            }
        });

        valider = (Button) findViewById(R.id.buttonValiderformulaireprofil);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profil.class);
                startActivity(nextScreen);

            }
        });
    }
}
