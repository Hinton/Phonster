package se.killergameab.phonster;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import me.grantland.widget.AutofitHelper;


public class InstructionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        TextView txtHeader = (TextView) findViewById(R.id.headerInstructionsText);
        TextView txtInstructions1 = (TextView) findViewById(R.id.instructionsText1);
        TextView txtInstructions2 = (TextView) findViewById(R.id.instructionsText2);
        TextView txtInstructions3 = (TextView) findViewById(R.id.instructionsText3);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/DoubleFeature20.ttf");

        txtHeader.setTypeface(typeface1);
        txtInstructions1.setTypeface(typeface1);
        txtInstructions2.setTypeface(typeface1);
        txtInstructions3.setTypeface(typeface1);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
        AlphaAnimation fadeIn2 = new AlphaAnimation(0.0f , 1.0f );
        AlphaAnimation fadeIn3 = new AlphaAnimation(0.0f , 1.0f );
        AlphaAnimation fadeIn4 = new AlphaAnimation(0.0f , 1.0f );


        txtHeader.startAnimation(fadeIn);
        txtInstructions1.startAnimation(fadeIn2);
        txtInstructions2.startAnimation(fadeIn3);
        txtInstructions3.startAnimation(fadeIn4);

        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        fadeIn2.setDuration(3200);
        fadeIn2.setFillAfter(true);
        fadeIn3.setDuration(3200);
        fadeIn3.setFillAfter(true);
        fadeIn4.setDuration(3200);
        fadeIn4.setFillAfter(true);

        fadeIn2.setStartOffset(1000 + fadeIn.getStartOffset());
        fadeIn3.setStartOffset(1500 + fadeIn2.getStartOffset());
        fadeIn4.setStartOffset(1500 + fadeIn3.getStartOffset());

        AutofitHelper.create(txtHeader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instructions, menu);
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
}
