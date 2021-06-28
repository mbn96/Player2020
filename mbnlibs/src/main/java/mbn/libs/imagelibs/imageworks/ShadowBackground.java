package mbn.libs.imagelibs.imageworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;

import mbn.libs.backgroundtask.Disposal;

public class ShadowBackground extends ShadowMakerDrawable {

    private boolean onCapture = false;
    private View view;

    public ShadowBackground(@NonNull Context context, View view) {
        super(context);
        setScale(1);
        this.view = view;
    }

    public ShadowBackground(Context context, View view, int shadowAngle, int shadowLength, int color) {
        super(context, shadowAngle, shadowLength, color);
        setScale(1);
        this.view = view;
    }

    private Disposal<CaptureBitmap> bitmapDisposal = new Disposal<CaptureBitmap>(3) {
        @Override
        public boolean isOK(CaptureBitmap object) {
            return ((currentWidth / scale) == object.getWidth()) && ((currentHeight / scale) == object.getHeight());
        }

        @Override
        public CaptureBitmap createNew() {
            return new CaptureBitmap((int) (currentWidth / scale), (int) (currentHeight / scale), scale);
        }

        @Override
        public void recycleObject(CaptureBitmap object) {
            object.bitmap.recycle();
        }
    };

    @Override
    public void setBitmap(Bitmap bitmap) {
        currentWidth = bitmap.getWidth();
        currentHeight = bitmap.getHeight();
        edges = Effects.getPixels(bitmap);
//        edges = Effects.boxFiltering_withAlpha(edges, currentWidth, currentHeight, Effects.BOX_BLUR_KERNEL);
//        edges = Effects.boxFiltering_withAlpha(edges, currentWidth, currentHeight, Effects.EDGE_DETECT_KERNEL);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (onCapture) return;

        currentWidth = view.getWidth();
        currentHeight = view.getHeight();
        CaptureBitmap capBit = bitmapDisposal.acquire();
        int save = capBit.canvas.save();
        onCapture = true;
        capBit.emptyBitmap();
        view.draw(capBit.canvas);
        onCapture = false;
        capBit.canvas.restoreToCount(save);




        setBitmap(capBit.bitmap);
        bitmapDisposal.release(capBit);
        Bitmap out = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        super.draw(outCanvas);
        canvas.drawBitmap(out, 0, 0, null);


    }


    private class CaptureBitmap {
        private Bitmap bitmap;
        private Canvas canvas;
        private int[] emptyPixels;

        CaptureBitmap(int x, int y, float scale) {
            bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
            emptyPixels = new int[x * y];
            canvas = new Canvas(bitmap);
//            canvas.drawColor(Color.TRANSPARENT);
            canvas.scale(1 / scale, 1 / scale);
        }

        void emptyBitmap() {
            bitmap.setPixels(emptyPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        }

        public int getWidth() {
            return bitmap.getWidth();
        }

        public int getHeight() {
            return bitmap.getHeight();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public Canvas getCanvas() {
            return canvas;
        }
    }
}
