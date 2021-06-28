package mbn.packfragmentmanager.fragmanager.backgroundtask;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.core.util.Pools;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadManager {
    private static final ThreadManager INSTANCE = new ThreadManager();
    private static final ThreadManager INSTANCE_MULTi_THREAD = new ThreadManager(1, true);

    private static final AtomicInteger ID_INT = new AtomicInteger();
    private final Pools.SynchronizedPool<BaseTaskHolder> taskHoldersPool = new Pools.SynchronizedPool<>(60);


    @SuppressLint("UseSparseArrays")
    static final Map<Integer, BaseTaskHolder> INTER_THREADS_MESSAGE_MAP = Collections.synchronizedMap(new HashMap<Integer, BaseTaskHolder>());

    static final Handler mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                BaseTaskHolder taskHolder = INTER_THREADS_MESSAGE_MAP.remove(msg.arg1);
                taskHolder.getResultReceiver().onResult(taskHolder.getResultObj(), taskHolder.getInfoObj());
                taskHolder.releaseThisHolder();
            } catch (Exception ignored) {
            }
            return true;
        }
    });


    public synchronized static BaseTaskHolder getAppGlobalTask() {
        return INSTANCE.getTaskHolder();
    }

    public synchronized static BaseTaskHolder getAppGlobalTask_MultiThread() {
        return INSTANCE_MULTi_THREAD.getTaskHolder();
    }

    /**
     * @param priority The priority to run the thread at. The value supplied must be from
     *                 {@link android.os.Process} and not from java.lang.Thread.
     */
//    public ThreadManager(int priority) {
//        this.priority = priority;
//    }

    /**
     * Use this for non-sequential thread Manager.
     *
     * @param threadCount The number of threads to use.
     */
    public ThreadManager(int threadCount, boolean goNuts) {
        if (goNuts) {
            executorService = Executors.newCachedThreadPool();
        } else {
//            executorService = Executors.newFixedThreadPool(threadCount);

            executorService = new ThreadPoolExecutor(0, threadCount,
                    5L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>());

//            executorService = new ThreadPoolExecutor(0, threadCount,
//                    3000L, TimeUnit.MILLISECONDS,
//                    new LinkedBlockingQueue<Runnable>());


        }
    }

    /**
     * Use this for sequential thread Manager.
     */
    public ThreadManager() {
        executorService = Executors.newSingleThreadExecutor();
    }

    private int priority;


//        private ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private ExecutorService executorService = new ThreadPoolExecutor(0, 1, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private final ExecutorService executorService;

    private HandlerThread mThread;
    private Handler backgroundTHandler;
    private final Object LOCK = new Object();
    private int timeToShutDown = 3 * 1000;

    private Runnable quiteCheck = new Runnable() {
        @Override
        public void run() {
            synchronized (LOCK) {
                if (backgroundTHandler.hasMessages(0)) {
                    timeToShutDown = 3 * 1000;
                    backgroundTHandler.postDelayed(this, 1000);
                } else {
                    if (timeToShutDown < 0) {
                        backgroundTHandler.removeCallbacks(this);
                        mThread.quitSafely();
                        mThread = null;
                        return;
                    }
                    timeToShutDown -= 1000;
                    backgroundTHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    private void generateNewThread() {
        mThread = new HandlerThread("back_thread" + System.currentTimeMillis(), priority);
        mThread.start();
        backgroundTHandler = new Handler(mThread.getLooper());
        backgroundTHandler.post(quiteCheck);
//        Log.i(TAG, "generateNewThread: ");
    }

    public void shutDown() {
        executorService.shutdown();
    }

    public synchronized BaseTaskHolder getTaskHolder() {
        BaseTaskHolder taskHolder = taskHoldersPool.acquire();
        if (taskHolder == null) taskHolder = new BaseTaskHolder(this);
        taskHolder.setCurrentID(ID_INT.incrementAndGet());
        return taskHolder;
    }

    void putInPool(BaseTaskHolder taskHolder) {
        try {
            taskHoldersPool.release(taskHolder);
        } catch (Exception ignored) {
        }
    }

    void postTask(Runnable task) {
        if (true) {
            executorService.execute(task);
            return;
        }
        synchronized (LOCK) {
            if (mThread == null) generateNewThread();
            backgroundTHandler.post(task);
        }
    }

//    static synchronized void onTaskFinished(BaseTaskObject.ResultReceiver resultReceiver) {
////        Log.i(TAG, "onTaskFinished: " + resultReceiver.toString());
//        INTER_THREADS_MESSAGE_MAP.put(ID_INT.incrementAndGet(), resultReceiver);
////        Log.i(TAG, "onTaskFinished: " + INTER_THREADS_MESSAGE_MAP.containsKey(ID_INT.get()));
//        Message message = Message.obtain(mainThreadHandler);
//        message.arg1 = ID_INT.get();
////        Log.i(TAG, "onTaskFinished: " + ID_INT.get());
//        message.sendToTarget();
//    }

    /*  space */

//    static synchronized void onTaskFinished(BaseTaskHolder.InterThreadsMessage interThreadsMessage) {
////        Log.i(TAG, "onTaskFinished: " + resultReceiver.toString());
//        INTER_THREADS_MESSAGE_MAP.put(ID_INT.incrementAndGet(), interThreadsMessage);
////        Log.i(TAG, "onTaskFinished: " + INTER_THREADS_MESSAGE_MAP.containsKey(ID_INT.get()));
//        Message message = Message.obtain(mainThreadHandler);
//        message.arg1 = ID_INT.get();
////        Log.i(TAG, "onTaskFinished: " + ID_INT.get());
//        message.sendToTarget();
//    }
}
