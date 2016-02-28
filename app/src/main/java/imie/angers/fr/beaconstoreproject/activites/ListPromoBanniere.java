package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBanniereAdapter;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBeaconAdapter;
import imie.angers.fr.beaconstoreproject.dao.ConsommateurDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.ConsommateurMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBanniereMetier;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;


public class ListPromoBanniere extends Activity {

    // Store context for dialogs
    public Context context = null;

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;

    private PromoBanniereDAO promoBanniereDAO;
    protected List<PromoBanniereMetier> listPromoBanniere;

    private ConsommateurDAO consommateurDAO;

    //----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_promo_banniere);

        verificationBluetoothDialog();
        remplirInfoDialog();

        Log.i("listBanniere", "bienvenue");

        promoBanniereDAO = new PromoBanniereDAO(this);
        promoBanniereDAO.open();


        try {

            listPromoBanniere = new getPromoBanniere().execute().get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        PromoBanniereAdapter promoBanniereAdapter = new PromoBanniereAdapter(ListPromoBanniere.this, (ArrayList<PromoBanniereMetier>) listPromoBanniere);

        ListView list = (ListView) findViewById(R.id.listpromobanniere);
        list.setAdapter(promoBanniereAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Sending image id to ImageSeule
                PromoBanniereMetier BannierePromo = (PromoBanniereMetier) parent.getItemAtPosition(position);

                Log.i("PromoBanniereMetier", "text :" + BannierePromo.getTxtBanniere());
                Log.i("PromoBanniereMetier2", "titre " + BannierePromo.getTitrePromo());


                Intent i = new Intent(getApplicationContext(), PromoBanniere.class);
                // passing array index
                i.putExtra("promoBanniere", BannierePromo);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        promoBanniereDAO.open();
        promoBanniereDAO.deleteTablePromoBanniere();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            listPromoBanniere = new getPromoBanniere().execute().get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        PromoBanniereAdapter promoBanniereAdapter = new PromoBanniereAdapter(ListPromoBanniere.this, (ArrayList<PromoBanniereMetier>) listPromoBanniere);

        ListView list = (ListView) findViewById(R.id.listpromobanniere);
        list.setAdapter(promoBanniereAdapter);

    }

    private class getPromoBanniere extends AsyncTask<Void, List<PromoBanniereMetier>, List<PromoBanniereMetier>> {

        @Override
        protected List<PromoBanniereMetier> doInBackground(Void... params) {

            List<PromoBanniereMetier> listPromoB = promoBanniereDAO.getListPromoBanniere();

            Log.i("listBanniere2", String.valueOf(listPromoB.size()));

            return listPromoB;
        }

        @Override
        protected void onPostExecute(List<PromoBanniereMetier> promoBanniereMetiers) {
            super.onPostExecute(promoBanniereMetiers);

            if (promoBanniereMetiers.size() == 0) {

                Log.i("listBanniere3", String.valueOf(promoBanniereMetiers.size()));

                Toast.makeText(getBaseContext(), "Aucune promotion pour le moment", Toast.LENGTH_LONG).show();

                Log.i("Hello there", "ICI");
            }
        }
    }

    private void verificationBluetoothDialog() {

        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) {

            // Le terminal ne possède pas le Bluetooth

            //Toast
            Toast.makeText(context, "Vous ne pouvez pas profiter de cette application", Toast.LENGTH_SHORT).show();
        } else {
            if (!blueAdapter.isEnabled()) {
                //boite de dialog de demande d'activation du bluetooth
                Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            return;
        if (resultCode == RESULT_OK) {
            // L'utilisation a activé le bluetooth

        } else {
            // L'utilisation n'a pas activé le bluetooth
            Toast.makeText(getBaseContext(), "Vous ne pourrez pas profiter des offres en magasin", Toast.LENGTH_LONG).show();
        }
    }

    private Dialog remplirInfoDialog() {

        consommateurDAO = new ConsommateurDAO(this);
        consommateurDAO.open();

        ConsommateurMetier consommateur = new ConsommateurMetier();

        int user= 1;

        if(user==0){
            long id =1;

        consommateur = consommateurDAO.getConsommateur(id);

        if (consommateur.getNom() == null || consommateur.getPrenom() == null || consommateur.getGenre() == null || consommateur.getTel() == null || consommateur.getDtnaiss() == null || consommateur.getCdpostal() == null || consommateur.getCatsocpf() == null) {

            AlertDialog.Builder infos;
            final EditText input = new EditText(this);
            infos = new AlertDialog.Builder(this);
            infos.setView(input);
            infos.setTitle("Information");
            infos.setIcon(R.drawable.ic_launcher);
            infos.setMessage("Vous n'avez pas remplis tous les champs lors de votre inscription.\n Les remplir maintenant ?");

            infos.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }
            );

            infos.setNeutralButton("Ignorer", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                }
            });

            infos.show();
        }
    }
        return null;
    }
}