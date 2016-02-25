package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

public class PromoBanniere extends Activity {

    private TextView titre;
    private TextView lbpromo;
    private TextView dateDebutPromo;
    private TextView dateFinPromo;
    private ImageView imageArt;
    private TextView txtPromo;

    private PromoBanniereMetier promoBanniere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_banniere);

        // récupérer l'ID avec une intent

        Intent i = getIntent();
        promoBanniere = i.getParcelableExtra("promoBanniere");

        //correspondance avec les vue
        titre = (TextView) findViewById(R.id.titrePromoBanniereView);
        //lbpromo = (TextView) findViewById(R.id.);
        dateDebutPromo = (TextView) findViewById(R.id.dtDebVal);
        dateFinPromo = (TextView) findViewById(R.id.dtFinVal);
        imageArt = (ImageView) findViewById(R.id.imgPromoBanniereView);
        txtPromo = (TextView) findViewById(R.id.textPromoBanniereView);

        //extraction des données
        titre.setText(promoBanniere.getTitrePromo());
        //lbpromo.setText(promoBanniere.getIdpromo());
        dateDebutPromo.setText(promoBanniere.getDtdebval());
        dateFinPromo.setText(promoBanniere.getDtfinval());
        imageArt.setImageBitmap(BitMapUtil.getBitmapFromString(promoBanniere.getImageart()));
        txtPromo.setText(promoBanniere.getTxtBanniere());
    }
}