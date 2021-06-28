package com.br.mreza.musicplayer.MBN.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class MbnCircleGrapher extends View {


    private int yStart;
    private int yBase = 450;
    private int xStart;
    private float[] yEntery;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;
    private float mX, mY;
    private static final float TOLERANT = 10;
    private float multyConstant = 0;
    private float plusConstant = 0;

    private int width = 0;
    private int height = 0;
    private int xEnd;
    private int yEnd;
    private float startAngle = 0;


    private Bitmap centerImg;
    private Rect rect;


    public void setCenterImg(Bitmap mg) {

        centerImg = mg;
        invalidate();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }



    public void setyEntery(float[] yEntery, int color, int alpha, float textSize) {



        this.yEntery = yEntery;

        mPaint.setColor(color);
        mPaint.setAlpha(alpha);
        mPaint3.setTextSize(textSize);

//        balanced();


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


    }

    public MbnCircleGrapher(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        mPath = new Path();
        mPaint = new Paint();
        mPaint1 = new Paint();
        mPaint2 = new Paint();
        mPaint3 = new Paint();
        mPaint.setAntiAlias(true);
        mPaint1.setAntiAlias(true);
        mPaint2.setAntiAlias(true);
        mPaint3.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint1.setColor(Color.BLACK);
        mPaint2.setColor(Color.BLUE);
        mPaint3.setColor(Color.WHITE);
        mPaint.setAlpha(150);
//        mPaint2.setAlpha(150);
        mPaint3.setAlpha(150);
//        mPaint1.setAlpha(150);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint3.setStyle(Paint.Style.FILL);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint1.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint3.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        mPaint1.setStrokeWidth(4f);
        mPaint2.setStrokeWidth(150f);
        mPaint3.setStrokeWidth(15f);
        mPaint.setTextSize(30);
        mPaint1.setTextSize(30);
        mPaint2.setTextSize(30);
        mPaint3.setTextSize(80);
        mPaint1.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));

    }


//    public MbnCircleGrapher(Context c, float[] xEntery, AttributeSet set) {
//        super(c, set);
//        this.yEntery = xEntery;
//        context = c;
//        mPath = new Path();
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(Color.BLACK);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeJoin(Paint.Join.BEVEL);
//        mPaint.setStrokeWidth(10f);
//
//
//    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        width = w;
        height = h;
        rect = new Rect((width / 2) - 200, (height / 2) - 200, (width / 2) + 200, (height / 2) + 200);


        System.out.println("width----->" + w);
        System.out.println("width----->" + h);

//        setyEntery(yEntery, mPaint.getColor(), mPaint.getAlpha(), mPaint.getTextSize());
        lineGrapher(225);
        lineGrapher(85);
//        lineGrapher(25);
//        lineGrapher(25);
//        lineGrapher(15);
//        lineGrapher(15);
//        lineGrapher(15);


        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
        canvas.drawPath(mPath, mPaint2);

        canvas.drawCircle(width / 2, height / 2, width / 2, mPaint);

        canvas.drawText("Night\n& Day", (width / 2) - 200, height / 2, mPaint3);

        String texty = "<-----------it is Night !------------------>";
//        mPath.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawTextOnPath(texty, mPath, 20, 20, mPaint3);



        if (centerImg != null) {
//            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.img1), 0, 0, null);
            canvas.drawBitmap(centerImg, null, rect, mPaint1);
        }


//        for (float y : yEntery) {
//
//            xStart += plusConstant;
//            canvas.drawText(Float.toString(y), xStart - 20, (yBase - 40) - (multyConstant * y), mPaint);
//
//        }
//        xStart = 0;
//
//
    }


    public void clearDrawing() {


        mPath.reset();
        invalidate();


    }


    private void lineGrapher(float angle) {

        yStart = 105;
//        yBase = 450;
        xStart = 105;

        yEnd = height - 105;
        xEnd = width - 105;

        System.out.println("startAngle---->" + startAngle + 5);


        mPath.addArc(xStart, yStart, xEnd, yEnd, startAngle + 5, angle - 5);

        startAngle += angle;


    }
}



