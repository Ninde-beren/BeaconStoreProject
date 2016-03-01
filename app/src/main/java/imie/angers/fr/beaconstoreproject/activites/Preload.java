package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.exceptions.RESTException;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

public class Preload extends Activity {

    private PromoBanniereDAO promoBanniereDAO;
    private PromoBeaconDAO promoBeaconDAO;

    private Boolean requete;
    private long lastInsertId;

    private AndrestClient rest = new AndrestClient();
    private String url = "http://beaconstore.ninde.fr/serverRest.php/promobanniere?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBanniereDAO = new PromoBanniereDAO(this);

        promoBanniereDAO.open();
        promoBeaconDAO.open();

        Log.i("url", url);
        Log.i("je suis dans onCreate", "ergaeg");

        //execution de la requête POST (cf API) en arrière plan dans un autre thread

        new DoRequest(Preload.this, "GET", url) {

           JSONObject result;

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                   result =  rest.request(url, method, data); // Do request - Envoi de la requête (API), réception des données (JSON)

                    Log.i("resultJSON", String.valueOf(result));

                   int jsonSize = result.length();

                    Log.i("sizeJson", String.valueOf(jsonSize));

                    //Parcours de notre objet JSON (plusieurs promotions)

                    promoBanniereDAO.deleteTablePromoBanniere();

                    for (int i = 0; i < jsonSize; i++) {

                        JSONObject jobj = null;

                        jobj = result.getJSONObject("" + i + "");

                        Log.i("JSON idpromo", jobj.getString("idpromo"));

                        //Enregistrement de la promotion dans la base de données SQLite
                        PromoBanniereMetier promo = new PromoBanniereMetier();

                        promo.setIdbanniere(jobj.getString("idpromo"));
                        promo.setLbPromo(jobj.getString("lbpromo"));
                        promo.setTitrePromo(jobj.getString("titrepro"));
                        promo.setTxtBanniere(jobj.getString("txtpromo"));
                        promo.setDtdebval(jobj.getString("dtdebval"));
                        promo.setDtfinval(jobj.getString("dtfinval"));
                        promo.setTypBanniere(jobj.getString("typpromo"));
                        promo.setImageart(jobj.getString("imageoff"));
                        promo.setImageoff(jobj.getString("imageart"));


                        //Enregistrement base SQLite

                        lastInsertId = promoBanniereDAO.addPromoBanniere(promo);

                    }

                    requete =  true;

                } catch (RESTException e1) {

                    e1.printStackTrace();

                    requete = false;

                } catch (JSONException e1) {
                    e1.printStackTrace();

                    requete = false;
                }

                return requete;
            }

            @Override
            protected void onPostExecute(Boolean data) {
                super.onPostExecute(data);

                if(data) {
                    nextpage();
                }
            }

        }.execute();

        //Log.i("je suis a la fin de onCreate", "dfgeg");

        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                nextpage();
            }
        }, 2000);*/
    }


    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        promoBeaconDAO.deleteTablePromoBeacon();
        promoBanniereDAO.deleteTablePromoBanniere();
    }*/

    private void nextpage() {
        Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(nextScreen);
    }

    //TODO faire une méthode qui check toute les certaine heure ou 1 fois par jours, si il y a des nouvelles Promo bannieres, si la date est dépassée elle sont supprimées

    private void chekPromoBanniere() {

    }

    //---------------------------------------------------------------------------------------
    /*private class doRequest extends AsyncTask<Void, Void, Boolean> {

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
    }*/
}