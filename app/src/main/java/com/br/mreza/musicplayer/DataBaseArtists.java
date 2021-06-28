package com.br.mreza.musicplayer;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artists")
public class DataBaseArtists {


    @PrimaryKey
    private long id;

    private String name;

    private int numberOfTracks;

    private int numberOfAlbums;


    public DataBaseArtists(long id, String name, int numberOfTracks, int numberOfAlbums) {
        this.id = id;
        this.name = name;
        this.numberOfTracks = numberOfTracks;
        this.numberOfAlbums = numberOfAlbums;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
    }
}
