package com.br.mreza.musicplayer.p2020.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import com.br.mreza.musicplayer.p2020.visualizer.VisualizerView_Bars;

import mbn.libs.imagelibs.imageworks.MbnUtils;
import mbn.libs.utils.views.ProgressBar_base;

public class VisualizerProgressBar extends VisualizerView_Bars implements ProgressBar_base.MBN_ProgressBar {

    private float progress = 0;
    private int accentColor = Color.GRAY;
    private int accentColor_emptyBars = MbnUtils.alphaChanger(accentColor, 255 / 3);
    private Paint emptyBarsPaint;
    private Vibrator vibrator;

    public VisualizerProgressBar(Context context) {
        super(context);
        checkPaint();
    }

    @Override
    protected float emptyBarValue() {
        return 0.2f;
    }

    public VisualizerProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        checkPaint();
        setBarCaptures(1);
        setBarsNum(5);
    }

    private void checkPaint() {
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(accentColor);
        emptyBarsPaint = new Paint(paint);
        emptyBarsPaint.setColor(accentColor_emptyBars);
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        accentColor_emptyBars = MbnUtils.alphaChanger(accentColor, 255 / 3);
        paint.setColor(accentColor);
        emptyBarsPaint.setColor(accentColor_emptyBars);
        invalidate();
    }

    @Override
    public void setProgress(@FloatRange(from = 0f, to = 1f) float progress) {
        if (progress > 1) progress = 1f;
        if (progress < 0) progress = 0f;
        this.progress = progress;
        invalidate();

    }

    @Override
    protected boolean isReverse() {
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setBarMaxHeight((int) ((h / 2.5f) - (density * 2)));
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    protected void drawLines(Canvas canvas) {
        float fillEnd = getBarsData().size() * progress;
        int emptyStart = (int) (getBarsData().size() * progress);

//        float first = getBarsData().get(0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && first >= 0.7f) {
//            vibrator.vibrate(VibrationEffect.createOneShot((long) (50 * first), VibrationEffect.DEFAULT_AMPLITUDE));
//        }

        for (int i = emptyStart; i < getBarsData().size(); i++) {
            float bar = getBarsData().get(i);
            float yPosition = getHeight() / 2f;
            emptyBarsPaint.setStrokeWidth(getBarMaxHeight() * bar);
            canvas.save();
            canvas.translate(i * barTotalSpace, 0);
            canvas.drawLine(barBlankSide, yPosition, barWidth + barBlankSide, yPosition, emptyBarsPaint);
            canvas.restore();
        }

        int save = canvas.save();
        canvas.clipRect(0, 0, getWidth() * progress, getHeight());
        for (int i = 0; i < fillEnd; i++) {
            float bar = getBarsData().get(i);
            float yPosition = getHeight() / 2f;
            paint.setStrokeWidth(getBarMaxHeight() * bar);
            canvas.save();
            canvas.translate(i * barTotalSpace, 0);
            canvas.drawLine(barBlankSide, yPosition, barWidth + barBlankSide, yPosition, paint);
            canvas.restore();
        }
        canvas.restoreToCount(save);

//        for (int i = 0; i < getBarsData().size() * progress; i++) {
//            float bar = getBarsData().get(i);
//            float yPosition = getHeight() / 2f;
//            paint.setStrokeWidth(getBarMaxHeight() * bar);
//            canvas.save();
//            canvas.translate(i * barTotalSpace, 0);
//            canvas.drawLine(barBlankSide, yPosition, barWidth + barBlankSide, yPosition, paint);
//            canvas.restore();
//        }
    }
}
