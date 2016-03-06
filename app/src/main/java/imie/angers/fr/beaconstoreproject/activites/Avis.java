package imie.angers.fr.beaconstoreproject.activites;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class Avis extends AppCompatActivity {

    private AndrestClient rest;
    private ConsommateurDAO consommateurDAO;

    private static at.markushi.ui.CircleButton btn_submit;
    private static RatingBar Avis_mag;
    private static RatingBar Avis_promo;
    private float noteMag;
    //private float notePromo;

    private static String URL = "http://beaconstore.ninde.fr/serverRest.php/notes";
    private static String URL2 = "http://beaconstore.ninde.fr/serverRest.php/notespromo";

    private int magId;
    //private int proId;
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
        btn_submit = (at.markushi.ui.CircleButton) findViewById(R.id.buttonValideavis);

        rest = new AndrestClient();

        Intent i = getIntent();
        magId = i.getIntExtra("magId", 0);
        //proId = i.getIntExtra("proId", 0);


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

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                noteMag = rating;
            }

        });

        Avis_promo.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //notePromo = rating;
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
                toPost.put("notemag", noteMag);
                toPost.put("idmag", magId);
                toPost.put("idconso", consoId);

                Log.i("le post", String.valueOf(toPost));

                //*****************************************************************************
                //execution de la requête POST (cf API) en arrière plan dans un autre thread
                new DoRequest(Avis.this, toPost, "POST", URL) {
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
                //*******************************************************************************

               /* final Map<String, Object> toPost2 = new HashMap<String, Object>();
                toPost.put("notepro", notePromo);
                toPost.put("idpromo", proId);
                toPost.put("idconso", consoId);

                Log.i("le post2", String.valueOf(toPost2));

                //execution de la requête POST (cf API) en arrière plan dans un autre thread
                new DoRequest(Avis.this, toPost2, "POST", URL2) {
                    @Override
                    protected Boolean doInBackground(Void... params) {

                        Log.i("url note", url);

                        try {

                            rest.post(url, toPost2);

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
                }.execute();*/
             finish();
            }
        });
    }
}
