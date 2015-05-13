package se.killergameab.phonster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class AimingView extends View {

    public float mX;
    public float mY;
    private final int mR;
    private Bitmap bmp;
    private Bitmap scaled;

    //construct new aiming object
    public AimingView(Context context, float x, float y, int r) {
        super(context);
        this.mX = x;
        this.mY = y;
        this.mR = r;  //radius
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.aimingpic);
        scaled = Bitmap.createScaledBitmap(bmp, mR, mR, true);
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(scaled, mX, mY, null);
    }
}