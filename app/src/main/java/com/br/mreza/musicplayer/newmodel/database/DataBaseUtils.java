package com.br.mreza.musicplayer.newmodel.database;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newmodel.artwork.ArtCachePath;
import com.br.mreza.musicplayer.p2020.management.MostPlayedItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mbn.libs.imagelibs.imageworks.Effects2;
import mbn.libs.imagelibs.imageworks.MbnUtils;

import static android.content.Context.MODE_PRIVATE;


public class DataBaseUtils {

    private final static String CURRENT_CODE = "currentCodeNew";
    private final static String CURRENT_QUEUE = "currentQueueNew";

//    private static String TAG = "DATA_BASE";


    /*------------------------ Get Methods ----------------------*/

    private static void prepareTheSong(DataSong checkSong) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(checkSong.getPath());

            String chTitle;
            String chArtist;
            String chAlbum;
            String bitRate;


            chTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            chArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            chAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

            retriever.release();

            if (chTitle != null) {
                checkSong.setTitle(chTitle);
            }
            if (chAlbum != null) {
                checkSong.setAlbumTitle(chAlbum);
            }
            if (chArtist != null) {
                checkSong.setArtistTitle(chArtist);
            }
            if (bitRate != null) {
                checkSong.setBitRate(bitRate);
            }

        } catch (Exception ignored) {
        }
    }


    public static DataSong getSong(Context context, long id) {
//        DataSong song = DataBaseHolder.getInstance(context).getSongDAO().getSong(id);
//        if (!song.isProcessed()) {
//            prepareTheSong(song);
//            song.setProcessed(true);
//            DataBaseHolder.getInstance(context).getSongDAO().addSong(song);
//            song = DataBaseHolder.getInstance(context).getSongDAO().getSong(id);
//        }
//        return song;
        return DataBaseHolder.getInstance(context).getSongDAO().getSong(id);
    }

    public static DataSong getSong(long id) {
        return DataBaseHolder.getInstance().getSongDAO().getSong(id);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    static long getCurrentTrackID(Context context) {
        return getSharedPreferences(context).getLong(CURRENT_CODE, -1);
    }

    static ArrayList<Long> getQueueIds(Context context) {
        List<Long> currentIds = DataBaseHolder.getInstance(context).getSongDAO().currentIds();
        List<Long> existingIds = DataBaseHolder.getInstance(context).getSongDAO().methodToCheckCurrent(currentIds);
        ArrayList<Long> outIds = new ArrayList<>();
        for (long id : currentIds) {
            if (existingIds.contains(id)) outIds.add(id);
        }
        if (outIds.isEmpty()) {
            outIds.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadAllSongsIdsOrderedByName());
        }
        if (!outIds.equals(currentIds)) {
            setCurrentQueueInternal(context, outIds, false);
        }
        currentIds.clear();
        existingIds.clear();
        return outIds;
    }

    static ArrayList<DataSong> getSongs(Context context, long[] ids) {
        ArrayList<DataSong> songs = new ArrayList<>();
        for (long id : ids) {
            DataSong song = getSong(context, id);
            if (song != null) songs.add(song);
        }
        return songs;
    }

    static DataSong getCurrentTrack(Context context) {
        long currentId = getCurrentTrackID(context);
        DataSong currentSong = null;
        if (currentId != -1) {
            currentSong = getSong(context, currentId);
        }
        if (currentSong == null) {
            ArrayList<Long> quID = getQueueIds(context);
            if (quID.size() > 0) currentSong = getSong(context, quID.get(0));
            quID.clear();
        } else {
            ArrayList<Long> quID = getQueueIds(context);
            if (quID.size() > 0 && !quID.contains(currentSong.getId()))
                currentSong = getSong(context, quID.get(0));
            quID.clear();
        }
        if (currentSong == null) {
            List<Long> allIDs = DataBaseHolder.getInstance(context).getSongDAO().loadAllSongsIds();
            currentSong = getSong(context, allIDs.get(0));
            allIDs.clear();
        }
        return currentSong;
    }


    public static long[] listToArray(List<Long> longs) {
        long[] arr = new long[longs.size()];
        for (int i = 0; i < longs.size(); i++) {
            arr[i] = longs.get(i);
        }
        return arr;
    }






    /*------------------------- Set methods ---------------------------*/

    static boolean setCurrentTrackID(Context context, long id) {
        if (getCurrentTrackID(context) == id) return false;
        getSharedEditor(context).putLong(CURRENT_CODE, id).commit();
        return true;
    }

//    static boolean setCurrentQueue(Context context, List<Long> ids) {
//        return setCurrentQueue(context, listToArray(ids));
//    }

    static boolean setCurrentQueue(Context context, List<Long> ids, boolean checkShuffle) {
        ArrayList<Long> oldCurrent = getQueueIds(context);
        ArrayList<Long> inIDs = new ArrayList<>(ids);
        if (oldCurrent.equals(inIDs)) {
            oldCurrent.clear();
            inIDs.clear();
            return false;
        }
        oldCurrent.clear();
        inIDs.clear();
        DataBaseHolder.getInstance(context).getSongDAO().clearNewCurrent();
        ArrayList<CurrentListElement> elements = new ArrayList<>();
        for (long id : ids) {
            elements.add(new CurrentListElement(id));
        }
        if (checkShuffle && StorageUtils.isShuffle(context)) {
            Collections.shuffle(elements);
        }
        putTracksNumber(elements);
        DataBaseHolder.getInstance(context).getSongDAO().setCurrentNew(elements);
        elements.clear();
        return true;
    }

    private static void setCurrentQueueInternal(Context context, List<Long> ids, boolean checkShuffle) {
        DataBaseHolder.getInstance(context).getSongDAO().clearNewCurrent();
        ArrayList<CurrentListElement> elements = new ArrayList<>();
        for (long id : ids) {
            elements.add(new CurrentListElement(id));
        }
        if (checkShuffle && StorageUtils.isShuffle(context)) {
            Collections.shuffle(elements);
        }
        putTracksNumber(elements);
        DataBaseHolder.getInstance(context).getSongDAO().setCurrentNew(elements);
        elements.clear();
    }

    private static void putTracksNumber(ArrayList<CurrentListElement> elements) {
//        for (int i = 0; i < elements.size(); i++) {
//            elements.get(i).setNumberInRow(i);
//        }
    }


    /*---------------------------- Artwork management --------------------------*/

    public static final int ART_UNKNOWN = 0;
    public static final int ART_EMBEDDED = 1;
    public static final int ART_SEARCH_NEEDED = 2;
    public static final int ART_DOWNLOADED = 3;
    public static final int ART_NO_RESULT = 4;
    public static final int ART_CACHED = 5;

    public static Bitmap defaultArt;
    public static Bitmap defaultArt_blur;

    public static int findArtworkStatus(DataSong song) {
        if (song.getArtworkStatus() != 0) {
            return song.getArtworkStatus();
        }
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(song.getPath());
            if (retriever.getEmbeddedPicture() != null) {
                DataBaseHolder.getInstance().getSongDAO().setSongArtworkStatus(song.getId(), ART_EMBEDDED);
                retriever.release();
                return ART_EMBEDDED;
            }
        } catch (Exception ignored) {
        }
        DataBaseHolder.getInstance().getSongDAO().setSongArtworkStatus(song.getId(), ART_SEARCH_NEEDED);
        return ART_SEARCH_NEEDED;
    }

    /**
     * Generates a bitmap of artwork.
     *
     * @param id   Song id.
     * @param size Put 0 for no scaling.
     * @return The artwork of the song.
     */
    public static Bitmap getSongArtwork(long id, int size, boolean blur, BitmapHolder bitmapHolder) {
        DataSong song = getSong(id);
        int status = song.getArtworkStatus();
        if (status == ART_UNKNOWN) status = findArtworkStatus(song);
        Bitmap art;
        switch (status) {
            case ART_CACHED:
                art = getCachedArt(id, size);
                if (blur) {
                    bitmapHolder.onBitmapReady(getCachedArt_blur(id));
                }
                return art;
            case ART_EMBEDDED:
                art = getEmbeddedBitmap(id, song.getPath(), size);
                break;
//            case ART_DOWNLOADED:
//                art = getDownloadedArt(song.getDownloadedArtPath(), size);
//                break;
            default:
                art = getDefaultArt(id, size);
                if (blur) {
                    bitmapHolder.onBitmapReady(getDefaultArt_blur());
                }
                return art;
        }
        if (blur) {
            bitmapHolder.onBitmapReady(mbn.libs.imagelibs.imageworks.MbnUtils.stackBlur(mbn.libs.imagelibs.imageworks.MbnUtils.createSmallBit(art, 270), 12, true));
        }
        return art;
    }

    public interface BitmapHolder {
        void onBitmapReady(Bitmap bitmap);
    }

    private static Bitmap getCachedArt(long id, int size) {
        ArtCachePath cachePath = DataBaseHolder.getInstance().getSongDAO().getArtCache(id);
        if (size == 0) {
            Bitmap out = BitmapFactory.decodeFile(cachePath.getArt());
            return out == null ? getDefaultArt(id, size) : out;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(cachePath.getArt(), options);
        options.inSampleSize = Math.max(options.outHeight, options.outWidth) / size;
        options.inJustDecodeBounds = false;
        Bitmap out = MbnUtils.createSmallBit(BitmapFactory.decodeFile(cachePath.getArt(), options), size);
        return out == null ? getDefaultArt(id, size) : out;
    }

    private static Bitmap getCachedArt_blur(long id) {
        ArtCachePath cachePath = DataBaseHolder.getInstance().getSongDAO().getArtCache(id);
        Bitmap out = BitmapFactory.decodeFile(cachePath.getArt_blur());
        return out == null ? getDefaultArt_blur() : out;
    }

    //    private static int accent_backCells = mbn.libs.imagelibs.imageworks.MbnUtils.alphaChanger(Color.RED, 125);
//    private static int accent_backCells = mbn.libs.imagelibs.imageworks.MbnUtils.alphaChanger(Color.parseColor("#0288D1"), 25);
    private static int accent_backCells = mbn.libs.imagelibs.imageworks.MbnUtils.alphaChanger(Color.BLACK, 100);
    private static int accent = mbn.libs.imagelibs.imageworks.MbnUtils.colorWhitener2(Color.BLACK, 8);
    private static int accent_subTexts = Color.DKGRAY;
//    private static int accent_subTexts = Color.parseColor("#0288D1");

    private static Bitmap getDefaultArt(long id, int size) {
        if (true) {
            if (size == 0) size = 800;
            DataSong song = getSong(id);

            float subTextSize = size / 12f;
            Bitmap out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//            Bitmap textsBit = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas outCanvas = new Canvas(out);
//            Canvas textsCanvas = new Canvas(textsBit);
            outCanvas.drawColor(accent);
            Effects2.logoPrinter(outCanvas, size, size, 0.5f,
                    25, new Effects2.RandomCharacterPrinter(song.getTitle() + song.getAlbumTitle() + song.getArtistTitle(), accent_backCells), Color.LTGRAY);

            outCanvas.save();
            outCanvas.translate(0, -2 * subTextSize);
            Effects2.makeTextBitmap(outCanvas, size, size, song.getArtistTitle(), 1, Color.TRANSPARENT, accent_subTexts, subTextSize);
//            Effects2.makeTextShadow_2(outCanvas, size, size, song.getArtistTitle(), 1, 0, accent_subTexts, subTextSize, 120, size / 1.5f, Color.GRAY);
            outCanvas.restore();

            outCanvas.save();
            outCanvas.translate(0, 3 * subTextSize);
            Effects2.makeTextBitmap(outCanvas, size, size, song.getAlbumTitle(), 1, Color.TRANSPARENT, accent_subTexts, subTextSize);
            outCanvas.restore();

//            Effects2.makeTextBitmap(textsCanvas, size, size, song.getTitle(), 1, Color.TRANSPARENT, Color.WHITE, size / 8f);
            Effects2.makeTextShadow_2(outCanvas, size, size, song.getTitle(), 1, accent, Color.WHITE, size / 8f, 120, size / 1.5f, Color.GRAY);
//            Effects2.makeShadow_2(outCanvas, textsBit, 120, size / 1.5f, Color.GRAY, Color.TRANSPARENT);


//            return Effects2.makeTextShadow(size, size, song.getTitle() /*+ "\n" + song.getAlbumTitle()*/, 1, accent, Color.WHITE, Color.GRAY, size / 8f, size / 20);
//            return Effects2.makeTextShadow_2(size, size, song.getTitle(), 1, accent, Color.WHITE, size / 8f, 120, size / 1.5f, Color.GRAY);
            return out;
        }

        if (defaultArt == null) {
            defaultArt = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(defaultArt);
            canvas.drawColor(Color.CYAN);
        }
        return defaultArt.copy(Bitmap.Config.ARGB_8888, true);
    }

    private static Bitmap getDefaultArt_blur() {
        if (defaultArt_blur == null) {
            defaultArt_blur = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(defaultArt_blur);
            canvas.drawColor(Color.CYAN);
        }
        return defaultArt_blur.copy(Bitmap.Config.ARGB_8888, true);
    }

//    private static Bitmap getDownloadedArt(String path, int size) {
//        if (size == 0) {
//            Bitmap out = BitmapFactory.decodeFile(path);
//            return out == null ? getDefaultArt() : out;
//        }
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        options.inSampleSize = Math.max(options.outHeight, options.outWidth) / size;
//        options.inJustDecodeBounds = false;
//        Bitmap out = MbnUtils.createSmallBit(BitmapFactory.decodeFile(path, options), size);
//        return out == null ? getDefaultArt() : out;
//    }

    private static Bitmap getEmbeddedBitmap(long id, String path, int size) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            byte[] data = retriever.getEmbeddedPicture();
            retriever.release();
            if (size == 0) {
                Bitmap out = BitmapFactory.decodeByteArray(data, 0, data.length);
                return out == null ? getDefaultArt(id, size) : out;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            options.inSampleSize = Math.max(options.outHeight, options.outWidth) / size;
            options.inJustDecodeBounds = false;
            Bitmap out = MbnUtils.createSmallBit(BitmapFactory.decodeByteArray(data, 0, data.length, options), size);
            return out == null ? getDefaultArt(id, size) : out;
        } catch (Exception ignored) {
        }
        return getDefaultArt(id, size);
    }

    public static void findArtsStatus() {
        ArrayList<Long> zeroStatusIDs = (ArrayList<Long>) DataBaseHolder.getInstance().getSongDAO().getSongIDsArtUnknown();
        for (long id : zeroStatusIDs) {
            findArtworkStatus(getSong(id));
        }
    }


    //--------------------------- Played Time Management ---------------------------------//
    //------------------------------------------------------------------------------------//

    public static final int PLAY_TIME = 1;
    public static final int PAUSE_TIME = 2;

    public static void setPlayTime(long songID, int time, int op) {
        MostPlayedItem mostPlayedItem = DataBaseHolder.getInstance().getSongDAO().getPlayTime(songID);
        boolean isNull = mostPlayedItem == null;
        if (op == PLAY_TIME) {
            if (isNull) {
                mostPlayedItem = new MostPlayedItem(songID);
            }
            mostPlayedItem.setLastPlay(time);
            DataBaseHolder.getInstance().getSongDAO().putPlayTime(mostPlayedItem);
        } else if (op == PAUSE_TIME) {
            if (isNull) {
                return;
            }
            int diff = time - mostPlayedItem.getLastPlay();
            mostPlayedItem.setTotalPlay(mostPlayedItem.getTotalPlay() + diff);
            DataBaseHolder.getInstance().getSongDAO().putPlayTime(mostPlayedItem);
        }
    }

}


