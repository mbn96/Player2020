//package mbn.libs.backgroundtask;
//
//
//import android.os.Handler;
//import android.util.SparseArray;
//
//import java.util.LinkedList;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public abstract class MultiThreadSequentialTasks_backup<R, O> implements BaseTaskHolder.ResultReceiver {
//
//    private AtomicInteger sendID = new AtomicInteger();
//    private AtomicInteger finalResultID = new AtomicInteger();
//    private final Object LOCK = new Object();
//    private final Object WORKING_THREAD_LOCK = new Object();
//    private final Object REQUEST_QUEUE_LOCK = new Object();
//
//    private LinkedList<R> requestsQueue = new LinkedList<R>();
//    private LinkedList<R> threadFeed = new LinkedList<>();
//    private SparseArray<Object> resultsMap = new SparseArray<>();
//
//    private int queueSize = 0;
//    private int threadsCount;
//    private int attempts = 0;
//    private int workingThreads = 0;
//    private boolean working = true;
//    private boolean sequentialResults;
//    private ThreadManager threadManager;
//
//    /*
//    It may be called from multiple threads , do not reach outside of method scope.
//     */
//    public abstract O neededProcess(R request);
//
//    public abstract void onFinalResult(R request, O result);
//
//    public void outDatedRequest(R request) {
//    }
//
//    public void shutDown() {
//        working = false;
//        threadManager.shutDown();
//    }
//
//
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
//
//    public MultiThreadSequentialTasks_backup(int queueSize, int threadsCount, boolean sequentialResults) {
//        this.sequentialResults = sequentialResults;
//        this.queueSize = queueSize;
//        this.threadsCount = threadsCount;
//        threadManager = new ThreadManager(threadsCount, true);
//        retriever.start();
//    }
//
//    public MultiThreadSequentialTasks_backup(int queueSize, int threadsCount, boolean sequentialResults, Handler handler) {
//        this.sequentialResults = sequentialResults;
//        this.queueSize = queueSize;
//        this.threadsCount = threadsCount;
//        threadManager = new ThreadManager(threadsCount, true, handler);
//        retriever.start();
//    }
//
//    public synchronized void sendRequest(R request) {
////        synchronized (LOCK) {
//        addToRequestQueue(request);
//        nextRequest();
//        while (queueSize > 0 && requestQueueSize() > queueSize) {
//            outDatedRequest(pollFromRequestQueue());
//        }
////            LOCK.notifyAll();
////        }
//    }
//
//    protected synchronized void nextRequest() {
//        while (getWorkingThreads() < threadsCount && (!isRequestQueueEmpty())) {
//            synchronized (LOCK) {
//                threadFeed.add(pollFromRequestQueue());
//                LOCK.notifyAll();
//                increaseWorkingThreads();
////                Log.i("THC", "nextRequest: " + workingThreads);
////                R request = requestsQueue.poll();
////                threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), this);
//            }
//        }
//    }
//
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
//                    threadManager.getTaskHolder().StartTask(new Task(sendID.getAndIncrement(), request), MultiThreadSequentialTasks_backup.this);
////                    Log.i("THC", "nextRequest: in request thread" );
//                }
//            }
//        }
//    });
//
//    private void sendResult() {
//        if (working) {
//            Object result = resultsMap.get(finalResultID.get());
//            if (result != null) {
//                attempts = 0;
//                resultsMap.remove(finalResultID.getAndIncrement());
//                Object[] objects = (Object[]) result;
//                R request = (R) objects[0];
//                O finalResult = (O) objects[1];
//                onFinalResult(request, finalResult);
//                sendResult();
//                return;
//            } else {
//                attempts++;
//            }
//            if (attempts > 10) {
//                finalResultID.incrementAndGet();
//                attempts = 0;
//                sendResult();
//            }
//        }
//    }
//
//
//    @Override
//    public void onResult(Object result, Object info) {
//        if (sequentialResults) {
//            resultsMap.put((Integer) info, result);
//            sendResult();
//        } else {
//            Object[] objects = (Object[]) result;
//            R request = (R) objects[0];
//            O finalResult = (O) objects[1];
//            onFinalResult(request, finalResult);
//        }
//        decreaseWorkingThreads();
//        nextRequest();
//    }
//
//    private class Task implements BaseTaskHolder.BaseTask {
//
//        private int id;
//        private R request;
//
//        Task(int id, R request) {
//            this.id = id;
//            this.request = request;
//        }
//
//        @Override
//        public Object onRun() {
//            return new Object[]{request, neededProcess(request)};
//        }
//
//        @Override
//        public Object getInfo() {
//            return id;
//        }
//    }
//
//}
