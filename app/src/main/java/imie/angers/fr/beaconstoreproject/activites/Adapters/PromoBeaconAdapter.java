package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import imie.angers.fr.beaconstoreproject.metiers.PromotionMetier;

/**
 * Created by plougastel.dl03 on 22/02/2016.
 */
public class PromoBeaconAdapter<T> extends ArrayAdapter<T> {

    public int idPromo;
    public String titre;
    public String image;
    public String desc;

    public PromoBeaconAdapter(Context context, ArrayList<T> promo) {
        super(context, 0, promo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       //PromotionMetier promo = getPosition(position);
        return super.getView(position, convertView, parent);
    }
}
