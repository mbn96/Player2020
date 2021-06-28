package com.br.mreza.musicplayer.newmodel.database;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import com.br.mreza.musicplayer.DataBaseAlbum;
import com.br.mreza.musicplayer.DataBaseArtists;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newdesign.asyncloader.AsyncLoaderManager;
import com.br.mreza.musicplayer.newmodel.search.SearchResult;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;

public class DataBaseManager {
    private static final DataBaseManager INSTANCE = new DataBaseManager();

    public static DataBaseManager getManager() {
        return INSTANCE;
    }

    private DataBaseManager() {
    }

    private final BaseTaskHolder.ResultReceiver DATABASE_CHANGE_RECEIVER = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            Info information = (Info) info;

            if (result != null && ((int) result) == 96) {
                ThemeEngine.getINSTANCE().trackChanged(information.track);
                return;
            }
            if (information.trackChanged && haveContext()) {
                StorageUtils.setStartFromPos(contextWeakReference.get(), 0);
            }
            for (DefaultCallback callback : changeCallbacks) {
                callback.process(information);
            }
            if (information.shouldStart) {
                PlayerCommunicates.getINSTANCE().play();
            }
            if (information.trackChanged && haveContext()) {
                ThemeEngine.getINSTANCE().trackChanged(information.track);
            }
        }
    };

    private ThreadManager mainThreadManager = new ThreadManager();
    private ThreadManager multiThreadManager = new ThreadManager(2, false);
    private WeakReference<Context> contextWeakReference;
    private ArrayList<DefaultCallback> changeCallbacks = new ArrayList<>();
    //    private HashMap<Integer, BaseTaskHolder.ResultReceiver> receiverHashMap = new HashMap<>();
    private AtomicInteger atomicInteger = new AtomicInteger();
//    private final Object LOCK = new Object();

    public void init(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    public BaseTaskHolder getTaskHolder() {
        return mainThreadManager.getTaskHolder();
    }

    private synchronized int getResultCode() {
        return atomicInteger.incrementAndGet();
    }

    private boolean haveContext() {
        return contextWeakReference.get() != null;
    }

    public void registerCallback(DefaultCallback callback) {
        if (!changeCallbacks.contains(callback)) changeCallbacks.add(callback);
    }

    public void unRegisterCallback(DefaultCallback callback) {
        if (changeCallbacks.contains(callback)) changeCallbacks.remove(callback);
    }


    /*-------------------------------- Request methods ----------------------------------*/
    /*-----------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------*/


    public boolean getSongs(long[] ids, BaseTaskHolder.ResultReceiver resultReceiver) {
        if (haveContext()) {
            multiThreadManager.getTaskHolder().StartTask(new GetSongsTask(contextWeakReference.get(), ids), resultReceiver);
            return true;
        }
        return false;
    }

    public boolean executeCustomTask(Method method, Object info, boolean multiThread, BaseTaskHolder.ResultReceiver resultReceiver, Object... prams) {
        if (haveContext()) {
            if (multiThread) {
                multiThreadManager.getTaskHolder().StartTask(new CustomDataBaseTask(contextWeakReference.get(), info, method, prams), resultReceiver);
            } else {
                getTaskHolder().StartTask(new CustomDataBaseTask(contextWeakReference.get(), info, method, prams), resultReceiver);
            }
            return true;
        }
        return false;
    }

    public boolean getSong(long id, BaseTaskHolder.ResultReceiver resultReceiver) {
        if (haveContext()) {
            multiThreadManager.getTaskHolder().StartTask(new GetSingleSongTask(contextWeakReference.get(), id), resultReceiver);
            return true;
        }
        return false;
    }

    public boolean search(String searchString, BaseTaskHolder.ResultReceiver resultReceiver) {
        if (haveContext()) {
            getTaskHolder().StartTask(new SearchTask(contextWeakReference.get(), searchString), resultReceiver);
            return true;
        }
        return false;
    }

    public boolean getSongArtWork(long id, int size, BaseTaskHolder.ResultReceiver resultReceiver) {
        if (haveContext()) {
            getTaskHolder().StartTask(new GetSingleSongArtWorkTask(contextWeakReference.get(), id, size), resultReceiver);
            return true;
        }
        return false;
    }

    public boolean startDataBase() {
        if (haveContext()) {
            AsyncLoaderManager.INSTANCE.refresh(contextWeakReference.get());
            startThemeEngine();
            return true;
        }
        return false;
    }

    public boolean startThemeEngine() {
        if (haveContext()) {
            getTaskHolder().StartTask(new StartTheThemeEngine(contextWeakReference.get()), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }

    public boolean getCurrentQueueAndTrack(BaseTaskHolder.ResultReceiver resultReceiver) {
        if (haveContext()) {
            getTaskHolder().StartTask(new GetCurrentQueueAndTrack(contextWeakReference.get()), resultReceiver);
            return true;
        }
        return false;
    }

    public boolean performNext() {
        if (haveContext()) {
            getTaskHolder().StartTask(new NextTrackTask(contextWeakReference.get()), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }

    public boolean performPrevious() {
        if (haveContext()) {
            getTaskHolder().StartTask(new PreviousTrackTask(contextWeakReference.get()), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }

    public boolean setCurrentTrack(long id) {
        if (haveContext()) {
            getTaskHolder().StartTask(new SetTrackTask(contextWeakReference.get(), id), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }

    public boolean playAfterCurrent(long id) {
        if (haveContext()) {
            getTaskHolder().StartTask(new PlayNext(contextWeakReference.get(), id), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }

    public boolean addToQueue(ArrayList<Long> ids) {
        if (haveContext()) {
            getTaskHolder().StartTask(new AddToQueue(contextWeakReference.get(), ids), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }

    public boolean changeTrackAndQueue(ArrayList<Long> queue, long track) {
        if (haveContext()) {
            getTaskHolder().StartTask(new ChangeTrackAndQueue(contextWeakReference.get(), queue, track), DATABASE_CHANGE_RECEIVER);
            return true;
        }
        return false;
    }



    /* ----------------------- Call-backs ---------------------*/

    public interface ChangeCallback {
        void onTrackChange(long id, DataSong song);

        void onQueueChange(ArrayList<Long> ids);
    }

    public static class DefaultCallback implements ChangeCallback {

        private boolean wantTrack;
        private boolean wantQueue;

        public DefaultCallback(boolean wantTrack, boolean wantQueue) {
            this.wantTrack = wantTrack;
            this.wantQueue = wantQueue;
        }

        private void process(Info info) {
            if (wantQueue && info.queueChanged) {
                onQueueChange(info.queueIds);
            }
            if (wantTrack && info.trackChanged) {
                onTrackChange(info.track.getId(), info.track);
            }
        }

        @Override
        public void onTrackChange(long id, DataSong song) {

        }

        @Override
        public void onQueueChange(ArrayList<Long> ids) {

        }
    }

    /*----------------------  TASKS ---------------------------*/

    private static abstract class BaseContextTask implements BaseTaskHolder.BaseTask {

        private Context context;

        BaseContextTask(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        void releaseContext() {
            context = null;
        }

        protected abstract Object onTaskRun();

        @Override
        public Object onRun() {
            Object out = onTaskRun();
            releaseContext();
            return out;
        }
    }

    /*------------------------ InActives ----------------------*/

    private static class GetSongsTask extends BaseContextTask {

        private long[] ids;

        GetSongsTask(Context context, long[] ids) {
            super(context);
            this.ids = ids;
        }

        @Override
        protected Object onTaskRun() {
            ArrayList<DataSong> songArrayList = DataBaseUtils.getSongs(getContext(), ids);
            DataSong[] songs = (DataSong[]) songArrayList.toArray();
            songArrayList.clear();
            return songs;
        }

        @Override
        public Object getInfo() {
            return ids;
        }
    }

    private static class GetSingleSongTask extends BaseContextTask {

        private long id;

        GetSingleSongTask(Context context, long id) {
            super(context);
            this.id = id;
        }

        @Override
        protected Object onTaskRun() {
            return DataBaseUtils.getSong(getContext(), id);
        }

        @Override
        public Object getInfo() {
            return id;
        }
    }

    private static class SearchTask extends BaseContextTask {

        private String searchString;

        SearchTask(Context context, String searchString) {
            super(context);
            this.searchString = searchString;
        }

        @Override
        protected Object onTaskRun() {
            String sentString = "%" + searchString + "%";
            ArrayList<Long> songs = (ArrayList<Long>) DataBaseHolder.getInstance(getContext()).getSongDAO().searchSongs(sentString);
            ArrayList<DataBaseAlbum> albums = (ArrayList<DataBaseAlbum>) DataBaseHolder.getInstance(getContext()).getSongDAO().searchAlbums(sentString);
            ArrayList<DataBaseArtists> artists = (ArrayList<DataBaseArtists>) DataBaseHolder.getInstance(getContext()).getSongDAO().searchArtists(sentString);
            return new SearchResult(songs, albums, artists);
        }

        @Override
        public Object getInfo() {
            return searchString;
        }
    }

    private static class GetSingleSongArtWorkTask extends BaseContextTask {

        private long id;
        private int size;

        GetSingleSongArtWorkTask(Context context, long id, int size) {
            super(context);
            this.id = id;
            this.size = size;
        }

        @Override
        protected Object onTaskRun() {
//            DataSong song = DataBaseUtils.getSong(getContext(), id);
            //            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(song.getPath());
//                byte[] bytes = retriever.getEmbeddedPicture();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//                options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 600;
//                options.inJustDecodeBounds = false;
//                main = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//                Bitmap out = MbnUtils.createSmallBit(main, size);
//                main.recycle();
//                return out;
//            } catch (Exception ignored) {
//            }
            return DataBaseUtils.getSongArtwork(id, size, false, null);
        }

        @Override
        public Object getInfo() {
            return id;
        }
    }

    private static class GetCurrentQueueAndTrack extends BaseContextTask {

        GetCurrentQueueAndTrack(Context context) {
            super(context);
        }

        @Override
        protected Object onTaskRun() {
            ArrayList<Long> currentQueue = DataBaseUtils.getQueueIds(getContext());
            DataSong track = DataBaseUtils.getCurrentTrack(getContext());
            return new Object[]{currentQueue, track};
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }

    private static class CustomDataBaseTask extends BaseContextTask {

        private Object info;
        private Method method;
        private Object[] prams;

        public CustomDataBaseTask(Context context, Object info, Method method, Object... prams) {
            super(context);
            this.info = info;
            this.method = method;
            this.prams = prams;
        }

        @Override
        protected Object onTaskRun() {
            Object out = null;
            try {
                out = method.invoke(DataBaseHolder.getInstance(getContext()).getSongDAO(), prams);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return out;
        }

        @Override
        public Object getInfo() {
            return info;
        }
    }


    /*----------------------- Actives --------------------------*/

    private static abstract class DataBaseTask extends BaseContextTask {
        private Info info = new Info();

        DataBaseTask(Context context) {
            super(context);
        }

        void currentTrackChanged() {
            info.trackChanged = true;
            info.track = DataBaseUtils.getCurrentTrack(getContext());
            A_B_Manager.INSTANCE.request(A_B_Manager.SET_STATE_OFF, 0, null);
        }

        void currentQueueChanged() {
            info.queueChanged = true;
            info.queueIds = DataBaseUtils.getQueueIds(getContext());
        }

        void shouldStart() {
            info.shouldStart = true;
        }

        @Override
        public Object getInfo() {
            return info;
        }
    }

    private static class StartTheThemeEngine extends DataBaseTask {


        StartTheThemeEngine(Context context) {
            super(context);
        }

        @Override
        protected Object onTaskRun() {
            currentTrackChanged();
            return 96;
        }
    }

    private static class NextTrackTask extends DataBaseTask {

        NextTrackTask(Context context) {
            super(context);
        }

        @Override
        protected Object onTaskRun() {
            long currentID = DataBaseUtils.getCurrentTrackID(getContext());
            ArrayList<Long> queueIDs = DataBaseUtils.getQueueIds(getContext());
            int index = queueIDs.indexOf(currentID);
            index++;
            long newID;
            if (index >= 0 && index < queueIDs.size()) {
                newID = queueIDs.get(index);
            } else {
                newID = queueIDs.get(0);
            }
            DataBaseUtils.setCurrentTrackID(getContext(), newID);
            currentTrackChanged();
            queueIDs.clear();
            return null;
        }

    }

    private static class PreviousTrackTask extends DataBaseTask {


        PreviousTrackTask(Context context) {
            super(context);
        }

        @Override
        protected Object onTaskRun() {
            long currentID = DataBaseUtils.getCurrentTrackID(getContext());
            ArrayList<Long> queueIDs = DataBaseUtils.getQueueIds(getContext());
            int index = queueIDs.indexOf(currentID);
            index--;
            long newID;
            if (index >= 0 && index < queueIDs.size()) {
                newID = queueIDs.get(index);
            } else {
                newID = queueIDs.get(queueIDs.size() - 1);
            }
            DataBaseUtils.setCurrentTrackID(getContext(), newID);
            currentTrackChanged();
            queueIDs.clear();
            return null;
        }

    }

    private static class SetTrackTask extends DataBaseTask {

        private long newID;

        SetTrackTask(Context context, long newID) {
            super(context);
            this.newID = newID;
        }

        @Override
        protected Object onTaskRun() {
            if (DataBaseUtils.setCurrentTrackID(getContext(), newID)) {
                currentTrackChanged();
                shouldStart();
            }
            return null;
        }

    }

    private static class PlayNext extends DataBaseTask {

        private long id;

        PlayNext(Context context, long id) {
            super(context);
            this.id = id;
        }

        @Override
        protected Object onTaskRun() {
            long currentID = DataBaseUtils.getCurrentTrackID(getContext());
            if (id == currentID) {
                return null;
            }
            ArrayList<Long> queue = DataBaseUtils.getQueueIds(getContext());
            if (queue.contains(id)) {
                DataBaseHolder.getInstance(getContext()).getSongDAO().clearNewCurrent();
                queue.remove(queue.indexOf(id));
            }
            int index = queue.indexOf(currentID);
            queue.add(index + 1, id);
            DataBaseUtils.setCurrentQueue(getContext(), queue, false);
            currentQueueChanged();
            return null;
        }

    }

    private static class AddToQueue extends DataBaseTask {

        private ArrayList<Long> ids;

        AddToQueue(Context context, ArrayList<Long> ids) {
            super(context);
            this.ids = ids;
        }

        @Override
        protected Object onTaskRun() {
            ArrayList<Long> queue = DataBaseUtils.getQueueIds(getContext());
            for (long id : ids) {
                if (!queue.contains(id)) queue.add(id);
            }
            if (DataBaseUtils.setCurrentQueue(getContext(), queue, false)) currentQueueChanged();
            return null;
        }
    }

    private static class ChangeTrackAndQueue extends DataBaseTask {

        private ArrayList<Long> newQueue;
        private long newTrack;

        ChangeTrackAndQueue(Context context, ArrayList<Long> newQueue, long newTrack) {
            super(context);
            this.newQueue = newQueue;
            this.newTrack = newTrack;
        }

        @Override
        protected Object onTaskRun() {
            if (DataBaseUtils.setCurrentQueue(getContext(), newQueue, true)) currentQueueChanged();
            if (DataBaseUtils.setCurrentTrackID(getContext(), newTrack)) {
                currentTrackChanged();
                shouldStart();
            }
            return null;
        }

    }

    private static class Info {
        private boolean trackChanged = false;
        private boolean queueChanged = false;
        private boolean shouldStart = false;
        private ArrayList<Long> queueIds;
        private DataSong track;
    }

}
