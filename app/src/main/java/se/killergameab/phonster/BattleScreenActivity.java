package se.killergameab.phonster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class BattleScreenActivity extends Activity {

    // Animation
    Animation animFadein;
    Animation animBlink;
    private MediaPlayer mp_battle_first;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_screen);

        mp_battle_first = MediaPlayer.create(this, R.raw.battlefirst);
        mp_battle_first.start();

        TextView txtTitle = (TextView) findViewById(R.id.battleButton);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/DoubleFeature20.ttf");
        txtTitle.setTypeface(typeface1);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);

        txtTitle.setVisibility(View.VISIBLE);

        // start the animation
        txtTitle.startAnimation(animFadein);
        txtTitle.startAnimation(animBlink);

        int timeout = 3000; // make the activity visible for 3 seconds

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                mp_battle_first.stop();
                Intent i = new Intent(getApplicationContext(), AimingActivity.class);
                startActivity(i);
            }
        }, timeout);

    }

    /** Called when the user clicks the invisible button */
    public void onReadyClick(View view) {
        timer.cancel();
        mp_battle_first.stop();
        Intent i = new Intent(getApplicationContext(), AimingActivity.class);
        startActivity(i);
    }

    // Disable back button
    @Override
    public void onBackPressed() {
    }
}
