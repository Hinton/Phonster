package se.killergameab.phonster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.media.MediaPlayer;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import me.grantland.widget.AutofitHelper;


public class MainActivity extends Activity {

    MediaPlayer mp_menu_song, mp_button_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mp_menu_song = MediaPlayer.create(this, R.raw.menusong);
        //mp_menu_song.start();
        mp_menu_song = MediaPlayer.create(this, R.raw.mainmenusong);
        mp_menu_song.setLooping(true);
        mp_menu_song.start();
        mp_button_sound = MediaPlayer.create(this, R.raw.menubutton);


        TextView txtPhonster = (TextView) findViewById(R.id.mainTitle);
        Button btnPlay = (Button) findViewById(R.id.playButton);
        Button btnInstructions = (Button) findViewById(R.id.instructionsButton);
        Button btnHighscores = (Button) findViewById(R.id.highscoresButton);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
        AlphaAnimation fadeIn2 = new AlphaAnimation(0.0f , 1.0f );
        AlphaAnimation fadeIn3 = new AlphaAnimation(0.0f , 1.0f );
        AlphaAnimation fadeIn4 = new AlphaAnimation(0.0f , 1.0f );

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/DoubleFeature20.ttf");

        txtPhonster.setTypeface(typeface1);
        btnPlay.setTypeface(typeface1);
        btnHighscores.setTypeface(typeface1);
        btnInstructions.setTypeface(typeface1);

        txtPhonster.startAnimation(fadeIn);
        btnPlay.startAnimation(fadeIn2);
        btnInstructions.startAnimation(fadeIn3);
        btnHighscores.startAnimation(fadeIn4);

        fadeIn.setDuration(4000);
        fadeIn.setFillAfter(true);
        fadeIn2.setDuration(4000);
        fadeIn2.setFillAfter(true);
        fadeIn3.setDuration(4000);
        fadeIn3.setFillAfter(true);
        fadeIn4.setDuration(4000);
        fadeIn4.setFillAfter(true);

        fadeIn2.setStartOffset(1000 + fadeIn.getStartOffset());
        fadeIn3.setStartOffset(500 + fadeIn2.getStartOffset());
        fadeIn4.setStartOffset(500 + fadeIn3.getStartOffset());

        AutofitHelper.create(txtPhonster);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPlayClick(View view) {
        mp_button_sound.start();
        mp_menu_song.stop();
        mp_menu_song.release();
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }

    public void onInstructionsClick(View view){
        mp_button_sound.start();
        Intent i = new Intent(getApplicationContext(), InstructionsActivity.class);
        startActivity(i);
    }

    public void onHighscoresClick(View view) {
        mp_button_sound.start();
        Intent i = new Intent(getApplicationContext(), HighscoresActivity.class);
        startActivity(i);
    }
}
