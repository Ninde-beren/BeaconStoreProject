package imie.angers.fr.beaconstoreproject.activites.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Created by plougastel.dl03 on 22/02/2016.
 * Adapter pour générer la liste des PromotionsBeacon
 */
public class PromoBeaconAdapter extends ArrayAdapter<PromoBeaconMetier> {

    public PromoBeaconAdapter(Context context, ArrayList<PromoBeaconMetier> promo) {
        super(context, 0, promo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PromoBeaconMetier promo = getItem(position); //retourne l'objet T à la position "position"

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_promobeacon, parent, false);
        }
        // Lookup view for data population
        TextView titleView = (TextView) convertView.findViewById(R.id.titrev);
        TextView descView = (TextView) convertView.findViewById(R.id.descv);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgv);

        // Populate the data into the template view using the data object
        titleView.setText(promo.getTitrePromo());
        descView.setText(promo.getLbPromo());
        //imgView.setImageBitmap(promo.getImageoff());

        // Return the completed view to render on screen
        return convertView;
    }
}
