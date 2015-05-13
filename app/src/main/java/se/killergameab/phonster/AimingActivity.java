package se.killergameab.phonster;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.os.CountDownTimer;

import se.killergameab.phonster.map.Zone;

public class AimingActivity extends Activity {

    AimingView mAimingView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread

    Timer mTmr = null;
    TimerTask mTsk = null;

    Point displaySize;
    android.graphics.PointF mAimPos, mAimSpd;
    Vibrator v = null;

    public enum AimField {
        FIRST, SECOND, THIRD, OUTSIDE
    }

    public AimField currentField = AimField.OUTSIDE;

    Player player = new Player();
    int turn = 0;

    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    int time = 0;

    ImageView img;
    int zone;
    Monster monster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(0xFFFFFFFF,
                LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent i = getIntent();
        zone = i.getIntExtra("zone", -1);

        monster = new Monster(zone);
        img = (ImageView) findViewById(R.id.none);

        switch (zone) {
            case 1: img.setImageResource(R.drawable.monster1);
            case 2: img.setImageResource(R.drawable.monster2);
            case 3: img.setImageResource(R.drawable.monster3);
        }



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aiming);

        //create pointer to main screen
        final FrameLayout mainAimView = (android.widget.FrameLayout) findViewById(R.id.main_aiming);

        //get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        mAimPos = new android.graphics.PointF();
        mAimSpd = new android.graphics.PointF();

        //create variables for aim position and speed
        mAimPos.x = displaySize.x / 2;
        mAimPos.y = displaySize.y / 2 + 650;
        mAimSpd.x = 0;
        mAimSpd.y = 0;

        //create initial aim
        mAimingView = new AimingView(this, mAimPos.x, mAimPos.y, 12);

        mainAimView.addView(mAimingView); //add aim to main screen
        mAimingView.invalidate(); //call onDraw in AimingView

        mAimingView.bringToFront();

        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //listener for accelerometer, use anonymous class for simplicity
        ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        //set aim speed based on phone tilt (ignore Z axis)
                        mAimSpd.x = -event.values[0];
                        mAimSpd.y = event.values[1];
                        //timer event will redraw aim
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    } //ignore
                },
                ((SensorManager) getSystemService(Context.SENSOR_SERVICE))
                        .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
                SensorManager.SENSOR_DELAY_NORMAL);


        // Proceed to attack when user clicks the screen
        View screenView = findViewById(R.id.touchListener);
        screenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Monster
                TextView monsterXP = (TextView) findViewById(R.id.monsterXP2);
                int monsterExperience = Integer.parseInt(monsterXP.getText().toString());

                TextView monsterHP = (TextView) findViewById(R.id.monsterHP2);
                int monsterLife = Integer.parseInt(monsterHP.getText().toString());

                //Player
                TextView playerXP = (TextView) findViewById(R.id.playerXP2);
                int playerExperience = Integer.parseInt(playerXP.getText().toString());

                TextView playerHP = (TextView) findViewById(R.id.playerHP2);
                int playerLife = Integer.parseInt(playerHP.getText().toString());

                //Battle
                if (turn == 0 && playerLife > 0) {
                    //System.out.print();
                        mCountDownTimer.onFinish();

                        Intent i = new Intent(getApplicationContext(), AttackActivity.class);

                        // Create bundle to pass accuracy integer to AttackActivity
                        Bundle sendAccuracy = new Bundle();
                        sendAccuracy.putInt("accuracy", getAccuracy());
                        i.putExtras(sendAccuracy);

                        int lifeLeft = monster.lifeLeft(getAccuracy());
                        monsterLife = lifeLeft - getProgress();

                        if(monsterLife < 0) {
                            monsterHP.setText(String.valueOf(0));
                        } else {
                            monsterHP.setText(String.valueOf(monsterLife));
                        }

                        turn = 1;

                        startActivity(i);
                    }

                    if(turn == 1 && monsterLife > 0) {

                        switch (zone) {
                            case 1: img.setImageResource(R.drawable.monster1_hit);
                            case 2: img.setImageResource(R.drawable.monster2_hit);
                            case 3: img.setImageResource(R.drawable.monster3_hit);
                        }

                        int ratio = monsterExperience / playerExperience * 15;
                        int lifeLeft = player.lifeLeft(ratio);
                        playerLife = lifeLeft;

                        if(playerLife < 0){
                            playerHP.setText(String.valueOf(0));
                        } else {
                            playerHP.setText(String.valueOf(lifeLeft));
                            turn = 0;
                        }
                    }
                }
            });

        //Create progress bar
        //http://stackoverflow.com/questions/10241633/android-progressbar-countdown
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setProgress(time);
        mCountDownTimer = new CountDownTimer(5000,950) {

            @Override
            public void onTick(long millisUntilFinished) {
                //Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                time++;
                mProgressBar.setProgress(time);
                mProgressBar.setRotation(180);
                Drawable drawable = mProgressBar.getProgressDrawable();
                drawable.setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onFinish() {
                //Do what you want
                time++;
                mProgressBar.setProgress(time);
            }
        };
        mCountDownTimer.start();
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
        //create timer to move aim to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                //if debugging with external device,
                //  a cat log viewer will be needed on the device
                android.util.Log.d(
                        "Aiming","Timer Hit - " + mAimPos.x + ":" + mAimPos.y);
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
            accuracy = 40;
        } else if (isInField(230, mAimPos.x, mAimPos.y)) {
            accuracy = 20;
        } else if (isInField(493, mAimPos.x, mAimPos.y)) {
            accuracy = 10;
        }
        return accuracy;
    }

    // Fields for progressbar
    public int getProgress() {
        int progress = 0;
        if (time >= 0 && time < 2) {
            progress = 40;
        } else if (time >= 2 && time < 3) {
            progress = 30;
        } else if (time >= 3 && time < 4) {
            progress = 20;
        } else if (time >= 4 && time < 5) {
            progress = 10;
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
}