package com.br.mreza.musicplayer.NewService;


import java.util.ArrayList;

public class MusicInfoHolder {
//    private static final Object CURRENT_POS_LOCK = new Object();
//    private static final Object SEEK_LOCK = new Object();
//    private static final Object DURATION_LOCK = new Object();
    private static final Object A_B_LOCK = new Object();

//    private static long currentPos = 0;
//    private static long seekPos = 0;
//    private static long duration = 0;
    private static long posA = 0;
    private static long posB = 0;

    private static boolean hasA = false;
    private static boolean hasB = false;

    private static ArrayList<CallBack> callBacks = new ArrayList<>();

//    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            for (CallBack cb : callBacks) {
//                cb.onCurrentPosChanged(getCurrentPos(), getDuration());
//            }
//            return true;
//        }
//    });
//
//    static void setDuration(long duration) {
//        synchronized (DURATION_LOCK) {
//            MusicInfoHolder.duration = duration;
//        }
//    }
//
//    static long getDuration() {
//        synchronized (DURATION_LOCK) {
//            return duration;
//        }
//    }

//    public static long getCurrentPos() {
//        synchronized (CURRENT_POS_LOCK) {
//            return currentPos;
//        }
//    }
//
//    static void setCurrentPos(long currentPos) {
//        synchronized (CURRENT_POS_LOCK) {
//            MusicInfoHolder.currentPos = currentPos;
//            mainThreadHandler.sendEmptyMessage(0);
//        }
//    }

//    static long getSeekPos() {
//        synchronized (SEEK_LOCK) {
//            return seekPos;
//        }
//    }
//
//    public static void setSeekPos(long seekPos) {
//        synchronized (SEEK_LOCK) {
//            MusicInfoHolder.seekPos = seekPos;
//        }
//    }

//    public static void setSeekPos(long seekPos, Context context) {
//        synchronized (SEEK_LOCK) {
//            MusicInfoHolder.seekPos = seekPos;
//            StorageUtils.setStartFromPos(context, seekPos);
//        }
//    }

    public static void setA_B() {
//        synchronized (A_B_LOCK) {
//            if (hasA) {
//                if (hasB) {
//                    resetA_B();
//                    return;
//                }
//                if (getCurrentPos() > posA) {
//                    hasB = true;
//                    posB = getCurrentPos();
//                } else {
//                    posA = getCurrentPos();
//                }
//            } else {
//                hasA = true;
//                posA = getCurrentPos();
//            }
//            for (CallBack cb : callBacks) {
//                cb.onA_B_Change();
//            }
//        }
    }

    public static void resetA_B() {
        synchronized (A_B_LOCK) {
            hasA = false;
            hasB = false;
            posA = 0;
            posB = 0;
            for (CallBack cb : callBacks) {
                cb.onA_B_Change();
            }
        }
    }


    public static long getPosA() {
        synchronized (A_B_LOCK) {
            return posA;
        }
    }

    public static long getPosB() {
        synchronized (A_B_LOCK) {
            return posB;
        }
    }

    public static boolean hasA() {
        synchronized (A_B_LOCK) {
            return hasA;
        }
    }

    public static boolean hasB() {
        synchronized (A_B_LOCK) {
            return hasB;
        }
    }

    public static void registerCallback(CallBack callBack) {
        callBacks.add(callBack);
    }

    public static void unRegisterCllBack(CallBack callBack) {
        callBacks.remove(callBack);
    }

    public interface CallBack {
//        void onCurrentPosChanged(long pos, long duration);

        void onA_B_Change();
    }

}
