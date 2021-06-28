package mbn.packfragmentmanager.fragmanager.backgroundtask;


import android.os.Message;

public class BaseTaskHolder implements Runnable {

    private ThreadManager threadManager;
    private ResultReceiver resultReceiver;
    private BaseTask baseTask;
    private int currentID;
    private volatile Object resultObj, infoObj;

    BaseTaskHolder(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    Object getResultObj() {
        return resultObj;
    }

    Object getInfoObj() {
        return infoObj;
    }

    public ResultReceiver getResultReceiver() {
        return resultReceiver;
    }

    void releaseThisHolder() {
        resultReceiver = null;
        baseTask = null;
        resultObj = null;
        infoObj = null;
        threadManager.putInPool(this);
    }

    public void StartTask(BaseTask baseTask, ResultReceiver resultReceiver) {
        this.baseTask = baseTask;
        this.resultReceiver = resultReceiver;
        ThreadManager.INTER_THREADS_MESSAGE_MAP.put(currentID, this);
        threadManager.postTask(this);
    }

    @Override
    public final void run() {
        resultObj = baseTask.onRun();
        infoObj = baseTask.getInfo();

        Message message = Message.obtain(ThreadManager.mainThreadHandler);
        message.arg1 = currentID;
        message.sendToTarget();
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

    public interface ResultReceiver {
        void onResult(Object result, Object info);
    }
}
