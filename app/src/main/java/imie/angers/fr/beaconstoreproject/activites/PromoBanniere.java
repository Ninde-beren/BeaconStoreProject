package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import imie.angers.fr.beaconstoreproject.R;

public class PromoBanniere extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_banniere);
        //TODO récupérer l'ID avec une intent
        //TODO BanniereMetier promoBanniere = PromoBAnniereDAO.getPromoBanniere(id)
        //TODO extraire et faire correspondre avec les élémentde la vue

    }
}
