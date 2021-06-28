package mbn.packfragmentmanager.fragmanager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class CropCenterDrawable extends Drawable {
    private static int screenW, screenH;

    public static int getScreenW() {
        return screenW;
    }

    public static int getScreenH() {
        return screenH;
    }

    private Bitmap bitmap;
    private boolean setScreenSize = false;

    public CropCenterDrawable(boolean setScreenSize) {
        this.setScreenSize = setScreenSize;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (setScreenSize) {
            screenW = canvas.getWidth();
            screenH = canvas.getHeight();
        }
        if (bitmap != null) {
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();


            float factor = Math.max(((float) canvasWidth) / bitmap.getWidth(), ((float) canvasHeight) / bitmap.getHeight());

            int useW = (int) (factor * bitmap.getWidth());
            int useH = (int) (factor * bitmap.getHeight());

            int diffHalfWidth = (useW - canvasWidth) / 2;
            int diffHalfHeight = (useH - canvasHeight) / 2;

            Rect rect = new Rect(-diffHalfWidth, -diffHalfHeight, canvasWidth + diffHalfWidth, canvasHeight + diffHalfHeight);

            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
