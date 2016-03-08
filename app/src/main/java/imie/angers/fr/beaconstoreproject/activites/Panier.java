package imie.angers.fr.beaconstoreproject.activites;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.daimajia.swipe.SwipeLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;
import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PanierSwipeAdapter;
import imie.angers.fr.beaconstoreproject.dao.PanierDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Created by Anne on 03/03/2016.
 */
public class Panier extends ListFragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private PanierDAO panierDAO;
    private List<PromoBeaconMetier> listPanier;
    private PanierSwipeAdapter panierSwipeAdapter;

    public static Panier newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Panier fragment = new Panier();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = getArguments().getInt(ARG_PAGE);

        panierDAO = new PanierDAO(getContext());
        panierDAO.open();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_liste_panier, container, false);

        Log.i("Panier", "dans OnCreate");

        try {

            listPanier = new AsyncTask<Void, Void, List<PromoBeaconMetier>>() {
                @Override
                protected List<PromoBeaconMetier> doInBackground(Void... params) {
                    return panierDAO.getPromoPanier();
                }
            }.execute().get();

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        panierSwipeAdapter = new PanierSwipeAdapter(getContext(), R.layout.activity_panier_swipe, R.id.position, listPanier);

        setListAdapter(panierSwipeAdapter);



        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*@Override
    public void onPause() {
        super.onPause();

        try {

            listPanier = new AsyncTask<Void, Void, List<PromoBeaconMetier>>() {
                @Override
                protected List<PromoBeaconMetier> doInBackground(Void... params) {
                    return panierDAO.getPromoPanier();
                }
            }.execute().get();

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        setListAdapter(panierSwipeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {

            listPanier = new AsyncTask<Void, Void, List<PromoBeaconMetier>>() {
                @Override
                protected List<PromoBeaconMetier> doInBackground(Void... params) {
                    return panierDAO.getPromoPanier();
                }
            }.execute().get();

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }
        setListAdapter(panierSwipeAdapter);
    }*/

    @Override
    public void onListItemClick(final ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        //int pos = position;

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ((SwipeLayout) (l.getChildAt(position - l.getFirstVisiblePosition()))).open(true);

                Log.e("ListView", "OnClick");

            }
        });

        l.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        l.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        l.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
    }
}
