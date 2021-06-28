package com.br.mreza.musicplayer;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import android.media.MediaMetadataRetriever;

@Entity(indices = {@Index(value = {"path"},
        unique = true)}, tableName = "songs")
public class DataSong {

    @PrimaryKey
    private long id = 0;

    private String title;


    private int duration;

    private String bitRate;


    private String artistTitle;
    private long artistID;

    private String albumTitle;

    private long albumID;

    private String path;

    private long dateAdded;

    private long size;

    private boolean isChecked;

    private boolean isProcessed = false;

    private int artworkStatus = 0;

    private int randomArtNum = 0;

//    private String downloadedArtPath;

//    private String blurredCover;


    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtistID() {
        return artistID;
    }

    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    public String getTitleForApp() {

//        System.out.println("rrrrrrrrrrrrrr  stars");

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

//        System.out.println("rrrrrrrrrrrrrr  instance");

        retriever.setDataSource(path);

//        System.out.println("rrrrrrrrrrrrrr  set");


        String meta = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

//        System.out.println("rrrrrrrrrrrrrr  get");


        retriever.release();


//        System.out.println("rrrrrrrrrrrrrr  release");


        if (meta != null) {

            return meta;
        }

        return title;

    }

    public String getAlbumForApp() {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(path);

        String meta = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        retriever.release();

        if (meta != null) {

            return meta;
        }

        return albumTitle;

    }

    public String getArtistForApp() {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(path);

        String meta = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        retriever.release();

        if (meta != null) {

            return meta;
        }

        return artistTitle;

    }

    public String getBitRate() {
        return bitRate;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public int getArtworkStatus() {
        return artworkStatus;
    }

    public void setArtworkStatus(int artworkStatus) {
        this.artworkStatus = artworkStatus;
    }

    public int getRandomArtNum() {
        return randomArtNum;
    }

    public void setRandomArtNum(int randomArtNum) {
        this.randomArtNum = randomArtNum;
    }

    //    public String getDownloadedArtPath() {
//        return downloadedArtPath;
//    }
//
//    public void setDownloadedArtPath(String downloadedArtPath) {
//        this.downloadedArtPath = downloadedArtPath;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

//    public String getBitRate() {
//        return bitRate;
//    }
//
//    public void setBitRate(String bitRate) {
//        this.bitRate = bitRate;
//    }


    public String getArtistTitle() {

        return artistTitle;
    }

    public void setArtistTitle(String artistTitle) {
        this.artistTitle = artistTitle;
    }

    public String getAlbumTitle() {

        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    //    public String getBlurredCover() {
//        return blurredCover;
//    }

//    public void setBlurredCover(String blurredCover) {
//        this.blurredCover = blurredCover;
//    }

    public DataSong(long id, String title, int duration, String artistTitle, long artistID, String albumTitle, String path, long dateAdded, long albumID, long size) {
        this.id = id;
        this.title = title;
        this.duration = duration;
//        this.bitRate = bitRate;
//        this.hashCode = hashCode;
        this.artistTitle = artistTitle;
        this.artistID = artistID;
        this.albumTitle = albumTitle;
        this.path = path;
        this.dateAdded = dateAdded;
        this.albumID = albumID;
        this.size = size;
//        this.blurredCover = blurredCover;
    }
}
