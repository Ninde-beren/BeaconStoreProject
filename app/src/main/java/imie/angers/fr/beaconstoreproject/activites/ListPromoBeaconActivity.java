package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    //protected List<PromoBeaconMetier> listPromoBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_beacon2);

        Log.i("listBeacon", "bienvenue");

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();

        new getPromoBeacon().execute();

        //Log.i("proBeacon", String.valueOf(listPromoBeacon.size()));
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();

    }*/

    private class getPromoBeacon extends AsyncTask<Void, List<PromoBeaconMetier>, List<PromoBeaconMetier>> {

        private List<PromoBeaconMetier> listPromoB;

        public getPromoBeacon() {

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

            RecyclerView recList = (RecyclerView) findViewById(R.id.beaconPromoList);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(ListPromoBeaconActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);

            Log.i("Hello there", "ICI");

            PromoBeaconAdapter promoBeaconAdapter = new PromoBeaconAdapter(promoBeaconMetiers);
            recList.setAdapter(promoBeaconAdapter);
        }
    }
}