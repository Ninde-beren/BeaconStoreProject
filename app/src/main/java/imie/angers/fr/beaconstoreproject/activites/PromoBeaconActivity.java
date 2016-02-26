package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_promo_beacon);

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
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
    }
}
