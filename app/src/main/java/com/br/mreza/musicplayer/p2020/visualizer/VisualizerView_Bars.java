package com.br.mreza.musicplayer.p2020.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class VisualizerView_Bars extends VisualizerViewSimple {

    private int barsNum = 300;
    private int barCaptures = 1;
    private int currentCapture;
    private FloatBuffer captureBuffer = FloatBuffer.allocate(barCaptures);
    protected float density = getResources().getDisplayMetrics().density;
    private int barMaxHeight = (int) (200 * density);
    private ArrayList<Float> barsData = new ArrayList<>();
    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected float barTotalSidePercentage = 0.2f;
    protected float barTotalSpace;
    protected float barWidth;
    protected float barBlankSide;


    public VisualizerView_Bars(Context context) {
        super(context);
        init();
    }

    public VisualizerView_Bars(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setBarCaptures(int barCaptures) {
        this.barCaptures = barCaptures;
        currentCapture = 0;
        captureBuffer = FloatBuffer.allocate(barCaptures);
    }

    public void setBarsNum(int barsNum) {
        this.barsNum = barsNum;
        prepareDataList();
        calculateSizes();
    }

    public int getBarsNum() {
        return barsNum;
    }

    public int getBarMaxHeight() {
        return barMaxHeight;
    }

    public void setBarMaxHeight(int barMaxHeight) {
        this.barMaxHeight = barMaxHeight;
    }

    public ArrayList<Float> getBarsData() {
        return barsData;
    }

    private void init() {
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        prepareDataList();
    }

    private void prepareDataList() {
        barsData.clear();
        for (int i = 0; i < barsNum; i++) {
            barsData.add(emptyBarValue());
        }
    }

    protected float emptyBarValue() {
        return 0;
    }

    protected float getDrawingLength() {
        return getWidth();
    }

    private void calculateSizes() {
        barTotalSpace = getDrawingLength() / (float) barsNum;
        barBlankSide = (barTotalSpace * barTotalSidePercentage) / 2;
        barWidth = barTotalSpace * (1 - barTotalSidePercentage);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateSizes();

        /*
        barTotalSpace = w / (float) barsNum;
        barBlankSide = (barTotalSpace * barTotalSidePercentage) / 2;
        barWidth = barTotalSpace * (1 - barTotalSidePercentage);
        */

    }

    protected boolean isReverse() {
        return true;
    }

    private void calculate() {
        float sum = 0;
        captureBuffer.clear();

        for (int i = 0; i < barCaptures; i++) {
            sum += captureBuffer.get();
        }
        sum = sum / barCaptures;

        if (isReverse()) {
            barsData.add(sum);
            barsData.remove(0);
        } else {
            barsData.add(0, sum);
            barsData.remove(barsData.size() - 1);
        }


        captureBuffer.clear();
        invalidate();
    }

    @Override
    public void onData(byte[] bytes, int samplingRate) {
        currentCapture++;

        int baseCount = (int) (bytes.length * 0.3f);
        float average = 0;
        for (int i = 0; i < baseCount; i++) {
            average += Math.abs(bytes[i]);
        }
        average = ((average * 20) / baseCount) / 128;
        captureBuffer.put(average);

        if (currentCapture >= barCaptures) {
            currentCapture = 0;
            calculate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLines(canvas);
    }

    protected void drawLines(Canvas canvas) {
        for (int i = 0; i < barsData.size(); i++) {
            float bar = barsData.get(i);
            float yPosition = getHeight() / 2f;
            paint.setStrokeWidth(barMaxHeight * bar);
            canvas.save();
            canvas.translate(i * barTotalSpace, 0);
            canvas.drawLine(barBlankSide, yPosition, barWidth + barBlankSide, yPosition, paint);
            canvas.restore();
        }
    }

}
