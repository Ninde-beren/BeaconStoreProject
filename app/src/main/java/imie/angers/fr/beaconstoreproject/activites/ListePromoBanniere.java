package imie.angers.fr.beaconstoreproject.activites;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBanniereAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;


public class ListePromoBanniere extends ListActivity {

    private PromoBanniereDAO promoBanniereDAO;
    private List<PromoBanniereMetier> listPromoBanniere;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_banniere);

        promoBanniereDAO = new PromoBanniereDAO(this);
        promoBanniereDAO.open();

        new getListPromoBanniere().execute();
        //Insérer l'adapter dans la listView de la listActivity

        PromoBanniereAdapter adapter = new PromoBanniereAdapter(this, (ArrayList<PromoBanniereMetier>) listPromoBanniere);
        ListView list = (ListView) findViewById(R.id.list);
        this.setListAdapter(adapter);


        //Envoyer l'ID avec une intent vers la page PromoBanniere


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

    private class getListPromoBanniere extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            listPromoBanniere = promoBanniereDAO.getListPromoBanniere();

            if (listPromoBanniere != null) {

                return 1;

            } else {

                return 0;
            }
        }
    }
}