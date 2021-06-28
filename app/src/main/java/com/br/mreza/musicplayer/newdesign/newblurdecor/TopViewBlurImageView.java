package com.br.mreza.musicplayer.newdesign.newblurdecor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import mbn.libs.fragmanager.CropCenterDrawable;


public class TopViewBlurImageView extends View {

    private Bitmap backGroundBitmap;
    private Bitmap itemBlurBitmap;
    private int screenWidth;
    private int screenHeight;

    Rect rect = new Rect();

    public TopViewBlurImageView(Context context) {
        super(context);
    }

    public TopViewBlurImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopViewBlurImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TopViewBlurImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = CropCenterDrawable.getScreenW();
        screenHeight = CropCenterDrawable.getScreenH();
    }

    public void setBackGroundBitmap(Bitmap backGroundBitmap) {
        this.backGroundBitmap = backGroundBitmap;
        invalidate();
    }

    public void setItemBlurBitmap(Bitmap itemBlurBitmap) {
        this.itemBlurBitmap = itemBlurBitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (backGroundBitmap != null) {

            screenWidth = CropCenterDrawable.getScreenW();
            screenHeight = CropCenterDrawable.getScreenH();

            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();


            float factor = Math.max(((float) screenWidth) / backGroundBitmap.getWidth(), ((float) screenHeight) / backGroundBitmap.getHeight());

            int useW = (int) (factor * backGroundBitmap.getWidth());
            int useH = (int) (factor * backGroundBitmap.getHeight());

            int diffHalfWidth = (useW - screenWidth) / 2;
//            int diffHalfHeight = (useH - canvasHeight) / 2;

//            int extraBottom = useH - (canvasHeight + diffHalfHeight);


            rect.set(-diffHalfWidth, 0, canvasWidth + diffHalfWidth, useH);

            canvas.drawBitmap(backGroundBitmap, null, rect, null);

        }

        if (itemBlurBitmap != null) {
            rect.set(0, 0, screenWidth, getResources().getDisplayMetrics().heightPixels);
            canvas.drawBitmap(itemBlurBitmap, null, rect, null);
        }

    }
}
