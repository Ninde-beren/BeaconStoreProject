package imie.angers.fr.beaconstoreproject.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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

import imie.angers.fr.beaconstoreproject.activites.Avis;
import imie.angers.fr.beaconstoreproject.activites.Notification;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.BeaconMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * ServicePrincipal permet de gérer la détection des beacons, la récupération des promotions correspondants aux beacons détectés, l'enregistrement des promotions dans la base de données, l'enregistrement des données relatives à la connexion entre l'utilisateur et le beacon
 * Created by plougastel.dl03 on 11/02/2016.
 *
 * @author Anne
 */

public class ServicePrincipal extends Service implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    protected static final String ART = "art";
    protected static final String OFF = "off";


    private BeaconManager beaconManager;
    //private NotificationDAO notificationDAO = new NotificationDAO(this);
    private PromoBeaconDAO promoBeaconDAO;

    private AndrestClient rest;
    private String url = "http://beaconstore.ninde.fr/serverRest.php/notifications?";

    //booleen permettant de savoir si les requête envoyée par l'API ont bien fonctionnées
    private Boolean requete = false;

    //id de la dernière promotion insérée dans la base SQLite
    private long insertId;

    //liste des beacons rencontrés
    private static List<BeaconMetier> listBeacons;

    private BeaconMetier beaconVu = new BeaconMetier();

    @Override
    public void onCreate() {

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setBackgroundScanPeriod(1000);
        //beaconManager.setForegroundScanPeriod(1000);
        //beaconManager.setBackgroundBetweenScanPeriod(60000l);

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();

        rest = new AndrestClient();

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
        //promoBeaconDAO.deleteTablePromoBeacon();

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

                            for(BeaconMetier beaconMetier : listBeacons) {

                                if(beaconMetier.getIdsBeacon().equals(beacon.getIdentifiers().toString()) && (new Date().getTime() - beaconMetier.getDateBeacon().getTime()) >= 10000) {

                                    beaconNonVu = true;
                                    beaconDejaVu = beaconMetier;
                                    beaconMetier.setDateBeacon(new Date());

                                    Log.i("beaconDejaVu", beaconDejaVu.getIdsBeacon());
                                    Log.i("beaconDejaVuPromo", String.valueOf(beaconDejaVu.getIdPromo()));

                                    break;
                                }
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



                        } else if(beaconNonVu && beaconDejaVu.getIdPromo() != 0) {

                            Notification notif = new Notification(beaconDejaVu.getIdPromo());
                            notif.sendNotification(ServicePrincipal.this);
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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(ServicePrincipal.this, Avis.class);
                        i.putExtra("magId", listBeacons.get(0).getIdMagasin());
                        startActivity(i);
                    }
                }, 900000);;
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

                int jsonSize = result.length();

                //Parcours de notre objet JSON (plusieurs promotions peuvent correspondre à un beacon)
                for (int i = 0; i < jsonSize; i++) {

                    JSONObject jobj = result.getJSONObject("" + i + "");

                    //String imgoffPath = BitMapUtil.downloadImage(jobj.getString("imageoff"), jobj.getString("idpromo"), ART);
                    //String imgartPath = BitMapUtil.downloadImage(jobj.getString("imageart"), jobj.getString("idpromo"), OFF);



                    //byte[] bImgoff = Base64.decode(jobj.getString("imageoff"), Base64.DEFAULT);
                    //byte[] bImageart = Base64.decode(jobj.getString("imageart"), Base64.DEFAULT);

                    //Enregistrement de la promotion dans la base de données SQLite
                    PromoBeaconMetier promo = new PromoBeaconMetier();

                    promo.setIdpromo(jobj.getString("idpromo"));
                    promo.setLbPromo(jobj.getString("lbpromo"));
                    promo.setTitrePromo(jobj.getString("titrepro"));
                    promo.setTxtPromo(jobj.getString("txtpromo"));
                    promo.setTyppromo(jobj.getString("typpromo"));
                    promo.setImageart(jobj.getString("imageoff"));
                    promo.setImageoff(jobj.getString("imageart"));
                    promo.setIdBeacon(jobj.getString("idbeacon"));
                    //promo.setIdmagasin(jobj.getString("idmag"));

                    insertId = promoBeaconDAO.addPromotion(promo);

                    beaconVu.setIdPromo(insertId);
                    //beaconVu.setIdMagasin(jobj.getString("idmag"));

                    listBeacons.add(beaconVu);

                    Log.i("insertId", String.valueOf(insertId));
                    Log.i("insertion", "OK");

                    requete = true;
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

                        Notification notif = new Notification(insertId);
                        notif.sendNotification(ServicePrincipal.this);
                }
            }
        }
    }
}