package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;
import imie.angers.fr.beaconstoreproject.metiers.NotificationMetier;

/**
 * Permet d'effectuer les opérations CRUD pour l'objet NotificationMetier
 * Hérite de la classe DAOBase
 * Created by Anne on 13/02/2016.
 */
public class NotificationDAO extends DAOBase {

    public NotificationDAO(Context pContext) {
        super(pContext);
    }

    /**
     * Ajoute une notification dans la base de données
     * @param notification
     */

    /*public long addNotification(NotificationMetier notification) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_PROMO, notification.getIdPromo());
        values.put(DatabaseHelper.COLUMN_BEACON, notification.getIdBeacon());
        values.put(DatabaseHelper.COLUMN_DATEN, "");

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_NOTIFICATION, null, values);

        Log.i("dao", "insertion en bdd:" + insertId);

        return insertId;
    }

    /**
     * Récupère la dernière notification insérée dans la base de données
     * @param id
     * @return
     */

    /*public NotificationMetier getLastNotificationInserted(long id) {

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATION + " WHERE " + DatabaseHelper.COLUMN_IDN + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        NotificationMetier notif = new NotificationMetier();

        notif.setId(cursor.getLong(0));
        notif.setTitre(cursor.getString(1));
        notif.setMessage(cursor.getString(2));
        notif.setImage(cursor.getString(3));
        notif.setIdBeacon(cursor.getString(4));
        notif.setDate(cursor.getString(5));

        //fermeture du cusor
        cursor.close();

        return notif;
    }

    /*public NotificationMetier addNotification(NotificationMetier notification) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_TITLE, notification.getTitre());
        values.put(DatabaseHelper.COLUMN_MESS, notification.getMessage());
        values.put(DatabaseHelper.COLUMN_IMAGE, notification.getImage());
        values.put(DatabaseHelper.COLUMN_BEACON, notification.getIdBeacon());
        values.put(DatabaseHelper.COLUMN_DATE, notification.getDate());

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_NOTIFICATION, null, values);

        Log.i("dao", "insertion en bdd:" + insertId);

        //selection du dernier enregistrement inséré pour retourner l'objet notificationMetier
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATION, new String[]{String.valueOf(insertId)});

        cursor.moveToFirst();

        NotificationMetier newNotif = new NotificationMetier();

        newNotif.setId(cursor.getLong(0));
        newNotif.setTitre(cursor.getString(1));
        newNotif.setMessage(cursor.getString(2));
        newNotif.setImage(cursor.getString(3));
        newNotif.setIdBeacon(cursor.getString(4));
        newNotif.setDate(cursor.getString(5));

        Log.i("select dao", "notif=" + newNotif);
        cursor.close();

        //retourne l'objet inséré en bdd
        return newNotif;
    }

    /**
     * Récupére toutes les notifications stockées dans la base de données
     * @return
     */
    /*public List<NotificationMetier> getNotifications() {

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATION;
        Cursor cursor = mDb.rawQuery(query,null);

        List<NotificationMetier> listNotifications = new ArrayList<>();

        cursor.moveToFirst();

        while (cursor.moveToNext()) {

            //création d'une nouvelle notification
            NotificationMetier notif = new NotificationMetier();

            notif.setId(cursor.getLong(0));
            notif.setTitre(cursor.getString(1));
            notif.setMessage(cursor.getString(2));
            notif.setImage(cursor.getString(3));
            notif.setIdBeacon(cursor.getString(4));
            notif.setDate(cursor.getString(5));

            listNotifications.add(notif);
        }

        // fermeture du cursor
        cursor.close();

        return listNotifications;
    }

    /**
     * Récupére une notification en fonction de son id
     * @param id
     * @return
     */
    /*public NotificationMetier getNotificationById(long id) {

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATION + " WHERE " + DatabaseHelper.COLUMN_IDN + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        NotificationMetier notif = new NotificationMetier();

        notif.setId(cursor.getLong(0));
        notif.setTitre(cursor.getString(1));
        notif.setMessage(cursor.getString(2));
        notif.setImage(cursor.getString(3));
        notif.setIdBeacon(cursor.getString(4));
        notif.setDate(cursor.getString(5));

        //fermeture du cusor
        cursor.close();

        return notif;
    }*/

    /**
     * Récupére une notification en fonction de l'id du beacon auquel il correspond
     * @param idBeacon
     * @return
     */

   /* public NotificationMetier getNotificationByBeaconId(String idBeacon) {

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NOTIFICATION + " WHERE " + DatabaseHelper.COLUMN_BEACON + " = ?";

        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(idBeacon)});

        cursor.moveToFirst();

        NotificationMetier notif = new NotificationMetier();

        notif.setId(cursor.getLong(0));
        notif.setTitre(cursor.getString(1));
        notif.setMessage(cursor.getString(2));
        notif.setImage(cursor.getString(3));
        notif.setIdBeacon(cursor.getString(4));
        notif.setDate(cursor.getString(5));

        //fermeture du cusor
        cursor.close();

        return notif;
    }*/
}
