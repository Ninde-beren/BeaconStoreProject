package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.services.ServicePromoBanniere;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;

public class Preload extends Activity {

    protected static final String ART = "art";
    protected static final String OFF = "off";

    private PromoBanniereDAO promoBanniereDAO;

    private AndrestClient rest = new AndrestClient();
    private String url = "http://beaconstore.ninde.fr/serverRest.php/promobanniere?";

    //booleen permettant de savoir si les requête envoyée par l'API ont bien fonctionnées
    private Boolean requete = false;

    //id de la dernière promotion insérée dans la base SQLite
    private long insertId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        Log.i("url", url);
        Log.i("je suis dans onCreate", ART);

        //execution de la requête POST (cf API) en arrière plan dans un autre thread
        new doRequest(Preload.this, "post", url).execute();

        Log.i("je suis a la fin de onCreate", ART);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                nextpage();
            }
        }, 2000);
    }

    private void nextpage() {
        Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(nextScreen);
    }

//TODO faire une méthode qui check toute les certaine heure ou 1 fois par jours, si il y a des nouvelles Promo bannieres, si la date est dépassée elle sont supprimées

    private void chekPromoBanniere() {

    }

    //---------------------------------------------------------------------------------------
    private class doRequest extends AsyncTask<Void, Void, Boolean> {

        // Store context for dialogs
        private Context context = null;
        // Store error message
        private Exception e = null;
        // Passed in data object
        private Map<String, Object> data = null;
        // Passed in method
        private String method = "";
        // Passed in url
       private String url = "";

        private JSONObject result;

        public doRequest(Context context, String method, String url) {

            this.context = context;
            this.data = data;
            this.method = method;
            this.url = url;
        }

        //-------------------------------------------------------------------------------------

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {

                Log.i("url", url);
                Log.i("je suis avant API", ART);

                result = rest.request(url, method, data); // Do request - Envoi de la requête (API), réception des données (JSON)

                Log.i("je suis apres API", ART);

                Log.i("JSON", String.valueOf(result));

                int jsonSize = result.length();

                //Parcours de notre objet JSON (plusieurs promotions)
                for (int i = 0; i < jsonSize; i++) {

                    JSONObject jobj = result.getJSONObject("" + i + "");

                    //Enregistrement de la promotion dans la base de données SQLite
                    PromoBanniereMetier promo = new PromoBanniereMetier();

                    promo.setIdbanniere(jobj.getString("idpromo"));
                    promo.setLbPromo(jobj.getString("lbpromo"));
                    promo.setTitrePromo(jobj.getString("titrepro"));
                    promo.setTxtBanniere(jobj.getString("txtpromo"));
                    promo.setDtdebval(jobj.getString("dtdebval"));
                    promo.setDtfinval(jobj.getString("dtfinval"));
                    promo.setTypBanniere(jobj.getString("typpromo"));
                    promo.setImageart(jobj.getString("imgoff"));
                    promo.setImageoff(jobj.getString("imgart"));

                    insertId = promoBanniereDAO.addPromoBanniere(promo);

                    Log.i("insertId", String.valueOf(insertId));

                    requete = insertId != -1;
                }

            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return requete;
        }

        //-------------------------------------------------------------------------------------

        @Override
        protected void onPostExecute(Boolean data) {
            super.onPostExecute(data);
            // Display based on error existence
            if (e != null) {

                //new ResponseDialog(context, "We found an error!", e.getMessage()).showDialog();
                requete = false;

            } else {

                if(data) {

                    Log.i("salut", "salut");

                    Intent intent = new Intent(Preload.this, Notification.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("lastIdInsert", insertId); //on insère dans l'intent l'id de la dernière promotion enregistrée en bdd SQLite

                    startActivity(intent); //Activation de l'activité
                }

                //new ResponseDialog(context, "OK", e.getMessage()).showDialog();
                Log.i("JSON2", data.toString());
            }
        }
    }
}