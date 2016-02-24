package imie.angers.fr.beaconstoreproject.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;

/**
 * Initialisation de la base de données
 * Cette classe doit être héritée par toutes les classes de notre DAO
 * Created by Anne on 13/02/2016.
 */
public abstract class DAOBase {

    protected SQLiteDatabase mDb = null;
    protected DatabaseHelper mHelper = null;

    public DAOBase(Context pContext) {
        this.mHelper = new DatabaseHelper(pContext);
    }

    public SQLiteDatabase open() throws SQLException {
        mDb = mHelper.getWritableDatabase();

        Log.i("DB", mDb.getPath());
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb(){
        return mDb;
    }
}
