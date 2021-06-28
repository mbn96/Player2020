package mbn.libs.io;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import mbn.libs.backgroundtask.MultiThreadSequentialTasks;


public abstract class SmallFilesDLManager<K, R> {

    private HashMap<K, R> results = new HashMap<>();
    private ArrayList<K> inProgress = new ArrayList<>();
    private MultiThreadSequentialTasks<DlRequest, R> engine = new MultiThreadSequentialTasks<DlRequest, R>(0, 5, false) {
        @Override
        public R neededProcess(DlRequest request) {
            byte[] bytes = IO_Utils.downloadToByteArray(request.url, null);
            if (bytes == null) {
                request.dlDone = false;
                return null;
            }
            request.dlDone = true;
            return onDownloadFinished(request.key, bytes);
        }

        @Override
        public void onFinalResult(DlRequest request, R result) {
            //noinspection RedundantCollectionOperation
            inProgress.remove(inProgress.indexOf(request.key));
            if (request.dlDone) {
                results.put(request.key, result);
                onReady(request.key, result);
            } else {
                engine.sendRequest(request);
            }
        }

        @Override
        public void sendRequest(DlRequest request) {
            inProgress.add(request.key);
            super.sendRequest(request);
        }
    };

    public R get(K key) {
        return results.get(key);
    }

    public R getAndStartIfNecessary(K key, URL url) {
        R out = results.get(key);
        if (out != null) {
            return out;
        }
        if (!inProgress.contains(key)) engine.sendRequest(new DlRequest(key, url));
        return null;
    }

    /**
     * Be careful , this method is called from a background thread.
     */
    protected abstract R onDownloadFinished(K key, byte[] bytes);

    protected abstract void onReady(K key, R result);

    private class DlRequest {
        private K key;
        private URL url;
        private boolean dlDone = false;

        DlRequest(K key, URL url) {
            this.key = key;
            this.url = url;
        }
    }
}
