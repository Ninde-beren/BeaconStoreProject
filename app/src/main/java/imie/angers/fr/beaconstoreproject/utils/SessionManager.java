package imie.angers.fr.beaconstoreproject.utils;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.Beacon;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import imie.angers.fr.beaconstoreproject.metiers.BeaconMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Created by Anne on 29/02/2016.
 */
public class SessionManager {

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    Context _context;

    //Gson object to serialize ArrayList into json
    private Gson gson;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ConsoInfos";

    // User name (make variable public to access from outside)
    private static final String CONSO_ID = "consoId";

    private static final String CONSO_ID_API = "consoId_api";

    // Email address (make variable public to access from outside)
    private static final String CONSO_NOM = "consoNom";

    private static final String CONSO_PRENOM = "consoPrenom";

    private static final String BEACON_SET = "beaconSet";

    private static final String ID_MAGASIN = "magasin";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        gson = new Gson();
        editor = pref.edit();
    }

    //----------------------------------------------------------------------------//

    /**
     * Crée une session beacon
     * @param beacon
     */
    public void setBeaconSession(List<BeaconMetier> beacon) {

        Log.i("json session", String.valueOf(beacon));

        String beaconJson = gson.toJson(beacon);
        editor.putString(BEACON_SET, beaconJson);
        editor.commit();

        Log.i("jsonbeaconsession", beaconJson);
    }

    /**
     * Crée une session consommateur
     * @param id
     * @param nom
     * @param prenom
     */
    public void createConsoSession(long id, long id_api, String nom, String prenom){

        // Storing name in pref
        editor.putString(CONSO_PRENOM, nom);

        // Storing email in pref
        editor.putString(CONSO_NOM, prenom);

        editor.putLong(CONSO_ID, id);

        editor.putLong(CONSO_ID_API, id_api);

        // commit changes
        editor.commit();
    }

    /**
     * Crée une session magasin pour stocker l'identifiant du magasin
     * @param idmag
     */

    public void createMagSession(String idmag){

        editor.putString(ID_MAGASIN, idmag);

        // commit changes
        editor.commit();
    }

    //----------------------------------------------------------------------------//

    public long getIdC(){return pref.getLong(CONSO_ID, -1);}

    //----------------------------------------------------------------------------//

    public long getIdC_API(){return pref.getLong(CONSO_ID_API, -2);}

    //----------------------------------------------------------------------------//

    public String getNomC(){return pref.getString(CONSO_NOM, null);}

    //----------------------------------------------------------------------------//

    public String getPrenomC(){return pref.getString(CONSO_PRENOM, null);}

    //----------------------------------------------------------------------------//

    public String getIdMagasin(){return pref.getString(ID_MAGASIN, null);}

    /**
     * Permet de récupérer la liste des beacons rencontrés
     * @return
     */
    public List<BeaconMetier> getBeaconsMeet() {

        List<BeaconMetier> beaconList = new ArrayList<>();

        String beaconListJson = pref.getString(BEACON_SET, null);

        JsonParser parser = new JsonParser();

        if(beaconListJson != null) {

            JsonArray arrayJ = parser.parse(beaconListJson).getAsJsonArray();

            for (JsonElement jsonElement : arrayJ) {

                JsonObject array = jsonElement.getAsJsonObject();

                long dateBeacon = array.get("dateBeacon").getAsLong();
                int idBeacon = array.get("idBeacon").getAsInt();
                long idPromo = array.get("idPromo").getAsLong();
                String idsBeacon = array.get("idsBeacon").getAsString();
                String majorBeacon = array.get("majorBeacon").getAsString();
                String minorBeacon = array.get("minorBeacon").getAsString();
                String uuidBeacon = array.get("uuidBeacon").getAsString();

                BeaconMetier beaconMetier = new BeaconMetier();

                beaconMetier.setDateBeacon(dateBeacon);
                beaconMetier.setMinorBeacon(minorBeacon);
                beaconMetier.setMajorBeacon(majorBeacon);
                beaconMetier.setIdBeacon(idBeacon);
                beaconMetier.setIdsBeacon(idsBeacon);
                beaconMetier.setIdPromo(idPromo);
                beaconMetier.setUuidBeacon(uuidBeacon);

                beaconList.add(beaconMetier);
            }
        }

        Log.i("beaconListFromJson", String.valueOf(beaconList));
        return beaconList;
    }


    /**
     * Vide la session consommateur
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.remove(CONSO_ID);
        editor.remove(CONSO_PRENOM);
        editor.remove(CONSO_NOM);
        editor.commit();
    }

    /**
     * vide la session beacon
     */
    public void beaconClear(){

        editor.remove(BEACON_SET);
        editor.commit();
    }
}
