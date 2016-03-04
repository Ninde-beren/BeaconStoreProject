package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Created by Anne on 01/03/2016.
 */
public class PanierAdapterSwipe extends ArraySwipeAdapter<PromoBeaconMetier> {

    private List<PromoBeaconMetier> listPanier;
    private PromoBeaconMetier promoPanier;

    public PanierAdapterSwipe(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

}
