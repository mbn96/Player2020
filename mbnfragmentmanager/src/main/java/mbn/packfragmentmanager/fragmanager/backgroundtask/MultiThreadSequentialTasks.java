package mbn.packfragmentmanager.fragmanager.backgroundtask;


import android.util.SparseArray;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiThreadSequentialTasks<R, O> implements BaseTaskHolder.ResultReceiver {

    private AtomicInteger sendID = new AtomicInteger();
    private AtomicInteger finalResultID = new AtomicInteger();
    private final Object LOCK = new Object();

    private LinkedList<R> requestsQueue = new LinkedList<>();
    private LinkedList<R> threadFeed = new LinkedList<>();
    private SparseArray<Object> resultsMap = new SparseArray<>();

    private int queueSize = 0;
    private int threadsCount;
    private int attempts = 0;
    protected int workingThreads = 0;
    private boolean working = true;
    private ThreadManager threadManager;

    /*
    It may be called from multiple threads , do not reach outside of method scope.
     */
    public abstract O neededProcess(R request);

    public abstract void onFinalResult(R request, O result);

    public void outDatedRequest(R request) {
    }

    public void shutDown() {
        working = false;
        threadManager.shutDown();
    }

    protected MultiThreadSequentialTasks(int queueSize, int threadsCount) {
        this.queueSize = queueSize;
        this.threadsCount = threadsCount;
        threadManager = new ThreadManager(threadsCount, true);
        retriever.start();
    }

    public void sendRequest(R request) {
//        synchronized (LOCK) {
        requestsQueue.add(request);
        nextRequest();
        while (queueSize > 0 && requestsQueue.size() > queueSize) {
            outDatedRequest(requestsQueue.poll());
        }
//            LOCK.notifyAll();
//        }
    }

    protected void nextRequest() {
        while (workingThreads < threadsCount && (!requestsQueue.isEmpty())) {
            synchronized (LOCK) {
                threadFeed.add(requestsQueue.poll());
                LOCK.notifyAll();
                workingThreads++;
//                Log.i("THC", "nextRequest: " + workingThreads);
//                R request = requestsQueue.poll();
//                threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), this);
            }
        }
    }

    private Thread retriever = new Thread(new Runnable() {
        @Override
        public void run() {
            while (working) {
                synchronized (LOCK) {
                    if (threadFeed.isEmpty()) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                synchronized (LOCK) {
                    R request = threadFeed.poll();
                    threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), MultiThreadSequentialTasks.this);
//                    Log.i("THC", "nextRequest: in request thread" );
                }
            }
        }
    });

    private void sendResult() {
        if (working) {
            Object result = resultsMap.get(finalResultID.get());
            if (result != null) {
                attempts = 0;
                resultsMap.remove(finalResultID.getAndIncrement());
                Object[] objects = (Object[]) result;
                R request = (R) objects[0];
                O finalResult = (O) objects[1];
                onFinalResult(request, finalResult);
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


    @Override
    public void onResult(Object result, Object info) {
        resultsMap.put((Integer) info, result);
        sendResult();
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

}
