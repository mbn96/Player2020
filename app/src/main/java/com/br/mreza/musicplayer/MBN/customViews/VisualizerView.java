package com.br.mreza.musicplayer.MBN.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class VisualizerView extends View {


    private static final float SEGMENT_SIZE = 100.f;
    private static final float BASS_SEGMENT_SIZE = 10.f / SEGMENT_SIZE;
    private static final float MID_SEGMENT_SIZE = 30.f / SEGMENT_SIZE;
    private static final float TREBLE_SEGMENT_SIZE = 60.f / SEGMENT_SIZE;

    private float bass;
    private float mid;
    private float treble;
    Visualizer mVisualizer;

    boolean empty = false;


    Paint p1;
    Paint p2;
    Paint p3;


    public VisualizerView(Context context) {
        super(context);
//        init();
        p1 = new Paint();
        p1.setColor(Color.RED);
//        p1.setAlpha(60);
        p2 = new Paint();
        p2.setColor(Color.MAGENTA);
        p3 = new Paint();
        p3.setColor(Color.CYAN);
        init(0);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        init();
        setAlpha(0f);

        p1 = new Paint();
        p1.setColor(Color.RED);
//        p1.setAlpha(60);
        p2 = new Paint();
        p2.setColor(Color.MAGENTA);
        p3 = new Paint();
        p3.setColor(Color.CYAN);

    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
    }


    private void init(int id) {

        try {

            if (mVisualizer != null) {
                mVisualizer.setEnabled(false);
//                mVisualizer.get
                mVisualizer.release();
            }

            mVisualizer = new Visualizer(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mVisualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
                mVisualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
            }

            // Set the size of the byte array returned for visualization
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0] * 4);
            // Whenever audio data is available, update the visualizer view
            mVisualizer.setDataCaptureListener(
                    new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer,
                                                          byte[] bytes, int samplingRate) {

                            try {
                                if (mVisualizer != null && mVisualizer.getEnabled()) {
                                    updateFFT(bytes);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // Do nothing, we are only interested in the FFT (aka fast Fourier transform)
                        }

                        public void onFftDataCapture(Visualizer visualizer,
                                                     byte[] bytes, int samplingRate) {
                            // If the Visualizer is ready and has data, send that data to the VisualizerView
//                            try {
//                                if (mVisualizer != null && mVisualizer.getEnabled()) {
//                                    updateFFT(bytes);
//
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            try {
                                if (mVisualizer != null && mVisualizer.getEnabled()) {
                                    updateFFT(bytes);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    Visualizer.getMaxCaptureRate(), false, true);

            // Start everything
            mVisualizer.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    public void mode(boolean mode) {
////
//        if (mode) {
//            active();
//        } else {
//            inActive();
//        }
//
//    }

    public void active(int id) {

        init(id);
        empty = false;
//        mVisualizer.setEnabled(true);

//        mVisualizer.release();

    }

    public void inActive() {


//            mVisualizer.setEnabled(false);
//
//            mVisualizer.release();

        closeHandler.sendEmptyMessage(0);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
        }
    }

    Handler closeHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (VisualizerView.this != null && mVisualizer != null) {
                mVisualizer.setEnabled(false);
                empty = true;
                invalidate();
            }
//            mVisualizer.release();
            return true;
        }
    });

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawColor(Color.RED);

        canvas.drawPaint(p1);


        for (DrawingCircle circle : circles) {


//            p2.setColor(circle.color);
//
//            canvas.drawCircle(circle.x, circle.y, circle.radios, p2);

            circle.gradientDrawable.draw(canvas);

        }

//        canvas.drawPaint(p2);
//        canvas.drawPaint(p3);

    }

    public void updateFFT(byte[] bytes) {
//        byte[] mBytes = bytes;

//        Log.i("siiiiiiiiiiiiiiiiii", String.valueOf(bytes.length));

        // Calculate average for bass segment
        float bassTotal = 0;
        for (int i = 0; i < bytes.length * BASS_SEGMENT_SIZE; i++) {
            bassTotal += Math.abs(bytes[i]);
        }
        bass = bassTotal / (bytes.length * BASS_SEGMENT_SIZE);

//        ArrayList<Byte> byteArrayList = new ArrayList<>();

        // Calculate average for mid segment

//        int counter = 0;

        float midTotal = 0;
        for (int i = (int) (bytes.length * BASS_SEGMENT_SIZE); i < bytes.length * MID_SEGMENT_SIZE; i++) {
            midTotal += Math.abs(bytes[i]);
//            byteArrayList.add(bytes[i]);
//            counter++;

        }
        mid = midTotal / (bytes.length * MID_SEGMENT_SIZE);

        // Calculate average for treble segment

        float trebleTotal = 0;
        for (int i = (int) (bytes.length * MID_SEGMENT_SIZE); i < bytes.length; i++) {
            trebleTotal += Math.abs(bytes[i]);
        }
        treble = trebleTotal / (bytes.length * TREBLE_SEGMENT_SIZE);

        setAlpha(bass / 100);

        p1.setAlpha((int) ((255) * (bass / 70)));


        invalidator();

//        p2.setAlpha((int) ((255) * (treble / 128)));
//        p3.setAlpha((int) ((255) * (mid / 128)));

//        invalidate();
    }

    private void invalidator() {

        circles.clear();

        Random random = new Random();

        for (int i = 0; i < mid / 10; i++) {

            DrawingCircle circle = new DrawingCircle();

            circle.x = random.nextInt(getWidth());
            circle.y = random.nextInt(getHeight());
            circle.radios = random.nextInt(210) + 25;
            circle.color = colors[random.nextInt(5)];

            circle.bake();

            circles.add(circle);


        }

        invalidate();

    }

    private List<DrawingCircle> circles = new ArrayList<>();

    int[] colors = new int[]{Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.RED};

    private class DrawingCircle {


        private int x, y, radios, color;

        private GradientDrawable gradientDrawable;


        void bake() {

            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{color, Color.TRANSPARENT});

//            gradientDrawable.setShape(GradientDrawable.OVAL);

//            gradientDrawable.setGradientCenter(x, y);

            gradientDrawable.setGradientRadius(radios);

            gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);

            gradientDrawable.setBounds(x - radios, y - radios, x + radios, y + radios);


        }


    }

}
