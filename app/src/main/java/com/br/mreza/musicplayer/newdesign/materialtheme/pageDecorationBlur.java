package com.br.mreza.musicplayer.newdesign.materialtheme;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;


import java.util.LinkedList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.Disposal;
import mbn.libs.backgroundtask.MultiThreadSequentialTasks;
import mbn.libs.backgroundtask.ThreadManager;


public class pageDecorationBlur extends pageDecorationMaterialTheme implements BaseTaskHolder.ResultReceiver {

    public pageDecorationBlur(Context context, View topMovingView, RecyclerView recyclerView, HeightChangerView background) {
        super(context, topMovingView, recyclerView, background);
        imageView = (ImageView) ((ViewGroup) topMovingView).getChildAt(0);
//        this.recyclerView = recyclerView;
    }

    private boolean inCapture = false;
    private boolean inOverDraw = false;
    //    private CaptureBitmap inQueueBitmap;
    private LinkedList<CaptureBitmap> inQueueBitmapsLinkedList = new LinkedList<>();
    //    private Bitmap captureBitmap;
//    private Bitmap blurBitmap;
//    private Canvas captureCanvas;
//    private Canvas blurCanvas;
//    private Rect rect, blurRect;
    private StopException stopException = new StopException();
    private int alphaAccent;

    private boolean inBackground = false;
    private ThreadManager threadManager = new ThreadManager();
    //    private boolean inQueue = false;
//    private boolean inInvalidate = false;

    private ImageView imageView;
//    private RecyclerView recyclerView;

    private static class StopException extends RuntimeException {
    }


    private MultiThreadSequentialTasks<CaptureBitmap, Bitmap> engine = new MultiThreadSequentialTasks<CaptureBitmap, Bitmap>(1, 3, true) {
        @Override
        public Bitmap neededProcess(CaptureBitmap request) {
//            Bitmap out = MbnUtils.createSmallBit(request.getBitmap().copy(Bitmap.Config.ARGB_8888, true), 200);
//            Bitmap out = MbnUtils.stackBlur(request.getBitmap(), 20, false);
            Bitmap out = MbnUtils.stackBlur(MbnUtils.createSmallBit(request.getBitmap(), 180), 20, true);
            request.getCanvas().drawColor(Color.WHITE);
            return out;
        }

        @Override
        public void onFinalResult(CaptureBitmap request, Bitmap result) {
            bitmapSimplePool.release(request);
            imageView.setImageBitmap(result);
        }

        @Override
        public void outDatedRequest(CaptureBitmap request) {
            bitmapSimplePool.release(request);
        }
    };


    private Disposal<CaptureBitmap> bitmapSimplePool = new Disposal<CaptureBitmap>(15) {
        @Override
        public boolean isOK(CaptureBitmap object) {
            return !shouldRemakeBitmap(object);
        }

        @Override
        public CaptureBitmap createNew() {
            return new CaptureBitmap(topMovingView.getWidth() / 6, topMovingView.getHeight() / 6, 6);
        }

        @Override
        public void recycleObject(CaptureBitmap object) {
            object.bitmap.recycle();
        }
    };
//    private Pools.SimplePool<CaptureBitmap> bitmapSimplePool = new Pools.SimplePool<>(15);

    @Override
    public void setAccent(int accent) {
        super.setAccent(accent);
//        alphaAccent = MbnUtils.colorAlphaChanger(MbnUtils.colorWhitener2(accent, 30), 130);
        alphaAccent = MbnUtils.colorAlphaChanger(accent, 130);
//        alphaAccent = Color.argb(160, Color.red(this.accent), Color.green(this.accent), Color.blue(this.accent));
        imageView.setImageTintList(ColorStateList.valueOf(alphaAccent));
        gradientDrawable.setColors(new int[]{MbnUtils.colorAlphaChanger(accent, 60), MbnUtils.colorAlphaChanger(accent, 0)});
    }

    public void setAccent(int accent, int back) {
//        super.setAccent(back);
        this.accent = back;
//        alphaAccent = MbnUtils.colorAlphaChanger(MbnUtils.colorWhitener2(accent, 30), 130);
        alphaAccent = MbnUtils.colorAlphaChanger(back, 130);
//        alphaAccent = Color.argb(160, Color.red(this.accent), Color.green(this.accent), Color.blue(this.accent));
        imageView.setImageTintList(ColorStateList.valueOf(alphaAccent));
        gradientDrawable.setColors(new int[]{MbnUtils.colorAlphaChanger(accent, 60), MbnUtils.colorAlphaChanger(accent, 0)});
    }

    private void putInQueue(CaptureBitmap captureBitmap) {
        inQueueBitmapsLinkedList.add(captureBitmap);
        while (inQueueBitmapsLinkedList.size() > 3) {
            bitmapSimplePool.release(inQueueBitmapsLinkedList.poll());
        }
    }

    private Runnable captureRunnable = () -> {
        CaptureBitmap inQueueBitmap = bitmapSimplePool.acquire();
        Canvas captureCanvas = inQueueBitmap.getCanvas();
        int saveCount = 0;
        try {
            inCapture = true;
            saveCount = captureCanvas.save();
//            captureCanvas.drawColor(Color.WHITE);
            parentView.draw(captureCanvas);
        } catch (StopException ignored) {
        } finally {
            inCapture = false;
            captureCanvas.restoreToCount(saveCount);
            engine.sendRequest(inQueueBitmap);
        }
    };

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (inCapture) {
            throw stopException;
        } else if (!shouldMove(parent)) {
            imageView.setVisibility(View.VISIBLE);
            parent.post(captureRunnable);
//            parent.postDelayed(captureRunnable, 100);


                /*
                CaptureBitmap inQueueBitmap = bitmapSimplePool.acquire();
                Canvas captureCanvas = inQueueBitmap.getCanvas();
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
                    engine.sendRequest(inQueueBitmap);
//                putInQueue(inQueueBitmap);
//                if (!inBackground) {
//                    startIfNecessary();
//                }
                }
                */


            inOverDraw = true;
            super.onDrawOver(c, parent, state);
            inOverDraw = false;
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void doTheScaling(Canvas canvas, RecyclerView parent) {
        //noinspection StatementWithEmptyBody
        if (inOverDraw) {
//            blurCanvas.drawBitmap(captureBitmap, null, blurRect, null);
//            canvas.drawBitmap(MbnUtils.stackBlur(blurBitmap, 10, true), null, rect, null);
//            canvas.drawColor(alphaAccent);
        } else {
//            super.doTheScaling(canvas, parent);
            if (imageBitmap != null && inCapture) {
                int screenWidth = parent.getWidth();
                int screenHeight = parent.getHeight();
//                int screenHeight = shouldMove(parent) ? parent.getChildAt(0).getTop() - topMovingView.getHeight() : 0;

                float factor = Math.max(((float) screenWidth) / imageBitmap.getWidth(), ((float) screenHeight) / imageBitmap.getHeight());

                int useW = (int) (factor * imageBitmap.getWidth());
                int useH = (int) (factor * imageBitmap.getHeight());

                int diffHalfWidth = (useW - screenWidth) / 2;
                int diffHalfHeight = (useH - screenHeight) / 2;

                rect.set(-diffHalfWidth, -diffHalfHeight, screenWidth + diffHalfWidth, screenHeight + diffHalfHeight);
                canvas.drawBitmap(imageBitmap, null, rect, null);
                canvas.drawColor(MbnUtils.colorAlphaChanger(accent, 160));
            } else {
                canvas.drawColor(accent);
            }

        }
    }

    private boolean shouldRemakeBitmap(CaptureBitmap captureBitmap) {
        return !(captureBitmap.getHeight() == topMovingView.getHeight() && captureBitmap.getWidth() == topMovingView.getWidth());
    }

    private CaptureBitmap getBitmap() {
//        if (inQueueBitmap != null) {
//            bitmapSimplePool.release(inQueueBitmap);
//        }
        CaptureBitmap bitmap;
        while ((bitmap = bitmapSimplePool.acquire()) != null) {
            if (!shouldRemakeBitmap(bitmap)) return bitmap;
        }
        bitmap = new CaptureBitmap(topMovingView.getWidth() / 2, topMovingView.getHeight() / 2, 2);
        return bitmap;
    }

//    private void checkBitmap() {
//        if (captureBitmap == null || shouldRemakeBitmap()) {
//            captureBitmap = Bitmap.createBitmap(topMovingView.getWidth(), topMovingView.getHeight(), Bitmap.Config.ARGB_8888);
//            captureCanvas = new Canvas(captureBitmap);
////            rect = new Rect(0, 0, topMovingView.getWidth(), topMovingView.getHeight());
////            blurBitmap = MbnUtils.createSmallBit_Empty(captureBitmap, 180);
////            blurCanvas = new Canvas(blurBitmap);
////            blurRect = new Rect(0, 0, blurBitmap.getWidth(), blurBitmap.getHeight());
//        }
//    }

    private boolean startIfNecessary() {
        if (!inQueueBitmapsLinkedList.isEmpty()) {
            threadManager.getTaskHolder().StartTask(new BlurTask(), this);
            return true;
        }
        return false;
    }

    @Override
    public void onResult(Object result, Object info) {
        imageView.setImageBitmap((Bitmap) result);
        bitmapSimplePool.release((CaptureBitmap) info);
//        inInvalidate = true;
//        recyclerView.invalidateItemDecorations();

        if (!startIfNecessary()) {
            inBackground = false;
        }
    }


    private static class CaptureBitmap {
        private Bitmap bitmap;
        private Canvas canvas;

        CaptureBitmap(int x, int y, float scale) {
            bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.scale(1 / scale, 1 / scale);
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

    private class BlurTask implements BaseTaskHolder.BaseTask {

        private CaptureBitmap bitmap;

        BlurTask() {
            inBackground = true;
            bitmap = inQueueBitmapsLinkedList.poll();
//            inQueueBitmap = null;
        }

        @Override
        public Object onRun() {
            return MbnUtils.stackBlur(MbnUtils.createSmallBit(bitmap.getBitmap(), 180), 18, true);
        }

        @Override
        public Object getInfo() {
            return bitmap;
        }
    }

}
