package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;

/**
 * Created by Anne on 21/02/2016.
 */
public class ConsommateurDAO extends DAOBase {

    public ConsommateurDAO(Context pContext) {
        super(pContext);
    }

    public long addConsommateur(ConsommateurMetier conso) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_IDCONSO, conso.getIdConso());
        values.put(DatabaseHelper.COLUMN_NOM, conso.getNom());
        values.put(DatabaseHelper.COLUMN_PRENOM, conso.getPrenom());
        values.put(DatabaseHelper.COLUMN_GENRE, conso.getGenre());
        values.put(DatabaseHelper.COLUMN_TEL, conso.getTel());
        values.put(DatabaseHelper.COLUMN_EMAIL, conso.getEmail());
        values.put(DatabaseHelper.COLUMN_MDP, conso.getPassword());
        values.put(DatabaseHelper.COLUMN_TOKEN, conso.getToken());
        values.put(DatabaseHelper.COLUMN_CSP, conso.getCatsocpf());
        values.put(DatabaseHelper.COLUMN_CP, conso.getCdpostal());
        values.put(DatabaseHelper.COLUMN_DTNAISS, String.valueOf(conso.getDtnaiss()));
        values.put(DatabaseHelper.COLUMN_DATEC, "");

        //insertion en base + recuperation du dernier id inséré
        long insertId = mDb.insert(DatabaseHelper.TABLE_PROMOBEACON, null, values);

        Log.i("dao", "insertion en bdd:" + insertId);

        return insertId;
    }
}
