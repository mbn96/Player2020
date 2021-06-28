package mbn.libs.backgroundtask;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.collection.SparseArrayCompat;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadInterrupter {
    private final AtomicInteger REG_ID = new AtomicInteger();
    private final SparseArrayCompat<Interruption> registeredThreads = new SparseArrayCompat<>();
    private final Object LOCK = new Object();
    private final Handler handler;
    private HandlerThread handlerThread;

    public ThreadInterrupter(boolean useMainThread) {
        if (useMainThread) {
            handler = new Handler(Looper.getMainLooper());
        } else {
            handlerThread = new HandlerThread("Interrupter_" + System.currentTimeMillis());
            handler = new Handler(handlerThread.getLooper());
        }
    }

    public int register(Thread thread) {
        synchronized (LOCK) {
            int id = REG_ID.getAndIncrement();
            registeredThreads.append(id, new Interruption(thread));
            return id;
        }
    }

    public void unregister(int id) {
        synchronized (LOCK) {
            cancelInterruption(id);
            registeredThreads.remove(id);
        }
    }

    public void putOrUpdate(int id, long delay) {
        synchronized (LOCK) {
            Interruption interruption = registeredThreads.get(id);
            if (interruption != null) {
                handler.removeCallbacks(interruption);
                handler.postDelayed(interruption, delay);
            }
        }
    }

    public void cancelInterruption(int id) {
        synchronized (LOCK) {
            Interruption interruption = registeredThreads.get(id);
            if (interruption != null) {
                handler.removeCallbacks(interruption);
            }
        }
    }

    public synchronized void shutDown() {
        if (handlerThread != null) handlerThread.quitSafely();
    }

    private static class Interruption implements Runnable {
        private final Thread thread;

        Interruption(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.interrupt();
            } catch (SecurityException ignored) {
            }
        }
    }
}
