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
import imie.angers.fr.beaconstoreproject.utils.DbUtil;

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

        values.put(DatabaseHelper.COLUMN_IDBANNIERE, DbUtil.sant(banniere.getIdbanniere()));
        values.put(DatabaseHelper.COLUMN_LBBANNIERE, DbUtil.sant(banniere.getLbPromo()));
        values.put(DatabaseHelper.COLUMN_TITREBAN,   DbUtil.sant(banniere.getTitrePromo()));
        values.put(DatabaseHelper.COLUMN_TXTBAN,     DbUtil.sant(banniere.getTxtBanniere()));
        values.put(DatabaseHelper.COLUMN_B_DTDEBVAL, banniere.getDtdebval());
        values.put(DatabaseHelper.COLUMN_B_DTFINVAL, banniere.getDtfinval());
        values.put(DatabaseHelper.COLUMN_TYPBAN,     DbUtil.sant(banniere.getTypBanniere()));
        values.put(DatabaseHelper.COLUMN_B_IMAGEOFF, DbUtil.sant(banniere.getImageoff()));
        values.put(DatabaseHelper.COLUMN_B_IMAGEART, DbUtil.sant(banniere.getImageart()));

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PROMOBANNIERE, null, values);

        return insertId;
    }

    public List<PromoBanniereMetier> getListPromoBanniere() {

        // selectionner la liste des banniere pour les inserer dans l'adapter

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB            + ", "
                                 + DatabaseHelper.COLUMN_TITREBAN       + ", "
                                 + DatabaseHelper.COLUMN_LBBANNIERE     + ", "
                                 + DatabaseHelper.COLUMN_TXTBAN       + ", "
                                 + DatabaseHelper.COLUMN_B_DTDEBVAL     + ", "
                                 + DatabaseHelper.COLUMN_B_DTFINVAL     + ", "
                                 + DatabaseHelper.COLUMN_B_IMAGEART     + ", "
                                 + DatabaseHelper.COLUMN_B_IMAGEOFF     +
                        " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE ;

        Cursor cursor = mDb.rawQuery(query, null);

        List<PromoBanniereMetier> listPromoBanniere = new ArrayList<>();

        while (cursor.moveToNext()) {

            //création d'une nouvelle banniere
            PromoBanniereMetier banniere = new PromoBanniereMetier();

            banniere.setId_Banniere(cursor.getInt(0));
            banniere.setTitrePromo(cursor.getString(1));
            banniere.setLbPromo(cursor.getString(2));
            banniere.setTxtBanniere(cursor.getString(3));
            banniere.setDtdebval(cursor.getString(4));
            banniere.setDtfinval(cursor.getString(5));
            banniere.setImageart(cursor.getString(6));
            banniere.setImageoff(cursor.getString(7));

            listPromoBanniere.add(banniere);

            Log.i("banniereId", String.valueOf(cursor.getInt(0)));
        }

        // fermeture du cursor
        cursor.close();

        Log.i("listbanDAO", String.valueOf(listPromoBanniere.size()));

        return listPromoBanniere;
    }

    public Object getPromoBanniere(long id) {

        // selectionner la banniere pour l'activity PromoBanniere

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB            + ", "
                                 + DatabaseHelper.COLUMN_B_DTDEBVAL     + ", "
                                 + DatabaseHelper.COLUMN_B_DTFINVAL     + ", "
                                 + DatabaseHelper.COLUMN_TITREBAN       + ", "
                                 + DatabaseHelper.COLUMN_TXTBAN         + ", "
                                 + DatabaseHelper.COLUMN_IMAGEART       +
                        " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE   +
                       " WHERE " + DatabaseHelper.COLUMN_IDB            + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        //création d'une nouvelle banniere
        PromoBanniereMetier promoBanniere = new PromoBanniereMetier();

        promoBanniere.setId_Banniere(cursor.getInt(0));
        promoBanniere.setDtdebval(cursor.getString(1));
        promoBanniere.setDtfinval(cursor.getString(2));
        promoBanniere.setTitrePromo(cursor.getString(3));
        promoBanniere.setTxtBanniere(cursor.getString(4));
        promoBanniere.setImageart(cursor.getString(5));

        //fermeture du cursor
        cursor.close();

        return promoBanniere;
    }

    public PromoBanniereMetier getLastPromoBanniereInserted(long id) {

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB        + ", "
                                 + DatabaseHelper.COLUMN_TITREBAN   + ", "
                                 + DatabaseHelper.COLUMN_LBBANNIERE + ", "
                                 + DatabaseHelper.COLUMN_IMAGEOFF   + ", "
                                 + DatabaseHelper.COLUMN_TXTBAN     + ", "
                                 + DatabaseHelper.COLUMN_IMAGEART   +
                        " FROM " + DatabaseHelper.TABLE_PROMOBANNIERE +
                       " WHERE " + DatabaseHelper.COLUMN_IDB        + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        //Instanciation d'une nouvelle notification
        PromoBanniereMetier promo = new PromoBanniereMetier();

        promo.setId_Banniere(cursor.getInt(0));
        promo.setTitrePromo(cursor.getString(1));
        promo.setLbPromo(cursor.getString(2));
        promo.setImageoff(cursor.getString(3));
        promo.setTxtBanniere(cursor.getString(4));
        promo.setImageart((cursor.getString(5)));

        //fermeture du cusor
        cursor.close();

        return promo;
    }

    public void deletePromoBanniere( long id) {

        String query ="DELETE FROM " + DatabaseHelper.TABLE_PROMOBANNIERE + "WHERE " + DatabaseHelper.COLUMN_IDBANNIERE + "=?";
        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.close();
    }

    public void deleteTablePromoBanniere() {
        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PROMOBANNIERE);
        mDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='" + DatabaseHelper.TABLE_PROMOBANNIERE +"'");
    }
}