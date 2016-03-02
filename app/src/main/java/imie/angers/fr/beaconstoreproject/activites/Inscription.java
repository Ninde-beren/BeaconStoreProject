package imie.angers.fr.beaconstoreproject.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.exceptions.RESTException;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

public class Inscription extends AppCompatActivity {

    private EditText mdp;
    private EditText nom;
    private EditText prenom;
    private Spinner genre;
    private EditText tel;
    private EditText email;
    private Spinner csp;
    private EditText cp;
    private DatePicker dateNaiss;
    private String dtNaiss;

    private String emailAPI;
    private String mdpAPI;
    private String nomAPI;
    private String prenomAPI;
    private char genreAPI;
    private String telAPI;
    private String cspAPI;
    private String cpAPI;
    private String dtnaissAPI;
    private String adressmacAPI;
    private String passwordAPI;

    private AndrestClient rest = new AndrestClient();
    private Boolean requete;

    private ConsommateurMetier consommateur;
    private ConsommateurDAO consommateurDAO;

    private String url = "http://beaconstore.ninde.fr/serverRest.php/consommateur?";
    private Button valider;

    private Boolean req;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        //instantiation de la classe ConsommateurDAO
        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        consommateur = new ConsommateurMetier();

        //-----------------------------------------------------------------------------------------

        email = (EditText) findViewById(R.id.emailInscription);
        mdp = (EditText) findViewById(R.id.passwordInscription);
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

        valider = (Button) findViewById(R.id.buttonValiderInscription);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                consommateur = inscriptionSQLite(); // appelle de la méthode inscription dans la base SQlite

                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {

                        long idConso = consommateurDAO.addConsommateur(consommateur);

                        req = idConso != -1;

                        return req;
                    }
                };

                inscriptionAPI(); // appelle de la méthode inscription dans la base SQlite

                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                nextScreen.putExtra("id", 3);
                startActivity(nextScreen);
            }
        });
    }

/**************************************************************************************************
* ENREGISTREMENT DES INFORMATIONS DANS LA BASE DE DONNEES SQLITE
**************************************************************************************************/

    private ConsommateurMetier inscriptionSQLite() {

        dtNaiss = String.valueOf(dateNaiss.getDayOfMonth() +"/" + dateNaiss.getMonth() +"/"+ dateNaiss.getYear());

        //récupération des données du formulaire pour la base SQLite

        consommateur.setEmail(email.getText().toString());
        consommateur.setPassword(mdp.getText().toString());
        consommateur.setNom(nom.getText().toString());
        consommateur.setPrenom(prenom.getText().toString());

        if(genre.getSelectedItem().toString().equals("Genre..."))
        {consommateur.setGenre("");}
        else{consommateur.setGenre(genre.getSelectedItem().toString());}
        consommateur.setTel(tel.getText().toString());

        if(csp.getSelectedItem().toString().equals("Catégorie social..."))
        {consommateur.setCatsocpf("");}
        else{consommateur.setCatsocpf(csp.getSelectedItem().toString());}
        consommateur.setCdpostal(cp.getText().toString());
        consommateur.setDtnaiss(dtNaiss);

        return consommateur;

    }

/*************************************************************************************************
* ENREGISTEREMENT DES INFORMATION DANS LA BASE DE DONNEES DE L API
*************************************************************************************************/

    private void inscriptionAPI() {

        dtNaiss = String.valueOf(dateNaiss.getDayOfMonth() + "/" + dateNaiss.getMonth() + "/" + dateNaiss.getYear());

        emailAPI     = consommateur.getEmail();
        mdpAPI       = consommateur.getPassword();
        nomAPI       = consommateur.getNom();
        prenomAPI    = consommateur.getPrenom();
        genreAPI     = consommateur.getGenre().equals("Homme") ? 'M' :
                       consommateur.getGenre().equals("Femmes") ? 'F' : ' ';
        telAPI       = consommateur.getTel();
        cspAPI       = consommateur.getCatsocpf();
        cpAPI        = consommateur.getCdpostal();
        dtnaissAPI   = consommateur.getDtnaiss();
        passwordAPI  = consommateur.getPassword();

        WifiManager mana = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = mana.getConnectionInfo();
        String mac = info.getMacAddress();

        adressmacAPI = mac == null ? null : mac;

        valider.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Inscription.this, ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Inscription...");
        progressDialog.show();

        Map<String, Object> toPost = new HashMap<String, Object>();
        toPost.put("nom", nomAPI);
        toPost.put("prenom", prenomAPI);
        toPost.put("email", emailAPI);
        toPost.put("sexe", genreAPI);
        toPost.put("tel", telAPI);
        toPost.put("catsocpf", cspAPI);
        toPost.put("cdpostal", cpAPI);
        toPost.put("dtnaiss", dtnaissAPI);
        toPost.put("adrmac", adressmacAPI);

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

                    requete = true;

                        //Toast "Merci est bienvenue"
                        Toast.makeText(context, "Merci et bienvenue", Toast.LENGTH_SHORT).show();

                    } catch (RESTException e1) {

                    e1.printStackTrace();

                    requete = false;

                } catch (Exception e) {
                    this.e = e;    // Store error

                    requete = false;
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

                        Intent intent = new Intent(Inscription.this, MainActivity.class);
                        intent.putExtra("id", 3);
                        startActivity(intent); //Activiation de l'activité

                    } else {

                        onInscriptionFailed();
                    }
                }
            }
        }.execute();
    }

/*************************************************************************************************
* VERIFICATION QUE TOUT C EST BIEN PASSE
*************************************************************************************************/

    public void onInscriptionSuccess() {
        valider.setEnabled(true);
        finish();
    }

    public void onInscriptionFailed() {
        Toast.makeText(getBaseContext(), "inscription failed", Toast.LENGTH_LONG).show();

        valider.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
        }
        return true;
    }

}