package imie.angers.fr.beaconstoreproject.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

public class Profil extends AppCompatActivity {

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

    private FloatingActionButton modifier;


    private SessionManager user;

    private long id;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        getSupportActionBar().setElevation(0);

        // Récupération des infos dans la base et les lier avec les vues
        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        consommateur = new ConsommateurMetier();

        user = new SessionManager(Profil.this);
        id = user.getIdC();

        Log.i("idAnne", String.valueOf(id));

        try {
            consommateur = new AsyncTask<Void, Void, ConsommateurMetier>() {
                @Override
                protected ConsommateurMetier doInBackground(Void... params) {

                    return consommateurDAO.getConsommateur(id);
                }
            }.execute().get() ;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("consomateurAnne :", consommateur.toString());

        //-------------------------------------------------------------------------------------

        nom       = (TextView) findViewById(R.id.nomProfil);
        prenom    = (TextView) findViewById(R.id.prenomProfil);
        genre     = (TextView) findViewById(R.id.genreProfil);
        tel       = (TextView) findViewById(R.id.telProfil);
        email     = (TextView) findViewById(R.id.emailProfil);
        csp       = (TextView) findViewById(R.id.cspProfil);
        cp        = (TextView) findViewById(R.id.cpProfil);
        dtNaiss   = (TextView) findViewById(R.id.dtNaissProfil);

        nom.setText(    getString(R.string.nom)     + " " + consommateur.getNom());
        prenom.setText( getString(R.string.prenom)  + " " + consommateur.getPrenom());
        if(consommateur.getGenre().equals("M")){ genre.setText(getString(R.string.genre)   + " " + "Homme");}
        else if(consommateur.getGenre().equals("F")) {genre.setText(getString(R.string.genre) + " " + "Femme");}
        else{genre.setText(getString(R.string.genre) + consommateur.getGenre());}
        tel.setText(    getString(R.string.tel)     + " " + consommateur.getTel());
        email.setText(  getString(R.string.email)   + " " + consommateur.getEmail());
        csp.setText(    getString(R.string.csp)     + " " + consommateur.getCatsocpf());
        cp.setText(     getString(R.string.cp)      + " " + consommateur.getCdpostal());
        dtNaiss.setText(getString(R.string.dtnaiss) + " " + consommateur.getDtnaiss());

        //-------------------------------------------------------------------------------------

        modifier = (FloatingActionButton) findViewById(R.id.buttonModifierProfil);
        modifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), ModifierProfil.class);
                nextScreen.putExtra("consoModif", consommateur);
                startActivity(nextScreen);
            }
       });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
        }
        return true;
    }
}