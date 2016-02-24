package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;
import imie.angers.fr.beaconstoreproject.metiers.NotificationPromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;

/**
 * Created by Ninde on 22/02/2016.
 */
public class PromoBanniereDAO extends DAOBase{

    public PromoBanniereDAO(Context pContext) {
        super(pContext);
    }

    /**
     * Ajoute une banniere beacon dans la base de données (table banniere)
     * Ajoute une nouvelle notification dans la table notification
     * @param banniere
     */

    public long addbanniere(PromoBanniereMetier banniere) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_IDBANNIERE, banniere.getId_Banniere());
        values.put(DatabaseHelper.COLUMN_TITREBAN, banniere.getTitrePromo());
        values.put(DatabaseHelper.COLUMN_LBBANNIERE, banniere.getLbPromo());
        values.put(DatabaseHelper.COLUMN_TXTBAN, banniere.getTxtBanniere());
        values.put(DatabaseHelper.COLUMN_DTDEBVAL, banniere.getDtdebval());
        values.put(DatabaseHelper.COLUMN_DTFINVAL, banniere.getDtfinval());
        values.put(DatabaseHelper.COLUMN_TYPBAN, banniere.getTypBanniere());
        values.put(DatabaseHelper.COLUMN_IMAGEOFF, banniere.getImageoff());
        values.put(DatabaseHelper.COLUMN_IMAGEART, banniere.getImageart());

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PROMOBANNIERE, null, values);

        Log.i("dao", "insertion en bdd:" + insertId);

        return insertId;
    }

    public NotificationPromoBanniereMetier getLastbanniereInserted(long id) {

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB + ", "
                + DatabaseHelper.COLUMN_TITREBAN + ", "
                + DatabaseHelper.COLUMN_LBBANNIERE + ", "
                + DatabaseHelper.COLUMN_IMAGEOFF + ", "
                + " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE +
                " WHERE " + DatabaseHelper.COLUMN_IDB + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        //Instanciation d'une nouvelle notification
        NotificationPromoBanniereMetier notif = new NotificationPromoBanniereMetier();

        notif.setId(cursor.getInt(0));
        notif.setTitreBanniere(cursor.getString(1));
        notif.setLbBanniere(cursor.getString(2));
        notif.setImageoff(cursor.getString(3));

        //fermeture du cusor
        cursor.close();

        return notif;
    }

    public void deleteTableBanniereBeacon() {

        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PROMOBANNIERE);
        mDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+DatabaseHelper.TABLE_PROMOBANNIERE+"'");
    }
}
