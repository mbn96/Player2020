package mbn.packfragmentmanager.fragmanager;


import android.util.SparseArray;

public class ResultReceivingManager {
    private static int atomicInt = 0;
    private static SparseArray<ResultReceiver> resultReceivers = new SparseArray<>();

    static boolean hasThisReceiver(int id) {
        return resultReceivers.get(id) != null;
    }

    public static ResultReceiver getReceiver(int id) {
        return resultReceivers.get(id);
    }

    public static void addReceiver(int id, ResultReceiver resultReceiver) {
        resultReceivers.put(id, resultReceiver);
    }

    public static void removeReceiver(int id) {
        resultReceivers.remove(id);
    }

    public static void sendResult(int id, Object resultObject) {
        if (hasThisReceiver(id)) resultReceivers.get(id).onReceiveResultFromFragment(resultObject);
    }

    static int generateID() {
        return atomicInt++;
    }

    public static void reset() {
        resultReceivers.clear();
    }


    //-------------//

    public interface ResultReceiver {
        void onReceiveResultFromFragment(Object resultObject);
    }


}
