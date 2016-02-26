package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBeaconAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;


/**
 * Created by Anne on 22/02/2016.
 */

public class ListPromoBeaconActivity extends Activity {

    private PromoBeaconDAO promoBeaconDAO;
    protected List<PromoBeaconMetier> listPromoBeacon;
    private RecyclerView recList;
    //private PromoBeaconAdapter promoBeaconAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_beacon);

        Log.i("listBeacon", "bienvenue");

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();

        new getPromoBeacon(listPromoBeacon).execute();
    }


   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();

    }*/

    private class getPromoBeacon extends AsyncTask<Void, List<PromoBeaconMetier>, List<PromoBeaconMetier>> {

        private List<PromoBeaconMetier> listPromoB;

        public getPromoBeacon(List<PromoBeaconMetier> listPromoBeacon) {

            this.listPromoB = new ArrayList<>();
        }


        @Override
        protected List<PromoBeaconMetier> doInBackground(Void... params) {

            listPromoB = promoBeaconDAO.getPromoBeacon();

            Log.i("listBeacon2", String.valueOf(listPromoB.size()));

            return listPromoB;
        }

        @Override
        protected void onPostExecute(List<PromoBeaconMetier> promoBeaconMetiers) {
            super.onPostExecute(promoBeaconMetiers);

            //listPromoBeacon = promoBeaconMetiers;

            Log.i("listBeacon3", String.valueOf(promoBeaconMetiers.size()));

            Toast.makeText(getBaseContext(), "Get list beacon", Toast.LENGTH_SHORT).show();

            Log.i("Hello there", "ICI");

            PromoBeaconAdapter promoBeaconAdapter = new PromoBeaconAdapter(ListPromoBeaconActivity.this, (ArrayList<PromoBeaconMetier>) promoBeaconMetiers);

            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(promoBeaconAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Sending image id to ImageSeule
                    PromoBeaconMetier beaconPromo = (PromoBeaconMetier) parent.getItemAtPosition(position);

                    Log.i("PromoBeaconMetier", " "+  beaconPromo.getTxtPromo());
                    Log.i("PromoBeaconMetier2", " "+  beaconPromo.getTitrePromo());


                    Intent i = new Intent(getApplicationContext(), PromoBeaconActivity.class);
                    // passing array index
                    i.putExtra("promoBeacon", beaconPromo);
                    startActivity(i);
                }
            });
        }
    }
}