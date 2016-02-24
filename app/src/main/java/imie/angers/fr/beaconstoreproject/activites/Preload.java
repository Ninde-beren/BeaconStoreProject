package imie.angers.fr.beaconstoreproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import imie.angers.fr.beaconstoreproject.R;

public class Preload extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                nextpage();
            }
        }, 2000);
    }

    private void nextpage(){
        Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(nextScreen);
    }
}
