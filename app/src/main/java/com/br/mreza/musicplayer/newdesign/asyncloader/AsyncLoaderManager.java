package com.br.mreza.musicplayer.newdesign.asyncloader;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;

import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;

public class AsyncLoaderManager {
    public static final AsyncLoaderManager INSTANCE = new AsyncLoaderManager();

    private AsyncLoaderManager() {
    }


    private final Object LOCK = new Object();
    private boolean hasStarted = false;
    private Handler progressHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String title = getProgTitle(bundle);
            int[] values = getProgInfo(bundle);
            for (ProgressListener listener : progressListeners) {
                listener.onProgress(title, values);
            }
            return true;
        }
    });

    private ArrayList<ProgressListener> progressListeners = new ArrayList<>();
    private ArrayList<FinishListener> finishListeners = new ArrayList<>();


    public void registerProgressListener(ProgressListener progressListener) {
        if (!progressListeners.contains(progressListener)) progressListeners.add(progressListener);
    }

    public void unRigesterProgressListener(ProgressListener progressListener) {
        progressListeners.remove(progressListener);
    }

    public void registerFinishListener(FinishListener finishListener) {
        if (!finishListeners.contains(finishListener)) finishListeners.add(finishListener);
    }

    public void unRigesterFinishListener(FinishListener finishListener) {
        finishListeners.remove(finishListener);
    }

    void setHasStarted() {
        synchronized (LOCK) {
            this.hasStarted = true;
            notifyAll();
        }
    }

    public boolean isHasStarted() {
        synchronized (LOCK) {
            return hasStarted;
        }
    }

    private void onFinishRefreshing() {
        for (FinishListener fin : finishListeners) {
            fin.onFinish();
        }
    }

    public void refresh(Context context) {
        DataBaseManager.getManager().getTaskHolder().StartTask(new DataLoaderTask(context, progressHandler), new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                onFinishRefreshing();
            }
        });
    }


    public interface FinishListener {
        void onFinish();
    }

    public interface ProgressListener {
        void onProgress(String title, int[] values);
    }

    static Bundle getProgressBundle(String title, int[] info) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putIntArray("info", info);
        return bundle;
    }

    private static String getProgTitle(Bundle bundle) {
        return bundle.getString("title", "");
    }

    private static int[] getProgInfo(Bundle bundle) {
        return bundle.getIntArray("info");
    }
}
