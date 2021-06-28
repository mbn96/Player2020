package com.br.mreza.musicplayer.MBN.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


public class MbnLineGrapher extends View {

    private int yStart;
    private int yBase = 450;
    private int xStart;
    private float[] yEntery;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANT = 10;
    private float multyConstant = 0;
    private float plusConstant = 0;

    int width = 0;

    public void setyEntery(float[] yEntery, int color, int alpha, float textSize) {

        this.yEntery = yEntery;

        mPaint.setColor(color);
        mPaint.setAlpha(alpha);
        mPaint.setTextSize(textSize);

        balanced();


    }

    private void balanced() {

        System.out.println("wwww------->" + width);

        float max = 0;
        for (float a : yEntery) {

            if (a > max) {
                max = a;
            }

        }

        System.out.println(max);

        while ((yBase - (multyConstant * max)) > 75) {

            multyConstant += 0.3;
            System.out.println(multyConstant);

        }

        while (plusConstant * yEntery.length < width - 100) {


            plusConstant += 0.5;


        }

//        yBase - (multyConstant * aYEntery));

    }

    public MbnLineGrapher(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(150);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        mPaint.setTextSize(30);
    }


    public MbnLineGrapher(Context c, float[] xEntery, AttributeSet set) {
        super(c, set);
        this.yEntery = xEntery;
        context = c;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeWidth(10f);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        width = w;
        System.out.println("width----->" + w);

        setyEntery(yEntery, mPaint.getColor(), mPaint.getAlpha(), mPaint.getTextSize());
        lineGrapher();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);

//        canvas.drawText("MBN HI", 100, 100, mPaint);
//        canvas.drawTextOnPath("MBN HI123",mPath,20,2
// 0,mPaint);
//canvas.dra

//
        for (float y : yEntery) {

            xStart += plusConstant;
            canvas.drawText(Float.toString(y), xStart - 20, (yBase - 40) - (multyConstant * y), mPaint);

        }
        xStart = 0;


    }


    public void clearDrawing() {


        mPath.reset();
        invalidate();


    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        float x = event.getX();
//        float y = event.getY();
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                startTouch(x, y);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveTouch(x, y);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                upTouch();
//                invalidate();
//                break;
//
//
//        }
//        return true;
//    }


    private void moveTouch(float x, float y) {

        if (Math.abs(x - mX) >= TOLERANT || Math.abs(y - mY) >= TOLERANT) {


            mPath.quadTo(mX, mY, x, y);
//            mPath.quadTo(mX, mY, (x + mY) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

        }


    }

    private void startTouch(float x, float y) {

        mPath.moveTo(x, y);

        mX = x;
        mY = y;

    }

    private void upTouch() {


        mPath.lineTo(mX, mY);
    }


    private void lineGrapher() {

        yStart = 500;
        yBase = 450;
        xStart = 0;


        System.out.println("GraphLength---->" + 300 * yEntery.length);

        mPath.moveTo(xStart, yStart);

//        mPath.quadTo(xStart, yStart, xStart, yBase);

        mPath.lineTo(xStart, yBase - (multyConstant * yEntery[0]));

//        mPath.quadTo(0, 0, 0, yEntery[0] * 100);

        for (float aYEntery : yEntery) {
            xStart += plusConstant;


            mPath.lineTo(xStart, yBase - (multyConstant * aYEntery));


        }

        System.out.println("y25---->" + (yBase - (multyConstant * yEntery[0])));

//        for (int aYEntery : yEntery) {
//            xStart += 150;
//
//
//            mPath.quadTo(xStart - 150, yBase, xStart, 150 - aYEntery);
//
//            yBase = aYEntery;
//
//
//        }

//        mPath.quadTo(xStart, yBase, xStart + 150, 150);
//        mPath.quadTo(xStart + 150, 150, xStart + 150, 200);

        mPath.lineTo(xStart + 150, yBase - (multyConstant * yEntery[yEntery.length - 1]));
        mPath.lineTo(xStart + 150, 500);
        xStart = 0;


    }
}



