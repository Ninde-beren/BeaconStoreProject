package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import imie.angers.fr.beaconstoreproject.R;
import imie.angers.fr.beaconstoreproject.dao.PromoBanniereDAO;
import imie.angers.fr.beaconstoreproject.metiers.NotificationMetier;

/**
 * Permet la création d'une notification
 * Utilisation du sample notification proposé par google
 * Created by Ninde on 25/02/2016.
 */
public class NotificationBanniere extends Activity {

    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */

    public static int notification_id = 1;
    private PromoBanniereDAO promoBanniereDAO;
    private NotificationMetier notification = new NotificationMetier();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.notification);

        promoBanniereDAO = new PromoBanniereDAO(this);
        promoBanniereDAO.open();

        Intent i = getIntent();

        //Récuprération de l'id de la dernière promo enregistrée dans la base de données via l'intent provenant de ServicePrincipal
        long lastIdInsert = i.getLongExtra("lastIdInsert", 0);
        notification = promoBanniereDAO.getLastPromoBanniereInserted(lastIdInsert); //retourne une instance de l'objet NotificationMetier

        Log.i("hello", "hello");

        sendNotification();
    }



    /**
     * Send a sample notification using the NotificationCompat API.
     */

    public void sendNotification() { //revoir la méthode sendNotification -> ajouter en paramètre l'activité à déclancher + les params

        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link PendingIntent} so that the
         * notification service can fire it on our behalf.
         */

        Intent intent = new Intent(this, ListPromoBanniere.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        /**
         * Use NotificationCompat.Builder to set up our notification.
         */

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

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

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        //builder.setLargeIcon(BitMapUtil.getBitmapFromString(this.notification.getImageoff()));

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */

        builder.setContentTitle(this.notification.getTitrePromo());
        builder.setContentText(this.notification.getLbPromo());
        builder.setSubText("En savoir plus...");

        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id, builder.build());
        notification_id++;
    }
}
