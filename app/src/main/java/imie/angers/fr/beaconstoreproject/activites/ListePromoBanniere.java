package imie.angers.fr.beaconstoreproject.activites;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
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

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBanniereAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;


public class ListePromoBanniere extends ListActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_liste_promo_banniere);

            //TODO BanniereMetier promoBanniere = PromoBAnniereDAO.getPromoBanniere(id)
            //TODO extraire et faire correspondre avec les élémentde la vue
            //TODO envoyer l'ID avec une intent vers la page PromoBanniere

        }
    }