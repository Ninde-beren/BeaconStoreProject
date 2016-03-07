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

        values.put(DatabaseHelper.COLUMN_IDBANNIERE, banniere.getIdbanniere());
        values.put(DatabaseHelper.COLUMN_LBBANNIERE, banniere.getLbPromo());
        values.put(DatabaseHelper.COLUMN_TITREBAN,   banniere.getTitrePromo());
        values.put(DatabaseHelper.COLUMN_TXTBAN,     banniere.getTxtBanniere());
        values.put(DatabaseHelper.COLUMN_DTDEBVAL,   banniere.getDtdebval());
        values.put(DatabaseHelper.COLUMN_DTFINVAL,   banniere.getDtfinval());
        values.put(DatabaseHelper.COLUMN_TYPBAN,     banniere.getTypBanniere());
        values.put(DatabaseHelper.COLUMN_IMAGEBAN,   banniere.getImageban());

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PROMOBANNIERE, null, values);

        return insertId;
    }

    public List<PromoBanniereMetier> getListPromoBanniere() {

        // selectionner la liste des banniere pour les inserer dans l'adapter

        String query = "SELECT " + DatabaseHelper.COLUMN_IDB          + ", "
                                 + DatabaseHelper.COLUMN_TITREBAN     + ", "
                                 + DatabaseHelper.COLUMN_LBBANNIERE   + ", "
                                 + DatabaseHelper.COLUMN_TXTBAN       + ", "
                                 + DatabaseHelper.COLUMN_DTDEBVAL     + ", "
                                 + DatabaseHelper.COLUMN_DTFINVAL     + ", "
                                 + DatabaseHelper.COLUMN_IMAGEBAN     +
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
            banniere.setImageban(cursor.getString(6));

                listPromoBanniere.add(banniere);

            Log.i("banniereId", String.valueOf(cursor.getInt(0)));
        }

        // fermeture du cursor
        cursor.close();

        Log.i("listbanDAO", String.valueOf(listPromoBanniere.size()));

        return listPromoBanniere;
    }

    public void deleteTablePromoBanniere() {
        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PROMOBANNIERE);
        mDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='" + DatabaseHelper.TABLE_PROMOBANNIERE +"'");
    }
}