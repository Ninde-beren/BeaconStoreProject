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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_beacon);

        Log.i("listBeacon", "bienvenue");

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();


        try {

            listPromoBeacon = new getPromoBeacon().execute().get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        PromoBeaconAdapter promoBeaconAdapter = new PromoBeaconAdapter(ListPromoBeaconActivity.this, (ArrayList<PromoBeaconMetier>) listPromoBeacon);

        ListView list = (ListView) findViewById(R.id.list);
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

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }


        PromoBeaconAdapter promoBeaconAdapter = new PromoBeaconAdapter(ListPromoBeaconActivity.this, (ArrayList<PromoBeaconMetier>) listPromoBeacon);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(promoBeaconAdapter);

        return super.onNavigateUpFromChild(child);

    }


    /* @Override
    protected void onResume() {
        super.onResume();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        PromoBeaconAdapter promoBeaconAdapter = new PromoBeaconAdapter(ListPromoBeaconActivity.this, (ArrayList<PromoBeaconMetier>) listPromoBeacon);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(promoBeaconAdapter);

    }*/

    private class getPromoBeacon extends AsyncTask<Void, List<PromoBeaconMetier>, List<PromoBeaconMetier>> {

        @Override
        protected List<PromoBeaconMetier> doInBackground(Void... params) {

            List<PromoBeaconMetier> listPromoB = promoBeaconDAO.getPromoBeacon();

            Log.i("listBeacon2", String.valueOf(listPromoB.size()));

            return listPromoB;
        }

        @Override
        protected void onPostExecute(List<PromoBeaconMetier> promoBeaconMetiers) {
            super.onPostExecute(promoBeaconMetiers);

           if(promoBeaconMetiers.size() == 0 ) {

               Log.i("listBeacon3", String.valueOf(promoBeaconMetiers.size()));

               Toast.makeText(getBaseContext(), "Aucune promotion pour le moment", Toast.LENGTH_LONG).show();

               Log.i("Hello there", "ICI");
           }
       }
    }
}