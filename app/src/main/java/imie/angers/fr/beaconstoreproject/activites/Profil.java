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

    private Button retour;
    private Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Récupération des infos dans la base et les lier avec les vues
        consommateurDAO = new ConsommateurDAO(this);
        consommateur = new ConsommateurMetier();

        consommateur = consommateurDAO.getConsommateur();

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

        nom.setText(    consommateur.getNom());
        prenom.setText( consommateur.getPrenom());
        genre.setText(  consommateur.getGenre());
        tel.setText(    consommateur.getTel());
        email.setText(  consommateur.getEmail());
        csp.setText(    consommateur.getCatsocpf());
        cp.setText(     consommateur.getCdpostal());
        dtNaiss.setText(consommateur.getDtnaiss());

        //-------------------------------------------------------------------------------------

        retour = (Button) findViewById(R.id.buttonPreviousProfil);
        retour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Inscription.class);
                startActivity(nextScreen);

            }
       });

        valider = (Button) findViewById(R.id.buttonValiderProfil);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);

            }
        });
    }
}