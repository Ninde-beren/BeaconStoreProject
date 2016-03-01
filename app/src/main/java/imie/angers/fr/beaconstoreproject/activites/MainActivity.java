package imie.angers.fr.beaconstoreproject.activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

public class MainActivity extends TabActivity {

    // Store context for dialogs
    public Context context = null;

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;

    private ConsommateurDAO consommateurDAO;

    private PromoBeaconDAO promoBeaconDAO;
    private PromoBanniereDAO promoBanniereDAO;

    private TabHost menuOnglet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBanniereDAO = new PromoBanniereDAO(this);

        // récupérer le statut de la session user

        SessionManager user = new SessionManager(MainActivity.this);

        Log.i("id de session", String.valueOf(user.getIdC()));
        int logger = (int) user.getIdC();

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

        if(logger==0){
            Intent intent2 = new Intent().setClass(this, LoginActivity.class);
            menuOnglet.addTab(menuOnglet.newTabSpec("onglet_2").setIndicator("Connexion").setContent(intent2));
        }else {
            Intent intent2 = new Intent().setClass(this, Profil.class);
            menuOnglet.addTab(menuOnglet.newTabSpec("onglet_2").setIndicator("Profils").setContent(intent2));
        }

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

        verificationBluetoothDialog();

        if(logger!=0){remplirInfoDialog(logger);}

    }

    @Override
     protected void onDestroy() {
        super.onDestroy();
        //promoBeaconDAO.open();
        //promoBeaconDAO.deleteTablePromoBeacon();
        promoBanniereDAO.open();
        promoBanniereDAO.deleteTablePromoBanniere();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();
        promoBanniereDAO.open();
        promoBanniereDAO.deleteTablePromoBanniere();
    }*/

    private void verificationBluetoothDialog() {

        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) {

            // Le terminal ne possède pas le Bluetooth

            //Toast
            Toast.makeText(context, "Vous ne pouvez pas profiter de cette application", Toast.LENGTH_SHORT).show();
        } else {
            if (!blueAdapter.isEnabled()) {
                //boite de dialog de demande d'activation du bluetooth
                Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            return;
        if (resultCode == RESULT_OK) {
            // L'utilisation a activé le bluetooth

        } else {
            // L'utilisation n'a pas activé le bluetooth
            Toast.makeText(getBaseContext(), "Vous ne pourrez pas profiter des offres en magasin", Toast.LENGTH_LONG).show();
        }
    }

    private Dialog remplirInfoDialog(long id) {

        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        ConsommateurMetier consommateur = new ConsommateurMetier();

            consommateur = consommateurDAO.getConsommateur(id);

            if (consommateur.getNom() == null || consommateur.getPrenom() == null || consommateur.getGenre() == null || consommateur.getTel() == null || consommateur.getDtnaiss() == null || consommateur.getCdpostal() == null || consommateur.getCatsocpf() == null) {

                AlertDialog.Builder infos;
                infos = new AlertDialog.Builder(this);
                infos.setTitle("Information");
                infos.setIcon(R.drawable.ic_launcher);
                infos.setMessage("Vous n'avez pas remplis tous les champs lors de votre inscription.\n Les remplir maintenant ?");


                infos.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent nextScreen = new Intent(getApplicationContext(), Inscription.class);
                                startActivity(nextScreen);
                            }
                       }
                );

                infos.setNeutralButton("Ignorer", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                infos.show();
            }

        return null;
    }

    public void switchTab(int tab){
        menuOnglet.setCurrentTab(tab);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);

        switch(id) {

            case 1 :
                menuOnglet.setCurrentTab(5);
                break;
            case 2 :
                menuOnglet.setCurrentTab(0);
                break;
            default:
                menuOnglet.setCurrentTab(0);
        }
    }
}