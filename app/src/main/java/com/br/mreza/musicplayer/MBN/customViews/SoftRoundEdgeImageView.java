package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class SoftRoundEdgeImageView extends View {


    private Bitmap bitmap;
    private Bitmap output;
    private Bitmap gradientBit;
    private float radiosForCornersTop;
    private float radiosForCornersBottom;
    //    private Background backy;

    public SoftRoundEdgeImageView(Context context) {
        super(context);
        initiate();
    }

    public SoftRoundEdgeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initiate();
    }

    public SoftRoundEdgeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initiate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SoftRoundEdgeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initiate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        getLayoutParams().height = w;
        requestLayout();

        initiate();
    }

    private void initiate() {

        float scaleForDen = getResources().getDisplayMetrics().density;
        radiosForCornersTop = 25 * scaleForDen;
        radiosForCornersBottom = 8 * scaleForDen;

//        setClipToOutline(true);


        try {
            makeTheGradient();
            rounder();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void makeTheGradient() {

        gradientBit = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(gradientBit);

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{Color.BLACK, Color.argb(0, 0, 0, 0)});
        gradientDrawable.setBounds(0, 0, getWidth(), getHeight() / 2);
        gradientDrawable.draw(canvas);

    }

    private void rounder() throws Exception {


//        try {
//            backy.cancel(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        backy = new Background();
//        backy.execute();

        setAlpha(0f);


        try {
            output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);


            float bitX, bitY;
            bitX = bitmap.getWidth();
            bitY = bitmap.getHeight();

//        float min = Math.min(bitX, bitX);
//
//        boolean xORy = min == bitX;

            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(0, 0, getWidth(), getHeight(), radiosForCornersTop, radiosForCornersTop, paint);
                canvas.drawRoundRect(0, getHeight() / 2, getWidth(), getHeight(), radiosForCornersBottom, radiosForCornersBottom, paint);
            } else {
                canvas.drawColor(Color.WHITE);
            }

            float factor = 1f;

            factor = Math.max(getWidth() / bitX, getHeight() / bitY);


//        while (bitX * factor < getWidth()) {
//
//            factor += 0.01f;
//
//        }
//
//        while (bitY * factor < getHeight()) {
//            factor += 0.01f;
//        }
//
//
//        while (bitX * factor > getWidth() && bitY * factor > getHeight()) {
//
//            factor -= 0.001f;
//
//        }


//        int counter = 0;
//
//        if (bitX <= getWidth() && bitY <= getWidth()) {
//
//            while (bitX * factor < getWidth() && bitY * factor < getHeight()) {
//
//                factor += 0.1f;
//
//
//            }
//
//        } else if (bitX <= getWidth()) {
//
//
//
//
//        } else if (bitY <= getHeight()) {
//
//
//        } else {
//
//            while (bitX * factor > getWidth() && bitY * factor > getHeight()) {
//
//                factor -= 0.001f;
//
//
//            }
//
//        }

            Rect rect = new Rect();
            bitX = bitX * factor;
            bitY = bitY * factor;

            int xDiff = (int) ((bitX - getWidth()) / 2);
            int yDiff = (int) ((bitY - getHeight()) / 2);

            rect.left = -xDiff;
            rect.right = getWidth() + xDiff;
            rect.top = -yDiff;
            rect.bottom = getHeight() + yDiff;


            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(bitmap, null, rect, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

//                canvas.drawBitmap(gradientBit, 0, getHeight() / 2, paint);
        } catch (Exception ignored) {

        }

        invalidate();
        animate().alpha(1f).setDuration(250).start();

//        output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//
//        float bitX, bitY;
//        bitX = bitmap.getWidth();
//        bitY = bitmap.getHeight();
//
////        float min = Math.min(bitX, bitX);
////
////        boolean xORy = min == bitX;
//
//        Paint paint = new Paint();
//
//        paint.setAntiAlias(true);
//        paint.setColor(Color.WHITE);
//        paint.setStyle(Paint.Style.FILL);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 30, 30, paint);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//
//        float factor = 1f;
//
//        factor = Math.max(getWidth() / bitX, getHeight() / bitY);
//
//
////        while (bitX * factor < getWidth()) {
////
////            factor += 0.01f;
////
////        }
////
////        while (bitY * factor < getHeight()) {
////            factor += 0.01f;
////        }
////
////
////        while (bitX * factor > getWidth() && bitY * factor > getHeight()) {
////
////            factor -= 0.001f;
////
////        }
//
//
////        int counter = 0;
////
////        if (bitX <= getWidth() && bitY <= getWidth()) {
////
////            while (bitX * factor < getWidth() && bitY * factor < getHeight()) {
////
////                factor += 0.1f;
////
////
////            }
////
////        } else if (bitX <= getWidth()) {
////
////
////
////
////        } else if (bitY <= getHeight()) {
////
////
////        } else {
////
////            while (bitX * factor > getWidth() && bitY * factor > getHeight()) {
////
////                factor -= 0.001f;
////
////
////            }
////
////        }
//
//        Rect rect = new Rect();
//        bitX = bitX * factor;
//        bitY = bitY * factor;
//
//        int xDiff = (int) ((bitX - getWidth()) / 2);
//        int yDiff = (int) ((bitY - getHeight()) / 2);
//
//        rect.left = -xDiff;
//        rect.right = getWidth() + xDiff;
//        rect.top = -yDiff;
//        rect.bottom = getHeight() + yDiff;
//
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//        canvas.drawBitmap(bitmap, null, rect, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//
//        canvas.drawBitmap(gradientBit, 0, getHeight() / 2, paint);


//        invalidate();
    }

    private class Background extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setAlpha(0f);

        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);


                float bitX, bitY;
                bitX = bitmap.getWidth();
                bitY = bitmap.getHeight();

//        float min = Math.min(bitX, bitX);
//
//        boolean xORy = min == bitX;

                Paint paint = new Paint();

                paint.setAntiAlias(true);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10, paint);
                } else {
                    canvas.drawColor(Color.WHITE);
                }

                float factor = 1f;

                factor = Math.max(getWidth() / bitX, getHeight() / bitY);


//        while (bitX * factor < getWidth()) {
//
//            factor += 0.01f;
//
//        }
//
//        while (bitY * factor < getHeight()) {
//            factor += 0.01f;
//        }
//
//
//        while (bitX * factor > getWidth() && bitY * factor > getHeight()) {
//
//            factor -= 0.001f;
//
//        }


//        int counter = 0;
//
//        if (bitX <= getWidth() && bitY <= getWidth()) {
//
//            while (bitX * factor < getWidth() && bitY * factor < getHeight()) {
//
//                factor += 0.1f;
//
//
//            }
//
//        } else if (bitX <= getWidth()) {
//
//
//
//
//        } else if (bitY <= getHeight()) {
//
//
//        } else {
//
//            while (bitX * factor > getWidth() && bitY * factor > getHeight()) {
//
//                factor -= 0.001f;
//
//
//            }
//
//        }

                Rect rect = new Rect();
                bitX = bitX * factor;
                bitY = bitY * factor;

                int xDiff = (int) ((bitX - getWidth()) / 2);
                int yDiff = (int) ((bitY - getHeight()) / 2);

                rect.left = -xDiff;
                rect.right = getWidth() + xDiff;
                rect.top = -yDiff;
                rect.bottom = getHeight() + yDiff;


                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                canvas.drawBitmap(bitmap, null, rect, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

//                canvas.drawBitmap(gradientBit, 0, getHeight() / 2, paint);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            invalidate();
            animate().alpha(1f).setDuration(250).start();
        }
    }

    public void setmBitmap(Bitmap mBitmap) {


        bitmap = mBitmap;

        try {
            rounder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (output != null) {

            canvas.drawBitmap(output, 0, 0, null);

        }


    }
}
