package com.br.mreza.musicplayer.MBN.customViews;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;

public class MbnCircularSeekBar extends View {
    Paint mPaint;
    Paint mPaint2;
    Paint mPaint3;
    Paint mPaint4;
    Paint mPaint5;


    Bitmap centerBitmap;
//    Canvas myCanvas;


    private float maxValue = 100;


    private float currentValue = 0;

    private float interval = 1;

    float fixedX;
    float fixedY;
    float currentAngleRaw = 0;
    float currentAngle = 0;

    MbnSeekBarHelper listener;
    private Path mPath;
    //    private Path mPath2;
    private Rect rect;
    private float height;
    private float width;
    private int radios;


    private float angleFinder(float x, float y) {
        float angle;

        if (y == fixedY) {
            if (x > fixedX) {

                angle = 90;
            } else {
                angle = 270;
            }


        } else if (x == fixedX) {

            if (y < fixedY) {

                angle = 0;
            } else {

                angle = 180;
            }


        } else {

            float height = fixedY - y;
            float width = x - fixedX;

//            System.out.println(height + "---" + width);
//
//            System.out.println(height / width);
//
//            System.out.println((Math.atan(height / width)) / Math.PI);

            double arcTan = Math.toDegrees(Math.atan(height / width));

            float tempAngle = (float) (90 - arcTan);

            if (x < fixedX && y > fixedY) {

                angle = 180 + tempAngle;

            } else if (x < fixedX && y < fixedY) {

                float a = 180 - tempAngle;
                angle = 360 - a;


            } else {

                angle = (float) (90 - arcTan);

            }
        }


        return angle;
    }


    public MbnCircularSeekBar(Context context) {
        super(context);
    }

    public MbnCircularSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint3 = new Paint();
        mPaint4 = new Paint();
        mPaint.setAntiAlias(true);
        mPaint3.setAntiAlias(true);
        mPaint4.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.BLACK);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeWidth(6f);
        mPaint2.setAlpha(120);
        mPaint.setAlpha(100);
        mPaint3.setColor(Color.parseColor("#1efffb"));
        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setStrokeJoin(Paint.Join.ROUND);
        mPaint3.setStrokeWidth(6f);
        mPaint3.setStrokeCap(Paint.Cap.ROUND);
        mPaint4.setColor(Color.parseColor("#1efffb"));
        mPaint4.setStyle(Paint.Style.STROKE);
        mPaint4.setStrokeJoin(Paint.Join.ROUND);
        mPaint4.setStrokeWidth(15f);
        mPaint4.setStrokeCap(Paint.Cap.ROUND);
        mPaint2.setTextSize(30f);
        mPaint5 = new Paint();
        mPaint5.setAntiAlias(true);
        mPaint5.setColor(Color.WHITE);
        mPaint5.setStyle(Paint.Style.FILL);
        mPaint5.setStrokeJoin(Paint.Join.ROUND);
        mPaint5.setStrokeWidth(20f);
        mPaint5.setTextSize(50f);

        mPath = new Path();
//        mPath2 = new Path();


    }


    private void measureUtils() {

        radios = (int) Math.min(fixedX, fixedY);

        int scale = radios - 45;

        rect = new Rect((int) (fixedX - scale), (int) (fixedY - scale), (int) (fixedX + scale), (int) (fixedY + scale));


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

//        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

//        myCanvas = new Canvas(bitmap);

        fixedX = w / 2;
        fixedY = h / 2;
        width = w;
        height = h;

        int scale = radios - 30;
//        rect = new Rect(45, 45, Math.min(this.getWidth(), this.getHeight()) - 45, Math.min(this.getWidth(), this.getHeight()) - 45);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPath.addArc(fixedX - scale, fixedY - scale, fixedX + scale, fixedY + scale, currentAngle - 90, 1);
        }
//        mPath.addArc(30, 30, Math.min(this.getWidth(), this.getHeight()) - 30, Math.min(this.getWidth(), this.getHeight()) - 30, currentAngle - 90, 1);

        measureUtils();

    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {

        int scale = radios - 30;

//        canvas.drawCircle(fixedX, fixedY, radios - 15, mPaint);

        if (centerBitmap != null) {

            canvas.drawBitmap(centerBitmap, null, rect, null);
        }

        canvas.drawCircle(fixedX, fixedY, radios - 30, mPaint2);

//        canvas.drawCircle(Math.min(this.getWidth(), this.getHeight()) / 2, Math.min(this.getWidth(), this.getHeight()) / 2, (Math.min(this.getWidth(), this.getHeight()) / 2) - 30, mPaint2);

        canvas.drawArc(fixedX - scale, fixedY - scale, fixedX + scale, fixedY + scale, -90, currentAngle, false, mPaint3);

//        canvas.drawArc(30, 30, Math.min(this.getWidth(), this.getHeight()) - 30, Math.min(this.getWidth(), this.getHeight()) - 30, -90, currentAngle, false, mPaint3);

        canvas.drawPath(mPath, mPaint4);

//        canvas.drawTextOnPath("Value: " + (int) currentValue, mPath2, 0, 60, mPaint5);


        super.onDraw(canvas);


//        System.out.println("Draw !!!!!!");
    }


    private boolean doContinue(MotionEvent event) {


        float x = event.getX();
        float y = event.getY();

        float distance = (float) Math.sqrt((Math.pow(x - fixedX, 2)) + (Math.pow(y - fixedY, 2)));


        if (distance < radios + 20 && distance > radios - 70) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }


        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {


            listener.onTouchEnd(currentValue);
            return false;
        }


        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            if (!doContinue(event)) {

                return false;

            }
            listener.onTouchStart(currentValue);


        }

        float a = event.getX();

        float b = event.getY();

        currentAngleRaw = angleFinder(a, b);


//        invalidate();


//        listener.onChange(angle);

        invalidator();

//        event.recycle();

        return true;
    }

    public void setOnChangeListener(MbnSeekBarHelper l) {

        this.listener = l;
    }

    public void setColor(int color) {


        mPaint3.setColor(color);
        mPaint4.setColor(color);
//        mPaint4.setShadowLayer(9, 0, 0, Color.WHITE);
        invalidate();
    }

    /**
     * @return THE MAXIMUM VALUE THAT THE SEEK BAR RETURNS.
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue SETS THE MAXIMUM VALUE THAT THE SEEK BAR RETURNS.
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getInterval() {
        return interval;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }


    private void invalidator() {

        float scale = 360 / maxValue;

        float rawData = currentAngleRaw / scale;

        if (Math.abs(rawData - currentValue) > interval) {

            currentValue = Math.round(rawData);


        }


        currentAngle = currentValue * scale;

        mPath.rewind();
//        mPath2.rewind();

        int capScale = radios - 30;

        mPath.addArc(fixedX - capScale, fixedY - capScale, fixedX + capScale, fixedY + capScale, currentAngle - 92, 5f);
//        mPath2.addArc(30, 30, Math.min(this.getWidth(), this.getHeight()) - 30, Math.min(this.getWidth(), this.getHeight()) - 30, currentAngle - 120, 130);


        listener.onChange(currentValue);

        invalidate();


    }

    public void setCenterImg(Bitmap bitmap) {


        centerBitmap = MbnUtils.roundedBitmap(bitmap);
        invalidate();


    }


    public void setCurrentValue(float value) {
        this.currentValue = value;

        currentAngleRaw = (currentValue / maxValue) * 360;

        invalidator();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(size, size);

    }
}


