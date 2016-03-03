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
 * Permet d'effectuer les opérations CRUD pour l'objet PromoBeaconMetier
 * Hérite de la classe DAOBase
 * Created by Anne on 17/02/2016.
 */
public class PromoBeaconDAO extends DAOBase {

    public PromoBeaconDAO(Context pContext) {
        super(pContext);
    }

    /**
     * Ajoute une promotion beacon dans la base de données (table promotion)
     * Ajoute une nouvelle notification dans la table notification
     * @param promotion
     */

    public long addPromotion(PromoBeaconMetier promotion) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_IDPROMO, promotion.getId_promo());
        values.put(DatabaseHelper.COLUMN_LBPROMO, promotion.getLbPromo());
        values.put(DatabaseHelper.COLUMN_TITREPRO, promotion.getTitrePromo());
        values.put(DatabaseHelper.COLUMN_TXTPROMO, promotion.getTxtPromo());
        values.put(DatabaseHelper.COLUMN_TYPPROMO, promotion.getTyppromo());
        values.put(DatabaseHelper.COLUMN_IMAGEART, promotion.getImageart());
        values.put(DatabaseHelper.COLUMN_IMAGEOFF, promotion.getImageoff());
        values.put(DatabaseHelper.COLUMN_BEACON, promotion.getIdBeacon());
        //values.put(DatabaseHelper.COLUMN_MAGASIN, promotion.getIdmagasin());

        values.put(DatabaseHelper.COLUMN_DATEP, "");

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PROMOBEACON, null, values);

        Log.i("daoBeacon", "insertion en bdd:" + insertId);

        return insertId;
    }

    public PromoBeaconMetier getLastPromotionInserted(long id) {

        String query = "SELECT "
                + DatabaseHelper.COLUMN_IDP + ", "
                + DatabaseHelper.COLUMN_TITREPRO + ", "
                + DatabaseHelper.COLUMN_LBPROMO + ", "
                + DatabaseHelper.COLUMN_IMAGEOFF + ", "
                + DatabaseHelper.COLUMN_TXTPROMO + ", "
                + DatabaseHelper.COLUMN_IMAGEART + ", "
                + DatabaseHelper.COLUMN_BEACON +
                " FROM " + DatabaseHelper.TABLE_PROMOBEACON +
                " WHERE " + DatabaseHelper.COLUMN_IDP + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        //Instanciation d'une nouvelle notification
        PromoBeaconMetier promo = new PromoBeaconMetier();

        promo.setId_promo(cursor.getInt(0));
        promo.setTitrePromo(cursor.getString(1));
        promo.setLbPromo(cursor.getString(2));
        promo.setImageoff(cursor.getString(3));
        promo.setTxtPromo(cursor.getString(4));
        promo.setImageart((cursor.getString(5)));
        promo.setIdBeacon(cursor.getString(6));

        //fermeture du cusor
        cursor.close();
        return promo;
    }

    public void deleteTablePromoBeacon() {

        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_PROMOBEACON);
        mDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+DatabaseHelper.TABLE_PROMOBEACON+"'");
    }

    public List<PromoBeaconMetier> getPromoBeacon() {

        String query = "SELECT "
                + DatabaseHelper.COLUMN_IDP + ", "
                + DatabaseHelper.COLUMN_TITREPRO + ", "
                + DatabaseHelper.COLUMN_LBPROMO + ", "
                + DatabaseHelper.COLUMN_IMAGEOFF + ", "
                + DatabaseHelper.COLUMN_TXTPROMO + ", "
                + DatabaseHelper.COLUMN_IMAGEART + ", "
                + DatabaseHelper.COLUMN_BEACON +
                " FROM " + DatabaseHelper.TABLE_PROMOBEACON;

        Cursor cursor = mDb.rawQuery(query, null);

        Log.i("cursor", String.valueOf(cursor));

        List<PromoBeaconMetier> listPromoBeacon = new ArrayList<>();

        while (cursor.moveToNext()) {

            //création d'une nouvelle notification
            PromoBeaconMetier promo = new PromoBeaconMetier();

            promo.setId_promo(cursor.getInt(0));
            promo.setTitrePromo(cursor.getString(1));
            promo.setLbPromo(cursor.getString(2));
            promo.setImageoff(cursor.getString(3));
            promo.setTxtPromo(cursor.getString(4));
            promo.setImageart((cursor.getString(5)));
            promo.setIdBeacon(cursor.getString(6));

            listPromoBeacon.add(promo);
        }

        // fermeture du cursor
        cursor.close();

        return listPromoBeacon;
    }
}
