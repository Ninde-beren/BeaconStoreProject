package imie.angers.fr.beaconstoreproject.activites;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import imie.angers.fr.beaconstoreproject.services.ServicePrincipal;

/**
 * Permet de lancer le ServicePrincipal en background si détection de beacons
 * Created by plougastel.dl03 on 11/02/2016.
 */
public class BeaconDetection extends Application implements BootstrapNotifier {
    private static final String TAG = ".MyApplicationName";
    private RegionBootstrap regionBootstrap;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "App started up");
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // wake up the service when any beacon is seen (you can specify specific id filers in the parameters below)
        Region region = new Region("com.example.myapp.boostrapRegion", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

/**************************************************************************************************
*
**************************************************************************************************/

    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {
        // Don't care
    }

/*************************************************************************************************
* QUAND ON RENTRE DANS LA REGION D UN BEACON
*************************************************************************************************/

    @Override
    public void didEnterRegion(Region arg0) {
        Log.i(TAG, "Got a didEnterRegion call");
        // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
        // if you want the Activity to launch every single time beacons come into view, remove this call.
        //regionBootstrap.disable();
        Intent intent = new Intent(this, ServicePrincipal.class);
        // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
        // created when a user launches the activity manually and it gets launched from here.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(intent);
    }

/*************************************************************************************************
* QUAND ON SORT DE LA REGION D UN BEACON
*************************************************************************************************/

    @Override
    public void didExitRegion(Region arg0) {
        // Don't care
    }
}

