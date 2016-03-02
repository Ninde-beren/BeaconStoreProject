package imie.angers.fr.beaconstoreproject.activites;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import java.util.concurrent.ExecutionException;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PromoBeaconDAO;
import imie.angers.fr.beaconstoreproject.metiers.PromoBeaconMetier;

/**
 * Permet la création d'une notification
 * Utilisation du sample notification proposé par google
 * Created by plougastel.dl03 on 11/02/2016.
 */
public class Notification {

    public static long notification_id;
    private PromoBeaconDAO promoBeaconDAO;
    private PromoBeaconMetier promoMetier;
    private long lastIdInsert;

    public Notification(long lastId){
        this.lastIdInsert = lastId;
        notification_id = lastId;
    }

    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */


    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(null);

        promoMetier = new PromoBeaconMetier();

        promoBeaconDAO = new PromoBeaconDAO(this);
        promoBeaconDAO.open();

        Intent i = getIntent();

        lastIdInsert = i.getLongExtra("lastIdInsert", 0);

        //Récuprération de l'id de la dernière promo enregistrée dans la base de données via l'intent provenant de ServicePrincipal

        try {

            promoMetier = new getPromoForNotif().execute().get(); //retourne une instance de l'objet PromotionMetier

        } catch (InterruptedException e) {

                e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }


        notification_id = lastIdInsert;

        Log.i("hello", "hello");
        Log.i("PromoBeacon0", promoMetier.getTxtPromo());

       sendNotification();
        finish();
    }*/



    /**
     * Send a sample notification using the NotificationCompat API.
     */

    public void sendNotification(Context context) { //revoir la méthode sendNotification -> ajouter en paramètre l'activité à déclancher + les params

        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
         * notification service can fire it on our behalf.
         */

        promoBeaconDAO = new PromoBeaconDAO(context);
        promoBeaconDAO.open();

        try {

            promoMetier = new getPromoForNotif().execute().get(); //retourne une instance de l'objet PromotionMetier

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        Intent intent = new Intent(context, PromoBeaconActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("promoBeacon", promoMetier);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * Use NotificationCompat.Builder to set up our notification.
         */

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        /** Set the icon that will appear in the notification bar. This icon also appears
         * in the lower right hand corner of the notification itself.
         *
         * Important note: although you can use any drawable as the small icon, Android
         * design guidelines state that the icon should be simple and monochrome. Full-color
         * bitmaps or busy images don't render well on smaller screens and can end up
         * confusing the user.
         */

        builder.setSmallIcon(R.drawable.ic_stat_custom);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Set the notification to auto-cancel. This means that the notification will disappear
        // after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(true);

        /**
         *Build the notification's appearance.
         * Set the large icon, which appears on the left of the notification. In this
         * sample we'll set the large icon to be the same as our app icon. The app icon is a
         * reasonable default if you don't have anything more compelling to use as an icon.
         */

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        //builder.setLargeIcon(BitMapUtil.getBitmapFromString(this.promoMetier.getImageoff()));

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */

        builder.setContentTitle(this.promoMetier.getTitrePromo());
        builder.setContentText(this.promoMetier.getLbPromo());
        builder.setSubText("En savoir plus...");

        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) notification_id, builder.build());
    }

    private class getPromoForNotif extends AsyncTask<Void, Void, PromoBeaconMetier>{

        private PromoBeaconMetier promoB;

        @Override
        protected PromoBeaconMetier doInBackground(Void... params) {

            promoB =  promoBeaconDAO.getLastPromotionInserted(lastIdInsert);

            Log.i("listBeacon2", promoB.getTitrePromo());

            return promoB;
        }

        @Override
        protected void onPostExecute(PromoBeaconMetier promoBeaconMetier) {
            super.onPostExecute(promoBeaconMetier);
        }
    }
}
