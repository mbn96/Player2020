package com.br.mreza.musicplayer.newmodel.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "current_table")
public class CurrentListElement {

    private long id;

    @PrimaryKey(autoGenerate = true)
    private int numberInRow;

    public int getNumberInRow() {
        return numberInRow;
    }

    public void setNumberInRow(int numberInRow) {
        this.numberInRow = numberInRow;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CurrentListElement(long id) {
        this.id = id;
    }
}
