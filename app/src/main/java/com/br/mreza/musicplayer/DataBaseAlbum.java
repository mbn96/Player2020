package com.br.mreza.musicplayer;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name"}
)}, tableName = "albums")
public class DataBaseAlbum {

    @PrimaryKey
    private long id;

    private String name;

    private String data;

    private int numberOfTracks;

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }


    //    private long albumID;

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

    public String getData() {
        return data;
    }


//    public long getAlbumID() {
//        return albumID;
//    }

//    public void setAlbumID(long albumID) {
//        this.albumID = albumID;
//    }

    public void setData(String data) {
        this.data = data;
    }

    public DataBaseAlbum(String name, String data, long id, int numberOfTracks) {
        this.name = name;
        this.data = data;
        this.id = id;
        this.numberOfTracks = numberOfTracks;
    }
}
