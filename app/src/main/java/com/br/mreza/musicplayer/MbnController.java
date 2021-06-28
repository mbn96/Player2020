package com.br.mreza.musicplayer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MbnController {


    private final static String PACKAGE_NAME = "com.br.mreza.musicplayer";

    public final static String intentID = PACKAGE_NAME + "CONTROL";

    //    final static String ACTION_CHANGE = PACKAGE_NAME + "CONTROL_CHANGE";
//    final static String ACTION_PAUSE = PACKAGE_NAME + "CONTROL_PAUSE";
//    final static String ACTION_PLAY = PACKAGE_NAME + "CONTROL_PLAY";
//    final static String ACTION_STOP = PACKAGE_NAME + "CONTROL_STOP";
//    final static String ACTION_NEXT = PACKAGE_NAME + "CONTROL_NEXT";
//    final static String ACTION_PREVIOUS = PACKAGE_NAME + "CONTROL_PREVIOUS";
    public final static String To_FRAGMENT = PACKAGE_NAME + "DO_YOU_COPY";
    public final static String ON_TICK = PACKAGE_NAME + "ONTick";

//    public static MediaSessionCompat.Token mToken;
//    public static boolean isNewServiceAlive = false;
//    public static boolean playerState = false;
//    static boolean serviceWorking = false;
//    public static boolean playerFragmentIsOn = false;
//    static boolean shouldStart = false;
//    public static int seekPosition = 0;
//    public static int audioID;
//
//    public static WeakReference<NewPlayerService> playerServiceWeakReference;
//
//    private static ArrayList<Integer> shuffleIndex = new ArrayList<>();

//    public static int colorToBe;
//    public static int colorForText = Color.BLACK;
//    public static ArrayList<DataSong> unShuffledQueue = new ArrayList<>();


//    public static void next(Context context) {
//        currentTrackNumberInQueue = currentQueue.indexOf(getCurrentTrack());
//        seekPosition = 0;
//        MusicInfoHolder.setSeekPos(0);
//
////        prepareTheSong(context, 1);
//
//        if ((currentTrackNumberInQueue + 1) < currentQueue.size()) {
//            currentTrackNumberInQueue++;
//        } else {
//            currentTrackNumberInQueue = 0;
//        }
////        currentTrackCode = currentQueue.get(currentTrackNumberInQueue);
//        setCurrentTrack(currentQueue.get(currentTrackNumberInQueue));
//        findColor(context);
////        MediaSessionHolder.getControls(context).skipToNext();
//        if (isNewServiceAlive) playerServiceWeakReference.get().next_previous_change();
////        broadCast(context, ACTION_PLAY, intentID);
//        broadCast(context, To_FRAGMENT);
//
//        StorageUtils.setCurrent(context);
//    }
//
//    public static void previous(Context context) {
//
//
//        if (MusicInfoHolder.getCurrentPos() >= 3000) {
//
//            seekPosition = 0;
//            MusicInfoHolder.setSeekPos(0);
//            play(context);
//            StorageUtils.setCurrent(context);
//            return;
//
//        }
//
//        currentTrackNumberInQueue = currentQueue.indexOf(getCurrentTrack());
//
//        seekPosition = 0;
////        prepareTheSong(context, 2);
//        if ((currentTrackNumberInQueue - 1) >= 0) {
//            currentTrackNumberInQueue--;
//        } else {
//            currentTrackNumberInQueue = currentQueue.size() - 1;
//        }
////        currentTrackCode = currentQueue.get(currentTrackNumberInQueue);
//        setCurrentTrack(currentQueue.get(currentTrackNumberInQueue));
////        currentTrack = allSongsMap.get(currentTrackCode);
//        findColor(context);
////        MediaSessionHolder.getControls(context).skipToPrevious();
//        if (isNewServiceAlive) playerServiceWeakReference.get().next_previous_change();
////        broadCast(context, ACTION_PLAY, intentID);
//        broadCast(context, To_FRAGMENT);
//        StorageUtils.setCurrent(context);
//
//    }
//
//
//    private static void makeChange(List<DataSong> album, DataSong track, Context context) {
//
////        if (!currentQueue.containsAll(album.getSongsCodes()) && album.getSongsCodes().size() == currentQueue.size()) {
//
//
//        if (!album.equals(currentQueue)) {
//            unShuffledQueue.clear();
//            currentQueue.clear();
//            currentQueue.addAll(album);
//            unShuffledQueue.addAll(currentQueue);
////            currentQueue.setSongsCodes(album.getSongsCodes());
//            if (StorageUtils.isShuffle(context)) {
//                makeShuffle(context);
//            }
//        }
////        }
//
//
//        if (track == null) {
//            currentTrackNumberInQueue = 0;
//        } else {
//            findTrack(track);
//            currentTrackNumberInQueue = currentQueue.indexOf(getCurrentTrack());
//        }
////        currentTrackCode = currentQueue.get(currentTrackNumberInQueue);
////        currentTrack = track;
//
//    }
//
//
//    private static void findTrack(DataSong track) {
//
//
//        for (DataSong song : currentQueue) {
//
//            if (song.getPath().equals(track.getPath())) {
//
//                setCurrentTrack(song);
//                return;
//
//            }
//
//        }
//
//    }
//
//
//    public static void change(Context context, List<DataSong> album, DataSong trackCode) {
//
////        if (true) return;
//
//
//        if (album.equals(currentQueue) && trackCode.equals(getCurrentTrack())) {
//            return;
//        }
//
//        seekPosition = 0;
//
//        makeChange(album, trackCode, context);
//
////        if (StorageUtils.isShuffle(context)) {
////            makeShuffle();
////        }
//
//
////        try {
////            currentQueue.clear();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        switch (mode) {
////            case 0:
////                currentQueue = albumsList.get(albumPosition).getSongsForQueue();
////                break;
////            case 1:
////                allSongsQueueMaker();
////                break;
////            case 2:
////                break;
////        }
//
////        checkShuffle(cont);
//
////        if (trackCode == null) {
////
////            currentTrackNumberInQueue = 0;
////        } else {
////            currentTrackNumberInQueue = currentQueue.indexOf(trackCode);
////        }
////        currentTrackCode = currentQueue.get(currentTrackNumberInQueue);
////        currentTrack = allSongsMap.get(currentTrackCode);
//
//
////        findColor();
//        playerState = true;
//        findColor(context);
//        StorageUtils.setCurrent(context);
//        if (isNewServiceAlive) playerServiceWeakReference.get().next_previous_change();
//        else {
//            MusicInfoHolder.setSeekPos(0);
//            intentSender(context, ACTION_PLAY);
//        }
//        broadCast(context, To_FRAGMENT);
//    }
//
//    static void findColor(Context context) {
//
//
////        Palette.Builder builder = MbnBitmapBucket.makeColorBright(Palette.from(currentTrack.getCover()).generate().getDominantColor(Color.CYAN));
//        Palette palette = Palette.from(MbnController.getCover(context, getCurrentTrack().getPath(), 8)).generate();
//
////        colorToBe = palette.getDominantColor(Color.CYAN);
//        colorToBe = MbnBitmapBucket.makeColorBright(palette.getDominantColor(Color.CYAN));
//
////        colorForText = palette.getDominantSwatch().getTitleTextColor();
//        colorForText = palette.getVibrantColor(Color.BLACK);
//
//    }
//
//    public static void onComplete(Context context) {
//
//        if (StorageUtils.isRepeat(context)) {
//
//            seekPosition = 0;
//
//            MusicInfoHolder.setSeekPos(0);
//
//            play(context);
//
//        } else {
//
//            shouldStart = true;
//            next(context);
//        }
//
//
//    }
//
//    @SuppressWarnings("unchecked")
//    private static void prepareTheSong(Context context, int mode) {
//
//        if (StorageUtils.isShuffle(context)) {
//
//            Random random = new Random();
//
//            switch (mode) {
//                case 1:
//                    if (shuffleIndex.size() == currentQueue.size()) {
//                        shuffleIndex.clear();
//                    }
//                    if (shuffleIndex.size() == 0) {
//                        shuffleIndex.add(currentTrackNumberInQueue);
//                    }
//                    int tempPos = random.nextInt(currentQueue.size());
//                    while (shuffleIndex.contains(tempPos) && currentQueue.size() > 1) {
//                        tempPos = random.nextInt(currentQueue.size());
//                    }
//                    currentTrackNumberInQueue = tempPos;
//                    shuffleIndex.add(currentTrackNumberInQueue);
//                    break;
//                case 2:
//                    if (shuffleIndex.size() == currentQueue.size()) {
//                        shuffleIndex.clear();
//                    }
//                    if (shuffleIndex.size() == 0) {
//                        shuffleIndex.add(currentTrackNumberInQueue);
//                    }
//                    if (shuffleIndex.indexOf(currentTrackNumberInQueue) == 0) {
//
//                        prepareTheSong(context, 1);
//                    } else {
//
//                        currentTrackNumberInQueue = shuffleIndex.get(shuffleIndex.indexOf(currentTrackNumberInQueue) - 1);
//
//                    }
//
//
//                    break;
//
//
//            }
//
//
////            ArrayList<String> temp = new ArrayList<>();
////
////            Random random = new Random();
////
////            int rand;
////
////            while (currentQueue.size() > 0) {
////
////                rand = random.nextInt(currentQueue.size());
////
////                temp.add(currentQueue.get(rand));
////
////                currentQueue.remove(rand);
////            }
////            currentQueue.clear();
////            currentQueue.addAll(temp);
////            temp.clear();
//        } else {
//
//            switch (mode) {
//
//                case 1:
//
//                    if ((currentTrackNumberInQueue + 1) < currentQueue.size()) {
//                        currentTrackNumberInQueue++;
//                    } else {
//                        currentTrackNumberInQueue = 0;
//                    }
//                    break;
//                case 2:
//                    if ((currentTrackNumberInQueue - 1) >= 0) {
//                        currentTrackNumberInQueue--;
//                    } else {
//                        currentTrackNumberInQueue = currentQueue.size() - 1;
//                    }
//
//                    break;
//            }
//        }
//    }
//
//    public static void unShuffle(Context context) {
//
//
//        currentQueue.clear();
//        currentQueue.addAll(unShuffledQueue);
//        currentTrackNumberInQueue = currentQueue.indexOf(getCurrentTrack());
//
//
//    }
//
//    public static void makeUnshuffledList() {
//
//        unShuffledQueue.clear();
//        unShuffledQueue.addAll(currentQueue);
//
//        Comparator<DataSong> comparator = new Comparator<DataSong>() {
//
//
//            @Override
//            public int compare(DataSong o1, DataSong o2) {
//
//
//                return o1.getTitle().compareTo(o2.getTitle());
//            }
//
//        };
//        Collections.sort(unShuffledQueue, comparator);
//    }
//
//    public static void makeShuffle(Context context) {
//
//
//        ArrayList<DataSong> temp = new ArrayList<>();
//
//        Random random = new Random();
//
//        int rand;
//
//        while (currentQueue.size() > 0) {
//
//            rand = random.nextInt(currentQueue.size());
//
//            temp.add(currentQueue.get(rand));
//
//            currentQueue.remove(rand);
//        }
//        currentQueue.clear();
//        currentQueue.addAll(temp);
//        temp.clear();
//
//        currentTrackNumberInQueue = currentQueue.indexOf(getCurrentTrack());
//
//        broadCast(context, To_FRAGMENT);
//
//
//    }
//
//    public static void pause(Context context) {
//
//        playerState = false;
//        if (isNewServiceAlive) playerServiceWeakReference.get().pause();
////        MediaSessionHolder.getControls(context).pause();
////        broadCast(context, ACTION_PAUSE, intentID);
//        broadCast(context, To_FRAGMENT);
//    }
//
//    public static void play(Context context) {
//        playerState = true;
//        findColor(context);
//        if (!isNewServiceAlive) {
//            intentSender(context, ACTION_PLAY);
//        } else {
//            playerServiceWeakReference.get().play();
//        }
//        broadCast(context, To_FRAGMENT);
//    }
//
//    public static void stop(Context context) {
//
//        playerState = false;
//
////        MediaSessionHolder.getMediaSession(context).release();
//
//
////        broadCast(context, ACTION_STOP, intentID);
//        broadCast(context, To_FRAGMENT);
//        if (isNewServiceAlive) playerServiceWeakReference.get().stop();
//    }
//
//
//    public static void seek(Context context) {
//        if (isNewServiceAlive) playerServiceWeakReference.get().seek();
//    }
//
//    private static void intentSender(Context context, String action) {
//        Intent intent = new Intent(context, NewPlayerService.class);
//        intent.setAction(intentID);
//        intent.putExtra("MBN", action);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent);
//        } else {
//            context.startService(intent);
//        }
//
//    }
//
//    private static void broadCast(Context context, String intentAction) {
//
////        Log.i("MbnConBr", intentAction);
//
//        Intent intent = new Intent(intentAction);
////        intent.putExtra("MBN", (String) null);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
////        BackImageChangeHandler.instance.onTrackChange(context);
////        context.sendBroadcast(intent);
//
//
//    }

    public static String makeBitRate(String bitrate) {

        int bit = Integer.parseInt(bitrate) / 1000;

        return bit + " kbps";

    }

    public static String makeMillisToTime(long milli) {

        String minDuration = zeroAdder(Integer.toString((int) milli / (60 * 1000)));
        String secondDuration = zeroAdder(Integer.toString((int) ((milli / 1000) % 60)));

        return minDuration + ":" + secondDuration;
    }

    public static String timeConverter(double time, double duration) {

//        System.out.println(time);

        //-------------------//

//        String min = zeroAdder(Integer.toString((int) time / (60 * 1000)));
//        String minDuration = zeroAdder(Integer.toString((int) duration / (60 * 1000)));
//
//
//        String second = zeroAdder(Integer.toString((int) ((time / 1000) % 60)));
//        String secondDuration = zeroAdder(Integer.toString((int) ((duration / 1000) % 60)));

        //---------------------//

//        System.out.println(second);
//        System.out.println(secondDuration);

        return makeMillisToTime((long) time) + " | " + makeMillisToTime((long) duration);
//        return min + ":" + second + " | " + minDuration + ":" + secondDuration;
    }

    public static String justCurrentTime(double time) {
        String min = zeroAdder(Integer.toString((int) time / (60 * 1000)));
        String second = zeroAdder(Integer.toString((int) ((time / 1000) % 60)));
        return min + ":" + second;
    }

    private static String zeroAdder(String input) {

        if (input.length() < 2) {
            return "0" + input;
        } else {

            return input;
        }


    }

    public static Bitmap getCoverForAlbums(String path) {


        if (path != null) {

            return BitmapFactory.decodeFile(path);

        }


        return null;
    }


    public static Bitmap getCover(Context context, String data, int scale) {
        Bitmap cover;

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(data);
            byte[] coverArray = retriever.getEmbeddedPicture();
            retriever.release();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = scale;
//            options.inPreferredConfig= Bitmap.Config.ARGB_8888;
            cover = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);

        } catch (Exception e) {
            cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_1);

        }

        return cover;
    }


    public static String getBitrate(String data) {

        String rate = null;

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(data);
            rate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            retriever.release();
            retriever = null;
        } catch (Exception e) {
        }
        return rate;
    }


    public static Uri saveCoverForShare(String data, Context context, boolean share) {


        File coverFile = null;

        try {
            Bitmap cover;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(data);
            byte[] coverArray = retriever.getEmbeddedPicture();
            retriever.release();
            cover = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length);

            File dir;
            if (share) {
                dir = new File(context.getFilesDir(), "images");
            } else {
                dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Covers");
            }

            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null;
                }
            }

            if (share) {
                coverFile = new File(dir, "new_cover" + ".jpg");
            } else {
                coverFile = new File(dir, new SimpleDateFormat("yyyy.MM.dd_HHmmss").format(new Date()) + ".jpg");
            }

            FileOutputStream outputStream = new FileOutputStream(coverFile);
            cover.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            if (share) {
                return FileProvider.getUriForFile(context, "com.br.mreza.musicplayer.fileprovider", coverFile);
            } else {
                Toast.makeText(context, "Album art saved in : \n" + coverFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(coverFile)));

                return null;
            }


//            ContentResolver contentResolver = context.getContentResolver();

//            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?", new String[]{coverFile.getPath()}, null);

//            if (cursor != null && cursor.moveToFirst()) {
//
//                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
//                cursor.close();
//
//                return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));
//
//
//            } else if (coverFile.exists()) {
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DATA, coverFile.getAbsolutePath());
//                return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//            } else {
//                return null;
//            }

        } catch (Exception e) {
            return null;
        }


    }


}
