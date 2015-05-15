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

public class BattleScreenActivity extends Activity{

        // Animation
        Animation animFadein;
        Animation animBlink;
        private MediaPlayer mp_battle_first;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_battle_screen);

            mp_battle_first = MediaPlayer.create(this, R.raw.battlefirst);
            mp_battle_first.start();

            TextView txtTitle = (TextView) findViewById(R.id.battleScreenText);
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

        }

        public void onReadyClick(View view) {
            mp_battle_first.stop();
            Intent i = new Intent(getApplicationContext(), AimingActivity.class);
            startActivity(i);
            finish();
        }

}
