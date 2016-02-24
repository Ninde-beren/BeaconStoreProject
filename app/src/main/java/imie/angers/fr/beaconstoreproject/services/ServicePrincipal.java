package imie.angers.fr.beaconstoreproject.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.activites.Notification;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.BeaconMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromotionMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;

/**
 * ServicePrincipal permet de gérer la détection des beacons, la récupération des promotions correspondants aux beacons détectés, l'enregistrement des promotions dans la base de données, l'enregistrement des données relatives à la connexion entre l'utilisateur et le beacon
 * Created by plougastel.dl03 on 11/02/2016.
 *
 * @author Anne
 */

public class ServicePrincipal extends Service implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    //private NotificationDAO notificationDAO = new NotificationDAO(this);
    private PromoBeaconDAO promoBeaconDAO = new PromoBeaconDAO(this);

    private AndrestClient rest = new AndrestClient();
    private String url = "http://beaconstore.ninde.fr/serverRest.php/notifications?";

    //booleen permettant de savoir si les requête envoyée par l'API ont bien fonctionnées
    private Boolean requete = false;

    //id de la dernière promotion insérée dans la base SQLite
    private long insertId;

    //liste des beacons rencontrés
    private static List<BeaconMetier> listBeacons;

    private BeaconMetier beaconVu = new BeaconMetier();

    //email de l'utilisateur
    private String emailConso;

    @Override
    public void onCreate() {

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setBackgroundScanPeriod(1000);
        //beaconManager.setForegroundScanPeriod(1000);
        //beaconManager.setBackgroundBetweenScanPeriod(60000l);

        listBeacons = new ArrayList<>();

        Log.i("Beac", "Hello");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // la méthode bind execute la méthode onBeaconServiceConnect() permettant de gérer les interactions avec les beacons rencontrés
        beaconManager.bind(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service", "service disconnect");
        beaconManager.unbind(this);
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();

        //stopSelf();
    }

    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                BeaconMetier beaconDejaVu = new BeaconMetier();
                Boolean beaconNonVu = true;

                if (beacons.size() > 0) {

                    for (Beacon beacon : beacons) { //parcours de la liste de beacons identifiés par le téléphone

                        Log.i("listBeacon", String.valueOf(listBeacons.size()));

                        if(listBeacons.size() == 0 ) {

                            beaconNonVu = false;

                        } else {

                            //int index = 0;

                            for(BeaconMetier beaconMetier : listBeacons) {

                                Log.i("beaconMetier", beaconMetier.getIdsBeacon());
                                Log.i("beacon", beacon.getIdentifiers().toString());

                                Log.i("date1", String.valueOf(new Date().getTime()));
                                Log.i("date2", String.valueOf(beaconMetier.getDateBeacon().getTime()));
                                Log.i("date3", String.valueOf(new Date().getTime() - beaconMetier.getDateBeacon().getTime()));


                                if(beaconMetier.getIdsBeacon().equals(beacon.getIdentifiers().toString()) && (new Date().getTime() - beaconMetier.getDateBeacon().getTime()) >= 10000) {

                                    Log.i("beaconId", beacon.getIdentifiers().toString());

                                    //index++;
                                    beaconNonVu = true;
                                    beaconDejaVu = beaconMetier;
                                    beaconMetier.setDateBeacon(new Date());

                                    Log.i("beaconDejaVu", beaconDejaVu.getIdsBeacon());
                                    Log.i("beaconDejaVuPromo", String.valueOf(beaconDejaVu.getIdPromo()));


                                    break;
                                }
                               // beaconNonVu = index == 0 ? false : true;
                            }
                        }

                        if(!beaconNonVu) {

                            beaconVu.setUuidBeacon(beacon.getId1().toString());
                            beaconVu.setMajorBeacon(beacon.getId2().toString());
                            beaconVu.setMinorBeacon(beacon.getId3().toString());
                            beaconVu.setIdsBeacon(beacon.getIdentifiers().toString());
                            beaconVu.setDateBeacon(new Date());

                            Map<String, Object> toPost = new HashMap<String, Object>();
                            toPost.put("uuid", beacon.getId1().toString());
                            toPost.put("major", beacon.getId2().toString());
                            toPost.put("minor", beacon.getId3().toString());

                            Log.i("toPost", "param du beacon : " + toPost);

                            //execution de la requête POST (cf API) en arrière plan dans un autre thread
                            new doRequest(ServicePrincipal.this, toPost, "POST", url).execute();



                        } else if(beaconNonVu == true && beaconDejaVu.getIdPromo() != 0) {

                            Log.i("idBeaconDejavu", String.valueOf(beaconDejaVu.getIdPromo()));

                            Intent intent = new Intent(ServicePrincipal.this, Notification.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("lastIdInsert", beaconDejaVu.getIdPromo()); //on insère dans l'intent l'id de la dernière promotion enregistrée en bdd SQLite

                            startActivity(intent); //Activiation de l'activité
                        }
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

        } catch (RemoteException e) {}

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                //stopSelf();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {}
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

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {

                Log.i("url", url);

                result = rest.request(url, method, data); // Do request - Envoi de la requête (API), réception des données (JSON)

                Log.i("JSON", String.valueOf(result));

                int jsonSize = result.length();

                //Parcours de notre objet JSON (plusieurs promotions peuvent correspondre à un beacon)
                for (int i = 0; i < jsonSize; i++) {

                    JSONObject jobj = result.getJSONObject(""+ i +"");

                    //Enregistrement de la promotion dans la base de données SQLite
                    PromotionMetier promo = new PromotionMetier();

                    promo.setIdpromo(jobj.getString("idpromo"));
                    promo.setLbPromo(jobj.getString("lbpromo"));
                    promo.setTitrePromo(jobj.getString("titrepro"));
                    promo.setTxtPromo(jobj.getString("txtpromo"));
                    promo.setDtdebval(jobj.getString("dtdebval"));
                    promo.setDtfinval(jobj.getString("dtfinval"));
                    promo.setTyppromo(jobj.getString("typpromo"));
                    promo.setImageart(jobj.getString("imageart"));
                    promo.setImageoff(jobj.getString("imageoff"));
                    promo.setIdBeacon(jobj.getString("idbeacon"));
                    //promo.setIdmagasin(jobj.getString("idmag"));

                    promoBeaconDAO.open();

                    insertId = promoBeaconDAO.addPromotion(promo);

                    beaconVu.setIdPromo(insertId);
                    //beaconVu.setIdMagasin(jobj.getString("idmag"));

                    listBeacons.add(beaconVu);

                    Log.i("insertId", String.valueOf(insertId));

                    requete = insertId != -1;
                }

            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return requete;
        }

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

                    Intent intent = new Intent(ServicePrincipal.this, Notification.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("lastIdInsert", insertId); //on insère dans l'intent l'id de la dernière promotion enregistrée en bdd SQLite

                    startActivity(intent); //Activiation de l'activité
                }

                //new ResponseDialog(context, "OK", e.getMessage()).showDialog();
                Log.i("JSON2", data.toString());
            }
        }
    }

    /**
     * Permet de récupérer l'email de l'utilisateur en arrière plan
     */
    private class getEmailConso extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            //return notificationDAO.getEmailConso();

            String result = "";

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            emailConso = result;
        }
    }
}