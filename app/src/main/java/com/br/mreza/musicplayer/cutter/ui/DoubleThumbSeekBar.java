package com.br.mreza.musicplayer.cutter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DoubleThumbSeekBar extends View {

    private float startValue = 5;
    private float endValue = 35;
    private float currentFirstX;
    private int currentSelection;
    private int accentColor = Color.parseColor("#00b4ff");
    private float density = getResources().getDisplayMetrics().density;
    private int padding = (int) ((density * 5) + 0.5);
    private int useWidth;
    private Listener listener;
    private float playerPos;
    private boolean playing = false;

    private Path borderPath, trackPath;
    private Paint borderPaint, trackPaint, indicatorPaint, playerPaint;

    private String TAG = "DOUBLE_SEEK";

    public DoubleThumbSeekBar(Context context) {
        super(context);
        init();
    }

    public DoubleThumbSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoubleThumbSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DoubleThumbSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        borderPath = new Path();
        trackPath = new Path();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(density);

        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setColor(accentColor);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(7 * density);
        trackPaint.setStrokeCap(Paint.Cap.ROUND);

        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint.setColor(Color.GRAY);
        indicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        indicatorPaint.setStrokeWidth(density);

        playerPaint = new Paint(indicatorPaint);
        playerPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        useWidth = w - (2 * padding);

        borderPath.rewind();
        borderPath.addRoundRect(density, density, w - density, 11 * density, 5 * density, 5 * density, Path.Direction.CW);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (50 * density));
    }

    private int getStartX() {
        return (int) (((startValue / 100) * useWidth));
    }

    private int getEndX() {
        return (int) (((endValue / 100) * useWidth));
    }

    private int getPlayX() {
        return (int) (((playerPos / 100) * useWidth));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public float getStartValue() {
        return startValue;
    }

    public float getEndValue() {
        return endValue;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX() - padding;
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            currentFirstX = currentX;
            float diffS = Math.abs(currentX - getStartX());
            float diffE = Math.abs(currentX - getEndX());
            float result = Math.min(diffS, diffE);
            Log.i(TAG, "onTouchEvent: " + result + "  " + diffS + "  " + diffE);
            if (result == diffS) {
                currentSelection = 0;
            } else {
                currentSelection = 1;
            }
        }
        calculator(currentX);
        return true;
    }

    private void calculator(float x) {
        float calculatedValue = 0;
        if (x >= 0 && x <= useWidth) {
            calculatedValue = (x / useWidth) * 100;
        } else if (x > 0) {
            calculatedValue = 100;
        }

        if (currentSelection == 0) {
            if (calculatedValue <= endValue) {
                startValue = calculatedValue;
                listener.onChange();
                invalidate();
            }
        } else {
            if (calculatedValue >= startValue) {
                endValue = calculatedValue;
                listener.onChange();
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(borderPath, borderPaint);
        // --- start indicator --- //
        canvas.drawLine(getStartX() + padding, 6 * density, getStartX() + padding, 36 * density, indicatorPaint);
        canvas.drawCircle(getStartX() + padding, 36 * density, 5 * density, indicatorPaint);
        // --- end indicator --- //
        canvas.drawLine(getEndX() + padding, 6 * density, getEndX() + padding, 36 * density, indicatorPaint);
        canvas.drawCircle(getEndX() + padding, 36 * density, 5 * density, indicatorPaint);
        // --- player indicator --- //
        if (playing) {
            canvas.drawLine(getPlayX() + padding, 6 * density, getPlayX() + padding, 20 * density, playerPaint);
            canvas.drawCircle(getPlayX() + padding, 20 * density, 2 * density, playerPaint);
        }
        // ---- track ---//
        canvas.drawLine(getStartX() + padding, 6 * density, getEndX() + padding, 6 * density, trackPaint);
    }

    public void setPlayerPos(float playerPos) {
        this.playerPos = playerPos;
        invalidate();
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        invalidate();
    }

    public interface Listener {
        void onChange();
    }

}
