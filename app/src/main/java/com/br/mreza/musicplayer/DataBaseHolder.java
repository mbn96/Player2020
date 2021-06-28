package com.br.mreza.musicplayer;


import androidx.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHolder {

    public final static String LOADING_DONE = "MBN_Loading_done";

    final static int PLAY_LIST_ADDED = 1;
    final static int PLAY_LIST_NOT_ADDED_NAME_EXISTS = 2;


    private static MyRoomDataBase roomDataBase;

//    private static List<DataSong> songs;

//    private static List<DataBaseAlbum> albums;

    private static List<DataBasePlayList> playLists;


    private DataBaseHolder() {
    }

    public static synchronized MyRoomDataBase getInstance(Context context) {
        if (roomDataBase == null) {
//            roomDataBase = Room.databaseBuilder(context, MyRoomDataBase.class, "database").build();
            roomDataBase = Room.databaseBuilder(context, MyRoomDataBase.class, "database").allowMainThreadQueries().build();
        }
        return roomDataBase;
    }

    public static MyRoomDataBase getInstance() {
        return roomDataBase;
    }

    public static synchronized List<DataSong> getAllSongs(Context context) {
        return getInstance(context).getSongDAO().loadAllSongs();
    }

    static synchronized List<DataBaseAlbum> getAlbums(Context context) {

        return albumLoader(context);
//            albums = new ArrayList<>();
//            albums.addAll(getInstance(context).getSongDAO().loadAllAlbums());

    }


    private static ArrayList<DataBaseAlbum> albumLoader(Context context) {
        ArrayList<DataBaseAlbum> albums = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Uri albumsUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor albumsCursor = contentResolver.query(albumsUri, null, null, null, MediaStore.Audio.Albums.ALBUM + " ASC");

        if (albumsCursor != null && albumsCursor.getCount() > 0) {
            while (albumsCursor.moveToNext()) {

                long al_ID = albumsCursor.getLong(albumsCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String al_Name = albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String al_Data = albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                int num = albumsCursor.getInt(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));

                albums.add(new DataBaseAlbum(al_Name, al_Data, al_ID, num));

            }
            albumsCursor.close();
        }

        return albums;
    }

    public static void forceRefresh(Context context) {

//
//        if (songs != null) {
//
//            songs.clear();
//
//            songs.addAll(getInstance(context).getSongDAO().loadAllSongs());
//
//        }
//
//        if (albums != null) {
//
//            albums.clear();
//
////            albums.addAll(getInstance(context).getSongDAO().loadAllAlbums());
//            albums = null;
//        }


        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(LOADING_DONE));
//        context.sendBroadcast(new Intent(LOADING_DONE));

    }


    /*------------------------PLAY LIST MANAGEMENT------------------*/


    public static synchronized List<DataBasePlayList> getPlayLists(Context context) {
        if (playLists == null) {
            playLists = new ArrayList<>();
            playLists.addAll(getInstance(context).getSongDAO().getAllPlayLists());
        }
        return playLists;
    }

    static synchronized void buildPlayLists(Context context) {
        for (DataBasePlayList pl : getPlayLists(context)) {
            pl.buildList(context);
        }
    }

    static synchronized int addToPlayLists(Context context, List<Long> songs, String name) {
        for (DataBasePlayList pl : getPlayLists(context)) {
            if (pl.getName().equals(name)) return PLAY_LIST_NOT_ADDED_NAME_EXISTS;
        }

        String json = getJsonFromIDs(songs);
        DataBasePlayList playList = new DataBasePlayList(json, name);
        getInstance(context).getSongDAO().addPlayList(playList);
        playList.buildList(context);
        playLists.add(playList);

        return PLAY_LIST_ADDED;
    }

    static synchronized String getJsonFromList(List<DataSong> list) {
        if (list == null) {
            return "";
        }

        ArrayList<Long> songsIDs = new ArrayList<>();

        for (DataSong s : list) {
            songsIDs.add(s.getId());
        }

        Gson gson = new Gson();

        return "{songs=" + gson.toJson(songsIDs) + "}";

    }

    static synchronized String getJsonFromIDs(List<Long> list) {
        if (list == null || list.size() == 0) {
            return "";
        }

        Gson gson = new Gson();

        return "{songs=" + gson.toJson(list) + "}";
    }


}
