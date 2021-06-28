package com.br.mreza.musicplayer.newmodel.artwork;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "art_paths")
public class ArtCachePath {

    @PrimaryKey
    private long id;

    private String art;
    private String art_blur;

//    public ArtCachePath(long id) {
//        this.id = id;
//    }

    public ArtCachePath(long id, String art, String art_blur) {
        this.id = id;
        this.art = art;
        this.art_blur = art_blur;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getArt_blur() {
        return art_blur;
    }

    public void setArt_blur(String art_blur) {
        this.art_blur = art_blur;
    }
}
