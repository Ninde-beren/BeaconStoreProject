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
public class PanierAdapter<T> extends ArrayAdapter<T> {

    private List<T> listPanier;
    private PromoBeaconMetier objPromoBeacon;
    private PromoBanniereMetier objPromoBanniere;


    public PanierAdapter(Context context, ArrayList<T> listPanier) {
        super(context, 0, listPanier);
        this.listPanier = listPanier;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(listPanier.get(0).getClass() == PromoBeaconMetier.class) {

            objPromoBeacon = (PromoBeaconMetier) getItem(position); //retourne l'objet T à la position "position"

        } else {

            objPromoBanniere = (PromoBanniereMetier) getItem(position); //retourne l'objet T à la position "position"
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_panier, parent, false);
        }
        // Lookup view for data population
        TextView titleView = (TextView) convertView.findViewById(R.id.titrePanier);
        TextView descView = (TextView) convertView.findViewById(R.id.txtPanier);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgPanier);

        // Populate the data into the template view using the data object

        if(objPromoBeacon.getClass() == PromoBeaconMetier.class) {

            titleView.setText(objPromoBeacon.getTitrePromo());
            descView.setText(objPromoBeacon.getLbPromo());
            imgView.setImageBitmap(BitMapUtil.getBitmapFromString(objPromoBeacon.getImageoff()));

        } else {

            titleView.setText(objPromoBanniere.getTitrePromo());
            descView.setText(objPromoBanniere.getLbPromo());
            imgView.setImageBitmap(BitMapUtil.getBitmapFromString(objPromoBanniere.getImageoff()));

        }

        // Return the completed view to render on screen
        return convertView;
    }
}
