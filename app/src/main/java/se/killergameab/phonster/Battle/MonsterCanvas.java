package se.killergameab.phonster.Battle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import se.killergameab.phonster.R;

public class MonsterCanvas extends ImageView {

    public MonsterCanvas(Context context) {
        super(context);
    }

    public MonsterCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonsterCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {

        final AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);

        List<Bitmap> images = new ArrayList<>();
        images.add(BitmapFactory.decodeResource(getResources(), R.drawable.m1_idle1));
        images.add(BitmapFactory.decodeResource(getResources(), R.drawable.m1_idle2));

        int duration = 200;

        for (Bitmap image : images) {
            BitmapDrawable frame = new BitmapDrawable(getResources(), image);
            animationDrawable.addFrame(frame, duration);
        }

        setBackground(animationDrawable);

        post(new Runnable() {
            public void run() {
                animationDrawable.start();
            }
        });
    }

}
