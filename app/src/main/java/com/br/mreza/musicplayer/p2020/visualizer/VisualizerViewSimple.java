package com.br.mreza.musicplayer.p2020.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class VisualizerViewSimple extends View implements VisualizerManager.VisualizerListener {

    private static final String TAG = "VISUALIZER_VIEW";

    private int alpha = 0;
    protected Random random = new Random();

    public VisualizerViewSimple(Context context) {
        super(context);
    }

    public VisualizerViewSimple(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        VisualizerManager.addListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        VisualizerManager.removeListener(this);
    }

    @Override
    public void onData(byte[] bytes, int samplingRate) {

        int baseCount = (int) (bytes.length * 0.1f);

        float average = 0;
        for (int i = 0; i < baseCount; i++) {
            average += Math.abs(bytes[i]);
        }
        average = average / baseCount;

        alpha = (int) (255 * ((average * 20) / 128));

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);
        canvas.drawColor(Color.argb(alpha, 255, 255, 255));
//        canvas.drawColor(Color.argb(alpha, random.nextInt(155) + 100, random.nextInt(155) + 100, random.nextInt(155) + 100));
    }
}
