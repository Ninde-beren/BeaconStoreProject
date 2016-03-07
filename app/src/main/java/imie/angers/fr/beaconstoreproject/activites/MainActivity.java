package imie.angers.fr.beaconstoreproject.activites;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.SampleFragmentPagerAdapter;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    // Store context for dialogs
    public Context context = null;
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private ConsommateurDAO consommateurDAO;
    private PromoBeaconDAO promoBeaconDAO;
    private PromoBanniereDAO promoBanniereDAO;
    private TabHost menuOnglet;
    private ConsommateurMetier consommateur;
    private long idConso;
    private SessionManager user;
    private PagerSlidingTabStrip tabsStrip;
    private ViewPager viewPager;


/**************************************************************************************************
* ACTION BAR
**************************************************************************************************/


    // BEGIN_INCLUDE(create_menu)

    /**
     * Use this method to instantiate your menu, and add your items to it. You
     * should return true if you have added items to it and want the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate our menu from the resources by using the menu inflater.

        if(idConso == -1) {

            getMenuInflater().inflate(R.menu.menu_main, menu);

        } else {

            getMenuInflater().inflate(R.menu.menu_main_connected, menu);
        }

        return true;
    }

    // END_INCLUDE(create_menu)

    // BEGIN_INCLUDE(menu_item_selected)
    /**
     * This method is called when one of the menu items to selected. These items
     * can be on the Action Bar, the overflow menu, or the standard options menu. You
     * should return true if you handle the selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextScreen;

        switch (item.getItemId()) {
           // case R.id.menu_refresh: //item actualisation
                // Here we might start a background refresh task
            //    return true;

            //case R.id.menu_settings:

            case R.id.menu_logout: //item deconnexion

                Log.i("logout", String.valueOf(item.getItemId()));

                int nb = consommateurDAO.deleteConso(idConso); //suppression du consommateur de la base de données sqlite

                user.logoutUser(); //on vide la session user

                if(nb > 0) {

                    Toast.makeText(MainActivity.this, "Vous êtes maintenant déconnecté(e)", Toast.LENGTH_SHORT).show();

                    nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                    nextScreen.putExtra("id", 2);
                    startActivity(nextScreen);
                }

                return true;

            case R.id.menu_profil: // item profil

                Log.i("monProfil", String.valueOf(item.getItemId()));

                nextScreen = new Intent(getApplicationContext(), Profil.class);
                startActivity(nextScreen);

                return true;

            case R.id.menu_login: //item connexion

                Log.i("Login", String.valueOf(item.getItemId()));

                nextScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(nextScreen);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // END_INCLUDE(menu_item_selected)

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide1);

        getSupportActionBar().setElevation(0);

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBanniereDAO = new PromoBanniereDAO(this);

        consommateur = new ConsommateurMetier();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        // récupérer le statut de la session user

        user = new SessionManager(MainActivity.this);

        idConso = user.getIdC();

        Log.i("id de session", String.valueOf(user.getIdC()));
        int logger = (int) user.getIdC();

        /****************************************************************************

        //Récupération du TabHost
        menuOnglet = (TabHost) findViewById(android.R.id.tabhost);

        //Récupération du TabHost
        menuOnglet = (TabHost) findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        menuOnglet.setup(mLocalActivityManager);
        //appeler la méthode setup du TabHost

        //ajouter les onglets au menuOnglet

        //parametre de l'onglet 1
        intent = new Intent().setClass(this, ListPromoBanniere.class);
        TabHost.TabSpec spec = menuOnglet.newTabSpec("Onglet_1").setIndicator("Liste banniere");
        //spécification du layout à afficher
        spec.setContent(intent);
        //On ajoute l'onglet au TabHost
        menuOnglet.addTab(spec);

        //on ajoute le reste des onglets

        Intent intent3 = new Intent().setClass(this, Panier.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_3").setIndicator("Panier").setContent(intent3));

       //Intent intent5 = new Intent().setClass(this, Avis.class);
       //menuOnglet.addTab(menuOnglet.newTabSpec("onglet_5").setIndicator("Onglet 5").setContent(intent5));

        Intent intent6 = new Intent().setClass(this, ListPromoBeaconActivity.class);
        menuOnglet.addTab(menuOnglet.newTabSpec("onglet_4").setIndicator("Promotion").setContent(intent6));

        //parametrer un écouteur onTabChangedListener pour récupérer le changement d'onglet
        menuOnglet.setOnTabChangedListener(
                new TabHost.OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String tabId) {
                    }
                }
        );

        menuOnglet.setCurrentTab(0);

         ********************************************************************************/
        
        //**********************************************
        
        verificationBluetoothDialog();

        if(logger != -1){

            try {

                remplirInfoDialog(logger);

            } catch (ExecutionException | InterruptedException e) {

                e.printStackTrace();

            }
        }

    }

/**************************************************************************************************
* VERIFICATION DU BLUETOOTH
**************************************************************************************************/

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

/**************************************************************************************************
* ENVOI UN TOAST SI LE BLUETOOH N EST PAS ACTIVE
**************************************************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH) return;

            if (resultCode == RESULT_OK) {
                // L'utilisation a activé le bluetooth

            } else {
                // L'utilisation n'a pas activé le bluetooth
                Toast.makeText(getBaseContext(), "Vous ne pourrez pas profiter des offres en magasin", Toast.LENGTH_LONG).show();
            }
    }
    
/**************************************************************************************************
* VERIFICATION DES INFORMATIONS DE L UTILISATEUR
**************************************************************************************************/

    private Dialog remplirInfoDialog(final long id) throws ExecutionException, InterruptedException {

        consommateurDAO = new ConsommateurDAO(getApplicationContext());
        consommateurDAO.open();

        consommateur = new AsyncTask<Void, Void, ConsommateurMetier>() {

            @Override
            protected ConsommateurMetier doInBackground(Void... params) {

                return consommateurDAO.getConsommateur(id);
            }
        }.execute().get();

        Log.i("consoInfo", String.valueOf(consommateur.getId_c()));


        if (consommateur.getNom() .equals("") || consommateur.getPrenom().equals("") || consommateur.getGenre().equals("") || consommateur.getTel().equals("") || consommateur.getDtnaiss().equals("") || consommateur.getCdpostal().equals("") || consommateur.getCatsocpf().equals("")) {

            AlertDialog.Builder infos;
            infos = new AlertDialog.Builder(this);
            infos.setTitle("Information");
            infos.setIcon(R.drawable.ic_launcher);
            infos.setMessage("Vous n'avez pas remplis tous les champs lors de votre inscription.\n Les remplir maintenant ?");


            infos.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent nextScreen = new Intent(getApplicationContext(), Profil.class);
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

/**************************************************************************************************
* ON RESUME
**************************************************************************************************/

    /*@Override
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
            case 3 :
                menuOnglet.setCurrentTab(1);
                break;
            default:
                menuOnglet.setCurrentTab(0);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);

        switch(id) {
            case 1:
                viewPager.setCurrentItem(2);
                break;
            case 2:
                viewPager.setCurrentItem(0);
                break;
            case 3:
                viewPager.setCurrentItem(1);
                break;
            default:
                tabsStrip.getChildAt(0);
        }
    }
}