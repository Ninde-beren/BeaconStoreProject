package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PanierAdapter;
import imie.angers.fr.beaconstoreproject.dao.PanierDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

public class
        Panier extends Activity {


        private PanierDAO panierDAO;
        private List<PromoBeaconMetier> listPanier;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_panier);

        panierDAO = new PanierDAO(this);
        panierDAO.open();

        try {

            listPanier = new AsyncTask<Void, Void, List<PromoBeaconMetier>>() {
                @Override
                protected List<PromoBeaconMetier> doInBackground(Void... params) {
                    return panierDAO.getPromoPanier();
                }
            }.execute().get();

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        PanierAdapter panierAdapter = new PanierAdapter(Panier.this, (ArrayList<PromoBeaconMetier>) listPanier);

        ListView list =(ListView) findViewById(R.id.listPanier);

        list.setAdapter(panierAdapter);
    }
}
