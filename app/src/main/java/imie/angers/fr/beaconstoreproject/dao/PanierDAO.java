package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Created by Anne on 03/03/2016.
 */
public class PanierDAO extends DAOBase {


    public PanierDAO(Context pContext) {
        super(pContext);
    }

    /**
     *
     * @param promo
     * @param time
     * @return
     */

    public long addPromoPanier(PromoBeaconMetier promo, long time) {

        long insertId;

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_PROMO, promo.getId_promo());
        values.put(DatabaseHelper.COLUMN_PROMO_IDSTRING, promo.getIdpromo());
        values.put(DatabaseHelper.COLUMN_TIME, time);

        try {

            insertId = mDb.insertOrThrow (DatabaseHelper.TABLE_PANIER, null, values);

        } catch (SQLiteConstraintException e) {

            insertId = -2;
        }

        //insertion en base + recuperation du dernier id inséré


        Log.i("daoPanier", "insertion en bdd:" + insertId);

        return insertId;
    }

    public void deleteTablePromoPanier() {

        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PANIER);
    }

    public List<PromoBeaconMetier> getPromoPanier() {

        String query = "SELECT "
                + " p." + DatabaseHelper.COLUMN_PROMO + ", "
                + " b." + DatabaseHelper.COLUMN_TITREPRO + ", "
                + " b." + DatabaseHelper.COLUMN_IMAGEART + ", "
                + " b." + DatabaseHelper.COLUMN_IMAGEOFF +
                " FROM " + DatabaseHelper.TABLE_PANIER + " p JOIN " + DatabaseHelper.TABLE_PROMOBEACON + " b ON p." + DatabaseHelper.COLUMN_PROMO + " = b." +
                DatabaseHelper.COLUMN_IDP;

        Cursor cursor = mDb.rawQuery(query, null);

        List<PromoBeaconMetier> listPromoPanier = new ArrayList<>();

        while (cursor.moveToNext()) {

            //création d'une nouvelle promoBeacon
            PromoBeaconMetier promo = new PromoBeaconMetier();

            promo.setId_promo(cursor.getInt(0));
            promo.setTitrePromo(cursor.getString(1));
            promo.setImageart((cursor.getString(2)));
            promo.setImageoff(cursor.getString(3));

            listPromoPanier.add(promo);
        }

        // fermeture du cursor
        cursor.close();

        return listPromoPanier;
    }

    public List<ArrayList> getPromoPanierForAPI() {

        String query = "SELECT "
                + DatabaseHelper.COLUMN_PROMO_IDSTRING + ", "
                + DatabaseHelper.COLUMN_TIME
                + "FROM " + DatabaseHelper.TABLE_PANIER;

        Cursor cursor = mDb.rawQuery(query, null);

        List<ArrayList> listPromoPanier = new ArrayList<>();

        while (cursor.moveToNext()) {

            ArrayList promoInfos = new ArrayList<>();

            promoInfos.add(cursor.getString(0));
            promoInfos.add(cursor.getLong(1));

            listPromoPanier.add(promoInfos);
        }

        // fermeture du cursor
        cursor.close();

        return listPromoPanier;
    }

    public int deletePromoPanier(long idPromo) {

        Log.i("idprom deletefrompanier", String.valueOf(idPromo));

        return mDb.delete(DatabaseHelper.TABLE_PANIER, DatabaseHelper.COLUMN_PROMO + " = ?", new String[]{String.valueOf(idPromo)});
    }
}
