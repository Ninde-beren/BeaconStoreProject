package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.activites.PromoBanniere;
import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;
import imie.angers.fr.beaconstoreproject.metiers.NotificationMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Permet d'effectuer les opérations CRUD pour l'objet PromoBanniereMetier
 * Hérite de la classe DAOBase
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

    public long addPromoBanniere(PromoBanniereMetier banniere) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_IDBANNIERE,banniere.getId_Banniere());
        values.put(DatabaseHelper.COLUMN_TITREBAN,  banniere.getTitrePromo());
        values.put(DatabaseHelper.COLUMN_LBBANNIERE,banniere.getLbPromo());
        values.put(DatabaseHelper.COLUMN_TXTBAN,    banniere.getTxtBanniere());
        values.put(DatabaseHelper.COLUMN_TYPBAN,    banniere.getTypBanniere());
        values.put(DatabaseHelper.COLUMN_IMAGEOFF,  banniere.getImageoff());
        values.put(DatabaseHelper.COLUMN_IMAGEART,  banniere.getImageart());

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PROMOBANNIERE, null, values);

        Log.i("dao", "insertion en bdd:" + insertId);

        return insertId;
    }

    public List<PromoBanniereMetier> getListPromoBanniere() {

        //TODO selectionner la liste des banniere pour les inserer dans l'adapter

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB            + ", "
                                 + DatabaseHelper.COLUMN_TITREBAN       + ", "
                                 + DatabaseHelper.COLUMN_LBBANNIERE     + ", "
                                 + DatabaseHelper.COLUMN_B_IMAGEOFF     + ", "
                      + " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE   + " = ?";

        Cursor cursor = mDb.rawQuery(query, null);

        cursor.moveToFirst();

        List<PromoBanniereMetier> listPromoBanniere = new ArrayList<>();

        while (cursor.moveToNext()) {

            //création d'une nouvelle banniere
            PromoBanniereMetier banniere = new PromoBanniereMetier();

            banniere.setId(cursor.getInt(0));
            banniere.setTitrePromo(cursor.getString(1));
            banniere.setLbPromo(cursor.getString(2));
            banniere.setImageoff(cursor.getString(3));

            listPromoBanniere.add(banniere);
        }

        // fermeture du cursor
        cursor.close();

        return listPromoBanniere;
    }

    public Object getPromoBanniere(long id) {

        //TODO selectionner la banniere pour l'activity PromoBanniere

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB            + ", "
                                 + DatabaseHelper.COLUMN_B_DTDEBVAL     + ", "
                                 + DatabaseHelper.COLUMN_B_DTFINVAL     + ", "
                                 + DatabaseHelper.COLUMN_TITREBAN       + ", "
                                 + DatabaseHelper.COLUMN_TXTBAN         + ", "
                                 + DatabaseHelper.COLUMN_IMAGEART       + ", "
                      + " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE   +
                       " WHERE " + DatabaseHelper.COLUMN_IDB            + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        //création d'une nouvelle banniere
        PromoBanniereMetier promoBanniere = new PromoBanniereMetier();

        promoBanniere.setId(cursor.getInt(0));
        promoBanniere.setDtdebval(cursor.getString(1));
        promoBanniere.setDtfinval(cursor.getString(2));
        promoBanniere.setTitrePromo(cursor.getString(3));
        promoBanniere.setTxtBanniere(cursor.getString(4));
        promoBanniere.setImageart(cursor.getString(5));

        //fermeture du cursor
        cursor.close();

        return promoBanniere;
    }

    public NotificationMetier getLastPromoBanniereInserted(long id) {

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB + ", "
                + DatabaseHelper.COLUMN_TITREBAN + ", "
                + DatabaseHelper.COLUMN_LBBANNIERE + ", "
                + DatabaseHelper.COLUMN_IMAGEOFF + ", "
                + " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE +
                " WHERE " + DatabaseHelper.COLUMN_IDB + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        //Instanciation d'une nouvelle notification
        NotificationMetier notif = new NotificationMetier();

        notif.setId(cursor.getInt(0));
        notif.setTitrePromo(cursor.getString(1));
        notif.setLbPromo(cursor.getString(2));
        notif.setImageoff(cursor.getString(3));

        //fermeture du cusor
        cursor.close();

        return notif;
    }

    public void deleteTablePromoBanniere() {

        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PROMOBANNIERE);
        mDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+DatabaseHelper.TABLE_PROMOBANNIERE+"'");
    }
}