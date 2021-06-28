package mbn.packfragmentmanager.fragmanager.backgroundtask;//package com.br.mreza.musicplayer.backgroundtask;
//
//
//import android.support.annotation.Nullable;
//
//public abstract class BaseTaskObject implements Runnable {
//
//    private ResultReceiver resultReceiver;
//    private InfoObject infoObject;
//
//    public BaseTaskObject(@Nullable ThreadManager threadManager, final ResultReceiver resultReceiver, InfoObject infoObject) {
//        this.resultReceiver = resultReceiver;
//        this.infoObject = infoObject;
//        this.resultReceiver.setInfoObject(infoObject);
//        if (threadManager == null) {
//            ThreadManager.INSTANCE.postTask(this);
//        } else {
//            threadManager.postTask(this);
//        }
//    }
//
//    public ResultReceiver getResultReceiver() {
//        return resultReceiver;
//    }
//
//    protected InfoObject getInfoObject() {
//        return infoObject;
//    }
//
//    @Override
//    public final void run() {
//        resultReceiver.setResult(onRun());
//        ThreadManager.onTaskFinished(resultReceiver);
//    }
//
//    public abstract ResultObject onRun();
//
//    //------------ Classes  -----------------//
//
//
//    /**
//     * sub-classes must extend this.
//     */
//    public static class InfoObject {
//    }
//
//    /**
//     * sub-classes must extend this.
//     */
//    public static class ResultObject {
//    }
//
//    public static abstract class ResultReceiver {
//        private ResultObject result;
//        private InfoObject infoObject;
//
//        public ResultObject getResult() {
//            return result;
//        }
//
//        public void setResult(ResultObject result) {
//            this.result = result;
//        }
//
//        public InfoObject getInfoObject() {
//            return infoObject;
//        }
//
//        public void setInfoObject(InfoObject infoObject) {
//            this.infoObject = infoObject;
//        }
//
//        public final void onFinish() {
//            onResult(result, infoObject);
//        }
//
//        public abstract void onResult(ResultObject result, InfoObject info);
//    }
//}
