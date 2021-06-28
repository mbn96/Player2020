package mbn.libs.backgroundtask;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadManager {
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private static final ThreadManager INSTANCE = new ThreadManager();
    private static final ThreadManager INSTANCE_MULTi_THREAD = new ThreadManager(1, true);

    private final AtomicInteger ID_INT = new AtomicInteger();
    private final SparseArray<Future> taskFutures = new SparseArray<>();
    private final ReentrantLock FUTURE_LOCK = new ReentrantLock();
//    private final Pools.SynchronizedPool<BaseTaskHolder> taskHoldersPool = new Pools.SynchronizedPool<>(60);


//    @SuppressLint("UseSparseArrays")
//    static final Map<Integer, BaseTaskHolder> INTER_THREADS_MESSAGE_MAP = Collections.synchronizedMap(new HashMap<Integer, BaseTaskHolder>());

//    , new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            BaseTaskHolder taskHolder = INTER_THREADS_MESSAGE_MAP.remove(msg.arg1);
//            if (taskHolder != null) {
//                if (taskHolder.getResultReceiver() != null) {
//                    taskHolder.getResultReceiver().onResult(taskHolder.getResultObj(), taskHolder.getInfoObj());
//                }
//                taskHolder.releaseThisHolder();
//            }
//            return true;
//        }
//    });


    public synchronized static BaseTaskHolder getAppGlobalTask() {
        return INSTANCE.getTaskHolder();
    }

    public synchronized static BaseTaskHolder getAppGlobalTask_MultiThread() {
        return INSTANCE_MULTi_THREAD.getTaskHolder();
    }

    /*
      @param priority The priority to run the thread at. The value supplied must be from
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
        this(threadCount, goNuts, null);
//        resultHandler = mainThreadHandler;
//        if (goNuts) {
//            executorService = Executors.newCachedThreadPool();
//        } else {
////            executorService = Executors.newFixedThreadPool(threadCount);
//
//            executorService = new ThreadPoolExecutor(0, threadCount,
//                    5L, TimeUnit.SECONDS,
//                    new LinkedBlockingQueue<>());
//
////            executorService = new ThreadPoolExecutor(0, threadCount,
////                    3000L, TimeUnit.MILLISECONDS,
////                    new LinkedBlockingQueue<Runnable>());
//
//
//        }
    }

    public ThreadManager(int threadCount, boolean goNuts, @Nullable Handler handler) {
        if (handler == null) {
            resultHandler = mainThreadHandler;
        } else {
            resultHandler = handler;
        }
        if (goNuts) {
            executorService = Executors.newCachedThreadPool();
        } else {
//            ThreadPoolExecutor e = (ThreadPoolExecutor) (executorService = Executors.newFixedThreadPool(threadCount));
//            e.allowCoreThreadTimeOut(true);
//
            ThreadPoolExecutor e = new ThreadPoolExecutor(threadCount, threadCount,
                    5L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>());
            e.allowCoreThreadTimeOut(true);
            executorService = e;

//            executorService = new ThreadPoolExecutor(0, threadCount,
//                    3000L, TimeUnit.MILLISECONDS,
//                    new LinkedBlockingQueue<Runnable>());


        }
    }

    /**
     * Use this for sequential thread Manager.
     */
    public ThreadManager() {
        resultHandler = mainThreadHandler;
        executorService = Executors.newSingleThreadExecutor();
    }

    public ThreadManager(Handler handler) {
        resultHandler = handler;
        executorService = Executors.newSingleThreadExecutor();
    }

    private int priority;


//        private ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private ExecutorService executorService = new ThreadPoolExecutor(0, 1, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private final ExecutorService executorService;
    private final Handler resultHandler;

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

    public BaseTaskHolder getTaskHolder() {
//        BaseTaskHolder taskHolder = taskHoldersPool.acquire();
//        if (taskHolder == null) taskHolder = new BaseTaskHolder(this);
        BaseTaskHolder taskHolder = new BaseTaskHolder(this);
//        taskHolder.setCurrentID(ID_INT.incrementAndGet());
        return taskHolder;
    }

    void sendResult(BaseTaskHolder.ResultMessage result) {
        resultHandler.post(result);
    }

//    void putInPool(BaseTaskHolder taskHolder) {
//        try {
//            taskHoldersPool.release(taskHolder);
//        } catch (Exception ignored) {
//        }
//    }

    int postTask(Runnable task) {
        FUTURE_LOCK.lock();
        Future f = executorService.submit(task);
        int id = ID_INT.getAndIncrement();
        taskFutures.put(id, f);
        FUTURE_LOCK.unlock();
        return id;
    }

    void releaseFuture(int id) {
        FUTURE_LOCK.lock();
        taskFutures.remove(id);
        FUTURE_LOCK.unlock();
    }

    public boolean cancelTask(int id, boolean interrupt) {
        FUTURE_LOCK.lock();
        boolean out = false;
        Future f = taskFutures.get(id);
        if (f != null) {
            out = f.cancel(interrupt);
            taskFutures.remove(id);
        }
        FUTURE_LOCK.unlock();
        return out;
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

    private MaxPriorityThreadFactory getThreadFactory() {
        return new MaxPriorityThreadFactory();
    }

    public static class MaxPriorityThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MaxPriorityThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool_MBN-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.MAX_PRIORITY)
                t.setPriority(Thread.MAX_PRIORITY);
//            Process.setThreadPriority((int) t.getId(), Process.THREAD_PRIORITY_FOREGROUND);
            return t;
        }
    }

}
