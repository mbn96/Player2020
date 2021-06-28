package com.br.mreza.musicplayer.newdesign.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import mbn.libs.imagelibs.imageworks.MbnUtils;

public class DotProgressBar extends ProgressBar {

    private Path progressPath = new Path();
    private Path dotsPath = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DotProgressBar(Context context) {
        super(context);
        init();
    }

    public DotProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.LTGRAY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dotsPath.rewind();
        float dotRadios = h / 2f;
        float space = h / 4f;
        float eachDotSpace = h + space;
        int dotsCount = (int) (w / eachDotSpace);
        for (int i = 0; i < dotsCount; i++) {
            float x = (i * eachDotSpace) + dotRadios;
            dotsPath.addCircle(x, dotRadios, dotRadios, Path.Direction.CW);
        }
    }

    public void setAccentColor(int accentColor) {
        paint.setColor(accentColor);
        paint2.setColor(MbnUtils.alphaChanger(accentColor, 100));
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int progressPix = (int) (((float) getProgress() / getMax()) * getWidth());
        progressPath.rewind();
        progressPath.addRect(0, 0, progressPix, getHeight(), Path.Direction.CW);
        progressPath.op(dotsPath, Path.Op.INTERSECT);
        canvas.drawPath(dotsPath, paint2);
        canvas.drawPath(progressPath, paint);
    }
}
