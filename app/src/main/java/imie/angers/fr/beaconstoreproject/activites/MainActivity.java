package imie.angers.fr.beaconstoreproject.activites;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;

public class MainActivity extends TabActivity {

    private PromoBeaconDAO promoBeaconDAO;
    private PromoBanniereDAO promoBanniereDAO;

    private TabHost menuOnglet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;

        //Récupération du TabHost
        menuOnglet = (TabHost) findViewById(android.R.id.tabhost);

        //appeler la méthode setup du TabHost
        menuOnglet.setup();

        //ajouter les onglets au menuOnglet

        //parametre de l'onglet 1
        intent = new Intent().setClass(this, ListPromoBanniere.class);
        TabHost.TabSpec spec = menuOnglet.newTabSpec("Onglet_1").setIndicator("Liste banniere");
        //spécification du layout à afficher
        spec.setContent(intent);
        //On ajoute l'onglet au TabHost
        menuOnglet.addTab(spec);

        //on ajoute le reste des onglets
        Intent intent2 = new Intent().setClass(this, Profil.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_2").setIndicator("Profils").setContent(intent2));
        Intent intent3 = new Intent().setClass(this, Panier.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_3").setIndicator("Panier").setContent(intent3));
        Intent intent4 = new Intent().setClass(this, PromoBanniere.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_4").setIndicator("Onglet 4").setContent(intent4));
        Intent intent5 = new Intent().setClass(this, Avis.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_5").setIndicator("Onglet 5").setContent(intent5));

        Intent intent6 = new Intent().setClass(this, ListPromoBeaconActivity.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_4").setIndicator("List beacon").setContent(intent6));

        //parametrer un écouteur onTabChangedListener pour récupérer le changement d'onglet
        menuOnglet.setOnTabChangedListener(
                new TabHost.OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String tabId) {
                        Toast.makeText(MainActivity.this, "l'onglet avec l'idendifiant :" + tabId + "a été cliqué",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        menuOnglet.setCurrentTab(0);

        promoBeaconDAO = new PromoBeaconDAO(this);

    }

    @Override
     protected void onDestroy() {
        super.onDestroy();
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();
        promoBanniereDAO.open();
        promoBanniereDAO.deleteTablePromoBanniere();
    }

    @Override
    protected void onStop() {
        super.onStop();
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();
        promoBanniereDAO.open();
        promoBanniereDAO.deleteTablePromoBanniere();
    }

}