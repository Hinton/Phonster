package se.killergameab.phonster;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CountDownBar extends ProgressBar {

    public CountDownBar(Context context) {
        super(context);
    }

    public CountDownBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startCountdown(int maxTime) {
        this.setMax(maxTime);
        System.out.println("HAI");

        new CountDownTimer(maxTime, 1) {

            public void onTick(long millisUntilFinished) {
                setProgress((int)millisUntilFinished);
            }

            public void onFinish() {
                setProgress(0);
            }
        }.start();

    }






}
