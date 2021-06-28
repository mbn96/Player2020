package com.br.mreza.musicplayer.MBN.customViews;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.NewService.MusicInfoHolder;

public class MbnNewDesignLinearSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private Paint paintForBoarder;
    private Paint paintForBackground;
    private Paint paintForProgress;


    private Path pathForBase;
    private Path pathForProgress;
    private Path pathForA_B = new Path();
    private Paint paintForProgress2;
    private Paint paintForA_B;

    private boolean showTime = false;
    private TextView popTimeText;


    private float densityForThis = getResources().getDisplayMetrics().density;
    private int scale = (int) (densityForThis * 10);

    private float drawProgressTo;

    private int color = Color.CYAN;

    public MbnNewDesignLinearSeekBar(Context context) {
        super(context);
        init();
    }

    public MbnNewDesignLinearSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MbnNewDesignLinearSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPopTimeText(TextView popTimeText) {
        this.popTimeText = popTimeText;
    }

    private void init() {


        setBackgroundColor(Color.TRANSPARENT);
//        setBackgroundColor(Color.argb(0, 0, 0, 0));

        pathForBase = new Path();
        pathForProgress = new Path();

        paintForBackground = new Paint();
        paintForBackground.setStrokeJoin(Paint.Join.ROUND);
        paintForBackground.setStrokeCap(Paint.Cap.ROUND);
        paintForBackground.setStyle(Paint.Style.STROKE);
        paintForBackground.setStrokeWidth(scale);
        paintForBackground.setColor(Color.argb(100, 100, 100, 100));
        paintForBackground.setAntiAlias(true);

        paintForProgress = new Paint();
        paintForProgress.setStrokeJoin(Paint.Join.ROUND);
        paintForProgress.setStrokeCap(Paint.Cap.ROUND);
        paintForProgress.setStyle(Paint.Style.STROKE);
        paintForProgress.setStrokeWidth(scale - (5 * densityForThis));
        paintForProgress.setColor(color);
        paintForProgress.setAntiAlias(true);

        paintForProgress2 = new Paint();
        paintForProgress2.setStrokeJoin(Paint.Join.ROUND);
        paintForProgress2.setStrokeCap(Paint.Cap.ROUND);
        paintForProgress2.setStyle(Paint.Style.STROKE);
        paintForProgress2.setStrokeWidth(scale - (3 * densityForThis));
        paintForProgress2.setColor(color);
        paintForProgress2.setAntiAlias(true);

        paintForBoarder = new Paint();
        paintForBoarder.setStrokeJoin(Paint.Join.ROUND);
        paintForBoarder.setStrokeCap(Paint.Cap.ROUND);
        paintForBoarder.setStyle(Paint.Style.STROKE);
        paintForBoarder.setStrokeWidth(scale - (1 * densityForThis));
        paintForBoarder.setColor(Color.argb(100, 255, 255, 255));
        paintForBoarder.setAntiAlias(true);

        paintForA_B = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForA_B.setColor(Color.GRAY);
        paintForA_B.setStyle(Paint.Style.STROKE);
        paintForA_B.setStrokeWidth(1 * densityForThis);
        paintForA_B.setTextSize(10 * densityForThis);

//        pathForA_B.moveTo(0, getHeight() / 2);
//        pathForA_B.lineTo(0, (getHeight() / 2) + (10 * densityForThis));


//        getInfo();
    }

    private void getInfo() {
//        int max = getMax();
//        int pr = getProgress();
        drawProgressTo = (getWidth() - (2 * scale)) * ((float) getProgress() / getMax());

        drawProgressTo += scale;
//        System.out.println(drawProgressTo);


//        if (drawProgressTo < scale) {
//            drawProgressTo += scale;
//        } else if (drawProgressTo > getWidth() - scale) {
//            drawProgressTo -= scale;
//        }

//        System.out.println(drawProgressTo);
//        System.out.println(scale);

//        System.out.println(drawProgressTo);

    }


    public void setColor(int color) {
        this.color = color;
        paintForProgress.setColor(color);
        paintForProgress2.setColor(color);
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

//        int width = getWidth();
//        int height = getHeight();

        getInfo();

        pathForBase.rewind();
        pathForProgress.rewind();

        pathForBase.moveTo(scale, getHeight() / 2);
        pathForBase.lineTo(getWidth() - scale, getHeight() / 2);

        pathForProgress.moveTo(scale, getHeight() / 2);
        pathForProgress.lineTo(drawProgressTo, getHeight() / 2);


        canvas.drawPath(pathForBase, paintForBackground);

        canvas.drawPath(pathForBase, paintForBoarder);

        checkForA_B(canvas);

        canvas.drawPath(pathForProgress, paintForProgress);

        if (showTime && popTimeText != null) drawTime();

//        canvas.drawLine(drawProgressTo, getHeight() / 2, drawProgressTo + scale, getHeight() / 2, paintForProgress2);


    }


    private void drawTime() {
        popTimeText.setText(MbnController.justCurrentTime(getProgress()));
    }

    private void animateTime() {
        if (showTime) {
            popTimeText.setVisibility(VISIBLE);
            popTimeText.setTranslationY(25 * densityForThis);
            popTimeText.animate().setDuration(800).translationY(0).alpha(1).setInterpolator(new OvershootInterpolator(4));
        } else {
            popTimeText.animate().alpha(0).setDuration(800);
        }
    }

//    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
//        @Override
//        public void onAnimationStart(Animator animation) {
//
//        }
//
//        @Override
//        public void onAnimationEnd(Animator animation) {
////            popTimeText.setVisibility(INVISIBLE);
//        }
//
//        @Override
//        public void onAnimationCancel(Animator animation) {
//
//        }
//
//        @Override
//        public void onAnimationRepeat(Animator animation) {
//
//        }
//    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                showTime = true;
                if (popTimeText != null) animateTime();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                showTime = false;
                if (popTimeText != null) animateTime();
                break;
        }


        return super.onTouchEvent(event);
    }

    private void checkForA_B(Canvas canvas) {
        pathForA_B.rewind();
        pathForA_B.moveTo(0, getHeight() / 2);
        pathForA_B.lineTo(0, (getHeight() / 2) + (10 * densityForThis));
        int size = getWidth() - (2 * scale);
        if (MusicInfoHolder.hasA() && MusicInfoHolder.getPosA() <= getMax()) {
            int place = (int) (size * (MusicInfoHolder.getPosA() / (float) getMax()));
            canvas.save();
            canvas.translate(place + scale, 0);
            canvas.drawPath(pathForA_B, paintForA_B);
            canvas.drawText("A", -3 * densityForThis, (getHeight() / 2) + (19 * densityForThis), paintForA_B);
            canvas.restore();
        }
        if (MusicInfoHolder.hasB() && MusicInfoHolder.getPosB() <= getMax()) {
            int place = (int) (size * (MusicInfoHolder.getPosB() / (float) getMax()));
            canvas.save();
            canvas.translate(place + scale, 0);
            canvas.drawPath(pathForA_B, paintForA_B);
            canvas.drawText("B", -3 * densityForThis, (getHeight() / 2) + (19 * densityForThis), paintForA_B);
            canvas.restore();
        }
    }

}
