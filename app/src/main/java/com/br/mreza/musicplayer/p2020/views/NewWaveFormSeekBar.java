package com.br.mreza.musicplayer.p2020.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.nio.ShortBuffer;

public class NewWaveFormSeekBar extends View {

    private volatile long songID;
    private int barCount = 600;
    private float[] barsData = new float[barCount];
    private float density = getContext().getResources().getDisplayMetrics().density;
    private float maxBarHeight = (220 * density), barPadding = density, barWidth;

    private ShortBuffer buffer;
    private int samplesInBar;
    private int currentBar;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public NewWaveFormSeekBar(Context context) {
        super(context);
    }

    public NewWaveFormSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewWaveFormSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewWaveFormSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        prepare();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setSongID(long songID) {
        this.songID = songID;
        if (buffer != null) {
            buffer.clear();
        }
        currentBar = 0;
    }

    public long getSongID() {
        return songID;

    }

    public void setBarCount(int barCount) {
        this.barCount = barCount;
        prepare();

    }

    public int getBarCount() {
        return barCount;
    }

    private void prepare() {
        if (barsData != null && barsData.length != barCount) {
            barsData = new float[barCount];
        }
        barWidth = ((float) getWidth() / barCount) - barPadding;
        mPaint.setStrokeWidth(barWidth);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public synchronized void setSamplesInBar(int sampleRate, long duration, long songid) {
        if (songID == songid) {
            post(() -> {
                samplesInBar = (int) ((sampleRate * ((duration / 1_000_000) + 1)) / barCount);
                buffer = ShortBuffer.allocate(samplesInBar);
            });
        }
    }

    public void feedSamples(short[] samples, long sID) {
        if (songID == sID) {
            post(() -> {
                for (int i = 0; i < samples.length; i += 2) {
                    feedSample(samples[i]);
                }
            });
        }
    }

    private void feedSample(short sample) {
        buffer.put(sample);
        if (buffer.position() >= samplesInBar) {
            buffer.clear();
            float sum = 0;
            for (short s : buffer.array()) {
                sum += Math.abs(s);
            }
            barsData[currentBar] = (sum / samplesInBar) / Short.MAX_VALUE;
            currentBar++;
            buffer.clear();
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h;
        for (int i = 0; i < barCount; i++) {
            h = maxBarHeight * barsData[i];

//            canvas.drawLine(i * (barWidth + barPadding), getHeight() / 2f, i * (barWidth + barPadding), (getHeight() / 2f) - h, mPaint);

            mPaint.setStrokeWidth(h * 2);
            canvas.drawLine(i * (barWidth + barPadding), getHeight() / 2f, (i * (barWidth + barPadding)) + barWidth, (getHeight() / 2f), mPaint);

        }
    }
}
