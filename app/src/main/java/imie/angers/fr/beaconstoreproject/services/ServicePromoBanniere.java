package imie.angers.fr.beaconstoreproject.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.activites.Notification;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * Created by Ninde on 24/02/2016.
 */
public class ServicePromoBanniere extends Service {

    protected static final String ART = "art";
    protected static final String OFF = "off";

    //private NotificationDAO notificationDAO = new NotificationDAO(this);
    private PromoBanniereDAO promoBanniereDAO;

    private AndrestClient rest = new AndrestClient();
    private String url = "http://beaconstore.ninde.fr/serverRest.php/promobaniere?";

    //booleen permettant de savoir si les requête envoyée par l'API ont bien fonctionnées
    private Boolean requete = false;

    //id de la dernière promotion insérée dans la base SQLite
    private long insertId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

     /**
     * Handles a button press and calls the AndrestClient.rest() method with the
     * given parameters. Runs in the background (Async) and pops up a dialog on
     * completion.
     *
     * @author Isaac Whitfield
     * @ersion 09/03/2014
     */

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

        public doRequest(Context context, Map<String, Object> data, String method, String url) {

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

                result = rest.request(url, method, data); // Do request - Envoi de la requête (API), réception des données (JSON)

                Log.i("JSON", String.valueOf(result));

                int jsonSize = result.length();

                //Parcours de notre objet JSON (plusieurs promotions peuvent correspondre à un beacon)
                for (int i = 0; i < jsonSize; i++) {

                    JSONObject jobj = result.getJSONObject("" + i + "");

                    String imgoffPath = BitMapUtil.downloadImage(jobj.getString("imageoff"), jobj.getString("idpromo"), ART);
                    String imgartPath = BitMapUtil.downloadImage(jobj.getString("imageart"), jobj.getString("idpromo"), OFF);


                    //byte[] bImgoff = Base64.decode(jobj.getString("imageoff"), Base64.DEFAULT);
                    //byte[] bImageart = Base64.decode(jobj.getString("imageart"), Base64.DEFAULT);

                    //Enregistrement de la promotion dans la base de données SQLite
                    PromoBanniereMetier promo = new PromoBanniereMetier();

                    promo.setIdbanniere(jobj.getString("idpromo"));
                    promo.setLbPromo(jobj.getString("lbpromo"));
                    promo.setTitrePromo(jobj.getString("titrepro"));
                    promo.setTxtBanniere(jobj.getString("txtpromo"));
                    promo.setDtdebval(jobj.getString("dtdebval"));
                    promo.setDtfinval(jobj.getString("dtfinval"));
                    promo.setTypBanniere(jobj.getString("typpromo"));
                    promo.setImageart(imgoffPath);
                    promo.setImageoff(imgartPath);
                    promo.setIdBeacon(jobj.getString("idbeacon"));
                    //promo.setIdmagasin(jobj.getString("idmag"));

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

                    Intent intent = new Intent(ServicePromoBanniere.this, Notification.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("lastIdInsert", insertId); //on insère dans l'intent l'id de la dernière promotion enregistrée en bdd SQLite

                    startActivity(intent); //Activation de l'activité
                }

                //new ResponseDialog(context, "OK", e.getMessage()).showDialog();
                Log.i("JSON2", data.toString());
            }
        }
    }

    //TODO faire une méthode qui check toute les certaine heure ou 1 fois par jours, si il y a des nouvelles Promo bannieres, si la date est dépassée elle sont supprimées
}
