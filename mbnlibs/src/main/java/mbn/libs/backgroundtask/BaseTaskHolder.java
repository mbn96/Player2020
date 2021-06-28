package mbn.libs.backgroundtask;


import android.os.Process;

public class BaseTaskHolder implements Runnable {

    private final ThreadManager threadManager;
    private volatile int future_ID;
    private volatile ResultReceiver resultReceiver;
    private volatile BaseTask baseTask;
    //    private int currentID;
    private volatile Object resultObj, infoObj;
    private volatile int priority = Process.THREAD_PRIORITY_DEFAULT;

    BaseTaskHolder(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

//    void setCurrentID(int currentID) {
//        this.currentID = currentID;
//    }

    Object getResultObj() {
        return resultObj;
    }

    Object getInfoObj() {
        return infoObj;
    }

    public ResultReceiver getResultReceiver() {
        return resultReceiver;
    }

    private void releaseThisHolder() {
        resultReceiver = null;
        baseTask = null;
        resultObj = null;
        infoObj = null;
        priority = Process.THREAD_PRIORITY_DEFAULT;
        threadManager.releaseFuture(future_ID);
//        threadManager.putInPool(this);
    }

    public int StartTask(BaseTask baseTask, ResultReceiver resultReceiver) {
        this.baseTask = baseTask;
        this.resultReceiver = resultReceiver;
//        ThreadManager.INTER_THREADS_MESSAGE_MAP.put(currentID, this);
        future_ID = threadManager.postTask(this);
        return future_ID;
    }

    public int StartTask(BaseTask baseTask, ResultReceiver resultReceiver, int priority) {
        this.baseTask = baseTask;
        this.resultReceiver = resultReceiver;
        this.priority = priority;
//        ThreadManager.INTER_THREADS_MESSAGE_MAP.put(currentID, this);
        future_ID = threadManager.postTask(this);
        return future_ID;
    }

    @Override
    public final void run() {
        Process.setThreadPriority(priority);
        resultObj = baseTask.onRun();
        infoObj = baseTask.getInfo();

        if (resultReceiver != null) {
            threadManager.sendResult(new ResultMessage(resultObj, infoObj, resultReceiver));
        }
        releaseThisHolder();

//        Message message = Message.obtain(ThreadManager.mainThreadHandler);
//        message.arg1 = currentID;
//        message.sendToTarget();
    }


    //------------ Classes  -----------------//

    /* space */
//    class InterThreadsMessage {
//        private Object result;
//        private Object info;
//        private ResultReceiver resultReceiver;
//
//
//        InterThreadsMessage(Object result, Object info, ResultReceiver resultReceiver) {
//            this.result = result;
//            this.info = info;
//            this.resultReceiver = resultReceiver;
//        }
//
//        public Object getResult() {
//            return result;
//        }
//
//        public Object getInfo() {
//            return info;
//        }
//
//        public ResultReceiver getResultReceiver() {
//            return resultReceiver;
//        }
//
//        public void releaseThis() {
//            result = null;
//            info = null;
//            resultReceiver = null;
//        }
//
//    }


    public interface BaseTask {
        Object onRun();

        Object getInfo();
    }

    public interface ResultReceiver<R, I> {
        void onResult(R result, I info);
    }

    static class ResultMessage implements Runnable {

        private volatile Object resultObj, infoObj;
        private volatile ResultReceiver resultReceiver;

        ResultMessage(Object resultObj, Object infoObj, ResultReceiver resultReceiver) {
            this.resultObj = resultObj;
            this.infoObj = infoObj;
            this.resultReceiver = resultReceiver;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            resultReceiver.onResult(resultObj, infoObj);
            resultObj = null;
            infoObj = null;
            resultReceiver = null;
        }
    }
}
