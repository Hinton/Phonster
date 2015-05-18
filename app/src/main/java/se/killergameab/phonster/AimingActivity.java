package se.killergameab.phonster;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import se.killergameab.phonster.Battle.Battle;
import se.killergameab.phonster.Battle.Monster;
import se.killergameab.phonster.Battle.MonsterCanvas;
import se.killergameab.phonster.Battle.Player;

public class AimingActivity extends Activity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensor;

    public Game game;
    Battle battle;
    Player player;
    Monster monster;

    AimingView mAimingView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread

    Timer mTmr = null;
    TimerTask mTsk = null;

    Point displaySize;
    android.graphics.PointF mAimPos = new android.graphics.PointF(),
                            mAimSpd = new android.graphics.PointF();
    Vibrator v = null;

    CountDownBar countDownBar;

    public enum AimField {
        FIRST, SECOND, THIRD, OUTSIDE
    }

    public AimField currentField = AimField.OUTSIDE;

    private long startTime = 0;
    private int countDownTime = 7500;
    private MediaPlayer mp_battle_second, mp_death;
    int zone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(0xFFFFFFFF,
                LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiming);

        mp_battle_second = MediaPlayer.create(this, R.raw.battlesecond);
        mp_death = MediaPlayer.create(this, R.raw.deathsound);
        mp_battle_second.setLooping(true);
        mp_battle_second.start();

        game = Game.instance();

        // Check if we need to create a new battle
        if (game.getActiveBattle() == null) {
            Monster monster = new Monster(game.getZone());
            game.newBattle(monster);
        }

        setupTextData(game.getActiveBattle());

        startTime = System.currentTimeMillis();

        // Create & start the countdown
        countDownBar = (CountDownBar) findViewById(R.id.progressbar);
        countDownBar.startCountdown(countDownTime);

        // Create the monsterView
        MonsterCanvas monsterCanvas = (MonsterCanvas) findViewById(R.id.monster);
        monsterCanvas.init(game.getActiveBattle().getMonster());

        // Create pointer to main screen
        final FrameLayout mainAimView = (android.widget.FrameLayout) findViewById(R.id.main_aiming);

        // Get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        mAimSpd.x = 0;
        mAimSpd.y = 0;

        // Create initial aim
        mAimingView = new AimingView(this, mAimPos.x, mAimPos.y, 20);

        mainAimView.addView(mAimingView); //add aim to main screen
        mAimingView.invalidate(); //call onDraw in AimingView

        mAimingView.bringToFront();

        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

        battle = game.getActiveBattle();
        player = battle.getPlayer();
        monster = battle.getMonster();

        // Proceed to attack when user clicks the screen
        View screenView = findViewById(R.id.touchListener);
        screenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Change activity to Attack for attack motion
                if (player.getLife() > 0 && monster.getLife() > 0){
                    Intent i = new Intent(getApplicationContext(), AttackActivity.class);
                    startActivity(i);
                }
                battle();
            }
        });

    }

    // Setup the initial information
    private void setupTextData(Battle activeBattle) {
        Player player = activeBattle.getPlayer();
        Monster monster = activeBattle.getMonster();

        setMonsterHP(monster.getLife());
        setMonsterXP(monster.getExperience());

        setPlayerHP(player.getLife());
        setPlayerXP(player.getExperience());
    }

    private void setMonsterHP(int hp) {
        setTextView(R.id.monsterHP2, hp);
    }
    private void setMonsterXP(int xp) {
        setTextView(R.id.monsterXP2, xp);
    }
    private void setPlayerHP(int hp) {
        setTextView(R.id.playerHP2, hp);
    }
    private void setPlayerXP(int xp) {
        setTextView(R.id.playerXP2, xp);
    }
    private void setTextView(int id, int value) {
        TextView textView = (TextView) findViewById(id);
        if (textView != null) {
            textView.setText("" + value);
        }
    }

    //For state flow see http://developer.android.com/reference/android/app/Activity.html
    @Override
    public void onPause() { //app moved to background, stop background threads
        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
        v.cancel();
        super.onPause();
    }

    @Override
    public void onResume() //app moved to foreground (also occurs at app startup)
    {

        if (monster.getLife() <= 0) {
            game.endBattle();
            mp_battle_second.stop();
            Intent i = new Intent(getApplicationContext(), WinningScreenActivity.class);
            startActivity(i);

        } else if (player.getLife() <= 0) {
            setPlayerHP(100);
            game.endBattle();
            mp_battle_second.stop();
            mp_death.start();
            // Should not start new activity but don't know how to solve this right now
            // Might be able to fix in calling activity (In this case MapsActivity)
            // This is a good enough solution I think, if player dies -> highscore screen
            Intent i = new Intent(getApplicationContext(), AddScoreActivity.class);
            i.putExtra("EXTRA_EXP", player.getExperience());
            startActivity(i);
        }

        setupTextData(battle);

        startTime = System.currentTimeMillis();
        countDownBar.reset();
        countDownBar.startCountdown(countDownTime);

        double randDeg = Math.random() * 360 + 1;
        mAimPos.x = 600 * (float)Math.cos(Math.toRadians(randDeg)) + displaySize.x / 2;
        mAimPos.y = 600 * (float)Math.sin(Math.toRadians(randDeg)) + displaySize.y / 2;

        //create timer to move aim to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                //move aim based on current speed
                mAimPos.x += mAimSpd.x;
                mAimPos.y += mAimSpd.y;

                //if ball goes off screen, stay on the side
                if (mAimPos.x > displaySize.x) mAimPos.x = displaySize.x;
                if (mAimPos.y > displaySize.y) mAimPos.y = displaySize.y;
                if (mAimPos.x < 0) mAimPos.x=0;
                if (mAimPos.y < 0) mAimPos.y=0;

                //update aim class instance
                mAimingView.mX = mAimPos.x;
                mAimingView.mY = mAimPos.y;

                int radius = 500;
                int firstFieldRadius = (int) (radius * 0.15f);
                int seconFieldRadius = (int) (radius * 0.45f);
                int thirdFieldRadius = (int) (radius * 1.0f);

                //Vibrate if the ball is in a field
                if (isInField(firstFieldRadius, mAimPos.x, mAimPos.y)) {
                    if (currentField != AimField.FIRST) {
                        currentField = AimField.FIRST;

                        long[] pattern = {0, 80, 50};
                        v.vibrate(pattern, 0);
                    }
                } else if (isInField(seconFieldRadius, mAimPos.x, mAimPos.y)) {
                    if (currentField != AimField.SECOND) {
                        currentField = AimField.SECOND;

                        long[] pattern = {0, 80, 200};
                        v.vibrate(pattern, 0);
                    }
                } else if (isInField(thirdFieldRadius, mAimPos.x, mAimPos.y)) {
                    if (currentField != AimField.THIRD) {
                        currentField = AimField.THIRD;

                        long[] pattern = {0, 80, 350};
                        v.vibrate(pattern, 0);
                    }
                } else {
                    if(currentField != AimField.OUTSIDE){
                        currentField = AimField.OUTSIDE;

                        v.cancel();
                    }
                }

                //redraw aim. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mAimingView.invalidate();
                    }});
            }}; // TimerTask

        mTmr.schedule(mTsk, 8, 8); //start timer
        super.onResume();
    }

    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        v.cancel();
    }

    //listener for config change.
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view, so bypass event
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    // Get accuracy for aim
    public int getAccuracy() {
        int accuracy = 0;
        if (isInField(82, mAimPos.x, mAimPos.y)) {
            accuracy = 20;
        } else if (isInField(230, mAimPos.x, mAimPos.y)) {
            accuracy = 10;
        } else if (isInField(493, mAimPos.x, mAimPos.y)) {
            accuracy = 5;
        }
        return accuracy;
    }

    // Fields for progressbar
    public int getProgress() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        elapsedTime /= 1000;

        int progress = 1;

        if (elapsedTime >= 0 && elapsedTime < 2) {
            progress = 3;
        } else if (elapsedTime >= 2 && elapsedTime < 3) {
            progress = 2;
        }

        return progress;
    }


    //Check if ball coordinates are in a specific field
    public boolean isInField(int radius, float x, float y){
        return Math.pow((x - displaySize.x / 2), 2) +
                Math.pow((y - displaySize.y / 2), 2) < Math.pow(radius, 2);
    }

    // Disable back button
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //set aim speed based on phone tilt (ignore Z axis)
        mAimSpd.x = -event.values[0];
        mAimSpd.y = event.values[1];
        //timer event will redraw aim
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void battle() {

        if (player.getLife() > 0 && monster.getLife() > 0) {
            battle.attack(getAccuracy(), getProgress());
        }
    }
}