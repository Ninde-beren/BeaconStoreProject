package imie.angers.fr.beaconstoreproject.metiers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Création de la bdd
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Nom et version de notre base de données
    private static final String DATABASE_NAME = "beacondatabase.db";
    private static final int DATABASE_VERSION = 1;

    //table promotion bannières
    public static final String TABLE_PROMOBANNIERE       = "banniere";
    public static final String COLUMN_IDB           = "id_b";
    public static final String COLUMN_IDBANNIERE    = "idbanniere";
    public static final String COLUMN_LBBANNIERE    = "lbbanniere";
    public static final String COLUMN_TITREBAN      = "titreban";
    public static final String COLUMN_TXTBAN        = "txtban";
    public static final String COLUMN_B_DTDEBVAL    = "dtdebval";
    public static final String COLUMN_B_DTFINVAL    = "dtfinval";
    public static final String COLUMN_TYPBAN        = "typeban";
    public static final String COLUMN_B_IMAGEOFF    = "imageoff";
    public static final String COLUMN_B_IMAGEART    = "imageart";

    //table promotion beacon
    public static final String TABLE_PROMOBEACON = "promoBeacon";
    public static final String COLUMN_IDP = "id_p";
    public static final String COLUMN_IDPROMO = "idpromo";
    public static final String COLUMN_LBPROMO = "lbpromo";
    public static final String COLUMN_TITREPRO = "titrepro";
    public static final String COLUMN_TXTPROMO = "txtpromo";
    public static final String COLUMN_DTDEBVAL = "dtdebval";
    public static final String COLUMN_DTFINVAL = "dtfinval";
    public static final String COLUMN_TYPPROMO = "typepromo";
    public static final String COLUMN_IMAGEOFF = "imageoff";
    public static final String COLUMN_IMAGEART = "imageart";
    public static final String COLUMN_BEACON = "id_beacon";
    //public static final String COLUMN_MAGASIN = "id_magasin";
    public static final String COLUMN_DATEP = "dateAjoutPromo";

    //table consommateur
    public static final String TABLE_CONSO = "consommateur";
    public static final String COLUMN_IDC = "id_c";
    public static final String COLUMN_IDCONSO = "idconso";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_PRENOM = "prenom";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_TEL = "tel";
    public static final String COLUMN_DTNAISS = "dtnaiss";
    public static final String COLUMN_CP = "cdpostal";
    public static final String COLUMN_CSP = "catsocpf";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MDP = "password";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_DATEC = "dateAjoutConso";



    //constante représentant la création de la table promobanniere
    public static final String TABLE_PROMOBANNIERE_CREATE = "CREATE TABLE " + TABLE_PROMOBANNIERE + " (" + COLUMN_IDB + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_IDBANNIERE + " VARCHAR(255),"+ COLUMN_LBBANNIERE + " VARCHAR(255)," + COLUMN_TITREBAN + " VARCHAR(255)," + COLUMN_TXTBAN + " VARCHAR(255)," + COLUMN_B_DTDEBVAL + " VARCHAR(255)," + COLUMN_B_DTFINVAL + " VARCHAR(255)," + COLUMN_TYPBAN + " VARCHAR(255)," + COLUMN_B_IMAGEOFF + " VARCHAR(255)," + COLUMN_B_IMAGEART + " VARCHAR(255)," + ");";

    //constante représentant la création de la table promobeacon
    public static final String TABLE_PROMOBEACON_CREATE = "CREATE TABLE " + TABLE_PROMOBEACON + " (" + COLUMN_IDP + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_IDPROMO + " VARCHAR(255),"+ COLUMN_LBPROMO + " VARCHAR(255)," + COLUMN_TITREPRO + " VARCHAR(255)," + COLUMN_TXTPROMO + " VARCHAR(255)," + COLUMN_DTDEBVAL + " VARCHAR(255)," + COLUMN_DTFINVAL + " VARCHAR(255)," + COLUMN_TYPPROMO + " VARCHAR(255)," + COLUMN_IMAGEOFF + " VARCHAR(255)," + COLUMN_IMAGEART + " VARCHAR(255)," + COLUMN_BEACON + " VARCHAR(255)," + COLUMN_DATEP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";

    //constante représentant la création de la table consommateur
    public static final String TABLE_CONSO_CREATE = "CREATE TABLE " + TABLE_CONSO + " (" + COLUMN_IDC + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_IDCONSO + " INTEGER, " + COLUMN_NOM + " VARCHAR(255),"+ COLUMN_PRENOM + " VARCHAR(255)," + COLUMN_GENRE + " VARCHAR(255)," + COLUMN_TEL + " VARCHAR(255)," + COLUMN_DTNAISS + " VARCHAR(255)," + COLUMN_CP + " VARCHAR(255)," + COLUMN_CSP  + " VARCHAR(255),"  + COLUMN_EMAIL + " VARCHAR(255)," + COLUMN_MDP + " VARCHAR(255)," + COLUMN_TOKEN + " VARCHAR(255)," + COLUMN_DATEC + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";


	// Création de la bdd à partir du Context, du Nom de la table et du numéro de version
	public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Création des tables
	@SuppressLint("LongLogTag")
    @Override
	public void onCreate(SQLiteDatabase db) {
        //db.execSQL(TABLE_NOTIFICATION_CREATE);
        db.execSQL(TABLE_PROMOBEACON_CREATE);
        db.execSQL(TABLE_CONSO_CREATE);

        Log.i("SQLite DB : Constructeur ", "Constructeur");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOBEACON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSO);
    }
}


