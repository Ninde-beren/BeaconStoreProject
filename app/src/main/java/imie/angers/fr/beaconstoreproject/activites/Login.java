package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

/**
 * Created by Anne on 21/02/2016.
 * Classe permettant de vérifier les informations de connexion d'un consommateur
 */

public class Login extends Activity {

        public ProgressDialog progressDialog;

        private EditText email;
        private EditText mdp;
        private Button validate;
        private Button cancel;

        private AndrestClient rest = new AndrestClient();
        private String url = "http://beaconstore.ninde.fr/serverRest.php/consommateur?";

        private Boolean requete;

        private ConsommateurDAO consommateurDAO;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            // initialisation de la progress bar
            progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);

            consommateurDAO = new ConsommateurDAO(this);

            email = (EditText) findViewById(R.id.emailprofil);
            mdp = (EditText) findViewById(R.id.password);
            validate = (Button) findViewById(R.id.email_sign_in_button);
            //cancel = (Button) findViewById(R.id.btnCancel);

            //On définit l'action liée au bouton Valider
            validate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int userMail = email.length();
                    int userpsswd = mdp.length();

                    if(userMail > 0 && userpsswd > 0) {

                        String login = email.getText().toString();

                        if(login.matches("^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$")) {

                            String password = mdp.getText().toString();

                            hasher(password, "MD5"); // hashe le mot de passe en md5

                            Map<String, Object> toPost = new HashMap<String, Object>();
                            toPost.put("email", login);
                            toPost.put("password", password);

                            Log.i("toPost", "param du conso : " + toPost);

                            //execution de la requête POST (cf API) en arrière plan dans un autre thread
                            new DoRequest(Login.this, toPost, "POST", url) {

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

                                        requete = result.getString("success").equals(1);

                                        if (requete) {

                                            //enregistrement dans la base de données SQLite
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

                                    progressDialog.dismiss();

                                    if (e != null) {

                                        //new ResponseDialog(context, "We found an error!", e.getMessage()).showDialog();
                                        requete = false;

                                    } else {

                                        if(data) {

                                            Log.i("salut", "salut");

                                            Intent intent = new Intent(Login.this, MainActivity.class);
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
                        } else {
                            Toast.makeText(getApplicationContext(), "Champ email invalide", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email ou mot de passe invalide", Toast.LENGTH_SHORT).show();
                        clearAll();
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearAll();
                }
            });
        }

    public byte[] hasher(String toHash, String algorythm) {
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance(algorythm).digest(toHash.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hash;
    }

    public void clearAll(){
        email.setText("");
        mdp.setText("");
    }
}