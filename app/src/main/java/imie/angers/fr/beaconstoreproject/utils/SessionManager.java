package imie.angers.fr.beaconstoreproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Anne on 29/02/2016.
 */
public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ConsoInfos";

    // All Shared Preferences Keys
    private static final String CONSO_INFOS = "conso_infos";

    // User name (make variable public to access from outside)
    public static final String CONSO_ID = "consoId";

    // Email address (make variable public to access from outside)
    public static final String CONSO_NOM = "consoNom";
    public static final String CONSO_PRENOM = "consoPrenom";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Crée une session consommateur
     * */
    public void createConsoSession(long id, String nom, String prenom){
        // Storing login value as TRUE
        editor.putBoolean(CONSO_INFOS, true);

        // Storing name in pref
        editor.putString(CONSO_PRENOM, nom);

        // Storing email in pref
        editor.putString(CONSO_NOM, prenom);

        editor.putLong(CONSO_ID, id);

        // commit changes
        editor.commit();
    }

    /**
     * Get conso session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(CONSO_NOM, pref.getString(CONSO_NOM, null));

        // user email id
        user.put(CONSO_PRENOM, pref.getString(CONSO_PRENOM, null));
        user.put(CONSO_ID, String.valueOf(pref.getLong(CONSO_ID, 0)));

        // return user
        return user;
    }

    public long getIdC(){return pref.getLong(CONSO_ID, 0);}

    public String getNomC(){return pref.getString(CONSO_NOM, null);}

    public String getPrenomC(){return pref.getString(CONSO_PRENOM, null);}

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
}
