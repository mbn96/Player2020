package com.br.mreza.musicplayer.newdesign.newblurdecor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.BlurDecor;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.fragmanager.CropCenterDrawable;


public class NewBlurItemDecor extends BlurDecor {

//
//    private BaseTaskObject.ResultReceiver resultReceiver = new BaseTaskObject.ResultReceiver() {
//        @Override
//        public void onResult(BaseTaskObject.ResultObject result, BaseTaskObject.InfoObject info) {
//            topViewBlurImageView.setItemBlurBitmap(((Result) result).getBitmap());
//        }
//    };

    private BaseTaskHolder.ResultReceiver resultReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            topViewBlurImageView.setItemBlurBitmap((Bitmap) result);
        }
    };

//    private class Info extends BaseTaskObject.InfoObject {
//        private Bitmap bitmap;
//
//        public Info(Bitmap bitmap) {
//            this.bitmap = bitmap;
//        }
//
//        public Bitmap getBitmap() {
//            return bitmap;
//        }
//    }

    @Override
    public void setImageBitmap(Bitmap blurBitmap) {
        super.setImageBitmap(blurBitmap);
        topViewBlurImageView.setBackGroundBitmap(blurBitmap);
        pageBlurBack.setImgBitmap(blurBitmap);
    }

    private View topMovingView;
    private PageBlurBack pageBlurBack;
    private TopViewBlurImageView topViewBlurImageView;
    private boolean receiverTouch = false;
    private boolean canIntercept = false;
    private RecyclerView parentView;
    private ThreadManager threadManager = new ThreadManager();


    public boolean canIntercept() {
//        Log.i(TAG, "canIntercept: " + canIntercept);
        return false;
    }

    public NewBlurItemDecor(Context context, View topMovingView, final RecyclerView recyclerView, PageBlurBack pageBlurBack) {
        super(context);
        this.parentView = recyclerView;
        this.topMovingView = topMovingView;
        topViewBlurImageView = (TopViewBlurImageView) ((ViewGroup) topMovingView).getChildAt(0);
        this.pageBlurBack = pageBlurBack;
        topMovingView.setClickable(false);
        topMovingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        canIntercept = receiverTouch;
//                        Log.i(TAG, "onTouch: " + receiverTouch);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        canIntercept = false;
                        break;
                }


                return receiverTouch;
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (!shouldMove(parentView)) {
                    parentView.destroyDrawingCache();
//                    new ItemBlurTask(resultReceiver, parentView.getDrawingCache().copy(Bitmap.Config.ARGB_8888, false));
                    threadManager.getTaskHolder().StartTask(new ItemBlurTask(createSmallBit(parentView.getDrawingCache(false))), resultReceiver);
                }
                parentView.postDelayed(this, 15);
            }
        });

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        doTheDrawing(c, parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        parent.destroyDrawingCache();
//        parent.getDrawingCache(true);
        if (shouldMove(parent)) {
            c.scale(0, 0);

//            c.save();
//            c.clipPath(mPath);
//            doTheScaling(c, parent);
//            c.restore();
        }
    }

    @Override
    protected void doTheDrawing(Canvas canvas, RecyclerView parent) {
        mPath.rewind();
        if (shouldMove(parent)) {
            topMovingView.setTranslationY(parent.getChildAt(0).getTop() - topMovingView.getHeight());
//            topMovingView.setElevation(0);
            receiverTouch = false;
//            mPath.addRect(0, parent.getChildAt(0).getTop() - topMovingView.getHeight(), canvas.getWidth(), canvas.getHeight(), Path.Direction.CCW);
            pageBlurBack.setCropTop(parent.getChildAt(0).getTop() - topMovingView.getHeight());
            if (topViewBlurImageView.getVisibility() == View.VISIBLE)
                topViewBlurImageView.setVisibility(View.INVISIBLE);
        } else {
            topMovingView.setTranslationY(0);
//            topMovingView.setElevation(3 * getDensity());
            receiverTouch = true;
//            mPath.addRect(0, 0, canvas.getWidth(), canvas.getHeight(), Path.Direction.CCW);
//            mPath.addRect(0, topMovingView.getHeight(), canvas.getWidth(), canvas.getHeight(), Path.Direction.CCW);
            pageBlurBack.setCropTop(0);
            if (topViewBlurImageView.getVisibility() != View.VISIBLE)
                topViewBlurImageView.setVisibility(View.VISIBLE);
        }

//        if (imageBitmap != null) {
//            canvas.save();
//            canvas.clipPath(mPath);
////            doTheScaling(canvas, parent);
//            canvas.restore();
//        }
    }


    @Override
    protected void doTheScaling(Canvas canvas, RecyclerView parent) {

        int screenWidth = CropCenterDrawable.getScreenW();
        int screenHeight = CropCenterDrawable.getScreenH();

        float scale = Math.max((float) parent.getWidth() / imageBitmap.getWidth(), (float) screenHeight / imageBitmap.getHeight());

        int diffX = (int) (((scale * imageBitmap.getWidth()) - parent.getWidth()) / 2);
        int diffY = (int) (((scale * imageBitmap.getHeight()) - parent.getHeight()) / 2);

        int extraBottom = (int) ((scale * imageBitmap.getHeight()) - (canvas.getHeight() + diffY));
//        Log.i("MBN", scale + "___" + diffX + "___" + diffY + "___" + imageBitmap.getHeight() + "___" + parent.getHeight() + "___" + imageBitmap.getHeight() * scale);

        Rect rect = new Rect(-diffX, -diffY, parent.getWidth() + diffX, parent.getHeight() + extraBottom + diffY);

        canvas.drawBitmap(imageBitmap, null, rect, null);

    }


    protected void doTheScalingForItemBlur(Canvas canvas, RecyclerView parent, Bitmap imageBitmap) {

        int screenWidth = CropCenterDrawable.getScreenW();
        int screenHeight = CropCenterDrawable.getScreenH();

        float scale = Math.max((float) parent.getWidth() / imageBitmap.getWidth(), (float) screenHeight / imageBitmap.getHeight());

        int diffX = (int) (((scale * imageBitmap.getWidth()) - parent.getWidth()) / 2);
        int diffY = (int) (((scale * imageBitmap.getHeight()) - parent.getHeight()) / 2);

        int extraBottom = (int) ((scale * imageBitmap.getHeight()) - (canvas.getHeight() + diffY));
//        Log.i("MBN", scale + "___" + diffX + "___" + diffY + "___" + imageBitmap.getHeight() + "___" + parent.getHeight() + "___" + imageBitmap.getHeight() * scale);

        Rect rect = new Rect(-diffX, -diffY, parent.getWidth() + diffX, parent.getHeight() + extraBottom + diffY);

        canvas.drawBitmap(imageBitmap, null, rect, null);

    }

    private boolean shouldMove(RecyclerView parent) {
        return parent.getChildAdapterPosition(parent.getChildAt(0)) == 0 && parent.getChildAt(0).getTop() > topMovingView.getHeight();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getPaddingTop() != topMovingView.getHeight())
//            parent.setPadding(parent.getPaddingLeft(), topMovingView.getHeight(), parent.getPaddingRight(), parent.getPaddingBottom());
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = parent.getHeight();
        }
    }


    private Bitmap createSmallBit(Bitmap bitmap) {
        float bigOne = 150f / Math.max(bitmap.getWidth(), bitmap.getHeight());
        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
        return bity;
    }

    private class Result {
        private Bitmap bitmap;

        Result(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }

    private class ItemBlurTask implements BaseTaskHolder.BaseTask {

        private Bitmap inBitmap;

        ItemBlurTask(Bitmap inBitmap) {
            this.inBitmap = inBitmap;
        }

        @Override
        public Object onRun() {
            return MbnUtils.stackBlur(inBitmap, 4, true);
//            return MbnUtils.renderScriptBlur(parentView.getContext(), inBitmap, 3, true);
        }

        private Bitmap createBlur(Bitmap bitmap) {

            float bigOne = 200f / Math.max(bitmap.getWidth(), bitmap.getHeight());

            int width = (int) (bitmap.getWidth() * bigOne);
            int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

            Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas cany = new Canvas(bity);
            cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

            int[] allTogether = new int[width * height];

            bity.getPixels(allTogether, 0, width, 0, 0, width, height);

            int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex,
                    one, two, three, four, five, six, seven, eight, nine, total,
                    r1, r2, r3, r4, r5, r6, r7, r8, r9, rt,
                    b1, b2, b3, b4, b5, b6, b7, b8, b9, bt,
                    g1, g2, g3, g4, g5, g6, g7, g8, g9, gt,
                    a1, a2, a3, a4, a5, a6, a7, a8, a9, at;

            for (int x = 0; x < 3; x++) {

                for (int i = 0; i < allTogether.length; i++) {
                    if ((i + 1) > width && (i + 1) % width > 1 && (i + 1) / width < height - 1) {

                        oneIndex = i;
                        try {

                            twoIndex = oneIndex - 1;
                            threeIndex = oneIndex + 1;
                            forIndex = oneIndex - width;
                            fiveIndex = oneIndex + width;

                            one = allTogether[oneIndex];
                            r1 = Color.red(one);
                            b1 = Color.blue(one);
                            g1 = Color.green(one);
                            a1 = Color.alpha(one);

                            two = allTogether[twoIndex];
                            r2 = Color.red(two);
                            b2 = Color.blue(two);
                            g2 = Color.green(two);
                            a2 = Color.alpha(two);

                            three = allTogether[threeIndex];
                            r3 = Color.red(three);
                            b3 = Color.blue(three);
                            g3 = Color.green(three);
                            a3 = Color.alpha(three);

                            four = allTogether[forIndex];
                            r4 = Color.red(four);
                            b4 = Color.blue(four);
                            g4 = Color.green(four);
                            a4 = Color.alpha(four);

                            six = allTogether[forIndex + 1];
                            r6 = Color.red(six);
                            b6 = Color.blue(six);
                            g6 = Color.green(six);
                            a6 = Color.alpha(six);

                            seven = allTogether[forIndex - 1];
                            r7 = Color.red(seven);
                            b7 = Color.blue(seven);
                            g7 = Color.green(seven);
                            a7 = Color.alpha(seven);

                            five = allTogether[fiveIndex];
                            r5 = Color.red(five);
                            b5 = Color.blue(five);
                            g5 = Color.green(five);
                            a5 = Color.alpha(five);

                            eight = allTogether[fiveIndex - 1];
                            r8 = Color.red(eight);
                            b8 = Color.blue(eight);
                            g8 = Color.green(eight);
                            a8 = Color.alpha(eight);

                            nine = allTogether[fiveIndex + 1];
                            r9 = Color.red(nine);
                            b9 = Color.blue(nine);
                            g9 = Color.green(nine);
                            a9 = Color.alpha(nine);

                            rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
                            bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
                            gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
                            at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;

                            total = Color.argb(at, rt, gt, bt);

                            allTogether[oneIndex] = total;
                            allTogether[twoIndex] = total;
                            allTogether[threeIndex] = total;
                            allTogether[forIndex] = total;
                            allTogether[fiveIndex] = total;
                            allTogether[fiveIndex - 1] = total;
                            allTogether[fiveIndex + 1] = total;
                            allTogether[forIndex - 1] = total;
                            allTogether[forIndex + 1] = total;


                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            return Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);
        }


        @Override
        public Object getInfo() {
            return null;
        }
    }


}


