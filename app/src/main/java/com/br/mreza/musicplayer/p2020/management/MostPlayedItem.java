package com.br.mreza.musicplayer.p2020.management;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "most_played")
public class MostPlayedItem {

    @PrimaryKey
    private long id;

    private int lastPlay;
    private int totalPlay;

    public MostPlayedItem(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLastPlay() {
        return lastPlay;
    }

    public void setLastPlay(int lastPlay) {
        this.lastPlay = lastPlay;
    }

    public int getTotalPlay() {
        return totalPlay;
    }

    public void setTotalPlay(int totalPlay) {
        this.totalPlay = totalPlay;
    }
}
