package com.br.mreza.musicplayer.MBN.customViews;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;


public class MbnPlayButton extends View {

    private int radios;
    private int test = 5;
    private int range;
    private int centerX;
    private int centerY;
    private Path mPath;
    private Path mPath2;
    private Paint mPaint;
    private ValueAnimator animator;
    private Point leftBottom;
    private Point leftTop;
    private boolean buttonMode = false;


    public MbnPlayButton(Context context) {
        super(context);
        radios = ((Math.min(getWidth(), getHeight())) / 2) - test;
        range = (Math.min(getWidth(), getHeight())) / 4;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        leftBottom = new Point();
        leftBottom.set(centerX - range, centerY + range);
        leftTop = new Point();
        leftTop.set(centerX - range, centerY - range);
        mPath = new Path();
        mPath2 = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.parseColor("#9bfff3"));
        mPath.rewind();
        mPath2.rewind();
        mPath2.addCircle(centerX, centerY, radios, Path.Direction.CW);
        startAnimation(buttonMode);
    }

    public MbnPlayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        radios = ((Math.min(getWidth(), getHeight())) / 2) - test;
        range = (Math.min(getWidth(), getHeight())) / 4;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        leftBottom = new Point();
        leftBottom.set(centerX - range, centerY + range);
        leftTop = new Point();
        leftTop.set(centerX - range, centerY - range);
        mPath = new Path();
        mPath2 = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.parseColor("#9bfff3"));
        mPath.rewind();
        mPath2.rewind();
        mPath2.addCircle(centerX, centerY, radios, Path.Direction.CW);
        startAnimation(buttonMode);
    }

    public MbnPlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radios = ((Math.min(getWidth(), getHeight())) / 2) - test;
        range = (Math.min(getWidth(), getHeight())) / 4;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        leftBottom = new Point();
        leftBottom.set(centerX - range, centerY + range);
        leftTop = new Point();
        leftTop.set(centerX - range, centerY - range);
        mPath = new Path();
        mPath2 = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.parseColor("#9bfff3"));
        mPath.rewind();
        mPath2.rewind();
        mPath2.addCircle(centerX, centerY, radios, Path.Direction.CW);
        startAnimation(buttonMode);
    }

    public MbnPlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        radios = ((Math.min(getWidth(), getHeight())) / 2) - test;
        range = (Math.min(getWidth(), getHeight())) / 4;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        leftBottom = new Point();
        leftBottom.set(centerX - range, centerY + range);
        leftTop = new Point();
        leftTop.set(centerX - range, centerY - range);
        mPath = new Path();
        mPath2 = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.parseColor("#9bfff3"));
        mPath.rewind();
        mPath2.rewind();
        mPath2.addCircle(centerX, centerY, radios, Path.Direction.CW);
        startAnimation(buttonMode);
    }


    public void setColor(int color) {

        mPaint.setColor(color);
//        mPaint.setShadowLayer(5, 0, 0, Color.WHITE);
        invalidate();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radios = ((Math.min(w, h)) / 2);
        range = (Math.min(w, h)) / 6;
        centerX = w / 2;
        centerY = h / 2;
        leftBottom = new Point();
        leftBottom.set(centerX - range, centerY + range);
        leftTop = new Point();
        leftTop.set(centerX - range, centerY - range);
        mPath = new Path();
        mPath2 = new Path();
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(5f);
//        mPaint.setColor(Color.parseColor("#9bfff3"));
        mPath.rewind();
        mPath2.rewind();
        mPath2.addCircle(centerX, centerY, radios, Path.Direction.CW);
        mPaint.setStyle(Paint.Style.FILL);
        startAnimation(buttonMode);

    }


    private void doTheWork(float value) {


        mPath.rewind();
        mPath2.rewind();
        mPath2.addCircle(centerX, centerY, radios - (2 * value), Path.Direction.CW);

        float extraX = (range / 2) * (1 - value);

        mPath.moveTo(leftBottom.x + extraX, leftBottom.y);
        mPath.lineTo(leftTop.x + extraX, leftTop.y);

        float topY = centerY - (value * range);
        float bottomY = centerY + (value * range);

        mPath.lineTo(centerX + range, topY);
        mPath.lineTo(centerX + range, bottomY);

        mPath.lineTo(leftBottom.x + extraX, leftBottom.y);

        mPath.op(mPath2, Path.Op.REVERSE_DIFFERENCE);

        invalidate();


    }

    @Override
    protected void onDraw(Canvas canvas) {

//        canvas.drawPath(mPath2, mPaint);

        canvas.drawPath(mPath, mPaint);

        super.onDraw(canvas);
    }

    public void setMode(boolean mode) {

        if (mode != buttonMode) {

            buttonMode = mode;

            startAnimation(mode);
        }
    }


    private void startAnimation(boolean mode) {

        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.cancel();
            animator = null;
        }

        float start;
        float end;

        if (mode) {
            start = 0f;
            end = 1f;
        } else {
            start = 1f;
            end = 0f;
        }

        animator = ValueAnimator.ofFloat(start, end).setDuration(500);
        if (mode) {
            animator.setInterpolator(new OvershootInterpolator(3f));
        } else {
            animator.setInterpolator(new AnticipateInterpolator(3f));
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

//                if ((float) animation.getAnimatedValue() >= 0) {

                doTheWork((Float) animation.getAnimatedValue());
//                }else {
//                    doTheWork(0);
//                }

            }
        });

        animator.start();

    }


}
