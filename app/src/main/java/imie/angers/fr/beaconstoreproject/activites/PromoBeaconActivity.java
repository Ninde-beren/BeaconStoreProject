package imie.angers.fr.beaconstoreproject.activites;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PanierDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

/**
 * Created by Anne on 23/02/2016.
 */
public class PromoBeaconActivity extends AppCompatActivity {

    private PromoBeaconMetier promoBeacon;

    private PanierDAO panierDAO;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_promo_beacon);

        panierDAO = new PanierDAO(this);
        panierDAO.open();

        TextView titrePromo = (TextView) findViewById(R.id.titrePromo);
        ImageView imageArt = (ImageView) findViewById(R.id.imagePromo);
        TextView txtPromo = (TextView) findViewById(R.id.textPromo);

        FloatingActionButton toPanier = (FloatingActionButton) findViewById(R.id.btnAddPanier);

        Intent i = getIntent();
        promoBeacon = i.getExtras().getParcelable("promoBeacon");

        assert promoBeacon != null;
        titrePromo.setText(promoBeacon.getTitrePromo());
        imageArt.setImageBitmap(BitMapUtil.getBitmapFromString(promoBeacon.getImageart()));
        txtPromo.setText(promoBeacon.getTxtPromo());

        toPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {

                        Log.i("time", String.valueOf(System.currentTimeMillis()));
                        Log.i("idPromo", String.valueOf(promoBeacon.getId_promo()));

                        panierDAO.addPromoPanier(promoBeacon.getId_promo(), System.currentTimeMillis());

                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean o) {

                        super.onPostExecute(o);

                        Toast.makeText(PromoBeaconActivity.this, "Cette promotion a bien été ajoutée à votre panier", Toast.LENGTH_SHORT).show();
                    }
                }.execute();

            }
        });
    }

/*************************************************************************************************
* INDICATION DE LA PAGE A AFFICHER AU RETOUR
*************************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("id", 1);
                startActivity(i);
        }
        return true;
    }
}
