package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.DatabaseHelper;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;

public class PromoBanniereAdapter extends ArrayAdapter<PromoBanniereMetier>{

    public PromoBanniereAdapter(Context context, ArrayList<PromoBanniereMetier> promo) {
        super(context, 0, promo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PromoBanniereMetier promo = getItem(position); //retourne l'objet T à la position "position"

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_liste_promo_banniere, parent, false);
        }
        // Lookup view for data population
        TextView titleView = (TextView) convertView.findViewById(R.id.titreBanniereView);
        TextView descView = (TextView) convertView.findViewById(R.id.lbBanniereView);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgBanniereView);

        // Populate the data into the template view using the data object
        titleView.setText(promo.getTitrePromo());
        descView.setText(promo.getLbPromo());
        //imgView.setImageBitmap(promo.getImageoff());

        // Return the completed view to render on screen
        return convertView;
    }
}