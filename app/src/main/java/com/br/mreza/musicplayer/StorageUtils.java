package com.br.mreza.musicplayer;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class StorageUtils {


    private final static String CURRENT_CODE = "currentCode";
    private final static String CURRENT_QUEUE = "currentQueue";
    private final static String PLAY_LISTS = "PlayLists";
    private final static String SHUFFLE = "shuffle";
    private final static String REPEAT = "repeat";
    private final static String TIMER = "timer";
    private final static String SHOW_ART = "art_show";
    private final static String BLUR_ART = "blur_art";
    private final static String SEEK_POS = "seek_pos";
    private final static String THEME_TYPE = "theme_code";


    private static boolean showAlbumArt;
    private static boolean showBlurAlbumArt;
    private static int themeType = 1;

//    private static void getCurrentList(Context context) {
//
//        List<String> tempList = new ArrayList<>();
//
//        for (DataBaseCurrentList listy : DataBaseHolder.getInstance(context).getSongDAO().getCurrentList()) {
//            if (listy != null) {
//                tempList.add(listy.getPath());
//            }
//        }
//        currentQueue.clear();
//        currentQueue.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadSongs(tempList));
//
////        DataSong temp = null;
////        try {
////            temp = DataBaseHolder.getInstance(context).getSongDAO().loadSong(listy.getPath()).get(0);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        if (temp != null) {
////        }
////        temp = null;
//    }
//
//
//    private static boolean checkCurrent(String path) {
//
//        if (currentQueue.size() > 0) {
//
//            for (DataSong s : currentQueue) {
//
//                if (s.getPath().equals(path)) {
//                    setCurrentTrack(s);
//                    return true;
//                }
//
//            }
//
//
//        }
//
//        return false;
//    }
//
//
//    private static void makeCurrent(Context context) {
//
//        currentQueue.clear();
//        currentQueue.addAll(DataBaseHolder.getAllSongs(context));
////        currentQueue.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadAllSongs());
//
//        setCurrentTrack(currentQueue.get(0));
//
//
//    }

//    @SuppressWarnings("unchecked")
//    static void getPreferences(Context context) {
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
//
//        showAlbumArt = sharedPreferences.getBoolean(SHOW_ART, true);
//        showBlurAlbumArt = sharedPreferences.getBoolean(BLUR_ART, false);
//        themeType = sharedPreferences.getInt(THEME_TYPE, 0);
//
////        String playListsString = sharedPreferences.getString(PLAY_LISTS, null);
////
////        Gson gson = new Gson();
////
////        if (playListsString == null) {
////            setPlayLists(context);
////        } else {
////            Type type = new TypeToken<ArrayList<MbnAlbum>>() {
////            }.getType();
////
////            playLists = playlistChecker((ArrayList<MbnAlbum>) gson.fromJson(playListsString, type));
////        }
//
//
//        getCurrentList(context);
//
//
//        String currentTrackString = sharedPreferences.getString(CURRENT_CODE, null);
//
//        if (!(currentTrackString != null && checkCurrent(currentTrackString))) {
//
//            if (currentQueue.size() > 0) {
//
//                setCurrentTrack(currentQueue.get(0));
//
//            } else {
//
//                makeCurrent(context);
//
//            }
//
//        }
//
////        if (currentTrackString != null) {
////
////
////            currentTrack = DataBaseHolder.getInstance(context).getSongDAO().loadSong(currentTrackString).get(0);
////
////            if (currentTrack == null) {
////
////                currentTrack = DataBaseHolder.getInstance(context).getSongDAO().loadAllSongs().get(0);
////
////            }
////
////        } else {
////
////
////            ListMaker.allSongsQueueMaker();
////
////            currentTrackCode = currentQueue.get(0);
////
////        }
////
////        currentTrack = allSongsMap.get(currentTrackCode);
////
////
////        String currentQuJson = sharedPreferences.getString(CURRENT_QUEUE, null);
////
////        if (currentQuJson == null) {
////
////            ListMaker.allSongsQueueMaker();
////
////
////        } else {
////
////            Type type2 = new TypeToken<ArrayList<String>>() {
////            }.getType();
////
////            try {
////                currentQueue.clear();
////                currentQueue.addAll(generalCheck((ArrayList<String>) gson.fromJson(currentQuJson, type2)));
////                currentQueue.setSongsCodes(currentQueue);
////            } catch (JsonSyntaxException e) {
////
////                ListMaker.allSongsQueueMaker();
////            }
////
////        }
////
////        currentTrackNumberInQueue = currentQueue.indexOf(currentTrackCode);
//
//        currentTrackNumberInQueue = currentQueue.indexOf(getCurrentTrack());
//
//        MbnController.findColor(context);
//
//        makeUnshuffleQueue();
//
//
//    }


//    private static void makeUnshuffleQueue() {
//
//        unShuffledQueue.clear();
//        unShuffledQueue.addAll(currentQueue);
//
//
//        <DataSong> comparator = new Comparator<DataSong>() {
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
////        Collections.sort(unShuffledQueue, comparator);
//
//    }

//    private static ArrayList<MbnAlbum> playlistChecker(ArrayList<MbnAlbum> temp) {
//
//        for (MbnAlbum album : temp) {
//
//            album.setSongsCodes(generalCheck(album.getSongsForQueue()));
//
//        }
//
//
//        return temp;
//    }

//    private static ArrayList<String> generalCheck(ArrayList<String> temp) {
//
//
//        try {
//            for (String code : temp) {
//
//                if (!allSongsCodes.contains(code)) {
//
//                    temp.remove(code);
//
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return temp;
//    }


//    public static void setCurrent(final Context context) {
//
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
////
////                SharedPreferences.Editor editor = sharedPreferences.edit();
////
////                editor.putString(CURRENT_CODE, currentTrackCode);
////
////                Gson gson = new Gson();
////
////                String jsonCurrentQueue = gson.toJson(currentQueue);
////
////                editor.putString(CURRENT_QUEUE, jsonCurrentQueue);
////
////                editor.apply();
////
////            }
////        }).start();
//
//
//        new SaveCurrent().execute(context);
//
//
////        DataBaseHolder.getInstance(context).getSongDAO().clearCurrentList();
////        DataBaseHolder.getInstance(context).getSongDAO().setCurrent(createCurrentListTable());
//
////        Gson gson = new Gson();
//
////        String jsonCurrentQueue = gson.toJson(currentQueue);
//
////        editor.putString(CURRENT_QUEUE, jsonCurrentQueue);
//
//
//    }

//    private static List<DataBaseCurrentList> createCurrentListTable() {
//
//        List<DataBaseCurrentList> temp = new ArrayList<>();
//
//        for (DataSong s : currentQueue) {
//
//            temp.add(new DataBaseCurrentList(s.getPath()));
//
//        }
//
//        return temp;
//    }

//    private static class SaveCurrent extends AsyncTask<Context, Void, Void> {
//
//
//        @SuppressLint("ApplySharedPref")
//        @Override
//        protected Void doInBackground(Context... contexts) {
//
//            SharedPreferences sharedPreferences = contexts[0].getSharedPreferences(contexts[0].getPackageName(), MODE_PRIVATE);
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            editor.putString(CURRENT_CODE, getCurrentTrack().getPath());
//
//            editor.commit();
//
//            DataBaseHolder.getInstance(contexts[0]).getSongDAO().clearCurrentList();
//            DataBaseHolder.getInstance(contexts[0]).getSongDAO().setCurrent(createCurrentListTable());
//
//            return null;
//        }
//
//
//    }


//    static void setPlayLists(Context context) {
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        Gson gson = new Gson();
//
//        String json = gson.toJson(playLists);
//
//        editor.putString(PLAY_LISTS, json);
//
//        editor.apply();
//
//
//    }

    public static boolean isShuffle(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);


        return sharedPreferences.getBoolean(SHUFFLE, false);
    }

    public static boolean isRepeat(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);


        return sharedPreferences.getBoolean(REPEAT, false);
    }

    public static void setShuffle(Context context, boolean shuffleMode) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHUFFLE, shuffleMode);

        editor.commit();


    }

    public static void setRepeat(Context context, boolean repeatMode) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(REPEAT, repeatMode);

        editor.commit();


    }

    public static void setShowAlbumArt(Context context, boolean showAlbumArt) {

        StorageUtils.showAlbumArt = showAlbumArt;

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHOW_ART, showAlbumArt);

        editor.commit();


    }

    public static void setShowBlurArt(Context context, boolean showBlurAlbumArt) {

        StorageUtils.showBlurAlbumArt = showBlurAlbumArt;

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(BLUR_ART, showBlurAlbumArt);

        editor.commit();


    }

    public static void setThemeType(Context context, int themeType) {

        StorageUtils.themeType = themeType;

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(THEME_TYPE, themeType);

        editor.commit();


    }


    public static void setTimer(Context context, long time) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(TIMER, time);

        editor.commit();

    }

    public static long getTimer(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        return sharedPreferences.getLong(TIMER, 0);
    }

    public static void setStartFromPos(Context context, long time) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(SEEK_POS, time);

        editor.commit();

    }

    public static long getStartFromPos(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        return sharedPreferences.getLong(SEEK_POS, 0);
    }


    public static boolean isShowAlbumArt() {
        return showAlbumArt;
    }

    public static int getThemeType() {
        return themeType;
    }

    public static boolean isShowBlurAlbumArt() {
        return showBlurAlbumArt;
    }
}
