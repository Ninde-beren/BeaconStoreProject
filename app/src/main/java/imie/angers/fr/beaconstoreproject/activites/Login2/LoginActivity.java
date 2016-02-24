package imie.angers.fr.beaconstoreproject.activites.Login2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import butterknife.Bind;
import butterknife.ButterKnife;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.MainActivity2;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

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

    private String url = "http://beaconstore.ninde.fr/serverRest.php/consommateur?";

    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.btn_login) Button loginButton;
    @Bind(R.id.link_signup) TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);

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
                // Activiation de l'activity Signup
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP); //demarre l'activité avec un resultat 0 si activité bien démarrée
            }
        });
    }

    /**
     * Permet de vérifier les informations de login
     */

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

        hasher(password, "MD5"); // hashe le mot de passe en md5

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

                    requete = result.getString("success").equals("1"); //on verifie que la requete s'est bien passée

                    if (requete) {

                        //enregistrement du consommateur dans la base de données SQLite
                        ConsommateurMetier conso = new ConsommateurMetier();

                        DateFormat df = new SimpleDateFormat();

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
                        conso.setDtnaiss(df.parse(result.getString("dtnaiss")));

                        consommateurDAO.addConsommateur(conso);
                    }

                } catch (Exception e) {
                    this.e = e;    // Store error
                }

                return requete;
            }

            @Override
            protected void onPostExecute(Boolean data) {
                super.onPostExecute(data);

                // On complete call either onLoginSuccess or onLoginFailed
                onLoginSuccess();
                // onLoginFailed();
                progressDialog.dismiss();

                if (e != null) {

                    //new ResponseDialog(context, "We found an error!", e.getMessage()).showDialog();
                    requete = false;

                } else {

                    if (data) {

                        Log.i("salut", "salut");

                        Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra("lastIdInsert", insertId); //on insère dans l'intent l'id de la dernière promotion enregistrée en bdd SQLite

                        startActivity(intent); //Activiation de l'activité

                    } else {

                        //Affichage d'un toast indiquant une erreur de connexion
                        Toast.makeText(context, "Email ou mot de passe invalide", Toast.LENGTH_SHORT).show();
                        clearAll();
                    }
                }
            }
        }.execute();
    }

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

    @Override
    public void onBackPressed() { //permet de revenir à l'activité précédente
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    /**
     * Méthode permettant de vérifier la format de l'email et du mdp
     * @return
     */

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

    /**
     * Permet de hasher le mot de passe
     * @param toHash
     * @param algorythm
     * @return
     */
    public byte[] hasher(String toHash, String algorythm) {
        byte[] hash = null;

        try {
        hash = MessageDigest.getInstance(algorythm).digest(toHash.getBytes());
        } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hash;
    }

    /**
     * Permet de remettre à zéro les champs email et mdp
     */
    public void clearAll(){
        emailText.setText("");
        passwordText.setText("");
    }
}

