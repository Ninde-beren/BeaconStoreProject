package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import imie.angers.fr.beaconstoreproject.exceptions.RESTException;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;
import imie.angers.fr.beaconstoreproject.utils.StringUtils;

/**
 * Created by Ninde on 27/02/2016.
 */
public class ModifierProfil extends Activity{

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
    private int idConsoAPI;

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

        Intent intent = getIntent();
        consommateur = intent.getParcelableExtra("consoModif");

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

        //************************************************************************
        // Récupération des infos dans la base et les lier avec les vues

        dtNaiss = String.valueOf(dateNaiss.getDayOfMonth() +"/" + dateNaiss.getMonth() +"/"+ dateNaiss.getYear());

        email.setText(consommateur.getEmail());
        mdp.setText(consommateur.getPassword());
        nom.setText(consommateur.getNom());
        prenom.setText(consommateur.getPrenom());
        genre.setPrompt(consommateur.getGenre());
        tel.setText(consommateur.getTel());
        csp.setPrompt(consommateur.getCatsocpf());
        cp.setText(consommateur.getCdpostal());

        //*************************************************************

        valider = (Button) findViewById(R.id.buttonValiderInscription);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                consommateur = inscriptionSQLite(); // appelle de la méthode inscription dans la base SQlite

                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {

                        Log.i("gotoupdateConso", "ICI");

                        long idConso = consommateurDAO.updateConso(consommateur);

                        req = idConso != -1;

                        return req;
                    }
                }.execute();

                inscriptionAPI(); // appelle de la méthode inscription dans la base SQlite
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
        consommateur.setPassword(StringUtils.md5(mdp.getText().toString()));
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

        idConsoAPI   = consommateur.getIdConso();
        emailAPI     = consommateur.getEmail();
        mdpAPI       = consommateur.getPassword();
        nomAPI       = consommateur.getNom();
        prenomAPI    = consommateur.getPrenom();
        genreAPI     = consommateur.getGenre().equals("Homme") ? 'M' :
                       consommateur.getGenre().equals("Femme") ? 'F' : ' ';
        telAPI       = consommateur.getTel();
        cspAPI       = consommateur.getCatsocpf();
        cpAPI        = consommateur.getCdpostal();
        dtnaissAPI   = consommateur.getDtnaiss();

        valider.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ModifierProfil.this, ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Modifications en cours...");
        progressDialog.show();

        Map<String, Object> toPut = new HashMap<String, Object>();

        toPut.put("idconso", idConsoAPI);
        toPut.put("nom", nomAPI);
        toPut.put("prenom", prenomAPI);
        toPut.put("email", emailAPI);
        toPut.put("password", mdpAPI);
        toPut.put("sexe", genreAPI);
        toPut.put("tel", telAPI);
        toPut.put("catsocpf", cspAPI);
        toPut.put("cdpostal", cpAPI);
        toPut.put("dtnaiss", dtnaissAPI);

        Log.i("toPut", "param du conso : " + toPut);

        //execution de la requête POST (cf API) en arrière plan dans un autre thread
        new DoRequest(ModifierProfil.this, toPut, "POST", url) {

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

                        onInscriptionSuccess();

                        //Toast "Merci est bienvenue"
                        Toast.makeText(context, "Votre profil a bien été mis à jour", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ModifierProfil.this, Profil.class);
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