package se.killergameab.phonster;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

public class AimingActivity extends Activity {
    AimingView mAimingView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    android.graphics.PointF mAimPos, mAimSpd;
    Vibrator v = null;
    boolean in_first, in_second, in_third = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //set app to full screen and keep screen on
        getWindow().setFlags(0xFFFFFFFF,
                LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiming);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        //create pointer to main screen
        final FrameLayout mainAimView = (android.widget.FrameLayout) findViewById(R.id.main_aiming);

        //get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        mScrWidth = display.getWidth();
        mScrHeight = display.getHeight();
        mAimPos = new android.graphics.PointF();
        mAimSpd = new android.graphics.PointF();

        //create variables for aim position and speed
        mAimPos.x = mScrWidth;
        mAimPos.y = mScrHeight;
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
        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
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
                Intent i = new Intent(getApplicationContext(), AttackActivity.class);
                // Create bundle to pass accuracy integer to AttackActivity
                Bundle sendAccuracy = new Bundle();
                sendAccuracy.putInt("accuracy", getAccuracy());
                i.putExtras(sendAccuracy);
                startActivity(i);
            }

        });
    }

    //For state flow see http://developer.android.com/reference/android/app/Activity.html
    @Override
    public void onPause() //app moved to background, stop background threads
    {
        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
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
                if (mAimPos.x > mScrWidth) mAimPos.x=mScrWidth;
                if (mAimPos.y > mScrHeight) mAimPos.y=mScrHeight;
                if (mAimPos.x < 0) mAimPos.x=0;
                if (mAimPos.y < 0) mAimPos.y=0;

                //update aim class instance
                mAimingView.mX = mAimPos.x;
                mAimingView.mY = mAimPos.y;

                //Vibrate if the ball is in a field
                if (isInField(55, mAimPos.x, mAimPos.y)) {
                    if (in_first) {
                        v.vibrate(400);
                        in_first = false;
                        in_second = true;
                        in_third = true;
                    }
                }else if (isInField(160, mAimPos.x, mAimPos.y)) {
                    if (in_second) {
                        v.vibrate(200);
                        in_second = false;
                        in_third = true;
                        in_first = true;
                    }
                }else if (isInField(315, mAimPos.x, mAimPos.y)) {
                    if (in_third) {
                        v.vibrate(100);
                        in_third = false;
                        in_second = true;
                        in_first = true;
                    }
                }else{
                    in_third = true;
                }

                //redraw aim. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mAimingView.invalidate();
                    }});
            }}; // TimerTask

        mTmr.schedule(mTsk,8,8); //start timer
        super.onResume();
    }

    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        //wait for threads to exit before clearing app
        System.runFinalizersOnExit(true);
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
    public int getAccuracy(){
        int accuracy = 0;
        if (isInField(55, mAimPos.x, mAimPos.y)) {
            accuracy = 100;
        } else if (isInField(160, mAimPos.x, mAimPos.y)) {
            accuracy = 50;
        } else if (isInField(315, mAimPos.x, mAimPos.y)) {
            accuracy = 20;
        }
        return accuracy;
    }

    //Check if ball coordinates are in a specific field
    public boolean isInField(int radius, float x, float y){
        return Math.pow((x - mScrWidth / 2), 2) +
                Math.pow((y - mScrHeight / 2), 2) < Math.pow(radius, 2);

    }
}