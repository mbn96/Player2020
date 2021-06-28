package mbn.libs.backgroundtask;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.annotation.CallSuper;
import android.util.SparseArray;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiThreadSequentialTasks<R, O> implements BaseTaskHolder.ResultReceiver {

    private AtomicInteger sendID = new AtomicInteger();
    private AtomicInteger finalResultID = new AtomicInteger();
    private final Object PAUSE_LOCK = new Object();
//    private final Object WORKING_THREAD_LOCK = new Object();
//    private final Object REQUEST_QUEUE_LOCK = new Object();

    private LinkedList<R> requestsQueue = new LinkedList<>();
    //    private LinkedList<R> threadFeed = new LinkedList<>();
    private SparseArray<Object> resultsMap = new SparseArray<>();

    private int queueSize = 0;
    private int threadsCount;
    private int attempts = 0;
    private int workingThreads = 0;
    private boolean working = true;
    private boolean sequentialResults;
    private ThreadManager threadManager;
    private HandlerThread runThread;
    private Handler runHandler, resultHandler;
    private boolean runOnResultThread = false;
    private boolean pause = false;

    /**
     * It may be called from multiple threads , do not reach outside of method scope.
     */
    public abstract O neededProcess(R request);

    public abstract void onFinalResult(R request, O result);

    public void outDatedRequest(R request) {
    }

    public void pause() {
        synchronized (PAUSE_LOCK) {
            pause = true;
        }
    }

    public void unPause() {
        synchronized (PAUSE_LOCK) {
            pause = false;
            runHandler.post(nextCheckRequest);
        }
    }

    private final Runnable nextCheckRequest = this::nextRequest;

    public void shutDown() {
        working = false;
        if (runThread != null) {
            runThread.quitSafely();
        }
        threadManager.shutDown();
    }


//    private int getWorkingThreads() {
//        synchronized (WORKING_THREAD_LOCK) {
//            return workingThreads;
//        }
//    }
//
//    private void increaseWorkingThreads() {
//        synchronized (WORKING_THREAD_LOCK) {
//            workingThreads++;
//        }
//    }
//
//    private void decreaseWorkingThreads() {
//        synchronized (WORKING_THREAD_LOCK) {
//            workingThreads--;
//        }
//    }
//
//    private void addToRequestQueue(R r) {
//        synchronized (REQUEST_QUEUE_LOCK) {
//            requestsQueue.add(r);
//        }
//    }
//
//    private int requestQueueSize() {
//        synchronized (REQUEST_QUEUE_LOCK) {
//            return requestsQueue.size();
//        }
//    }
//
//    private R pollFromRequestQueue() {
//        synchronized (REQUEST_QUEUE_LOCK) {
//            return requestsQueue.poll();
//        }
//    }
//
//    private boolean isRequestQueueEmpty() {
//        synchronized (REQUEST_QUEUE_LOCK) {
//            return requestsQueue.isEmpty();
//        }
//    }

    public MultiThreadSequentialTasks(int queueSize, int threadsCount, boolean sequentialResults) {
        this.sequentialResults = sequentialResults;
        this.queueSize = queueSize;
        this.threadsCount = threadsCount;

        runThread = new HandlerThread("multi_MBN");
        runThread.start();
        runHandler = new Handler(runThread.getLooper());
        resultHandler = new Handler(Looper.getMainLooper());

        threadManager = new ThreadManager(threadsCount, true, runHandler);
    }

    public MultiThreadSequentialTasks(int queueSize, int threadsCount, boolean sequentialResults, Handler resultHandler, boolean runOnResultThread) {
        this.sequentialResults = sequentialResults;
        this.queueSize = queueSize;
        this.threadsCount = threadsCount;
        this.runOnResultThread = runOnResultThread;
        this.resultHandler = resultHandler;
        if (runOnResultThread) {
            runHandler = resultHandler;
        } else {
            runThread = new HandlerThread("multi_MBN");
            runThread.start();
            runHandler = new Handler(runThread.getLooper());
        }
        threadManager = new ThreadManager(threadsCount, true, runHandler);
    }

    public MultiThreadSequentialTasks(int queueSize, int threadsCount, boolean sequentialResults, boolean resultsOnRunThread) {
        this.sequentialResults = sequentialResults;
        this.queueSize = queueSize;
        this.threadsCount = threadsCount;
        this.runOnResultThread = resultsOnRunThread;

        runThread = new HandlerThread("multi_MBN");
        runThread.start();
        runHandler = new Handler(runThread.getLooper());
        if (runOnResultThread) {
            resultHandler = runHandler;
        } else {
            resultHandler = new Handler(Looper.getMainLooper());
        }

        threadManager = new ThreadManager(threadsCount, true, runHandler);
    }

    public void sendRequest(R request, long delayMillis) {
        runHandler.postDelayed(new RequestSender(request), delayMillis);
    }

    public void sendRequest(R request) {
        runHandler.post(new RequestSender(request));
//        synchronized (LOCK) {
//        addToRequestQueue(request);
//        nextRequest();
//        while (queueSize > 0 && requestQueueSize() > queueSize) {
//            outDatedRequest(pollFromRequestQueue());
//        }
//            LOCK.notifyAll();
//        }
    }

    /**
     * Do NOT call this , Only for Override , You'd better call super.
     */
    protected void sendRequestInternal(R request) {
        requestsQueue.add(request);
        nextRequest();
        while (queueSize > 0 && requestsQueue.size() > queueSize) {
            resultHandler.post(new OutDatedRequestSender(requestsQueue.poll()));
        }
    }

    private void nextRequest() {
        while (!pause && workingThreads < threadsCount && (!requestsQueue.isEmpty())) {
            R request = requestsQueue.poll();
            threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), this);
            workingThreads++;
//                Log.i("THC", "nextRequest: " + workingThreads);
//                R request = requestsQueue.poll();
//                threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), this);

        }
    }

//    private Thread retriever = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            while (working) {
//                synchronized (LOCK) {
//                    if (threadFeed.isEmpty()) {
//                        try {
//                            LOCK.wait();
//                        } catch (InterruptedException ignored) {
//                        }
//                    }
//                }
//                synchronized (LOCK) {
//                    R request = threadFeed.poll();
//                    threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), MultiThreadSequentialTasks.this);
////                    Log.i("THC", "nextRequest: in request thread" );
//                }
//            }
//        }
//    });

    @CallSuper
    protected void sentFinalResult(R request, O finalResult) {
        if (runOnResultThread) {
            onFinalResult(request, finalResult);
        } else {
            resultHandler.post(new ResultSender(request, finalResult));
        }
    }

    @SuppressWarnings("unchecked")
    private void sendResult() {
        if (working) {
            Object result = resultsMap.get(finalResultID.get());
            if (result != null) {
                attempts = 0;
                resultsMap.remove(finalResultID.getAndIncrement());
                Object[] objects = (Object[]) result;
                R request = (R) objects[0];
                O finalResult = (O) objects[1];
                sentFinalResult(request, finalResult);
//                123 hey onFinalResult (request, finalResult);
                sendResult();
                return;
            } else {
                attempts++;
            }
            if (attempts > 10) {
                finalResultID.incrementAndGet();
                attempts = 0;
                sendResult();
            }
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void onResult(Object result, Object info) {
        if (sequentialResults) {
            resultsMap.put((Integer) info, result);
            sendResult();
        } else {
            Object[] objects = (Object[]) result;
            R request = (R) objects[0];
            O finalResult = (O) objects[1];
            sentFinalResult(request, finalResult);
//            123 hey onFinalResult (request, finalResult);
        }
        workingThreads--;
        nextRequest();
    }

    private class Task implements BaseTaskHolder.BaseTask {

        private int id;
        private R request;

        Task(int id, R request) {
            this.id = id;
            this.request = request;
        }

        @Override
        public Object onRun() {
            return new Object[]{request, neededProcess(request)};
        }

        @Override
        public Object getInfo() {
            return id;
        }
    }

    private class RequestSender implements Runnable {

        private volatile R request;

        RequestSender(R request) {
            this.request = request;
        }

        @Override
        public void run() {
            sendRequestInternal(request);
            request = null;
        }
    }

    private class OutDatedRequestSender implements Runnable {

        private volatile R request;

        OutDatedRequestSender(R request) {
            this.request = request;
        }

        @Override
        public void run() {
            outDatedRequest(request);
            request = null;
        }
    }

    private class ResultSender implements Runnable {

        private volatile R request;
        private volatile O result;

        ResultSender(R request, O result) {
            this.request = request;
            this.result = result;
        }

        @Override
        public void run() {
            onFinalResult(request, result);
            request = null;
            result = null;
        }
    }

}
