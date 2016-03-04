package imie.angers.fr.beaconstoreproject.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.activites.Notification;
import imie.angers.fr.beaconstoreproject.activites.NotificationBanniere;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.exceptions.RESTException;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

/**
 * * ServicePromoBanniere permet la récupération des promotions bannières avec un chek toute les 24h, l'enregistrement des promotions dans la base de données
 * Created by Ninde on 24/02/2016.
 */
public class ServicePromoBanniere extends Service {

    private PromoBanniereDAO promoBanniereDAO;
    private AndrestClient rest;
    private final static String URL = "http://beaconstore.ninde.fr/serverRest.php/promobaniere?";

    private Boolean requete;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("ServicePromoBanniere1", "LA AUSSI");

        promoBanniereDAO = new PromoBanniereDAO(this);
        promoBanniereDAO.open();

        rest = new AndrestClient();

        requete = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("ServicePromoBanniere2", "dans le on start command");

        new DoRequest(ServicePromoBanniere.this, "GET", URL) {

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
                            long lastInsertId = promoBanniereDAO.addPromoBanniere(promo);
                        }
                        requete =  true;

                    } catch (RESTException | JSONException e1) {

                        e1.printStackTrace();

                        requete = false;

                    }

                    return requete;
                }

                @Override
                protected void onPostExecute(Boolean data) {
                    super.onPostExecute(data);

                    if(data) {

                        NotificationBanniere notifBanniere = new NotificationBanniere();
                        notifBanniere.sendNotification(ServicePromoBanniere.this);
                    }
                }

            }.execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
