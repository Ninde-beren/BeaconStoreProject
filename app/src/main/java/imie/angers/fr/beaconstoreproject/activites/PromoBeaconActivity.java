package imie.angers.fr.beaconstoreproject.activites;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * Created by Anne on 23/02/2016.
 */
public class PromoBeaconActivity extends AppCompatActivity {

    private TextView titrePromo;
    private ImageView imageArt;
    private TextView txtPromo;

    private PromoBeaconMetier promoBeacon;

    private FloatingActionButton toPanier;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_promo_beacon);

        //ActionBar actionBargetActionBar().setDisplayHomeAsUpEnabled(true);

        titrePromo = (TextView) findViewById(R.id.titrePromo);
        imageArt = (ImageView) findViewById(R.id.imagePromo);
        txtPromo = (TextView) findViewById(R.id.textPromo);

        toPanier = (FloatingActionButton) findViewById(R.id.btnAddPanier);

        Intent i = getIntent();
        promoBeacon = i.getExtras().getParcelable("promoBeacon");

        Log.i("Beacon", promoBeacon.getTitrePromo());

        titrePromo.setText(promoBeacon.getTitrePromo());
        imageArt.setImageBitmap(BitMapUtil.getBitmapFromString(promoBeacon.getImageart()));
        txtPromo.setText(promoBeacon.getTxtPromo());

        toPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PromoBeaconActivity.this, Panier.class);
                i.putExtra("promo", promoBeacon);
            }
        });
    }

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
