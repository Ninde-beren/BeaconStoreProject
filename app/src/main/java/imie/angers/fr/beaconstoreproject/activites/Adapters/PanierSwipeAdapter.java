package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.List;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PanierDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Created by Anne on 03/03/2016.
 */
public class PanierSwipeAdapter<T> extends ArraySwipeAdapter {

    private List<PromoBeaconMetier> listPanier;
    private PromoBeaconMetier promoPanier;
    private PanierDAO panierDAO;

    private Boolean req;


    public PanierSwipeAdapter(Context context, int resource, int textViewResourceId, List promoPanier) {
        super(context, resource, textViewResourceId, promoPanier);
        this.panierDAO = new PanierDAO(context);
        this.req = false;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public boolean getDeleteResponse() {

        return req;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        promoPanier = (PromoBeaconMetier) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_panier_swipe, parent, false);
        }
        // Lookup view for data population
        TextView titleView = (TextView) convertView.findViewById(R.id.titrePanier);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgPanier);
        ImageView imgViewCodeBarre = (ImageView) convertView.findViewById(R.id.imgCodeBarrePanier);

        // Populate the data into the template view using the data object
        titleView.setText(promoPanier.getTitrePromo());
        //imgView.setImageBitmap(BitMapUtil.getBitmapFromString(promoPanier.getImageoff()));
        //imgViewCodeBarre.setImageBitmap(BitMapUtil.getBitmapFromString(promoPanier.getImageart()));

        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.

                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {

                       int reponse = panierDAO.deletePromoPanier(promoPanier.getId_promo());



                        req = reponse != -1;

                        if(req) {

                            PanierSwipeAdapter.this.notifyDataSetChanged();
                        }

                        return req;

                    }

                    @Override
                    protected void onPostExecute(Boolean requete) {
                        super.onPostExecute(requete);

                        if(requete){

                            Toast.makeText(getContext(), "Cette promotion a bien été supprimée de votre panier", Toast.LENGTH_SHORT).show();

                        }
                    }
                }.execute();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


        return convertView;
    }

}
