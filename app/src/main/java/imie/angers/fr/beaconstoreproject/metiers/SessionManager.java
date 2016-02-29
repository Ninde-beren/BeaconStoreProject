package imie.angers.fr.beaconstoreproject.metiers;

import android.content.Context;
import android.content.SharedPreferences;

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
     * Create login session
     * */
    public void createLoginSession(int id, String nom, String prenom){
        // Storing login value as TRUE
        editor.putBoolean(CONSO_INFOS, true);

        // Storing name in pref
        editor.putString(CONSO_PRENOM, nom);

        // Storing email in pref
        editor.putString(CONSO_NOM, prenom);

        editor.putInt(CONSO_ID, id);

        // commit changes
        editor.commit();
    }
}
