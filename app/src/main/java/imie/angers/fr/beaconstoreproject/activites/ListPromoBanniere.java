package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBanniereAdapter;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;

public class ListPromoBanniere extends Activity {

    private PromoBanniereDAO promoBanniereDAO;
    protected List<PromoBanniereMetier> listPromoBanniere;

    //----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_banniere);

        Log.i("listBanniere", "bienvenue");

        promoBanniereDAO = new PromoBanniereDAO(this);
        promoBanniereDAO.open();

        try {

            listPromoBanniere = new getPromoBanniere().execute().get();

            Log.i("listbeanniere", String.valueOf(listPromoBanniere.size()));

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        PromoBanniereAdapter promoBanniereAdapter = new PromoBanniereAdapter(ListPromoBanniere.this, (ArrayList<PromoBanniereMetier>) listPromoBanniere);

        ListView list = (ListView) findViewById(R.id.listpromobanniere);
        list.setAdapter(promoBanniereAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Sending image id to ImageSeule
                PromoBanniereMetier BannierePromo = (PromoBanniereMetier) parent.getItemAtPosition(position);

                Intent i = new Intent(getApplicationContext(), PromoBanniere.class);
                // passing array index
                i.putExtra("promoBanniere", BannierePromo);
                startActivity(i);
            }
        });
    }

    private class getPromoBanniere extends AsyncTask<Void, List<PromoBanniereMetier>, List<PromoBanniereMetier>> {

        @Override
        protected List<PromoBanniereMetier> doInBackground(Void... params) {

            List<PromoBanniereMetier> listPromoB = promoBanniereDAO.getListPromoBanniere();

            return listPromoB;
        }

        @Override
        protected void onPostExecute(List<PromoBanniereMetier> promoBanniereMetiers) {
            super.onPostExecute(promoBanniereMetiers);

            if (promoBanniereMetiers.size() == 0) {

                Toast.makeText(getBaseContext(), "Aucune promotion pour le moment", Toast.LENGTH_LONG).show();

            }
        }
    }
}