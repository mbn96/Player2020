package com.br.mreza.musicplayer.MBN.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import java.util.concurrent.ThreadLocalRandom;


public class VisualizerNewDesign extends VisualizerView {

    private float[] barHeights = new float[45];
    private float density;
    //    private Path path = new Path();


    public VisualizerNewDesign(Context context) {
        super(context);
    }

    public VisualizerNewDesign(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAlpha(1f);
        p1.setAntiAlias(true);
        p1.setColor(Color.CYAN);
        p1.setStyle(Paint.Style.STROKE);
//        p1.setStrokeCap(Paint.Cap.ROUND);

        density = getResources().getDisplayMetrics().density;
    }

    public VisualizerNewDesign(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VisualizerNewDesign(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void updateFFT(byte[] bytes) {

//        Log.i("seeeeBBBB", String.valueOf(bytes.length));

        int segSize = bytes.length / 45;


        for (int i = 0; i < 45; i++) {
            barCalculator(i, segSize, bytes);
        }

//        empty = true;

//        for (byte b : bytes) {
//
//
//        }

//        for (float f : barHeights) {
//            if (f > 0) {
//                Log.i("seeeeeeee", String.valueOf(f));
//                empty = false;
//                break;
//            }
//        }

        invalidate();

    }

    private void barCalculator(int pos, int segSize, byte[] bytes) {
        int start = segSize * pos;
        int end;
        end = pos == 44 ? bytes.length : start + segSize;
        if (end < start) end = start;

        float sum = 0;

        if (end == start) {
            barHeights[pos] = bytes[start];
            return;
        }

        for (int i = start; i < end; i++) {
//            sum += Math.abs(bytes[i]);
            sum += bytes[i];
        }

        barHeights[pos] = sum / (end - start);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float barH;


//        path.rewind();
//        path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CCW);
//
//        canvas.save();
//        canvas.clipPath(path);
//
//        path.rewind();
//        path.addCircle(getWidth() / 2, getHeight() / 2,getWidth()/4, Path.Direction.CCW);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutPath(path);
//        }


        float margin = 3 * density;

        if (empty) {

            for (int i = 0; i < barHeights.length; i++) {
                p1.setStrokeWidth(1);
//            p1.setColor(colors[ThreadLocalRandom.current().nextInt(5)]);
                canvas.drawArc(margin * density, margin * density, getWidth() - (margin * density), getHeight() - (margin * density), (i * 8),
                        4, false, p1);
            }

            return;
        }

//        int start = ThreadLocalRandom.current().nextInt(360);

//         margin = (int) ((1 + ThreadLocalRandom.current().nextInt(5)) * barHeights[0] / 128);

        margin = (3 + (4 * ThreadLocalRandom.current().nextFloat()));


        for (int i = 0; i < barHeights.length; i++) {
            barH = barHeights[i] / 127;

            p1.setStrokeWidth(barH * 9 * density);
//            p1.setColor(colors[ThreadLocalRandom.current().nextInt(5)]);
            canvas.drawArc(margin * density, margin * density, getWidth() - (margin * density), getHeight() - (margin * density), (i * 8) - 90,
                    4, false, p1);
        }

//        canvas.restore();

    }

    public void setColor(int color) {
        p1.setColor(color);
    }
}
