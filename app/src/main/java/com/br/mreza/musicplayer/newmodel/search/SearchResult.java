package com.br.mreza.musicplayer.newmodel.search;


import com.br.mreza.musicplayer.DataBaseAlbum;
import com.br.mreza.musicplayer.DataBaseArtists;

import java.util.ArrayList;

public class SearchResult {

    private ArrayList<Long> songsID;
    private ArrayList<DataBaseAlbum> albums;
    private ArrayList<DataBaseArtists> artists;

    public SearchResult(ArrayList<Long> songsID, ArrayList<DataBaseAlbum> albums, ArrayList<DataBaseArtists> artists) {
        this.songsID = songsID;
        this.albums = albums;
        this.artists = artists;
    }

    public ArrayList<Long> getSongsID() {
        return songsID;
    }

    public ArrayList<DataBaseAlbum> getAlbums() {
        return albums;
    }

    public ArrayList<DataBaseArtists> getArtists() {
        return artists;
    }
}
