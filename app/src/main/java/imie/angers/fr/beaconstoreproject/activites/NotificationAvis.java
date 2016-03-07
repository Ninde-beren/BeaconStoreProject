package imie.angers.fr.beaconstoreproject.activites;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import imie.angers.fr.beaconstoreproject.R;

/**
 * Created by Anne on 05/03/2016.
 */
public class NotificationAvis {

    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */

    public static long NOTIFICATION_ID = 1;

    /**************************************************************************************************
     * CONSTRUCTEUR
     **************************************************************************************************/
    public NotificationAvis() {}


    /**************************************************************************************************
     * SEND NOTIFICATION
     **************************************************************************************************/

    public void sendNotification(Context context){ //revoir la méthode sendNotification -> ajouter en paramètre l'activité à déclancher + les params

        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link PendingIntent} so that the
         * notification service can fire it on our behalf.
         */

        Intent intent = new Intent(context, Avis.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */

        builder.setContentTitle("BeaconStore");
        builder.setContentText("Merci d'être venu dans notre magasin");
        builder.setSubText("Donnez une note...");

        builder.setDefaults(android.app.Notification.DEFAULT_VIBRATE);

        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) NOTIFICATION_ID, builder.build());
    }
}
