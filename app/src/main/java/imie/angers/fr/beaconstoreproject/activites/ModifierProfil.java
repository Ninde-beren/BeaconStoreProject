package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

/**
 * Created by Ninde on 27/02/2016.
 */
public class ModifierProfil extends Activity{

    private EditText nom;
    private EditText prenom;
    private Spinner genre;
    private EditText tel;
    private EditText email;
    private Spinner csp;
    private EditText cp;
    private DatePicker dateNaiss;
    private String dtNaiss = String.valueOf(dateNaiss.getDayOfMonth() +"/" + dateNaiss.getMonth() +"/"+ dateNaiss.getYear());

    private AndrestClient rest = new AndrestClient();
    private Boolean requete;

    private ConsommateurMetier consommateur;
    private ConsommateurDAO consommateurDAO;

    private String url = "http://beaconstore.ninde.fr/serverRest.php/consommateur?";

    private Button annuler;
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
        tel = (EditText) findViewById(R.id.telInscription);
        email = (EditText) findViewById(R.id.emailInscription);
        cp = (EditText) findViewById(R.id.cpInscription);
        dateNaiss = (DatePicker) findViewById(R.id.dtNaissInscription);

        //-----------------------------------------------------------------------------------------

        genre = (Spinner) findViewById(R.id.genreInscription);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSexeType = ArrayAdapter.createFromResource(this,
                R.array.sexetype_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSexeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genre.setAdapter(adapterSexeType);

        csp = (Spinner) findViewById(R.id.cspInscription);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSocialStatut = ArrayAdapter.createFromResource(this,
                R.array.socialstatut_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSocialStatut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        csp.setAdapter(adapterSocialStatut);

        annuler = (Button) findViewById(R.id.buttonPreviousInscription);
        annuler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profil.class);
                startActivity(nextScreen);

            }
        });

        valider = (Button) findViewById(R.id.buttonValiderInscription);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profil.class);
                startActivity(nextScreen);
                //On ecoute les clique sur le bouton login
                modifierProfilSQLite(); // appelle de la méthode inscription dans la base SQlite
                consommateurDAO.addConsommateur(consommateur);
                modifierProfilAPI(); // appelle de la méthode inscription dans la base SQlite
            }
        });

        //-------------------------------------------------------------------------------------

        nom.setText(   "Nom :"                  + consommateur.getNom());
        prenom.setText("Prénom :" + consommateur.getPrenom());
        genre.setPrompt("Genre :" + consommateur.getGenre());
        tel.setText("Tél :" + consommateur.getTel());
        email.setText("E-mail :" + consommateur.getEmail());
        csp.setPrompt("Catégorie social :\n" + consommateur.getCatsocpf());
        cp.setText("Code postal :" + consommateur.getCdpostal());
        //dateNaiss.setText("Date de naissance :\n%s", consommateur.getDtnaiss());

        //-------------------------------------------------------------------------------------

        // lier les infos du formulaire à des variables et les insérer dans une méthode requête add
    }

    private ConsommateurMetier modifierProfilSQLite() {

        consommateur = new ConsommateurMetier();

        // récupération des données du formulaire pour la base SQLite
        consommateur.setNom(nom.getText().toString());
        consommateur.setPrenom(prenom.getText().toString());
        consommateur.setGenre(String.valueOf(genre.getOnItemSelectedListener()));
        consommateur.setTel(tel.getText().toString());
        consommateur.setEmail(email.getText().toString());
        consommateur.setCatsocpf(String.valueOf(csp.getOnItemSelectedListener()));
        consommateur.setCdpostal(cp.getText().toString());
        consommateur.setDtnaiss(dtNaiss);

        return consommateur;
    }

    private void modifierProfilAPI() {


        // récupération des données du formulaire pour la base API
        String nomAPI = nom.getText().toString();
        String prenomAPI = prenom.getText().toString();
        String genreAPI = String.valueOf(genre.getOnItemSelectedListener());
        String telAPI = tel.getText().toString();
        //String emailAPI =   email.getText().toString();
        String cspAPI = String.valueOf(csp.getOnItemSelectedListener());
        String cpAPI = cp.getText().toString();
        String dtnaissAPI = dtNaiss;

        valider.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ModifierProfil.this, ProgressDialog.STYLE_SPINNER);

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
        new DoRequest(ModifierProfil.this, toPost, "PUT", url) {

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

                        //Toast "Merci est bienvenue"
                        Toast.makeText(context, "Merci et bienvenue", Toast.LENGTH_SHORT).show();


                    } else {

                        requete = false;

                        Log.i("requete", String.valueOf(requete));

                        //Toast "Dsl vous n'avons pas pu vous s'inscrire".
                        Toast.makeText(context, "Dsl vous n'avons pas pu vous s'inscrire", Toast.LENGTH_SHORT).show();

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

                        onModifierProfilSuccess();
                        Intent intent = new Intent(ModifierProfil.this, ListPromoBanniere.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent); //Activiation de l'activité

                    } else {

                        onModifierProfilFailed();
                    }
                }
            }
        }.execute();
    }

    public void onModifierProfilSuccess() {
        valider.setEnabled(true);
        finish();
    }

    public void onModifierProfilFailed() {
        Toast.makeText(getBaseContext(), "inscription failed", Toast.LENGTH_LONG).show();

        valider.setEnabled(true);
    }
}