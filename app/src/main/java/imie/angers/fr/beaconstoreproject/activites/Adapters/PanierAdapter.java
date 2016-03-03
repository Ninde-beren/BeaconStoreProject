package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * Created by Anne on 01/03/2016.
 */
public class PanierAdapter extends ArrayAdapter {

    private List<PromoBeaconMetier> listPanier;
    private PromoBeaconMetier promoPanier;


    public PanierAdapter(Context context, ArrayList<PromoBeaconMetier> listPanier) {
        super(context, 0, listPanier);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        promoPanier = (PromoBeaconMetier) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_panier, parent, false);
        }
        // Lookup view for data population
        TextView titleView = (TextView) convertView.findViewById(R.id.titrePanier);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgPanier);
        ImageView imgViewCodeBarre = (ImageView) convertView.findViewById(R.id.imgCodeBarrePanier);

        // Populate the data into the template view using the data object

        titleView.setText(promoPanier.getTitrePromo());
        //imgView.setImageBitmap(BitMapUtil.getBitmapFromString(promoPanier.getImageoff()));
        //imgViewCodeBarre.setImageBitmap(BitMapUtil.getBitmapFromString(promoPanier.getImageart()));

        // Return the completed view to render on screen
        return convertView;
    }
}
