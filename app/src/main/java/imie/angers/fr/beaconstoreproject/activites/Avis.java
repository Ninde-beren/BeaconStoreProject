package imie.angers.fr.beaconstoreproject.activites;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import imie.angers.fr.beaconstoreproject.R;

public class Avis extends Activity {

    //dfbcvnbnvbnfnfnchgnnfnfgngfnfcgn

    private static Button btn_submit;
    private static RatingBar Avis_mag;
    private static RatingBar Avis_promo;
    private static TextView texttestmag;
    private static TextView texttestpromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);

        ListenerOnRatingBar();
        ListenerOnButton();

    }

    public void ListenerOnRatingBar(){

    Avis_mag = (RatingBar) findViewById(R.id.ratingBarMag);
    Avis_promo = (RatingBar) findViewById(R.id.ratingBarPromo);
    btn_submit = (Button) findViewById(R.id.buttonValideavis);
    texttestmag = (TextView)findViewById(R.id.resultmag);
    texttestpromo =(TextView)findViewById(R.id.resultpromo);

        Avis_mag.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                texttestmag.setText(String.valueOf(rating));
            }

        });

        Avis_promo.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                texttestpromo.setText(String.valueOf(rating));
            }

        });
    }

    public void ListenerOnButton() {

        Avis_mag = (RatingBar) findViewById(R.id.ratingBarMag);
        Avis_promo = (RatingBar) findViewById(R.id.ratingBarPromo);

        btn_submit = (Button) findViewById(R.id.buttonValideavis);
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              Toast.makeText(Avis.this, String.valueOf(Avis_mag.getRating()), Toast.LENGTH_LONG).show();
            }
        });
    }
}
