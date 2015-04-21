package se.killergameab.phonster;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onAiming2Click(View view) {
        Intent i = new Intent(getApplicationContext(), AimingActivity2.class);
        startActivity(i);
    }

    public void onAttackClick(View view) {
        Intent i = new Intent(getApplicationContext(), AttackActivity.class);
        startActivity(i);
    }

    public void onBattleClick(View view) {
        Intent i = new Intent(getApplicationContext(), BattleActivity.class);
        startActivity(i);
    }
}
