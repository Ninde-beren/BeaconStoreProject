package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

public class Inscription extends Activity {

    private EditText nom;
    private EditText prenom;
    private Spinner genre;
    private EditText tel;
    private EditText email;
    private Spinner csp;
    private EditText cp;
    private EditText dtNaiss;

    private AndrestClient rest = new AndrestClient();
    private Boolean requete;

    private ConsommateurMetier consommateur;
    private ConsommateurDAO consommateurDAO;

    private String url = "http://beaconstore.ninde.fr/serverRest.php/consommateur?";

    private Button retour;
    private Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        //instantiation de la classe ConsommateurDAO
        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        //-----------------------------------------------------------------------------------------

        nom = (EditText) findViewById(R.id.nomInscription);
        prenom = (EditText) findViewById(R.id.prenomInscription);
        genre = (Spinner) findViewById(R.id.genreInscription);
        tel = (EditText) findViewById(R.id.telInscription);
        email = (EditText) findViewById(R.id.emailInscription);
        csp = (Spinner) findViewById(R.id.cspInscription);
        cp = (EditText) findViewById(R.id.cpInscription);
        dtNaiss = (EditText) findViewById(R.id.dtNaissInscription);

        //-----------------------------------------------------------------------------------------

        Spinner spinnerSexeType = (Spinner) findViewById(R.id.genreInscription);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSexeType = ArrayAdapter.createFromResource(this,
                R.array.sexetype_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSexeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSexeType.setAdapter(adapterSexeType);

        Spinner spinnerSocialStatut = (Spinner) findViewById(R.id.cspInscription);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSocialStatut = ArrayAdapter.createFromResource(this,
                R.array.socialstatut_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSocialStatut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSocialStatut.setAdapter(adapterSocialStatut);

        retour = (Button) findViewById(R.id.buttonPreviousInscription);
        retour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);

            }
        });

        valider = (Button) findViewById(R.id.buttonValiderInscription);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profil.class);
                startActivity(nextScreen);
                //On ecoute les clique sur le bouton login
                inscriptionSQLite(); // appelle de la méthode inscription dans la base SQlite
                consommateurDAO.addConsommateur(consommateur);
                inscriptionAPI(); // appelle de la méthode inscription dans la base SQlite
            }
        });

        //-------------------------------------------------------------------------------------

        // lier les infos du formulaire à des variables et les insérer dans une méthode requête add
    }

    private ConsommateurMetier inscriptionSQLite() {

        consommateur = new ConsommateurMetier();

        // récupération des données du formulaire pour la base SQLite
        consommateur.setNom(nom.getText().toString());
        consommateur.setPrenom(prenom.getText().toString());
        consommateur.setGenre(String.valueOf(genre.getOnItemSelectedListener()));
        consommateur.setTel(tel.getText().toString());
        consommateur.setEmail(email.getText().toString());
        consommateur.setCatsocpf(String.valueOf(csp.getOnItemSelectedListener()));
        consommateur.setCdpostal(cp.getText().toString());
        consommateur.setDtnaiss(dtNaiss.getText().toString());

        return consommateur;
    }

    private void inscriptionAPI() {

        // récupération des données du formulaire pour la base API
        String nomAPI = nom.getText().toString();
        String prenomAPI = prenom.getText().toString();
        String genreAPI = String.valueOf(genre.getOnItemSelectedListener());
        String telAPI = tel.getText().toString();
        //String emailAPI =   email.getText().toString();
        String cspAPI = String.valueOf(csp.getOnItemSelectedListener());
        String cpAPI = cp.getText().toString();
        String dtnaissAPI = dtNaiss.getText().toString();

        valider.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Inscription.this, ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Inscription...");
        progressDialog.show();

        Map<String, Object> toPost = new HashMap<String, Object>();
        toPost.put("nom", nomAPI);
        toPost.put("prenom", prenomAPI);
        toPost.put("sexe", genreAPI);
        toPost.put("tel", telAPI);
        toPost.put("catsocpf", cspAPI);
        toPost.put("cdpostal", cpAPI);
        toPost.put("dtnaiss", dtnaissAPI);

        Log.i("toPost", "param du conso : " + toPost);

        //execution de la requête POST (cf API) en arrière plan dans un autre thread
        new DoRequest(Inscription.this, toPost, "POST", url) {

            JSONObject result;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    Log.i("url", url);

                    result = rest.request(url, method, data); // Do request - Envoi de la requête (API), réception des données (JSON)

                    Log.i("JSON", String.valueOf(result));

                    if (result.getString("success").equals("1")) {

                        requete = true;

                        //TODO Toast "Merci est bienvenue"


                    } else {

                        requete = false;

                        Log.i("requete", String.valueOf(requete));

                        //TODO Toast "Dsl vous n'avons pas pu vous s'inscrire"
                    }

                } catch (Exception e) {
                    this.e = e;    // Store error
                }

                return requete;
            }

            @Override
            protected void onPostExecute(Boolean data) {
                super.onPostExecute(data);

                progressDialog.dismiss();

                if (e != null) {

                    Log.i("We found an error!", e.getMessage());
                    requete = false;

                } else {

                    if (data) {

                        Log.i("salut", "salut");

                        onInscriptionSuccess();
                        Intent intent = new Intent(Inscription.this, ListPromoBanniere.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent); //Activiation de l'activité

                    } else {

                        onInscriptionFailed();
                    }
                }
            }
        }.execute();
    }

    public void onInscriptionSuccess() {
        valider.setEnabled(true);
        finish();
    }

    public void onInscriptionFailed() {
        Toast.makeText(getBaseContext(), "inscription failed", Toast.LENGTH_LONG).show();

        valider.setEnabled(true);
    }
}