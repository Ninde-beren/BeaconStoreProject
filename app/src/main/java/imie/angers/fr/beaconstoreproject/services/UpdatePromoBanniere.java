package imie.angers.fr.beaconstoreproject.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import imie.angers.fr.beaconstoreproject.activites.NotificationBanniere;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.exceptions.RESTException;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;

/**
 * Created by Anne on 07/03/2016.
 */
public class UpdatePromoBanniere {

    private PromoBanniereDAO promoBanniereDAO;
    private AndrestClient rest;
    private final static String URL = "http://beaconstore.ninde.fr/serverRest.php/promobanniere?";

    private Boolean requete;

    public UpdatePromoBanniere(Context context){


        Log.i("ServicePromoBanniere1", "LA AUSSI");

        promoBanniereDAO = new PromoBanniereDAO(context);
        promoBanniereDAO.open();

        rest = new AndrestClient();

        requete = false;

        Log.i("ServicePromoBanniere2", "dans le on startCommand");

        new DoRequest(context, "GET", URL) {

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
                        promo.setImageban(jobj.getString("imageban"));

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
                    notifBanniere.sendNotification(context);
                }
            }

        }.execute();
    }
}