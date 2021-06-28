package com.br.mreza.musicplayer.newdesign.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.br.mreza.musicplayer.newmodel.database.A_B_Manager;


public class AB_ProgressBar extends View {

    private float density = getResources().getDisplayMetrics().density;
    private float dotRadios = 1.5f * density;
    private Paint paint = new Paint();

    private boolean aS = false, bS = false;
    private float aP, bP;

    public AB_ProgressBar(Context context) {
        super(context);
        init();
    }

    public AB_ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AB_ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AB_ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) ((dotRadios * 3) + 0.5f), MeasureSpec.getMode(heightMeasureSpec)));
    }

    public void setPoints(A_B_Manager.A_B_Object a_b_object, int duration) {
        aS = a_b_object.isaState();
        bS = a_b_object.isbState();
        if (aS) aP = ((float) a_b_object.getA()) / duration;
        if (bS) bP = ((float) a_b_object.getB()) / duration;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (aS) {
            canvas.drawCircle(getWidth() * aP, getHeight() / 2, dotRadios, paint);
        }
        if (bS) {
            canvas.drawCircle(getWidth() * bP, getHeight() / 2, dotRadios, paint);
        }
        if (aS && bS) {
            canvas.drawLine(getWidth() * aP, getHeight() / 2, getWidth() * bP, getHeight() / 2, paint);
        }

//        if (aS) {
//            canvas.drawCircle(getWidth() * aP, getHeight(), dotRadios, paint);
//            canvas.drawLine(getWidth() * aP, 0, getWidth() * aP, getHeight(), paint);
//        }
//        if (bS) {
//            canvas.drawCircle(getWidth() * bP, getHeight(), dotRadios, paint);
//            canvas.drawLine(getWidth() * bP, 0, getWidth() * bP, getHeight(), paint);
//        }
//        if (aS && bS) {
//            canvas.save();
//            canvas.translate(0, -1);
//            canvas.drawLine(getWidth() * aP, getHeight(), getWidth() * bP, getHeight(), paint);
//            canvas.restore();
//        }
    }
}
