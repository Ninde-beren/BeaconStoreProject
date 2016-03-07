package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBanniereAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;

public class ListPromoBanniere extends ListFragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private PromoBanniereDAO promoBanniereDAO;
    private List<PromoBanniereMetier> listPromoBanniere;

    public static ListPromoBanniere newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListPromoBanniere fragment = new ListPromoBanniere();
        fragment.setArguments(args);
        return fragment;
    }


/**************************************************************************************************
* ON CREATE
**************************************************************************************************/



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        promoBanniereDAO = new PromoBanniereDAO(getContext());
        promoBanniereDAO.open();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_liste_promo_banniere, container, false);

        Log.i("listBanniere", "bienvenue");

        try {

            listPromoBanniere = new getPromoBanniere().execute().get();

            Log.i("listbeanniere", String.valueOf(listPromoBanniere.size()));

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        PromoBanniereAdapter promoBanniereAdapter = new PromoBanniereAdapter(getContext(), (ArrayList<PromoBanniereMetier>) listPromoBanniere);

        setListAdapter(promoBanniereAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

/*************************************************************************************************
* Actions effectuées lorsqu'un item est cliqué
* @param l
* @param v
 * @param position
* @param id
***********************************************************************************************/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Sending image id to ImageSeule
        PromoBanniereMetier BannierePromo = (PromoBanniereMetier) l.getAdapter().getItem(position);

        Intent i = new Intent(getContext(), PromoBanniere.class);
        // passing array index
        i.putExtra("promoBanniere", BannierePromo);
        startActivity(i);
    }


/*************************************************************************************************
* DONNE LA LISTE DES PROMOTIONS BANNNIERES
*************************************************************************************************/

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

                Toast.makeText(getContext(), "Aucune promotion pour le moment", Toast.LENGTH_LONG).show();

            }
        }
    }
}