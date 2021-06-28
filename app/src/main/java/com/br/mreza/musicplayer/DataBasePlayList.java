package com.br.mreza.musicplayer;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "playLists")
public class DataBasePlayList {


    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String songsJson;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongsJson() {
        return songsJson;
    }

    public void setSongsJson(String songsJson) {
        this.songsJson = songsJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataBasePlayList(String songsJson, String name) {
        this.songsJson = songsJson;
        this.name = name;
    }



    /*----------------- non-DataBase part-----------------*/


    private void updateToDataBase(Context context) {

        DataBaseHolder.getInstance(context).getSongDAO().updatePlayList(this);
    }

    @Ignore
    private ArrayList<Long> songsIDs;

    @Ignore
    private ArrayList<DataSong> songs;

    public void addSongs(List<DataSong> list, Context context) {

        if (songs == null) {
            buildList(context);
        }
        songs.addAll(list);
        process(context);

    }

    public void addSongsWithIDs(List<Long> iDs, Context context) {
        if (songs == null) {
            buildList(context);
        }

        ArrayList<Long> longs = new ArrayList<>();
        for (long id : iDs) {
            if (!songsIDs.contains(id)) {
                longs.add(id);
            }
        }
        songs.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadSongsWithIDs(longs));
        process(context);


    }

    public void removeSongsWithIDs(List<Long> iDs, Context context) {
        if (songs == null) {
            buildList(context);
        }

        ArrayList<Long> longs = new ArrayList<>();
        for (long id : iDs) {
            if (songsIDs.contains(id)) {
                longs.add(id);
            }
        }
        songs.removeAll(DataBaseHolder.getInstance(context).getSongDAO().loadSongsWithIDs(longs));
        process(context);
    }

    public void removeSongWithID(long iD, Context context) {
        if (songs == null) {
            buildList(context);
        }
        ArrayList<Long> longs = new ArrayList<>();
        if (songsIDs.contains(iD)) {
            longs.add(iD);


            songs.removeAll(DataBaseHolder.getInstance(context).getSongDAO().loadSongsWithIDs(longs));
            process(context);
        }
    }

    public void removeSongs(List<DataSong> list, Context context) {
        if (songs == null) {
            buildList(context);
        }
        songs.removeAll(list);
        process(context);

    }

    public void removeSong(DataSong song, Context context) {
        if (songs == null) {
            buildList(context);
        }
        songs.remove(song);
        process(context);

    }

    public ArrayList<DataSong> getSongs(Context context) {

        if (songs == null) {
            buildList(context);
        }
        return songs;
    }

    public ArrayList<Long> getSongIDs(Context context) {

        if (songs == null) {
            buildList(context);
        }
        return songsIDs;
    }

    public void buildList(Context context) {

        songs = new ArrayList<>();
        songsIDs = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(songsJson);
            JSONArray jsonArray = jsonObject.optJSONArray("songs");
            for (int i = 0; i < jsonArray.length(); i++) {
                songsIDs.add(jsonArray.optLong(i));
            }

            songs.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadSongsWithIDs(songsIDs));
            process(context);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void process(Context context) {

        songsIDs.clear();

        for (DataSong s : songs) {
            songsIDs.add(s.getId());
        }

        songsJson = DataBaseHolder.getJsonFromIDs(songsIDs);

        updateToDataBase(context);

    }
}
