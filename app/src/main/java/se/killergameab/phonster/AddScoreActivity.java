package se.killergameab.phonster;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddScoreActivity extends Activity {

    public final static String EXTRA_MESSAGE = "se.killergameab.phonster.MESSAGE";
    int exp = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        Bundle getExp = getIntent().getExtras();
        exp = getExp.getInt("EXTRA_EXP");
        TextView textView = (TextView) findViewById(R.id.playerScore);
        textView.setText(Integer.toString(exp));
    }

    /** Called when the user clicks the Ok button */
    public void sendScore(View view) {
        Intent intent = new Intent(this, HighscoresActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("EXTRA_EXP", exp);
        startActivity(intent);
    }
}
