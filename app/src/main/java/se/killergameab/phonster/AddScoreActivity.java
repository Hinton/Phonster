package se.killergameab.phonster;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddScoreActivity extends Activity {

    public final static String EXTRA_MESSAGE = "se.killergameab.phonster.MESSAGE";
    int score;
    MediaPlayer mp_button_sound;
    MediaPlayer mp_death;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        Bundle getExp = getIntent().getExtras();
        score = getExp.getInt("Score");
        TextView textView = (TextView) findViewById(R.id.playerScore);
        textView.setText(Integer.toString(score));

        mp_button_sound = MediaPlayer.create(this, R.raw.menubutton);
        mp_death = MediaPlayer.create(this, R.raw.deathsound);
        mp_death.start();
    }

    /** Called when the user clicks the Ok button */
    public void sendScore(View view) {
        mp_button_sound.start();
        Intent intent = new Intent(this, HighscoresActivityEnd.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("EXTRA_EXP", score);
        mp_death.stop();
        mp_death.release();
        startActivity(intent);
    }

    // Disable back button
    @Override
    public void onBackPressed() {
    }
}
