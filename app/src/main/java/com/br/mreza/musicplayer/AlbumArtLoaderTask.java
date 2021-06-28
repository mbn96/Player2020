package com.br.mreza.musicplayer;


import android.content.Context;

import androidx.annotation.Nullable;

abstract class AlbumArtLoaderTask implements Runnable {

//    private Bitmap mBitmap;

//    private Thread mThread;

    private Context mContext;

    private String mData;

    private int width, height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    Context getmContext() {
        return mContext;
    }

    String getmData() {
        return mData;
    }

    //    private Task mTask;




    AlbumArtLoaderTask(Context context, String data, int width) {
        mContext = context;
        mData = data;

        this.width = width;
        height = width * 2;

        AlbumArtLoaderManager.sInstance.execute(this);


    }


    abstract void finish();

    @Override
    public void run() {
        AlbumArtLoaderManager.sInstance.finish(this);
    }


    //    {
//
//        onFinish(mBitmap, mData);
//
//    }


//    public void set(String data) {
////        try {
////            if (mThread != null) {
//////                mThread.interrupt();
////            }
////        } catch (Exception ignored) {
////        }
//
//
//        AlbumArtLoaderManager.sInstance.execute(new Task(data, this));
//
//
//    }


//    @Override
//    public void run() {
////        mThread = Thread.currentThread();
//        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//        try {
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(mData);
//            byte[] coverArray = retriever.getEmbeddedPicture();
//            retriever.release();
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            options.inJustDecodeBounds = true;
//
//            BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);
//
//            int max = Math.max(options.outHeight, options.outWidth);
//
//            Log.i("MAX", String.valueOf(max));
//
//            options.inSampleSize = max / 200;
//            options.inJustDecodeBounds = false;
//
//            mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options));
//        } catch (Exception e) {
//
//            mBitmap = MbnUtils.getGreyBackground(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_note_png));
////            mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.night_rain_1));
//        }
//
//        AlbumArtLoaderManager.sInstance.finish(this);
//    }


//    class Task implements Runnable {
//
//        private String d;
//
//        private AlbumArtLoaderTask loaderTask;
//
//        public Task(String d, AlbumArtLoaderTask loaderTask) {
//            this.d = d;
//            this.loaderTask = loaderTask;
//        }
//
//        @Override
//        public void run() {
//
//
//            mThread = Thread.currentThread();
//            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(d);
//                byte[] coverArray = retriever.getEmbeddedPicture();
//                retriever.release();
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inSampleSize = scale;
//                mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length));
//            } catch (Exception e) {
//                mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.night_rain_1));
//            }
//
//            AlbumArtLoaderManager.sInstance.finish(loaderTask);
//
//        }
//    }

}
