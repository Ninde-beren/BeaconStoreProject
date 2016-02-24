package imie.angers.fr.beaconstoreproject.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.DAOBase;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;

public class MainActivity2 extends AppCompatActivity {

    private PromoBeaconDAO promoBeaconDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promoBeaconDAO = new PromoBeaconDAO(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        promoBeaconDAO.open();
        promoBeaconDAO.deleteTablePromoBeacon();

    }
}
