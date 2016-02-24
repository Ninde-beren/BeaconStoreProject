package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class PromoBeaconActivity extends Activity {

    private TextView titre;
    private TextView lbpromo;
    private TextView dateDebutPromo;
    private TextView dateFinPromo;
    private ImageView imageArt;
    private TextView txtPromo;

    private PromoBeaconMetier promoBeacon;

    private Button toPanier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.PromoBeacon);

        titre = (TextView) findViewById(R.id.);
        lbpromo = (TextView) findViewById(R.id.);
        dateDebutPromo = (TextView) findViewById(R.id.);
        dateFinPromo = (TextView) findViewById(R.id.);
        imageArt = (ImageView) findViewById(R.id.);
        txtPromo = (TextView) findViewById(R.id.);

        toPanier = (Button) findViewById(R.id.);

        Intent i = getIntent();
        promoBeacon = i.getParcelableExtra("promoBeacon");

        titre.setText(promoBeacon.getTitrePromo());
        lbpromo.setText(promoBeacon.getIdpromo());
        dateDebutPromo.setText(promoBeacon.getDtdebval());
        dateFinPromo.setText(promoBeacon.getDtfinval());
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
}
