package imie.angers.fr.beaconstoreproject.activites;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.exceptions.RESTException;
import imie.angers.fr.beaconstoreproject.utils.AndrestClient;
import imie.angers.fr.beaconstoreproject.utils.DoRequest;
import imie.angers.fr.beaconstoreproject.utils.SessionManager;

public class Avis extends Activity {

    private AndrestClient rest;
    private ConsommateurDAO consommateurDAO;

    private static Button btn_submit;
    private static RatingBar Avis_mag;
    private static RatingBar Avis_promo;
    private float noteMag;

    private static String url = "http://beaconstore.ninde.fr/serverRest.php/note";

    private int magId;
    private long consoId;
    private SessionManager session;

/**************************************************************************************************
* ON CREATE
**************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);

        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        Avis_mag = (RatingBar) findViewById(R.id.ratingBarMag);
        Avis_promo = (RatingBar) findViewById(R.id.ratingBarPromo);
        btn_submit = (Button) findViewById(R.id.buttonValideavis);

        rest = new AndrestClient();

        Intent i = getIntent();
        magId = i.getIntExtra("magId", 0);

        session = new SessionManager(this);

        consoId = session.getIdC();

        ListenerOnRatingBar();
        ListenerOnButton();
    }

/*************************************************************************************************
* RECUPERER LES NOTES ATTRIBUEES
*************************************************************************************************/

    public void ListenerOnRatingBar(){

        Avis_mag.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {}

        });

        Avis_promo.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                noteMag = rating;
            }
        });
    }

/*************************************************************************************************
* ENVOYER LES NOTES A L API, UNE FOIS LE BOUTON ENVOYER APPUYE
*************************************************************************************************/

    public void ListenerOnButton() {

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Map<String, Object> toPost = new HashMap<String, Object>();
                toPost.put("noteMag", noteMag);
                toPost.put("magId", magId);
                toPost.put("consoId", consoId);

                //execution de la requête POST (cf API) en arrière plan dans un autre thread
                new DoRequest(Avis.this, toPost, "POST", url) {
                    @Override
                    protected Boolean doInBackground(Void... params) {

                        Log.i("url note", url);

                        try {

                            rest.post(url, toPost);

                        } catch (RESTException e1) {

                            e1.printStackTrace();
                        }

                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean data) {
                        super.onPostExecute(data);

                        if(data) {

                            Toast.makeText(getBaseContext(), "Votre avis à bien été pris enregistré", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });
    }
}
