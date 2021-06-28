package com.br.mreza.musicplayer.newdesign.materialtheme;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.br.mreza.musicplayer.newdesign.BackgroundFullScreenImageView;


public class PlayerPageBackgroundMaterial extends BackgroundFullScreenImageView {

    private int height;

    public PlayerPageBackgroundMaterial(Context context) {
        super(context);
        init();
    }

    public PlayerPageBackgroundMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerPageBackgroundMaterial(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    public void setScreenHeight(int screenHeight) {
        super.setScreenHeight(screenHeight);
        height = screenHeight;
    }

    private void init() {
        setMaterial(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawBitmap != null) {
            int screenWidth = getWidth();
            int screenHeight = height;
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
