package com.br.mreza.musicplayer;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class AlbumArtLoaderManager {


    public static AlbumArtLoaderManager sInstance;

    static {

        sInstance = new AlbumArtLoaderManager();

    }

    private Handler mHandler;

    private ThreadPoolExecutor threadPoolExecutor;

    private final BlockingQueue<Runnable> queue;


    private void createHandler() {

        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {

                AlbumArtLoaderTask loaderTask = (AlbumArtLoaderTask) msg.obj;

                loaderTask.finish();

                super.handleMessage(msg);
            }
        };
    }


    private AlbumArtLoaderManager() {
        createHandler();

        queue = new LinkedBlockingQueue<>();

        int cores = Runtime.getRuntime().availableProcessors();

//        Log.i("cores", String.valueOf(cores));

        if (cores > 2) {
            cores = 2;
        }


        threadPoolExecutor = new ThreadPoolExecutor(0, 2, 500, TimeUnit.MILLISECONDS, queue);

//        threadPoolExecutor.allowCoreThreadTimeOut(true);

    }


    public void execute(AlbumArtLoaderTask loaderTask) {

        threadPoolExecutor.execute(loaderTask);

    }

    public void finish(AlbumArtLoaderTask loaderTask) {

        mHandler.obtainMessage(0, loaderTask).sendToTarget();

    }


}
