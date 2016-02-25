package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;
import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * Created by plougastel.dl03 on 22/02/2016.
 * Adapter pour générer la liste des PromotionsBeacon
 */
public class PromoBeaconAdapter extends RecyclerView.Adapter<PromoBeaconAdapter.ViewHolder> {

    private List<PromoBeaconMetier> listPromoBeacon;

    public PromoBeaconAdapter(List<PromoBeaconMetier> listPromoBeacon) {
        this.listPromoBeacon = listPromoBeacon;
    }

    @Override
    public PromoBeaconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_promobeacon, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PromoBeaconAdapter.ViewHolder holder, int position) {

        PromoBeaconMetier promo = listPromoBeacon.get(position);

        holder.titrePbeacon.setText(promo.getTitrePromo());
        holder.txtPbeacon.setText(promo.getTxtPromo());
        holder.imgPbeacon.setImageBitmap(BitMapUtil.getBitmapFromString(promo.getImageoff()));
    }

    @Override
    public int getItemCount() {

//        Log.i("listPromoBeacon", String.valueOf(listPromoBeacon.size()));
        return 0;
        //return listPromoBeacon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titrePbeacon;
        public TextView txtPbeacon;
        public ImageView imgPbeacon;

        public ViewHolder(View v) {
            super(v);
            titrePbeacon = (TextView) v.findViewById(R.id.titrePromoBeacon);
            txtPbeacon = (TextView) v.findViewById(R.id.txtPromoBeacon);
            imgPbeacon = (ImageView) v.findViewById(R.id.imgPromoBeacon);
            //mTextView = v;
        }
    }
}
