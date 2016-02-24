package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
    private List<PromoBeaconMetier> listPromoBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();

        new getPromoBeacon().execute();

        PromoBeaconAdapter promoBeaconAdapter = new PromoBeaconAdapter(this, (ArrayList<PromoBeaconMetier>) listPromoBeacon);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(promoBeaconAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Sending image id to ImageSeule
                PromoBeaconMetier BeaconPromo = (PromoBeaconMetier) parent.getItemAtPosition(position);

                Intent i = new Intent(getApplicationContext(), PromoBeaconActivity.class);
                // passing array index
                i.putExtra("promoBeacon", BeaconPromo);
                startActivity(i);
            }
        });
    }

    private class getPromoBeacon extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            listPromoBeacon = promoBeaconDAO.getPromoBeacon();

            if(listPromoBeacon != null) {

                return 1;

            } else {

                return 0;
            }
        }
    }
}
