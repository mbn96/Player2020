package com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.br.mreza.musicplayer.R;


public class BlurDecor extends RecyclerView.ItemDecoration {

    protected Bitmap imageBitmap;


    protected Path mPath;
    private Paint paint;

    private int corner;
    private float density;

    public BlurDecor(Bitmap imageBitmap) {
        createBlur(imageBitmap);
        mPath = new Path();
    }

    public float getDensity() {
        return density;
    }

    @SuppressLint("ResourceType")
    public BlurDecor(Context context) {
        mPath = new Path();
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(context.getString(R.color.mbn_background_for_appbar_super_light)));

        density = context.getResources().getDisplayMetrics().density;
        corner = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }


    public void setImageBitmap(Bitmap blurBitmap) {
        imageBitmap = blurBitmap;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (imageBitmap != null) {
            doTheDrawing(c, parent);
        }

    }


    protected void doTheDrawing(Canvas canvas, RecyclerView parent) {
        canvas.save();
        mPath.rewind();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childAt = parent.getChildAt(i);
            mPath.addRoundRect(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom(), corner, corner, Path.Direction.CCW);
//            mPath.addRoundRect(childAt.getLeft() - (density), childAt.getTop() - (density), childAt.getRight() + (density), childAt.getBottom() + (density), corner, corner, Path.Direction.CCW);
        }
        canvas.clipPath(mPath);
        doTheScaling(canvas, parent);
        canvas.restore();
//        canvas.drawPath(mPath, paint);
//        mPath.rewind();
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            View childAt = parent.getChildAt(i);
//            mPath.addRoundRect(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom(), corner, corner, Path.Direction.CCW);
//        }
//        canvas.clipPath(mPath);
    }

    protected void doTheScaling(Canvas canvas, RecyclerView parent) {

//        if (StorageUtils.getThemeType() != 0) {
//            canvas.drawColor(Color.WHITE);
//            return;
//        }

        float scale = Math.max((float) parent.getWidth() / imageBitmap.getWidth(), (float) parent.getHeight() / imageBitmap.getHeight());

        int diffX = (int) ((parent.getWidth() - (scale * imageBitmap.getWidth())) / 2);
        int diffY = (int) ((parent.getHeight() - (scale * imageBitmap.getHeight())) / 2);

//        Log.i("MBN", scale + "___" + diffX + "___" + diffY + "___" + imageBitmap.getHeight() + "___" + parent.getHeight() + "___" + imageBitmap.getHeight() * scale);

        Rect rect = new Rect(diffX, diffY, parent.getWidth() - diffX, parent.getHeight() - diffY);

        canvas.drawBitmap(imageBitmap, null, rect, null);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            canvas.drawColor(parent.getContext().getColor(R.color.mbn_background_for_appbar_super_light));
////            canvas.drawPaint(paint);
//        } else {
//            canvas.drawColor(Color.parseColor(String.valueOf(R.color.mbn_background_for_appbar_super_light)));
//        }

    }

    private void createBlur(Bitmap bitmap) {

        float bigOne = 350f / Math.max(bitmap.getWidth(), bitmap.getHeight());

        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        int[] allTogether = new int[width * height];

        bity.getPixels(allTogether, 0, width, 0, 0, width, height);

        int oneIndex;
        int threeIndex;
        int forIndex;
        int fiveIndex;
        int twoIndex;
        int one;
        int two;
        int three;
        int four;
        int five;
        int six;
        int seven;
        int eight;
        int nine;
        int total;
        int r1;
        int r2;
        int r3;
        int r4;
        int r5;
        int r6;
        int r7;
        int r8;
        int r9;
        int rt;
        int b1;
        int b2;
        int b3;
        int b4;
        int b5;
        int b6;
        int b7;
        int b8;
        int b9;
        int bt;
        int g1;
        int g2;
        int g3;
        int g4;
        int g5;
        int g6;
        int g7;
        int g8;
        int g9;
        int gt;
        int a1;
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int at;

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


        imageBitmap = Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);


    }

}
