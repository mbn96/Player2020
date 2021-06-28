package com.br.mreza.musicplayer;


import android.content.Context;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MbnSearchEngine {

    private Context context;

    public MbnSearchEngine(Context context) {
        this.context = context;
    }

    private ArrayList<SearchItem> songs = new ArrayList<>();
    private ArrayList<SearchItem> albums = new ArrayList<>();
    private ArrayList<SearchItem> output = new ArrayList<>();

    public void inPut(String phrase) {

        songs.clear();
        albums.clear();

        songFinder(phrase);
        albumFinder(phrase);

    }

    public ArrayList<SearchItem> getList() {
        output.clear();

        if (songs.size() > 0) {
            output.add(new SearchItem(0, null, null, "Songs"));

            output.addAll(songs);

        } else {
            output.add(new SearchItem(0, null, null, "No result in songs !"));
        }

        if (albums.size() > 0) {

            output.add(new SearchItem(0, null, null, "Albums"));

            output.addAll(albums);


        } else {
            output.add(new SearchItem(0, null, null, "No result in albums !"));
        }


        return output;
    }

    private void songFinder(String phrase) {

        for (DataSong a : DataBaseHolder.getAllSongs(context)) {

            if (a.getTitle().toLowerCase().contains(phrase.toLowerCase())) {

                songs.add(new SearchItem(1, a, null, null));

            }

        }

    }


    private void albumFinder(String phrase) {


        for (DataBaseAlbum album : DataBaseHolder.getAlbums(context)) {

            if (album.getName().toLowerCase().contains(phrase.toLowerCase())) {

                albums.add(new SearchItem(2, null, album, null));
            }

        }

    }


    public class SearchItem {

        private int type;
        private DataSong song;
        private DataBaseAlbum album;
        private String header;

        SearchItem(int type, @Nullable DataSong songCode, @Nullable DataBaseAlbum album, @Nullable String header) {
            try {
                this.type = type;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.song = songCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.album = album;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.header = header;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public int getType() {
            return type;
        }

        public DataSong getSong() {
            return song;
        }

        public DataBaseAlbum getAlbum() {
            return album;
        }

        String getHeader() {
            return header;
        }
    }
}
