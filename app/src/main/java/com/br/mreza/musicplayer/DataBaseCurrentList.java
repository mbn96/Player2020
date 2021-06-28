package com.br.mreza.musicplayer;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "current")
public class DataBaseCurrentList {


    @PrimaryKey(autoGenerate = true)
    public int id = 0;


    private String path;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public DataBaseCurrentList(String path) {
        this.path = path;
    }

}
