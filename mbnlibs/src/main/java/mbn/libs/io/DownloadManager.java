package mbn.libs.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import mbn.libs.backgroundtask.MultiThreadSequentialTasks;


public class DownloadManager extends MultiThreadSequentialTasks<DownloadManager.DownloadRequest, Boolean> implements InternetChecker.InternetListener {

    /*---------------------- static part ----------------------*/

    private static final DownloadManager INSTANCE = new DownloadManager(0, 4);

    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private static Message getMessage() {
        return Message.obtain(INSTANCE.handler);
    }

    public static void startManager(Context context) {
        INSTANCE.start(context);
    }

    public static int requestDownload(File dest, URL url, boolean progressReport) {
        if (isMainThread()) {
            return INSTANCE.sendDownloadRequest(dest, url, progressReport);
        } else {
            int id = INSTANCE.getNewId();
            Message message = getMessage();
            message.what = START_DL;
            message.arg1 = id;
            Bundle bundle = message.getData();
            bundle.putStringArray("INFO", new String[]{dest.getPath(), url.toString()});
            bundle.putBoolean("PROGRESS_REPORT", progressReport);
            message.setData(bundle);
            message.sendToTarget();
            return id;
        }
    }

    public static void cancelDownload(int id) {
        if (isMainThread()) {
            INSTANCE.cancel(id);
        } else {
            Message message = getMessage();
            message.what = CANCEL_DL;
            message.arg1 = id;
            message.sendToTarget();
        }
    }

    public static void setOptionalKey(int id, Object key) {
        INSTANCE.optionalKeys.put(id, key);
        if (isMainThread()) {
            INSTANCE.setInternalOptionalKey(id);
        } else {
            Message message = getMessage();
            message.what = SET_KEY;
            message.arg1 = id;
            message.sendToTarget();
        }
    }

    public static void registerCallback(int id, DownloadCallback callback) {
        if (isMainThread()) {
            INSTANCE.registerCallBack(id, callback);
        } else {
            Message message = getMessage();
            message.what = REG_CALLBACK;
            message.arg1 = id;
            int callid = INSTANCE.getNewCallbackId();
            message.arg2 = callid;
            INSTANCE.callbacksQueueFromOtherThreads.put(callid, callback);
            message.sendToTarget();
        }
    }

    public static void unregisterCallback(int id, DownloadCallback callback) {
        if (isMainThread()) {
            INSTANCE.unRegisterCallback(id, callback);
        } else {
            Message message = getMessage();
            message.what = UNREG_CALLBACK;
            message.arg1 = id;
            int callid = INSTANCE.getNewCallbackId();
            message.arg2 = callid;
            INSTANCE.callbacksQueueFromOtherThreads.put(callid, callback);
            message.sendToTarget();
        }
    }

    /* ---------------------- class --------------------------*/

    private static final int PROG_REPORT = 0;
    private static final int START_DL = 1;
    private static final int CANCEL_DL = 2;
    private static final int REG_CALLBACK = 3;
    private static final int UNREG_CALLBACK = 4;
    private static final int SET_KEY = 5;


    private final AtomicInteger downloadID = new AtomicInteger();
    private final AtomicInteger callbacksID = new AtomicInteger();
    private boolean isStarted = false;
    //    private boolean hasInternet = false;
//    private ConnectivityManager connectivityManager;
    private InternetChecker internetChecker;
//    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
//        @Override
//        public void onAvailable(Network network) {
//            checkForInternet();
//        }
//
//        @Override
//        public void onLost(Network network) {
//            checkForInternet();
//        }
//
//        @Override
//        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
//            checkForInternet();
//        }
//    };

    private SparseArray<DownloadListener> downloadListeners = new SparseArray<>();
    private SparseArray<CallbackHolder> callbackHolders = new SparseArray<>();
    private SparseArray<File> finishedDownloads = new SparseArray<>();
    @SuppressLint("UseSparseArrays")
    private Map<Integer, DownloadCallback> callbacksQueueFromOtherThreads = Collections.synchronizedMap(new HashMap<>());
    private Map<Integer, Object> optionalKeys = Collections.synchronizedMap(new HashMap<>());


    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PROG_REPORT:
                    CallbackHolder holder = callbackHolders.get(msg.arg1);
                    if (holder != null) {
                        holder.sendProgress(msg.arg2);
                    }
                    break;
                case START_DL:
                    String[] info = msg.getData().getStringArray("INFO");
                    URL url = IO_Utils.makeURL(info[1], null);
                    internalDownloadRequest(msg.arg1, new File(info[0]), url, msg.getData().getBoolean("PROGRESS_REPORT"));
                    break;
                case CANCEL_DL:
                    cancel(msg.arg1);
                    break;
                case SET_KEY:
                    setInternalOptionalKey(msg.arg1);
                    break;
                case REG_CALLBACK:
                    DownloadManager.this.registerCallBack(msg.arg1, callbacksQueueFromOtherThreads.remove(msg.arg2));
                    break;
                case UNREG_CALLBACK:
                    DownloadManager.this.unRegisterCallback(msg.arg1, callbacksQueueFromOtherThreads.remove(msg.arg2));
                    break;
            }
            return true;
        }
    });

//    private void checkForInternet() {
//        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//        if (info != null && info.isConnected()) {
//            hasInternet = true;
//            unPause();
//        } else {
//            hasInternet = false;
//            pause();
//        }
//    }

    @Override
    public void onInternetAvailable() {
        unPause();
    }

    @Override
    public void onInternetUnavailable() {
        pause();
    }

    private DownloadManager(int queueSize, int threadsCount) {
        super(queueSize, threadsCount, false);
    }

    private void start(Context context) {
        if (!isStarted) {
//            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkRequest.Builder builder = new NetworkRequest.Builder();
//            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
//            connectivityManager.registerNetworkCallback(builder.build(), networkCallback);


            internetChecker = new InternetChecker(this, context);
            isStarted = true;
        }
    }

    private void setInternalOptionalKey(int id) {
        CallbackHolder holder = callbackHolders.get(id);
        if (holder != null) {
            holder.setOptionalKey(optionalKeys.get(id));
        }
    }

    private synchronized int getNewId() {
        return downloadID.getAndIncrement();
    }

    private synchronized int getNewCallbackId() {
        return callbacksID.getAndIncrement();
    }

    private void internalDownloadRequest(int id, File dest, URL url, boolean progressReport) {
        if (!isStarted) {
            throw new RuntimeException("Download manager has not been started!");
        }
        DownloadListener downloadListener = new DownloadListener(id, this, progressReport);
        downloadListeners.put(id, downloadListener);
        sendRequest(new DownloadRequest(dest, url, downloadListener));
    }

    private int sendDownloadRequest(File dest, URL url, boolean progressReport) {
//        if (!isStarted) {
//            throw new RuntimeException("Download manager has not been started!");
//        }
        int id = getNewId();
        internalDownloadRequest(id, dest, url, progressReport);
        return id;
    }

    private void cancel(int id) {
        DownloadListener listener = downloadListeners.get(id);
        if (listener != null) listener.setCanceled(true);
    }

    private void registerCallBack(int id, DownloadCallback callback) {
        if (finishedDownloads.get(id) != null) {
            callback.onFinish(id, optionalKeys.get(id), finishedDownloads.get(id));
            return;
        }
        CallbackHolder holder = callbackHolders.get(id);
        if (holder == null) {
            holder = new CallbackHolder(id, optionalKeys.get(id));
            callbackHolders.put(id, holder);
        }
        holder.addCallback(callback);
    }

    private void unRegisterCallback(int id, DownloadCallback callback) {
        CallbackHolder holder = callbackHolders.get(id);
        if (holder != null) holder.removeCallback(callback);
    }

    private void sendProgress(int id, int progress) {
        Message message = handler.obtainMessage();
        message.what = PROG_REPORT;
        message.arg1 = id;
        message.arg2 = progress;
        message.sendToTarget();
    }


    @Override
    public Boolean neededProcess(DownloadRequest request) {
        return IO_Utils.download(request.url, null, request.destination, request.downLoadListener);
    }

    @Override
    public void onFinalResult(DownloadRequest request, Boolean result) {
        if (result) {
            finishedDownloads.put(request.downLoadListener.ID, request.destination);
            downloadListeners.remove(request.downLoadListener.ID);
            CallbackHolder holder = callbackHolders.get(request.downLoadListener.ID);
            if (holder != null) {
                holder.setFinish(request.destination);
                callbackHolders.remove(request.downLoadListener.ID);
            }
            return;
        }
        if (request.downLoadListener.isCanceled()) {
            downloadListeners.remove(request.downLoadListener.ID);
            optionalKeys.remove(request.downLoadListener.ID);
            CallbackHolder holder = callbackHolders.get(request.downLoadListener.ID);
            if (holder != null) {
                holder.onCanceled();
                callbackHolders.remove(request.downLoadListener.ID);
            }
        } else {
            sendRequest(request);
        }
    }


//    @Override
//    public void onResult(Object result, Object info) {
//        Object[] objects = (Object[]) result;
//        DownloadRequest request = (DownloadRequest) objects[0];
//        Boolean finalResult = (boolean) objects[1];
//        onFinalResult(request, finalResult);
//        workingThreads--;
//        nextRequest();
//    }

    static class DownloadRequest {
        private File destination;
        private URL url;
        private DownloadListener downLoadListener;

        DownloadRequest(File destination, URL url, DownloadListener downLoadListener) {
            this.destination = destination;
            this.url = url;
            this.downLoadListener = downLoadListener;
        }
    }

    private static class DownloadListener extends IO_Utils.DownloadListenerBase {

        private int ID;
        private DownloadManager downloadManager;
        private int minPercentSize;
        private int currentInternalVal;
        private boolean wantsProgress;

        public DownloadListener(int ID, DownloadManager downloadManager, boolean wantsProgress) {
            this.ID = ID;
            this.downloadManager = downloadManager;
            this.wantsProgress = wantsProgress;
        }

        @Override
        public void setLength(int length) {
            super.setLength(length);
            if (length > 100) {
                minPercentSize = length / 100;
            }
        }

        private void sendProgress() {
            downloadManager.sendProgress(ID, (int) (100 * (float) startPos / getLength()));
            currentInternalVal = 0;
        }

        @Override
        public void nextByte() {
            startPos++;
            if (minPercentSize > 0 && wantsProgress) {
                currentInternalVal++;
                if (currentInternalVal >= minPercentSize) sendProgress();
            }
        }
    }

    private static class CallbackHolder {
        private ArrayList<DownloadCallback> downloadCallbacks = new ArrayList<>();
        private int id;
        private Object optionalKey;

        CallbackHolder(int id, Object optionalKey) {
            this.id = id;
            this.optionalKey = optionalKey;
        }

        void setOptionalKey(Object optionalKey) {
            this.optionalKey = optionalKey;
        }

        private void addCallback(DownloadCallback callback) {
            if (!downloadCallbacks.contains(callback)) downloadCallbacks.add(callback);
        }

        private void removeCallback(DownloadCallback callback) {
            downloadCallbacks.remove(callback);
        }

        void sendProgress(int prog) {
            for (DownloadCallback c : downloadCallbacks) {
                c.onProgress(id, prog, optionalKey);
            }
        }

        void setFinish(File dest) {
            for (DownloadCallback c : downloadCallbacks) {
                c.onFinish(id, optionalKey, dest);
            }
        }

        void onCanceled() {
            for (DownloadCallback c : downloadCallbacks) {
                c.onCanceled(id, optionalKey);
            }
        }

    }

    public interface DownloadCallback {
        void onFinish(int id, Object optionalKey, File dest);

        void onCanceled(int id, Object optionalKey);

        void onStarted(int id, Object optionalKey);

        void onProgress(int id, int progress, Object optionalKey);
    }

    public static class DefaultDownloadCallback implements DownloadCallback{
        @Override
        public void onFinish(int id, Object optionalKey, File dest) {

        }

        @Override
        public void onCanceled(int id, Object optionalKey) {

        }

        @Override
        public void onStarted(int id, Object optionalKey) {

        }

        @Override
        public void onProgress(int id, int progress, Object optionalKey) {

        }
    }

}
