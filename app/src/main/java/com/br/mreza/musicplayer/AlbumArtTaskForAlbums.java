package com.br.mreza.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;


public abstract class AlbumArtTaskForAlbums extends AlbumArtLoaderTask {

    private Bitmap mBitmap = null;


    public AlbumArtTaskForAlbums(Context context, String data, int viewWidth) {
        super(context, data, viewWidth);

    }

    protected abstract void onFinish(Bitmap bitmap, String data);

    @Override
    void finish() {

        onFinish(mBitmap, getmData());

    }


    @Override
    public void run() {


        if (getmData() != null) {

            mBitmap = makeSquare(BitmapFactory.decodeFile(getmData()));

        }
        if (mBitmap == null) {

//            Log.e("here", "bitmap done");
//            mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.night_rain_1);
            mBitmap = makeSquare(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.night_rain_1));

        }

        super.run();

    }

    @Override
    public int getWidth() {

        return 500;
    }

    @Override
    public int getHeight() {
        return getWidth();
    }


    private Bitmap makeSquare(Bitmap bitmap) {

        if (bitmap == null) return null;


        float rectW, rectH;

        float scale = Math.max((float) getWidth() / bitmap.getWidth(), (float) getHeight() / bitmap.getHeight());

        rectW = (bitmap.getWidth() * scale);
        rectH = (bitmap.getHeight() * scale);

//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.WHITE);
        Rect rect = new Rect();

        int xDiff = (int) ((rectW - getWidth()) / 2);
        int yDiff = (int) ((rectH - getHeight()) / 2);

        rect.left = -xDiff;
        rect.right = getWidth() + xDiff;
        rect.top = -yDiff;
        rect.bottom = getHeight() + yDiff;

//        Bitmap income = Bitmap.

        Bitmap outBitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 30, 30, paint);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

//        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(bitmap, null, rect, null);


        bitmap.recycle();
//        return bitmap;
        return outBitmap;
    }

}
