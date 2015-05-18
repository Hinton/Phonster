package se.killergameab.phonster;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class WinningScreenActivity extends Activity {
    private MediaPlayer mp_winning;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_screen);

        mp_winning = MediaPlayer.create(this, R.raw.winning);
        mp_winning.start();

        int timeout = 4000; // make the activity visible for 4 seconds

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                mp_winning.stop();
                mp_winning.release();
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        }, timeout);
    }

    /** Called when the user clicks the invisible button */
    public void onReadyClick(View view) {
        timer.cancel();
        mp_winning.stop();
        mp_winning.release();
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }

    // Disable back button
    @Override
    public void onBackPressed() {
    }
}
