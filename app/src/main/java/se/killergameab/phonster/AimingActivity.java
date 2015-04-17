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
import android.widget.RelativeLayout;


public class AimingActivity extends Activity {
    AimingView mAimingView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    android.graphics.PointF mAimPos, mAimSpd;
    Vibrator v = null;

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


        //listener for touch event
        mainAimView.setOnTouchListener(new android.view.View.OnTouchListener() {
            public boolean onTouch(android.view.View v, android.view.MotionEvent e) {
                //set aim position based on screen touch
                mAimPos.x = e.getX();
                mAimPos.y = e.getY();
                //timer event will redraw aim
                return true;
            }});

        // Proceed to attack when user clicks the screen
        View screenView = findViewById(R.id.touchListener);
        screenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AttackActivity.class);
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

                //REPLACED THIS
                //if ball goes off screen, stay on the side
                if (mAimPos.x > mScrWidth) mAimPos.x=mScrWidth;
                if (mAimPos.y > mScrHeight) mAimPos.y=mScrHeight;
                if (mAimPos.x < 0) mAimPos.x=0;
                if (mAimPos.y < 0) mAimPos.y=0;

                //update aim class instance
                mAimingView.mX = mAimPos.x;
                mAimingView.mY = mAimPos.y;

                //Check the area closest to the middle, vibrate if in it
                if (mAimPos.x > mScrWidth / 2 - 40 && mAimPos.x < mScrWidth / 2 + 40
                        && mAimPos.y > mScrHeight / 2 - 40 && mAimPos.y < mScrHeight / 2 + 40){
                    v.vibrate(300);
                }
                //redraw aim. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mAimingView.invalidate();
                    }});
            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer
        super.onResume();
    }

    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        //wait for threads to exit before clearing app
        System.runFinalizersOnExit(true);
        //remove app from memory
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //listener for config change.
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view, so bypass event
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}