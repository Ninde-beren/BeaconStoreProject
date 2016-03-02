package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PanierAdapter;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

public class Panier extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_panier);

        SessionManager sessionPanier = new SessionManager(this);

        //noinspection unchecked
        List<PromoBeaconMetier> listPromBeacon = (List<PromoBeaconMetier>) sessionPanier.getPromoBeaconSession();

        Log.i("tablistPanier1", String.valueOf(listPromBeacon));
        Log.i("tablistPanier2", String.valueOf(listPromBeacon.getClass()));

        //noinspection unchecked
        //List<PromoBanniereMetier> listPromoBanniere = (List<PromoBanniereMetier>) sessionPanier.getPromoBanniereSession();

        List<Object> listPanier = new ArrayList<>();

        listPanier.addAll(listPromBeacon);
        //listPanier.addAll(listPromoBanniere);

        Log.i("tablistPanier3", String.valueOf(listPanier));
        Log.i("tablistPanier4", String.valueOf(listPanier.getClass()));
        Log.i("tablistPanier5", String.valueOf(listPanier.get(0).getClass()));



        PanierAdapter panierAdapter = new PanierAdapter(Panier.this, (ArrayList) listPanier);

        ListView list =(ListView) findViewById(R.id.listPanier);

        list.setAdapter(panierAdapter);
    }
}
