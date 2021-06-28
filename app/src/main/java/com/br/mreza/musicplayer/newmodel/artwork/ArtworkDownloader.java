package com.br.mreza.musicplayer.newmodel.artwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import com.br.mreza.musicplayer.AlbumArtTaskForSongs;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.newdesign.asyncloader.AsyncLoaderManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.MultiThreadSequentialTasks;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.imagelibs.imageworks.MbnUtils;
import mbn.libs.io.DownloadManager;
import mbn.libs.io.IO_Utils;

public class ArtworkDownloader extends MultiThreadSequentialTasks<Long, int[]> {

    private static final ArtworkDownloader INSTANCE = new ArtworkDownloader();

    public static void initArtworkDownloader(Context context) {
        INSTANCE.init(context);
    }


    private static final String KEY = "mNUlNEuPXdmVKobzJlTN";
    private static final String SECRET = "ejjAYQoPEQOmeRQGQWeWwqKpMtOUySOS";
    private static final String BASE_URL = "https://api.discogs.com/database/search";
    private static final IO_Utils.UrlPart KEY_PART = new IO_Utils.UrlPart("key", KEY);
    private static final IO_Utils.UrlPart SECRET_PART = new IO_Utils.UrlPart("secret", SECRET);
    private static final IO_Utils.UrlPart PER_PAGE_PART = new IO_Utils.UrlPart("per_page", "5");
    private static final IO_Utils.UrlPart PAGE_PART = new IO_Utils.UrlPart("page", "1");

    private static final int RESULT_OK = 0;
    private static final int RESULT_RETRY = 1;
    private static final int RESULT_NOT_FOUND = 3;

    private static final int MAX_SIZE = 500;

    private final Object LOCK = new Object();
    private long previousRequest;
    private boolean hasInternet = false;
    private ConnectivityManager connectivityManager;
    private WeakReference<Context> contextReference;
    private File dl_dir;
    private File covers_dir;


    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            checkForInternet();
        }

        @Override
        public void onLost(Network network) {
            checkForInternet();
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            checkForInternet();
        }
    };
    private ArrayList<Long> inProgress = new ArrayList<>();

    private ArtworkDownloader() {
        super(0, 2, false);
    }

    private void checkForInternet() {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            hasInternet = true;
            unPause();
        } else {
            hasInternet = false;
            pause();
        }
    }

    private void init(Context context) {
        contextReference = new WeakReference<>(context);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

        dl_dir = new File(context.getFilesDir(), "DL_covers");
        covers_dir = new File(context.getFilesDir(), "covers");
        if (!dl_dir.exists()) dl_dir.mkdirs();
        if (!covers_dir.exists()) covers_dir.mkdirs();

        connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
        AsyncLoaderManager.INSTANCE.registerFinishListener(new AsyncLoaderManager.FinishListener() {
            @Override
            public void onFinish() {
                checkForNew();
            }
        });
    }

    private void checkForNew() {
        ThreadManager.getAppGlobalTask_MultiThread().StartTask(new BaseTaskHolder.BaseTask() {
            @Override
            public Object onRun() {
                DataBaseUtils.findArtsStatus();
                return DataBaseHolder.getInstance().getSongDAO().getSongIDsForSearch();
            }

            @Override
            public Object getInfo() {
                return null;
            }
        }, new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                @SuppressWarnings("unchecked") ArrayList<Long> ids = (ArrayList<Long>) result;
                for (long id : ids) {
                    if (!inProgress.contains(id)) sendRequest(id);
                }
                ThreadManager.getAppGlobalTask_MultiThread().StartTask(new CacheEmbedded(DataBaseHolder.getInstance().getSongDAO().getSongIDsForArtStatus(DataBaseUtils.ART_EMBEDDED)
                        , covers_dir), (result1, info1) -> {

                });

            }
        });
    }


    @Override
    public void sendRequest(Long request) {
        super.sendRequest(request);
        inProgress.add(request);
    }

    @Override
    public int[] neededProcess(Long request) {
        int[] result = new int[2];
        DataSong song = DataBaseUtils.getSong(request);
        String query = song.getTitle();
        if (!song.getArtistTitle().isEmpty() && !song.getArtistTitle().toLowerCase().equals("unknown")) {
            query += (" " + song.getArtistTitle());
        }
        synchronized (LOCK) {
//            while (System.currentTimeMillis() - previousRequest < 1000) ;
            long slp_time = System.currentTimeMillis() - previousRequest;
            if (slp_time < 1000) {
                try {
                    Thread.sleep(1000 - slp_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            previousRequest = System.currentTimeMillis();
        }
        String response = IO_Utils.getTextResponse(IO_Utils.makeURL(BASE_URL,
                KEY_PART, SECRET_PART, PER_PAGE_PART, PAGE_PART, new IO_Utils.UrlPart("query", query)),
                null);
        if (response == null || response.isEmpty()) {
            result[0] = RESULT_RETRY;
            return result;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonResults = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonResults.length(); i++) {
                String dlPath = jsonResults.optJSONObject(i).optString("cover_image");
                if (!dlPath.isEmpty()) {
                    if (dlPath.endsWith("gif")) continue;
                    int dlID = DownloadManager.requestDownload(getDlFile(dl_dir, request), IO_Utils.makeURL(dlPath, null), false);
                    DownloadManager.setOptionalKey(dlID, request);
                    DownloadManager.registerCallback(dlID, downloadCallback);
                    result[0] = RESULT_OK;
                    return result;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result[0] = RESULT_NOT_FOUND;
        DataBaseHolder.getInstance().getSongDAO().setSongArtworkStatus(request, DataBaseUtils.ART_NO_RESULT);
        return result;
    }

    private File getDlFile(File dir, long id) {
//        File dir = new File(Environment.getExternalStorageDirectory(), "DrMusic" + File.separator + "covers");
//        File dir = new File(contextReference.get().getFilesDir(), "DL_covers");
//        File dir = contextReference.get().getDir("downloaded images", Context.MODE_PRIVATE);
        return new File(dir, id + "_" + System.currentTimeMillis() + ".jpg");
    }

    private final DownloadManager.DownloadCallback downloadCallback = new DownloadManager.DownloadCallback() {
        @Override
        public void onFinish(int id, Object optionalKey, File dest) {
            long songID = (long) optionalKey;
            ThreadManager.getAppGlobalTask_MultiThread().StartTask(new PutInDatabase(songID, dest.getPath(), covers_dir), (result, info) -> {
            });
        }

        @Override
        public void onCanceled(int id, Object optionalKey) {

        }

        @Override
        public void onStarted(int id, Object optionalKey) {

        }

        @Override
        public void onProgress(int id, int progress, Object optionalKey) {

        }
    };

    @Override
    public void onFinalResult(Long request, int[] result) {
        if (result[0] == RESULT_RETRY) {
            sendRequest(request);
        }
    }

    private static class PutInDatabase implements BaseTaskHolder.BaseTask {
        private long songID;
        private String path;
        private File dir;

        PutInDatabase(long songID, String path, File dir) {
            this.songID = songID;
            this.path = path;
            this.dir = dir;
        }

        @Override
        public Object onRun() {
//            DataBaseHolder.getInstance().getSongDAO().setSongArtworkStatus(songID, DataBaseUtils.ART_DOWNLOADED);
//            DataBaseHolder.getInstance().getSongDAO().setSongDownloadedArtworkPath(songID, path);
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = Math.max(options.outHeight, options.outWidth) / MAX_SIZE;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap != null) putCache(bitmap, songID, dir);
            AlbumArtTaskForSongs.HOLDER_MAP.remove((int) songID);
            return null;
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }

    private static void putCache(Bitmap bitmap, long id, File dir) {
        try {
            Bitmap out = MbnUtils.createSmallBit(bitmap, MAX_SIZE);
            bitmap.recycle();
            Bitmap blur = MbnUtils.stackBlur(MbnUtils.createSmallBit(out, 270), 12, true);

            File mainFile = new File(dir, id + "_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream stream_main = new FileOutputStream(mainFile);

            File blurFile = new File(dir, id + "_blur_" + System.currentTimeMillis() + ".png");
            FileOutputStream stream_blur = new FileOutputStream(blurFile);


            out.compress(Bitmap.CompressFormat.JPEG, 100, stream_main);
            stream_main.flush();
            stream_main.close();

            blur.compress(Bitmap.CompressFormat.PNG, 100, stream_blur);
            stream_blur.flush();
            stream_blur.close();

            ArtCachePath cachePath = new ArtCachePath(id, mainFile.getPath(), blurFile.getPath());
            DataBaseHolder.getInstance().getSongDAO().addArtCache(cachePath);
            DataBaseHolder.getInstance().getSongDAO().setSongArtworkStatus(id, DataBaseUtils.ART_CACHED);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class CacheEmbedded implements BaseTaskHolder.BaseTask {

        private List<Long> ids;
        private File dir;

        CacheEmbedded(List<Long> ids, File dir) {
            this.ids = ids;
            this.dir = dir;
        }

        @Override
        public Object onRun() {
            for (long id : ids) {
                try {
                    String path = DataBaseHolder.getInstance().getSongDAO().getSongPath(id);
                    MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                    metadataRetriever.setDataSource(path);
                    byte[] bytes = metadataRetriever.getEmbeddedPicture();
                    metadataRetriever.release();

                    Bitmap bitmap;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                    options.inSampleSize = Math.max(options.outHeight, options.outWidth) / MAX_SIZE;
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                    if (bitmap != null) putCache(bitmap, id, dir);
                } catch (Exception ignored) {
                }
            }
            return null;
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }

}
