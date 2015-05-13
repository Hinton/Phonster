package se.killergameab.phonster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.TextView;

import me.grantland.widget.AutofitHelper;


public class MainActivity extends Activity {

    MediaPlayer mp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp1 = MediaPlayer.create(this, R.raw.menusong);
        mp1.start();

        TextView txtTitle = (TextView) findViewById(R.id.mainTitle);
        Button btnPlay = (Button) findViewById(R.id.playButton);
        Button btnInstructions = (Button) findViewById(R.id.instructionsButton);
        Button btnHighscores = (Button) findViewById(R.id.highscoresButton);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/DoubleFeature20.ttf");
        txtTitle.setTypeface(typeface1);
        btnPlay.setTypeface(typeface1);
        btnInstructions.setTypeface(typeface1);
        btnHighscores.setTypeface(typeface1);

        AutofitHelper.create(txtTitle);

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
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }

    public void onInstructionsClick(View view) {
        Intent i = new Intent(getApplicationContext(), InstructionsActivity.class);
        startActivity(i);
    }

    public void onHighscoresClick(View view) {
        Intent i = new Intent(getApplicationContext(), HighscoresActivity.class);
        startActivity(i);
    }

    public void onAimingClick(View view) {
        Intent i = new Intent(getApplicationContext(), AimingActivity.class);
        startActivity(i);
    }

    public void onAttackClick(View view) {
        Intent i = new Intent(getApplicationContext(), AttackActivity.class);
        startActivity(i);
    }
}
