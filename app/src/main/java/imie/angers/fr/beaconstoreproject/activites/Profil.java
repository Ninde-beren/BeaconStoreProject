package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;

public class Profil extends Activity {

    private TextView nom;
    private TextView prenom;
    private TextView genre;
    private TextView tel;
    private TextView email;
    private TextView csp;
    private TextView cp;
    private TextView dtNaiss;

    private ConsommateurMetier consommateur;
    private ConsommateurDAO consommateurDAO;

    private Button modifier;
    private Button supprimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Récupération des infos dans la base et les lier avec les vues
        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();
        consommateur = new ConsommateurMetier();

        //int user =1;

       // if(user==0){
            long id =0;

            consommateur = consommateurDAO.getConsommateur(id);
       // }

        //Log.i("consomateur :", consommateur.toString());

        //-------------------------------------------------------------------------------------

        nom       = (TextView) findViewById(R.id.nomProfil);
        prenom    = (TextView) findViewById(R.id.prenomProfil);
        genre     = (TextView) findViewById(R.id.genreProfil);
        tel       = (TextView) findViewById(R.id.telProfil);
        email     = (TextView) findViewById(R.id.emailProfil);
        csp       = (TextView) findViewById(R.id.cspProfil);
        cp        = (TextView) findViewById(R.id.cpProfil);
        dtNaiss   = (TextView) findViewById(R.id.dtNaissProfil);

        nom.setText(   "Nom :"                  + consommateur.getNom());
        prenom.setText( "Prénom :"              + consommateur.getPrenom());
        genre.setText(  "Genre :"               + consommateur.getGenre());
        tel.setText(    "Tél :"                 + consommateur.getTel());
        email.setText(  "E-mail :"              + consommateur.getEmail());
        csp.setText(    "Catégorie social :\n"  + consommateur.getCatsocpf());
        cp.setText(     "Code postal :"         + consommateur.getCdpostal());
        dtNaiss.setText("Date de naissance :\n" + consommateur.getDtnaiss());

        //-------------------------------------------------------------------------------------

        modifier = (Button) findViewById(R.id.buttonModifierProfil);
        modifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), ModifierProfil.class);
                startActivity(nextScreen);

            }
       });

        supprimer = (Button) findViewById(R.id.buttonSupprimerProfil);
        supprimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                consommateurDAO.deleteConso(1);

                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);

            }
        });
    }
}