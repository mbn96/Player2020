package com.br.mreza.musicplayer.newdesign;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.br.mreza.musicplayer.newdesign.materialtheme.pageDecorationMaterialTheme;

import mbn.libs.fragmanager.CropCenterDrawable;


public class BackgroundFullScreenImageView extends View implements pageDecorationMaterialTheme.HeightChangerView {

    protected Bitmap drawBitmap;
    private int screenWidth;
    private int screenHeight;


    private boolean isMaterial = false;

    protected Rect rect = new Rect();

    private String TAG = "back_image";

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

    public BackgroundFullScreenImageView(Context context) {
        super(context);
    }

    public BackgroundFullScreenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundFullScreenImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isMaterial) {
            screenWidth = CropCenterDrawable.getScreenW();
            screenHeight = CropCenterDrawable.getScreenH();
        } else {
//            screenWidth = w;
//            screenHeight = h;
        }
    }

    public void setMaterial(boolean material) {
        isMaterial = material;
    }

    @Override
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
        invalidate();
    }

    public void setImgBitmap(Bitmap bitmap) {
        drawBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawBitmap != null) {
            if (!isMaterial) {
                screenWidth = CropCenterDrawable.getScreenW();
                screenHeight = CropCenterDrawable.getScreenH();

                int canvasWidth = canvas.getWidth();
                int canvasHeight = canvas.getHeight();

                float factor = Math.max(((float) screenWidth) / drawBitmap.getWidth(), ((float) screenHeight) / drawBitmap.getHeight());

                int useW = (int) (factor * drawBitmap.getWidth());
                int useH = (int) (factor * drawBitmap.getHeight());

                int diffHalfWidth = (useW - screenWidth) / 2;
                int diffHalfHeight = (useH - canvasHeight) / 2;

                int extraBottom = useH - (canvasHeight + diffHalfHeight);

                rect.set(-diffHalfWidth, -diffHalfHeight, canvasWidth + diffHalfWidth, canvasHeight + diffHalfHeight + extraBottom);
                canvas.drawBitmap(drawBitmap, null, rect, null);

            } else {
                screenWidth = getWidth();
                screenHeight = getHeight();
                canvas.save();
                canvas.clipRect(0, 0, screenWidth, screenHeight);
                float factor = Math.max(((float) screenWidth) / drawBitmap.getWidth(), ((float) screenHeight) / drawBitmap.getHeight());

                int useW = (int) (factor * drawBitmap.getWidth());
                int useH = (int) (factor * drawBitmap.getHeight());

                int diffHalfWidth = (useW - screenWidth) / 2;
                int diffHalfHeight = (useH - screenHeight) / 2;

                rect.set(-diffHalfWidth, -diffHalfHeight, screenWidth + diffHalfWidth, screenHeight + diffHalfHeight);
                canvas.drawBitmap(drawBitmap, null, rect, null);
                canvas.restore();
            }
        }
    }
}
