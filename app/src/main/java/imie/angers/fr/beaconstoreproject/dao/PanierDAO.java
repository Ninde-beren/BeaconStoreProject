package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
     * @param idPromoBeacon
     * @param time
     * @return
     */

    public long addPromoPanier(long idPromoBeacon, long time) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_PROMO, idPromoBeacon);
        values.put(DatabaseHelper.COLUMN_TIME, time);

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PANIER, null, values);

        Log.i("daoPanier", "insertion en bdd:" + insertId);

        return insertId;
    }

    public void deleteTablePromoPanier() {

        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PANIER);
    }

    public List<PromoBeaconMetier> getPromoPanier() {

        String query = "SELECT "
                + " p." + DatabaseHelper.COLUMN_PROMO + ", "
                + " b." +DatabaseHelper.COLUMN_TITREPRO + ", "
                + " b." +DatabaseHelper.COLUMN_IMAGEART + ", "
                + " b." +DatabaseHelper.COLUMN_IMAGEOFF +
                " FROM " + DatabaseHelper.TABLE_PANIER + " p JOIN " + DatabaseHelper.TABLE_PROMOBEACON + " b ON p." + DatabaseHelper.COLUMN_IDPA + " = b." +
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

    public int deletePromoPanier(long idPromo) {

        return mDb.delete(DatabaseHelper.TABLE_PANIER, DatabaseHelper.COLUMN_PROMO + " = ?", new String[]{String.valueOf(idPromo)});
    }
}
