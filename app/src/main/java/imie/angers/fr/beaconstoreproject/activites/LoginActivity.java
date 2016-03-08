package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;
import imie.angers.fr.beaconstoreproject.utils.StringUtils;

/**
 * Classe permettant de vérifier les informations de connexion d'un consommateur
 * Created by plougastel.dl03 on 23/02/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private AndrestClient rest = new AndrestClient();
    private Boolean requete;

    private ConsommateurDAO consommateurDAO;
    private ConsommateurMetier conso;

    private String url = "http://beaconstore.ninde.fr/serverRest.php/consommateur?";

    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.btn_login) Button loginButton;
    @Bind(R.id.link_signup) TextView signupLink;

/**************************************************************************************************
* ACTION BAR
**************************************************************************************************/

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

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);


        //instantiation de la classe ConsommateurDAO
        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        //On ecoute les clique sur le bouton login
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(); // appelle de la méthode login permettant de ce logger
            }
        });

        //Ecoute des cliques sur le lien sign up
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Activiation de l'activity Incription
                Intent intent = new Intent(getApplicationContext(), Inscription.class);
                startActivityForResult(intent, REQUEST_SIGNUP); //demarre l'activité avec un resultat 0 si activité bien démarrée
            }
        });
    }

/*************************************************************************************************
* PERMET DE VERIFIER LES INFORMATION DU LOGIN
*************************************************************************************************/

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) { //si email et/ou mdp sont invalides

            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        password = StringUtils.md5(password);

        //password = hasher(password, "MD5"); // hashe le mot de passe en md5

        Map<String, Object> toPost = new HashMap<String, Object>();
        toPost.put("email", email);
        toPost.put("password", password);

        Log.i("toPost", "param du conso : " + toPost);

        //execution de la requête POST (cf API) en arrière plan dans un autre thread
        new DoRequest(LoginActivity.this, toPost, "POST", url) {

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

                        //enregistrement du consommateur dans la base de données SQLite
                        conso = new ConsommateurMetier();

                        //SimpleDateFormat df = new SimpleDateFormat(result.getString("dtnaiss"), Locale.FRANCE);

                        conso.setIdConso(result.getInt("idconso"));
                        conso.setEmail(result.getString("email"));
                        conso.setPassword(result.getString("password"));
                        conso.setNom(result.getString("nom"));
                        conso.setPrenom(result.getString("prenom"));
                        conso.setTel(result.getString("tel"));
                        conso.setToken(result.getString("token"));
                        conso.setCatsocpf(result.getString("catsocpf"));
                        conso.setCdpostal(result.getString("cdpostal"));
                        conso.setGenre(result.getString("sexe"));
                        conso.setDtnaiss(result.getString("dtnaiss"));

                        long consoId = consommateurDAO.addConsommateur(conso);

                        SessionManager session = new SessionManager(LoginActivity.this);
                        session.createConsoSession(consoId, result.getString("nom"), result.getString("prenom"));

                    } else {

                        requete = false;

                        Log.i("requete", String.valueOf(requete));
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

                        onLoginSuccess();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra("lastIdInsert", conso); //on insère dans l'intent l'id de la dernière promotion enregistrée en bdd SQLite
                        intent.putExtra("id", 2);
                        startActivity(intent); //Activiation de l'activité

                    } else {

                        onLoginFailed();
                        //Affichage d'un toast indiquant une erreur de connexion
                        //Toast.makeText(context, "Email ou mot de passe invalide", Toast.LENGTH_SHORT).show();
                        //clearAll();
                    }
                }
            }
        }.execute();
    }

/*************************************************************************************************
* RETOURNE QUELQUE CHOSE QUAND L AUTHENTIFICATION A FONCTIONNE
*************************************************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

/*************************************************************************************************
* PERMET DE REVENIR A L ACTIVITE PRECEDENTE
*************************************************************************************************/

    @Override
    public void onBackPressed() { //permet de revenir à l'activité précédente
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

/*************************************************************************************************
* ACTION QUAND L AUTHENTIFICATION A FONCTIONNE
*************************************************************************************************/

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }
/*************************************************************************************************
* ACTION QUAND L AUTHENTIFICATION N A PAS FONCTIONNE
*************************************************************************************************/

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

/*************************************************************************************************
* METHODE PERMETTANT DE VERIFIER LE FORMAT DE L EMAIL ET DU MOT DE PASSE
* @return
**************************************************************************************************/

    public boolean validate() {

        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Email invalide");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

/*************************************************************************************************
* PERMET DE REMETTRE A ZERO LES CHAMPS EMZAIL ET MOT DE PASSE
*************************************************************************************************/

    public void clearAll(){
        emailText.setText("");
        passwordText.setText("");
    }
}

