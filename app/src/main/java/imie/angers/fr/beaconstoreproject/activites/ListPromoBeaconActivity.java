package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBeaconAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;


/**
 * Created by Anne on 22/02/2016.
 */

public class ListPromoBeaconActivity extends AppCompatActivity {

    private PromoBeaconDAO promoBeaconDAO;
    protected List<PromoBeaconMetier> listPromoBeacon;
    private PromoBeaconAdapter promoBeaconAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_beacon);

        Log.i("listBeacon", "bienvenue");

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();


        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat0", "onCreate");

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        promoBeaconAdapter = new PromoBeaconAdapter(ListPromoBeaconActivity.this, (ArrayList<PromoBeaconMetier>) listPromoBeacon);

        list = (ListView) findViewById(R.id.list);
        list.setAdapter(promoBeaconAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Sending image id to ImageSeule
                PromoBeaconMetier beaconPromo = (PromoBeaconMetier) parent.getItemAtPosition(position);

                Intent i = new Intent(getApplicationContext(), PromoBeaconActivity.class);
                // passing array index
                i.putExtra("promoBeacon", beaconPromo);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onNavigateUpFromChild(Activity child) {

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();

            Log.i("Etat1", "onNavigateUpFromChild");

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        list.setAdapter(promoBeaconAdapter);

        return super.onNavigateUpFromChild(child);

    }


    @Override
    protected void onResume() {
        super.onResume();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat2", "onResume");

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        list.setAdapter(promoBeaconAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat3", "onPause");

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        list.setAdapter(promoBeaconAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat4", "onRestart");

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        list.setAdapter(promoBeaconAdapter);
    }

    private class getPromoBeacon extends AsyncTask<Void, List<PromoBeaconMetier>, List<PromoBeaconMetier>> {

        List<PromoBeaconMetier> listPromoB;

        @Override
        protected List<PromoBeaconMetier> doInBackground(Void... params) {

            listPromoB = promoBeaconDAO.getPromoBeacon();

            return listPromoB;
        }

        @Override
        protected void onPostExecute(List<PromoBeaconMetier> promoBeaconMetiers) {
            super.onPostExecute(promoBeaconMetiers);

           if(promoBeaconMetiers.size() == 0 ) {

               Toast.makeText(getBaseContext(), "Aucune promotion pour le moment", Toast.LENGTH_LONG).show();

           }
       }
    }
}