package com.br.mreza.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Process;
import android.util.Log;

import com.br.mreza.musicplayer.BackgroundForUI.BitmapHolderMap;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;


public abstract class AlbumArtTaskForSongs extends AlbumArtLoaderTask {
    public static final BitmapHolderMap HOLDER_MAP = new BitmapHolderMap();

    private Bitmap mBitmap;


    public AlbumArtTaskForSongs(Context context, String data, int id) {
        super(context, data, id);
    }


    abstract public void onFinish(Bitmap bitmap, String data);

    @Override
    void finish() {
        onFinish(mBitmap, getmData());
    }

    @Override
    public void run() {
//        mThread = Thread.currentThread();
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        mBitmap = HOLDER_MAP.get(getWidth());

//        if (mBitmap == null) {
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(getmData());
//                byte[] coverArray = retriever.getEmbeddedPicture();
//                retriever.release();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//
//                options.inJustDecodeBounds = true;
//
//                BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);
//
//                int max = Math.max(options.outHeight, options.outWidth);
//
//                Log.i("MAX", String.valueOf(max));
//
//                options.inSampleSize = max / 200;
//                options.inJustDecodeBounds = false;
//
//                mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options));
//                coverArray = null;
//            } catch (Exception e) {
//                BitmapFactory.Options ops = new BitmapFactory.Options();
//                ops.inMutable = true;
//                mBitmap = MbnUtils.getGreyBackground(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.ic_note_png, ops));
//            }
//            HOLDER_MAP.put(getWidth(), mBitmap);
//        }

        if (mBitmap == null) {
            try {

                mBitmap = MbnUtils.roundedBitmap(DataBaseUtils.getSongArtwork(getWidth(), 300, false, null));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("FUCK", "run: " + DataBaseHolder.getInstance().getSongDAO().getSongArtworkStatus(getWidth()));
            }
            HOLDER_MAP.put(getWidth(), mBitmap);
        }

        super.run();

    }


}
