//package com.br.mreza.musicplayer.BackgroundForUI;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.os.Process;
//
//import com.br.mreza.musicplayer.DataSong;
//import com.br.mreza.musicplayer.backgroundtask.BaseTaskHolder;
//import com.br.mreza.musicplayer.backgroundtask.ThreadManager;
//import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
//
//import java.util.ArrayList;
//
//public class BackImageChangeHandler implements BaseTaskHolder.ResultReceiver {
//
//    public static BackImageChangeHandler instance;
//
//    static {
//        instance = new BackImageChangeHandler();
//    }
//
//    private ArrayList<ImageChangeCallback> callbackArrayList;
//
//    private Bitmap main;
//    private Bitmap blur;
//    private Context tempContext;
//    private boolean hasAsked = false;
//    private TrackChangeAnimator trackChangeAnimator = new TrackChangeAnimator();
//
////    private Context context;
//
//
//    private BackImageChangeHandler() {
//        callbackArrayList = new ArrayList<>();
//    }
//
////    public void start() {
//////        if (context == null) {
//////            context = con;
//////            LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(To_FRAGMENT));
//////        }
////
////
////    }
//
//    public void addCallback(ImageChangeCallback callback) {
//        callbackArrayList.add(callback);
//    }
//
//    public void removeCallback(ImageChangeCallback callback) {
//        if (callbackArrayList.contains(callback)) {
//            callbackArrayList.remove(callback);
//        }
//        if (callbackArrayList.isEmpty()) {
//            shutDown();
//        }
//    }
//
//    private void shutDown() {
////        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
////        context = null;
////        main.recycle();
//        main = null;
//        blur = null;
//    }
//
//    private void onCall(Bitmap main, Bitmap blur) {
//        for (ImageChangeCallback call : callbackArrayList) {
//            call.onChange(main, blur);
//        }
//    }
//
//    public void force(Context context) {
//        if (main == null && !hasAsked) {
//            tempContext = context;
//            DataBaseManager.getManager().getCurrentQueueAndTrack(this);
//            hasAsked = true;
//        } else {
//            onCall(main, blur);
//        }
//    }
//
//
//    public void onTrackChange(Context context, DataSong track) {
//
//        try {
//            ThreadManager.getAppGlobalTask().StartTask(new NewLoaderTask(track.getId(), track.getPath(), context, true), new BaseTaskHolder.ResultReceiver() {
//                @Override
//                public void onResult(Object result, Object info) {
//                    if (main != null && blur != null) {
//                        try {
//                            main.recycle();
//                            blur.recycle();
//                        } catch (Exception ignored) {
//                        }
//                    }
//                    BackImageChangeHandler.this.main = ((NewLoaderTask.Result) result).getMain();
//                    BackImageChangeHandler.this.blur = ((NewLoaderTask.Result) result).getBlur();
//                    onCall(main, blur);
//
////                    if (((NewLoaderTask.Result) result).isChange())
////                        trackChangeAnimator.setBitmaps(main, blur);
////                    else {
////                        onCall(main, blur);
////                        trackChangeAnimator.setBitmapsWithOutAnim(main, blur);
////                    }
//                }
//            });
////            ThreadManager.getAppGlobalTask().StartTask(new NewLoaderTask_backup(currentTrack.getId(), currentTrack.getPath(), context.getResources()), new BaseTaskHolder.ResultReceiver() {
////                @Override
////                public void onResult(Object result, Object info) {
////                    BackImageChangeHandler.this.main = ((NewLoaderTask_backup.Result) result).getMain();
////                    BackImageChangeHandler.this.blur = ((NewLoaderTask_backup.Result) result).getBlur();
////                    onCall();
////                }
////            });
//
//        } catch (Exception e) {
//            try {
//                Thread.sleep(10);
//                onTrackChange(context, track);
//            } catch (InterruptedException ignored) {
//
//            }
//        }
//    }
//
//    @Override
//    public void onResult(Object result, Object info) {
//        onTrackChange(tempContext, (DataSong) ((Object[]) result)[1]);
//        tempContext = null;
//    }
//
//    public interface ImageChangeCallback {
//
//        void onChange(Bitmap original, Bitmap blur);
//
//    }
//
//    private class TrackChangeAnimator {
//        private ThreadManager threadManager = new ThreadManager(Process.THREAD_PRIORITY_DISPLAY);
//        private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Bundle bundle = msg.getData();
//                onCall((Bitmap) bundle.getParcelable("M"), (Bitmap) bundle.getParcelable("B"));
//                return true;
//            }
//        });
//
//        private Bitmap main, Blur;
//
//        void setBitmaps(Bitmap main, Bitmap blur) {
//            threadManager.getTaskHolder().StartTask(new ChangeTask(this.main, this.Blur, main, blur, 0, handler), new BaseTaskHolder.ResultReceiver() {
//                @Override
//                public void onResult(Object result, Object info) {
//
//                }
//            });
//            this.main = main;
//            this.Blur = blur;
//        }
//
//        void setBitmapsWithOutAnim(Bitmap main, Bitmap blur) {
//            this.main = main;
//            this.Blur = blur;
//        }
//
//    }
//
//    private class ChangeTask implements BaseTaskHolder.BaseTask {
//
//        private Bitmap main, blur, newMain, newBlur;
//        private int type = 0;
//        private Handler handler;
//        private Paint paint = new Paint();
//
//        ChangeTask(Bitmap main, Bitmap blur, Bitmap newMain, Bitmap newBlur, int type, Handler handler) {
//            this.main = main;
//            this.blur = blur;
//            this.newMain = newMain;
//            this.newBlur = newBlur;
//            this.type = type;
//            this.handler = handler;
//            paint.setStyle(Paint.Style.FILL);
//            paint.setColor(Color.WHITE);
//        }
//
//        @Override
//        public Object onRun() {
//            float faction = 0f;
//            while ((faction += 0.03f) <= 1) {
//                Message message = Message.obtain(handler);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("M", makeBitmap(main, newMain, faction));
//                bundle.putParcelable("B", makeBitmap(blur, newBlur, faction));
//                message.setData(bundle);
//                message.sendToTarget();
//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException ignored) {
//                }
//            }
//
//            Message message = Message.obtain(handler);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("M", newMain);
//            bundle.putParcelable("B", newBlur);
//            message.setData(bundle);
//            message.sendToTarget();
//
//            return null;
//        }
//
//        @Override
//        public Object getInfo() {
//            return null;
//        }
//
//
//        private Bitmap makeBitmap(Bitmap oldB, Bitmap newB, float fraction) {
//            Bitmap outBitmap = oldB.copy(Bitmap.Config.ARGB_8888, true);
//            Canvas canvas = new Canvas(outBitmap);
//            canvas.drawCircle(0, 0, 800 * fraction, paint);
//            return outBitmap;
//        }
//
//
//    }
//
//}
