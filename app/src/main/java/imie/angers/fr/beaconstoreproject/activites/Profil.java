package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import imie.angers.fr.beaconstoreproject.R;

public class Profil extends Activity {

    Button retour;
    Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        //retour = (Button) findViewById(R.id.buttonPreviousprofil);
        //retour.setOnClickListener(new View.OnClickListener() {
       //     public void onClick(View v) {
       //         Intent nextScreen = new Intent(getApplicationContext(), FormulaireMoreInfos.class);
       //         startActivity(nextScreen);

      //      }
    //   });

       // valider = (Button) findViewById(R.id.buttonValiderprofil);
        //valider.setOnClickListener(new View.OnClickListener() {
         //   public void onClick(View v) {
           //     Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
             //   startActivity(nextScreen);

           // }
        //});
    }
}
