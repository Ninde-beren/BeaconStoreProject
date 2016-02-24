package imie.angers.fr.beaconstoreproject.activites;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.activites.Adapters.PromoBanniereAdapter;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;


public class ListePromoBanniere extends ListActivity {

        private PromoBanniereDAO dbHelper;
        private SimpleCursorAdapter dataAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_liste_promo_banniere);

            dbHelper = new PromoBanniereDAO(this);
            dbHelper.open();

            //Clean all data
            //dbHelper.deleteAllpromos();
            //Add some data
            //dbHelper.insertSomePromos();

            //Generate ListView from SQLite Database
            displayListView();

        }

        private void displayListView() {


            //Cursor cursor = dbHelper.fetchAllPromos();

            // The desired columns to be bound
            String[] columns = new String[] {
           //         PromoBanniereAdapter.KEY_CODE,
            //        PromoBanniereAdapter.KEY_NAME,
            //        PromoBanniereAdapter.KEY_CONTINENT,
            //        PromoBanniereAdapter.KEY_REGION
            };

            // the XML defined views which the data will be bound to
            int[] to = new int[] {
                    R.id.icon,
                    R.id.titreView,
                    R.id.slogan,
            };

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
           // dataAdapter = new SimpleCursorAdapter(
              //      this, R.layout.activity_promo_banniere_list_adapter,
               //     cursor,
               //     columns,
               //     to,
                //    0);

            ListView listView = (ListView) findViewById(android.R.id.list);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View view,
                                        int position, long id) {
                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String countryCode =
                            cursor.getString(cursor.getColumnIndexOrThrow("code"));
                    Toast.makeText(getApplicationContext(),
                            countryCode, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }