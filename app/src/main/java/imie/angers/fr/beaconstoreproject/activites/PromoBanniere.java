package imie.angers.fr.beaconstoreproject.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

public class PromoBanniere extends AppCompatActivity {

    private TextView titre;
    private TextView lbpromo;
    private TextView dateDebutPromo;
    private TextView dateFinPromo;
    private ImageView imageArt;
    private TextView txtPromo;

    private PromoBanniereMetier promoBanniere;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

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

/*************************************************************************************************
* INDICATION DE LA PAGE A AFFICHER AU RETOUR
*************************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
        }
        return true;
    }
}