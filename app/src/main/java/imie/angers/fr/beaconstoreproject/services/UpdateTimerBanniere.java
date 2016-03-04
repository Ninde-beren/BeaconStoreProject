package imie.angers.fr.beaconstoreproject.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Anne on 04/03/2016.
 */
public class UpdateTimerBanniere extends BroadcastReceiver{

    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("UpdateTimeBanniere", "LA");
        Intent i = new Intent(context, ServicePromoBanniere.class);
        context.startService(i);
    }
}
