package imie.angers.fr.beaconstoreproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        long insertId = mDb.insert(DatabaseHelper.TABLE_CONSO, null, values);


        Log.i("dao", "insertion en bdd:" + insertId);

        return insertId;
    }

    public ConsommateurMetier getConsommateur( long id) {

        //TODO selectionner la banniere pour l'activity consommateur

        String query = "SELECT " + DatabaseHelper.COLUMN_IDCONSO + ", "
                                 + DatabaseHelper.COLUMN_NOM     + ", "
                                 + DatabaseHelper.COLUMN_PRENOM  + ", "
                                 + DatabaseHelper.COLUMN_GENRE   + ", "
                                 + DatabaseHelper.COLUMN_TEL     + ", "
                                 + DatabaseHelper.COLUMN_EMAIL   + ", "
                                 + DatabaseHelper.COLUMN_MDP     + ", "
                                 + DatabaseHelper.COLUMN_CSP     + ", "
                                 + DatabaseHelper.COLUMN_CP      + ", "
                                 + DatabaseHelper.COLUMN_DTNAISS +
                        " FROM " + DatabaseHelper.TABLE_CONSO    +
                       " WHERE " + DatabaseHelper.COLUMN_IDCONSO + " = 1";

        Log.i("requete getconsommateur", query);

        Cursor cursor = mDb.rawQuery(query, null);

        Log.i("nombre de valeur : ", String.valueOf(cursor.getCount()));
        Log.i("nombre de column : ", String.valueOf(cursor.getColumnCount()));

        cursor.moveToFirst();

        Log.i("nom des column : ", String.valueOf(cursor.getColumnNames()));

            //création d'un nouveau consommateur
            ConsommateurMetier consommateur = new ConsommateurMetier();

        if (cursor.getCount() != 0) {

            consommateur.setIdConso(cursor.getInt(0));
            //Log.i("id conso :", consommateur.setIdConso(cursor.getInt(0)));
            consommateur.setNom(cursor.getString(1));
            //Log.i("Nom :", consommateur.setNom(cursor.getString(1)));
            consommateur.setPrenom(cursor.getString(2));
            //Log.i("prenom :", consommateur.setPrenom(cursor.getString(2)));
            consommateur.setGenre(cursor.getString(3));
            //Log.i("genre :", consommateur.setGenre(cursor.getString(3)));
            consommateur.setTel(cursor.getString(4));
            //Log.i("tel :", consommateur.setTel(cursor.getString(4)));
            consommateur.setEmail(cursor.getString(5));
            //Log.i("email :", consommateur.setEmail(cursor.getString(5)));
            consommateur.setPassword(cursor.getString(6));
            //Log.i("password :", consommateur.setPassword(cursor.getString(6)));
            consommateur.setCatsocpf(cursor.getString(7));
            //Log.i("cat social :", consommateur.setCatsocpf(cursor.getString(7)));
            consommateur.setCdpostal(cursor.getString(8));
            //Log.i("code postal :", consommateur.setCdpostal(cursor.getString(8)));
            consommateur.setDtnaiss(cursor.getString(9));
            //Log.i("date de naissance :", consommateur.setDtnaiss(cursor.getString(9)));
            Log.i("consommateur : ", String.valueOf(consommateur));

            //fermeture du cursor
            cursor.close();

            return consommateur;
        } else {
            Log.i("requete nest pas passer", String.valueOf(cursor.getColumnCount()));

            //fermeture du cursor
            cursor.close();

            return consommateur;
        }



    }

    public void deleteConso(long id) {

        String query ="DELETE FROM " + DatabaseHelper.TABLE_CONSO;
        Cursor cursor = mDb.rawQuery(query, new String[]{String.valueOf(id)});

        cursor.close();

    }

    public void deleteTableConso() {

        mDb.execSQL("DELETE FROM " + DatabaseHelper.TABLE_CONSO);
        mDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+DatabaseHelper.TABLE_CONSO+"'");
    }
}