package com.br.mreza.musicplayer.newdesign.materialtheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;


public class pageDecorationBlur_backup extends pageDecorationMaterialTheme {
    public pageDecorationBlur_backup(Context context, View topMovingView, RecyclerView recyclerView, HeightChangerView background) {
        super(context, topMovingView, recyclerView, background);
    }

    private boolean inCapture = false;
    private boolean inOverDraw = false;
    private Bitmap captureBitmap;
    private Bitmap blurBitmap;
    private Canvas captureCanvas;
    private Canvas blurCanvas;
    private Rect rect, blurRect;
    private StopException stopException = new StopException();
    private int alphaAccent;

    public static class StopException extends RuntimeException {
    }


    @Override
    public void setAccent(int accent) {
        super.setAccent(accent);
        alphaAccent = Color.argb(170, Color.red(this.accent), Color.green(this.accent), Color.blue(this.accent));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (inCapture) {
            throw stopException;
        } else if (!shouldMove(parent)) {
            checkBitmap();
            int saveCount = 0;
            try {
                inCapture = true;
                saveCount = captureCanvas.save();
                captureCanvas.drawColor(Color.WHITE);
                parent.draw(captureCanvas);
            } catch (StopException ignored) {
            } finally {
                inCapture = false;
                captureCanvas.restoreToCount(saveCount);
            }
            inOverDraw = true;
            super.onDrawOver(c, parent, state);
            inOverDraw = false;
        }
    }

    @Override
    protected void doTheScaling(Canvas canvas, RecyclerView parent) {
        if (inOverDraw) {
            blurCanvas.drawBitmap(captureBitmap, null, blurRect, null);
            canvas.drawBitmap(MbnUtils.stackBlur(blurBitmap, 10, true), null, rect, null);
            canvas.drawColor(alphaAccent);
        } else {
            super.doTheScaling(canvas, parent);
        }
    }

    private boolean shouldRemakeBitmap() {
        return !(captureBitmap.getHeight() == topMovingView.getHeight() && captureBitmap.getWidth() == topMovingView.getWidth());
    }

    private void checkBitmap() {
        if (captureBitmap == null || shouldRemakeBitmap()) {
            captureBitmap = Bitmap.createBitmap(topMovingView.getWidth(), topMovingView.getHeight(), Bitmap.Config.ARGB_8888);
            captureCanvas = new Canvas(captureBitmap);
            rect = new Rect(0, 0, topMovingView.getWidth(), topMovingView.getHeight());
            blurBitmap = MbnUtils.createSmallBit_Empty(captureBitmap, 180);
            blurCanvas = new Canvas(blurBitmap);
            blurRect = new Rect(0, 0, blurBitmap.getWidth(), blurBitmap.getHeight());
        }
    }
}
