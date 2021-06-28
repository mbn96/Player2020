package com.br.mreza.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Process;
import androidx.palette.graphics.Palette;
import android.util.Log;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;


public abstract class AlbumArtTaskForSeekbarColor extends AlbumArtLoaderTask {


    private Bitmap mBitmap;
    private Palette mPalette;
    private boolean hasArt = true;
    private byte[] coverArray;


    public AlbumArtTaskForSeekbarColor(Context context, String data, int id) {
        super(context, data, id);
    }


    abstract public void onFinish(Bitmap bitmap, String data);

    public void palette(Palette palette) {
    }

    @Override
    void finish() {
        onFinish(mBitmap, getmData());
        palette(mPalette);
    }

    @Override
    public void run() {
//        mThread = Thread.currentThread();
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        if (mBitmap == null) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getmData());
                coverArray = retriever.getEmbeddedPicture();
                retriever.release();


                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inJustDecodeBounds = true;

                BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);

                int max = Math.max(options.outHeight, options.outWidth);

                Log.i("MAX", String.valueOf(max));

                options.inSampleSize = max / 200;
                options.inJustDecodeBounds = false;

                mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options));
            } catch (Exception e) {
                BitmapFactory.Options ops = new BitmapFactory.Options();
                ops.inMutable = true;
                mBitmap = MbnUtils.getGreyBackground(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.ic_note_png, ops));
                hasArt = false;
//            mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.night_rain_1));
            }

            if (hasArt) {
                try {
                    mPalette = Palette.from(BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length)).generate();
                } catch (Exception ignored) {
                }
            } else {
                mPalette = Palette.from(mBitmap).generate();
            }

        }

        coverArray = null;
        super.run();

    }


}
