package com.br.mreza.musicplayer.newdesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.BlurDecor;

import mbn.libs.fragmanager.CropCenterDrawable;


public class PlayerPageDecoration extends BlurDecor {


//    private BaseTaskObject.ResultReceiver resultReceiver = new BaseTaskObject.ResultReceiver() {
//        @Override
//        public void onResult(BaseTaskObject.ResultObject result, BaseTaskObject.InfoObject info) {
//            itemBlurBitmap = ((Result) result).getBitmap();
//        }
//    };

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


    protected View topMovingView;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @ColorInt
    protected int almostWhite = Color.argb(230, 255, 255, 255);
    private boolean receiverTouch = false;
    private boolean canIntercept = false;
    protected RecyclerView parentView;
    private Bitmap itemBlurBitmap;


    public boolean canIntercept() {
//        Log.i(TAG, "canIntercept: " + canIntercept);
        return false;
    }

    public PlayerPageDecoration(Context context, View topMovingView, RecyclerView recyclerView) {
        super(context);
        this.parentView = recyclerView;
        this.topMovingView = topMovingView;
        topMovingView.setClickable(false);
        topMovingView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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

        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(getDensity() * 3);
//        paint.setShadowLayer(15 * getDensity(), 0, 0, Color.LTGRAY);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        doTheDrawing(c, parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!shouldMove(parent)) {
//            c.drawLine(25 * getDensity(), topMovingView.getHeight(), c.getWidth() - (25 * getDensity()), topMovingView.getHeight(), paint);
            mPath.rewind();
            mPath.addRect(0, 0, c.getWidth(), topMovingView.getHeight(), Path.Direction.CCW);
            c.save();
            c.clipPath(mPath);
            doTheScaling(c, parent);
            c.restore();
        } else {
            c.scale(0, 0);
        }
    }

    @Override
    protected void doTheDrawing(Canvas canvas, RecyclerView parent) {
        mPath.rewind();
        if (shouldMove(parent)) {
            topMovingView.setTranslationY(parent.getChildAt(0).getTop() - topMovingView.getHeight());
//            if (topMovingView.getTranslationZ() != 0) {
//                topMovingView.animate().translationZ(0 * getDensity()).setDuration(250);
//            }
            topMovingView.setElevation(0);
            receiverTouch = false;
            mPath.addRect(0, parent.getChildAt(0).getTop() - topMovingView.getHeight(), parent.getWidth(), parent.getHeight(), Path.Direction.CCW);
        } else {
            topMovingView.setTranslationY(0);
            topMovingView.setElevation(3 * getDensity());
//            if (topMovingView.getTranslationZ() == 0) {
//                topMovingView.animate().translationZ(3 * getDensity()).setDuration(250);
//            }
            receiverTouch = true;
            mPath.addRect(0, 0, parent.getWidth(), parent.getHeight(), Path.Direction.CCW);
        }

        if (imageBitmap != null) {
            canvas.save();
            canvas.clipPath(mPath);
            doTheScaling(canvas, parent);
            canvas.restore();
        }
    }


    @Override
    protected void doTheScaling(Canvas canvas, RecyclerView parent) {
        if (imageBitmap != null) {
//            int screenWidth = CropCenterDrawable.getScreenW();
            int screenHeight = canvas.getHeight();

            float scale = Math.max((float) parent.getWidth() / imageBitmap.getWidth(), (float) screenHeight / imageBitmap.getHeight());

            int diffX = (int) (((scale * imageBitmap.getWidth()) - parent.getWidth()) / 2);
            int diffY = (int) (((scale * imageBitmap.getHeight()) - parent.getHeight()) / 2);

            int extraBottom = (int) ((scale * imageBitmap.getHeight()) - (canvas.getHeight() + diffY));
//        Log.i("MBN", scale + "___" + diffX + "___" + diffY + "___" + imageBitmap.getHeight() + "___" + parent.getHeight() + "___" + imageBitmap.getHeight() * scale);

            Rect rect = new Rect(-diffX, -diffY, parent.getWidth() + diffX, parent.getHeight() + extraBottom + diffY);

            canvas.drawBitmap(imageBitmap, null, rect, null);
//            canvas.drawColor(Color.argb(210, 255, 255, 255));

        }
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

    protected boolean shouldMove(RecyclerView parent) {
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


//    private class Result extends BaseTaskObject.ResultObject {
//        private Bitmap bitmap;
//
//        Result(Bitmap bitmap) {
//            this.bitmap = bitmap;
//        }
//
//        public Bitmap getBitmap() {
//            return bitmap;
//        }
//    }
//
//    private class ItemBlurTask extends BaseTaskObject {
//        public ItemBlurTask(ResultReceiver resultReceiver, Info infoObject) {
//            super(null, resultReceiver, infoObject);
//        }
//
//
//        @Override
//        public ResultObject onRun() {
//            return new Result(createBlur(((Info) getInfoObject()).getBitmap()));
//        }
//
//        private Bitmap createBlur(Bitmap bitmap) {
//
//            float bigOne = 200f / Math.max(bitmap.getWidth(), bitmap.getHeight());
//
//            int width = (int) (bitmap.getWidth() * bigOne);
//            int height = (int) (bitmap.getHeight() * bigOne);
//
////        int width = bitmap.getWidth();
////        int height = bitmap.getHeight();
//
//            Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            Canvas cany = new Canvas(bity);
//            cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
//
//            int[] allTogether = new int[width * height];
//
//            bity.getPixels(allTogether, 0, width, 0, 0, width, height);
//
//            int oneIndex;
//            int threeIndex;
//            int forIndex;
//            int fiveIndex;
//            int twoIndex;
//            int one;
//            int two;
//            int three;
//            int four;
//            int five;
//            int six;
//            int seven;
//            int eight;
//            int nine;
//            int total;
//            int r1;
//            int r2;
//            int r3;
//            int r4;
//            int r5;
//            int r6;
//            int r7;
//            int r8;
//            int r9;
//            int rt;
//            int b1;
//            int b2;
//            int b3;
//            int b4;
//            int b5;
//            int b6;
//            int b7;
//            int b8;
//            int b9;
//            int bt;
//            int g1;
//            int g2;
//            int g3;
//            int g4;
//            int g5;
//            int g6;
//            int g7;
//            int g8;
//            int g9;
//            int gt;
//            int a1;
//            int a2;
//            int a3;
//            int a4;
//            int a5;
//            int a6;
//            int a7;
//            int a8;
//            int a9;
//            int at;
//
//            for (int x = 0; x < 3; x++) {
//
//                for (int i = 0; i < allTogether.length; i++) {
//                    if ((i + 1) > width && (i + 1) % width > 1 && (i + 1) / width < height - 1) {
//
//                        oneIndex = i;
//                        try {
//
//                            twoIndex = oneIndex - 1;
//                            threeIndex = oneIndex + 1;
//                            forIndex = oneIndex - width;
//                            fiveIndex = oneIndex + width;
//
//                            one = allTogether[oneIndex];
//                            r1 = Color.red(one);
//                            b1 = Color.blue(one);
//                            g1 = Color.green(one);
//                            a1 = Color.alpha(one);
//
//                            two = allTogether[twoIndex];
//                            r2 = Color.red(two);
//                            b2 = Color.blue(two);
//                            g2 = Color.green(two);
//                            a2 = Color.alpha(two);
//
//                            three = allTogether[threeIndex];
//                            r3 = Color.red(three);
//                            b3 = Color.blue(three);
//                            g3 = Color.green(three);
//                            a3 = Color.alpha(three);
//
//                            four = allTogether[forIndex];
//                            r4 = Color.red(four);
//                            b4 = Color.blue(four);
//                            g4 = Color.green(four);
//                            a4 = Color.alpha(four);
//
//                            six = allTogether[forIndex + 1];
//                            r6 = Color.red(six);
//                            b6 = Color.blue(six);
//                            g6 = Color.green(six);
//                            a6 = Color.alpha(six);
//
//                            seven = allTogether[forIndex - 1];
//                            r7 = Color.red(seven);
//                            b7 = Color.blue(seven);
//                            g7 = Color.green(seven);
//                            a7 = Color.alpha(seven);
//
//                            five = allTogether[fiveIndex];
//                            r5 = Color.red(five);
//                            b5 = Color.blue(five);
//                            g5 = Color.green(five);
//                            a5 = Color.alpha(five);
//
//                            eight = allTogether[fiveIndex - 1];
//                            r8 = Color.red(eight);
//                            b8 = Color.blue(eight);
//                            g8 = Color.green(eight);
//                            a8 = Color.alpha(eight);
//
//                            nine = allTogether[fiveIndex + 1];
//                            r9 = Color.red(nine);
//                            b9 = Color.blue(nine);
//                            g9 = Color.green(nine);
//                            a9 = Color.alpha(nine);
//
//                            rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
//                            bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
//                            gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
//                            at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;
//
//                            total = Color.argb(at, rt, gt, bt);
//
//                            allTogether[oneIndex] = total;
//                            allTogether[twoIndex] = total;
//                            allTogether[threeIndex] = total;
//                            allTogether[forIndex] = total;
//                            allTogether[fiveIndex] = total;
//                            allTogether[fiveIndex - 1] = total;
//                            allTogether[fiveIndex + 1] = total;
//                            allTogether[forIndex - 1] = total;
//                            allTogether[forIndex + 1] = total;
//
//
//                        } catch (Exception ignored) {
//                        }
//                    }
//                }
//            }
//            return Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);
//        }
//
//    }


}
