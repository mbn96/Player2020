//package com.br.mreza.musicplayer;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.MediaMetadataRetriever;
//
//import java.util.Random;
//
//class MbnSong {
//
////        private class InformationCatcher extends AsyncTask<Void, Void, Void> {
////            @Override
////            protected Void doInBackground(Void... voids) {
////
////                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////                retriever.setDataSource(path);
////                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
////                retriever.release();
////
////                return null;
////            }
////        }
//
//
//    MbnSong(String title, String albumTitle, String path, String artist, int duration, long dateAdded) {
//        this.title = title;
//        this.albumTitle = albumTitle;
//        this.path = path;
//        this.hashCode = title + path;
//        this.artist = artist;
//        this.duration = duration;
//        this.dateAdded = dateAdded;
//
////            new InformationCatcher().execute();
//
//
//    }
//
//    private long dateAdded;
//
//    public long getDateAdded() {
//        return dateAdded;
//    }
//
//    @Override
//    public String toString() {
//
//
//        return title;
//    }
//
//    String getHashCode() {
//        return hashCode;
//    }
//
//    private String title;
//
//    int getDuration() {
//        return duration;
//    }
//
//    private int duration;
//
//    private String bitRate;
//
//    private String hashCode;
//
//    private String artist;
//
//    private String albumTitle;
//    private String path;
//
//
//    private Bitmap cover;
//
//    private void catchBitrate() {
//
//        try {
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(path);
//            bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
//            retriever.release();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            bitRate = "0";
//        }
//    }
//
//    String getBitRate() {
//
//        if (bitRate != null) {
//            return bitRate;
//        }
//        catchBitrate();
//
//        return bitRate;
//    }
//
////        MbnSong(String song) {
//////            retriever.setDataSource(Uri.parse(song).getPath());
//////
//////            cover = retriever.getEmbeddedPicture();
//////
//////            title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//////            albumTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
////
////            if (title == null) {
////
////
////                title = "Unknown";
////
//////                title = song.getName().substring(0, song.getName().length() - 4);
////            }
////            if (albumTitle == null) {
////
////                albumTitle = "Album Unknown";
////            }
////
////
////            this.path = Uri.parse(song).getPath();
////
//////            if (cover == null) {
//////                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1);
//////
//////                cover = bitmap.
//////
//////            }
////
//////            System.out.println(title);
////        }
//
//    public String getAlbumTitle() {
//        return albumTitle;
//    }
//
//    String getTitle() {
//        return title;
//    }
//
//    public String getArtistTitle() {
//        return artist;
//    }
//
//    private void makeCover(Context context) {
//
//
//        try {
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(path);
//            byte[] coverArray = retriever.getEmbeddedPicture();
//            retriever.release();
//            cover = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length);
//        } catch (Exception e) {
//
//            Random random = new Random();
//
//            int rand = random.nextInt(5);
//            if (rand == 0) {
//                cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_love5_ppcorn);
//            } else if (rand == 1) {
//                cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_1);
//            } else if (rand == 2) {
//                cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_2);
//            } else if (rand == 3) {
//                cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_4);
//            } else {
//                cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.day_fog);
//            }
//        }
//
//
//    }
//
//    public Bitmap getCover(Context context) {
//
//        if (cover != null) {
//
//            return cover;
//        }
//        makeCover(context);
//
//        return cover;
//    }
//
//    String getPath() {
//        return path;
//    }
//
//}
