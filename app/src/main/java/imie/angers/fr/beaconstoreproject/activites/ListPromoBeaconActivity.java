package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBeaconAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;


/**
 * Created by Anne on 22/02/2016.
 */

public class ListPromoBeaconActivity extends ListFragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private PromoBeaconDAO promoBeaconDAO;
    protected List<PromoBeaconMetier> listPromoBeacon;
    private PromoBeaconAdapter promoBeaconAdapter;


/**************************************************************************************************
* CONSTRUCTEUR (SINGLETON)
**************************************************************************************************/

    public static ListPromoBeaconActivity newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListPromoBeaconActivity fragment = new ListPromoBeaconActivity();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_liste_promo_beacon, container, false);

        Log.i("listBeacon", "bienvenue");

        promoBeaconDAO = new PromoBeaconDAO(getContext());
        promoBeaconDAO.open();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat0", "onCreate");

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        promoBeaconAdapter = new PromoBeaconAdapter(getContext(), (ArrayList<PromoBeaconMetier>)listPromoBeacon);

        setListAdapter(promoBeaconAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

/*************************************************************************************************
* LORSQU'UN ITEM EST CLIQUE
*************************************************************************************************/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Sending image id to ImageSeule
        PromoBeaconMetier beaconPromo = (PromoBeaconMetier) l.getAdapter().getItem(position);

        Intent i = new Intent(getContext(), PromoBeaconActivity.class);
        // passing array index
        i.putExtra("promoBeacon", beaconPromo);
        startActivity(i);
    }

/*************************************************************************************************
 * ON RESUME
*************************************************************************************************/

   /* @Override
    public void onResume() {
        super.onResume();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat2", "onResume");

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }

        setListAdapter(promoBeaconAdapter);
    }*/

/*************************************************************************************************
* ON PAUSE
*************************************************************************************************/

   /* @Override
    public void onPause() {
        super.onPause();

        try {

            listPromoBeacon = new getPromoBeacon().execute().get();
            Log.i("Etat3", "onPause");

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        setListAdapter(promoBeaconAdapter);
    }*/


/*************************************************************************************************
* DONNE LA LISTE DES PROMOTION BEACON
*************************************************************************************************/

    private class getPromoBeacon extends AsyncTask<Void, List<PromoBeaconMetier>, List<PromoBeaconMetier>> {

        List<PromoBeaconMetier> listPromoB;

        @Override
        protected List<PromoBeaconMetier> doInBackground(Void... params) {

            listPromoB = promoBeaconDAO.getPromoBeacon();

            return listPromoB;
        }

        @Override
        protected void onPostExecute(List<PromoBeaconMetier> promoBeaconMetiers) {
            super.onPostExecute(promoBeaconMetiers);

           if(promoBeaconMetiers.size() == 0 ) {

               Toast.makeText(getContext(), "Aucune promotion pour le moment", Toast.LENGTH_LONG).show();

           }
       }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setDivider(null);
    }
}