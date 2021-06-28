package com.br.mreza.musicplayer.newdesign.asyncloader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.br.mreza.musicplayer.DataBaseAlbum;
import com.br.mreza.musicplayer.DataBaseArtists;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.DataSong;

import java.util.List;

import mbn.libs.backgroundtask.BaseTaskHolder;


public class DataLoaderTask implements BaseTaskHolder.BaseTask {


    private Context context;
    private Handler handler;
    private String TAG = "DataLoader";

    DataLoaderTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Object onRun() {
//        finder(context);
        finder2(context);
        context = null;
        synchronized (AsyncLoaderManager.INSTANCE) {
            AsyncLoaderManager.INSTANCE.setHasStarted();
        }
        return null;
    }

    @Override
    public Object getInfo() {
        return null;
    }

//    private void finder(Context context) {
//
////        ListMaker.listReceiver(getApplicationContext());
//
//
////        tempList = new ArrayList<>();
//
//
//        List<DataSong> tempDataSongList = new ArrayList<>();
//        List<Long> tempSongIdsList = new ArrayList<>();
////        List<DataBaseAlbum> tempDataAlbumList = new ArrayList<>();
////        List<DataBaseArtists> tempDataArtistList = new ArrayList<>();
//
//        ContentResolver contentResolver = context.getContentResolver();
//
//
//        Uri songsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
////        Uri albumsUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
////        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//
//        Cursor songsCursor = contentResolver.query(songsUri, null, selection, null, sortOrder);
//
//
//        tempDataSongList.clear();
//
//        if (songsCursor != null && songsCursor.getCount() > 0) {
//            while (songsCursor.moveToNext()) {
//                long id = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media._ID));
//                String data = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                String title = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String album = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                String artist = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                int duration = songsCursor.getInt(songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                long date = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
//                long albumID = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                long size = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//
//
////                tempList.add(new MbnSong(title, album, data, artist, duration, date));
//                tempDataSongList.add(new DataSong(id, title, duration, artist, album, data, date, albumID, size));
//
//                tempSongIdsList.add(id);
//
//
////                if (!tempDataAlbumNamesList.contains(album)) {
////                    tempDataAlbumNamesList.add(album);
////                    tempDataAlbumList.add(new DataBaseAlbum(album, data, albumID));
////                }
//
//
//            }
//        }
//        assert songsCursor != null;
//        songsCursor.close();
//
//
//        List<Long> fromDatabase = DataBaseHolder.getInstance(context).getSongDAO().loadAllSongsIds();
//
//        List<DataSong> toAdd;
//        List<Long> toRemove;
//
//        if (fromDatabase != null && fromDatabase.size() > 0)
//
//        {
//            toAdd = new ArrayList<>();
//            toRemove = new ArrayList<>();
//            for (long newS : tempSongIdsList) {
//                if (!fromDatabase.contains(newS)) {
//                    toAdd.add(tempDataSongList.get(tempSongIdsList.indexOf(newS)));
//                }
//            }
//
//            for (long oldS : fromDatabase) {
//                if (!tempSongIdsList.contains(oldS)) {
//                    toRemove.add(oldS);
//                }
//            }
//
//            DataBaseHolder.getInstance(context).getSongDAO().removeOldSongsAfterCheck(toRemove);
//            prepareTheSongs(toAdd);
//            DataBaseHolder.getInstance(context).getSongDAO().addSongs(toAdd);
//            toAdd.clear();
//            toRemove.clear();
//        } else
//
//        {
//            prepareTheSongs(tempDataSongList);
//            DataBaseHolder.getInstance(context).getSongDAO().addSongs(tempDataSongList);
//        }
//
//
//        tempDataSongList.clear();
//        tempSongIdsList.clear();
//        if (fromDatabase != null)
//
//        {
//            fromDatabase.clear();
//        }
//
//
//        //===========================================================//
//         /*------------------------ End of finding--------------------*/
//        //============================================================//
//
//
////        deleteOldCovers(context);
//
//
//        DataBaseHolder.forceRefresh(context);
//
//
////        ListMaker.listReceiver(context);
//
//
//    }

    private void finder2(Context context) {


        ContentResolver contentResolver = context.getContentResolver();


        //-------------------- SONGS PART -------------------------//

        Uri songsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Uri albumsUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
//        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor songsCursor = contentResolver.query(songsUri, null, selection, null, sortOrder);

        int progress = 0;
        int count;

        DataBaseHolder.getInstance(context).getSongDAO().setAllSongsUnchecked();

        if (songsCursor != null && songsCursor.getCount() > 0) {
            count = songsCursor.getCount();
            while (songsCursor.moveToNext()) {
                progress++;
                long id = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                DataSong s = DataBaseHolder.getInstance(context).getSongDAO().getSong(id);
                if (s != null) {
                    DataBaseHolder.getInstance(context).getSongDAO().setSongChecked(id);
                } else {
                    String data = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String title = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String album = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artist = songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    long artistID = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                    int duration = songsCursor.getInt(songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    long date = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    long albumID = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    long size = songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                    DataSong newSong = new DataSong(id, title, duration, artist, artistID, album, data, date, albumID, size);
                    newSong.setChecked(true);
                    prepareTheSong(newSong);
                    DataBaseHolder.getInstance(context).getSongDAO().addSong(newSong);

                    Message message = Message.obtain(handler);
                    message.setData(AsyncLoaderManager.getProgressBundle(newSong.getTitle(), new int[]{count, progress}));
                    message.sendToTarget();
                }
            }
        }
        assert songsCursor != null;
        songsCursor.close();
        DataBaseHolder.getInstance(context).getSongDAO().removeUncheckedSongs();


        //----------------------------- ALBUMS PART ----------------------------//

        Uri albumsUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor albumsCursor = contentResolver.query(albumsUri, null,
                null, null,
                MediaStore.Audio.Albums.ALBUM + " ASC");

        DataBaseHolder.getInstance(context).getSongDAO().setAllAlbumsUnchecked();

        if (albumsCursor != null && albumsCursor.getCount() > 0) {
            while (albumsCursor.moveToNext()) {
                long id = albumsCursor.getLong(albumsCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                int num = albumsCursor.getInt(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                DataBaseAlbum album = DataBaseHolder.getInstance(context).getSongDAO().getAlbum(id);
                if (album != null) {
                    DataBaseHolder.getInstance(context).getSongDAO().setAlbumChecked(id);
                    DataBaseHolder.getInstance(context).getSongDAO().setAlbumNumberOfTracks(id, num);
                } else {
                    String al_Name = albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                    String al_Data = albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                    DataBaseAlbum newAlbum = new DataBaseAlbum(al_Name, al_Data, id, num);
                    newAlbum.setChecked(true);
                    DataBaseHolder.getInstance(context).getSongDAO().addAlbum(newAlbum);
                }
            }
            albumsCursor.close();
        }

        DataBaseHolder.getInstance(context).getSongDAO().removeUncheckedAlbums();


        //------------------------- ARTISTS PART -------------------------------//

        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor artistCursor = contentResolver.query(artistUri, null,
                null, null,
                MediaStore.Audio.Artists.ARTIST + " ASC");

        DataBaseHolder.getInstance(context).getSongDAO().clearArtistTable();

        if (artistCursor != null && artistCursor.getCount() > 0) {
            while (artistCursor.moveToNext()) {
                long artistID = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                int numAlb = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                int numTrk = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                String artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));

                DataBaseArtists dataBaseArtist = new DataBaseArtists(artistID, artistName, numTrk, numAlb);
                DataBaseHolder.getInstance(context).getSongDAO().addArtist(dataBaseArtist);
            }
            artistCursor.close();
        }



         /*------------------------ End of finding--------------------*/
        //============================================================//


//        deleteOldCovers(context);


        DataBaseHolder.forceRefresh(context);


//        ListMaker.listReceiver(context);


    }

    private void prepareTheSongs(List<DataSong> songs) {


        for (DataSong checkSong : songs) {

            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(checkSong.getPath());

                String chTitle;
                String chArtist;
                String chAlbum;
                String bitRate;


                chTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                chArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                chAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

                retriever.release();

                if (chTitle != null) {
                    checkSong.setTitle(chTitle);
                }
                if (chAlbum != null) {
                    checkSong.setAlbumTitle(chAlbum);
                }
                if (chArtist != null) {
                    checkSong.setArtistTitle(chArtist);
                }
                if (bitRate != null) {
                    checkSong.setBitRate(bitRate);
                }


//                File cover = getBlurredFile(context, checkSong.getPath());

//                if (cover != null) {
//
//                    if (cover.exists()) {
//                        try {
//                            cover.delete();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

//                        System.out.println("mmmmm  " + cover.getPath());

//                        Intent intent = new Intent("newFile");
//                        intent.putExtra("path", data.substring(data.lastIndexOf(File.separator) + 1, data.length()));
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


//                    getBlur(checkSong.getPath(), cover);

//                    checkSong.setBlurredCover(cover.getPath());


//                }


//                Intent intent = new Intent("newFile");
//                intent.putExtra("song", checkSong.getTitle());
//                intent.putExtra("loader", new int[]{songs.size(), songs.indexOf(checkSong)});
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                Message message = Message.obtain(handler);
                message.setData(AsyncLoaderManager.getProgressBundle(checkSong.getTitle(), new int[]{songs.size(), songs.indexOf(checkSong)}));
                message.sendToTarget();

            } catch (Exception ignored) {
            }

        }


    }

    private void prepareTheSong(DataSong checkSong) {


        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(checkSong.getPath());

            String chTitle;
            String chArtist;
            String chAlbum;
            String bitRate;


            chTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            chArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            chAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

            retriever.release();

            if (chTitle != null) {
                checkSong.setTitle(chTitle);
            }
            if (chAlbum != null) {
                checkSong.setAlbumTitle(chAlbum);
            }
            if (chArtist != null) {
                checkSong.setArtistTitle(chArtist);
            }
            if (bitRate != null) {
                checkSong.setBitRate(bitRate);
            }


//                File cover = getBlurredFile(context, checkSong.getPath());

//                if (cover != null) {
//
//                    if (cover.exists()) {
//                        try {
//                            cover.delete();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

//                        System.out.println("mmmmm  " + cover.getPath());

//                        Intent intent = new Intent("newFile");
//                        intent.putExtra("path", data.substring(data.lastIndexOf(File.separator) + 1, data.length()));
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


//                    getBlur(checkSong.getPath(), cover);

//                    checkSong.setBlurredCover(cover.getPath());


//                }


//                Intent intent = new Intent("newFile");
//                intent.putExtra("song", checkSong.getTitle());
//                intent.putExtra("loader", new int[]{songs.size(), songs.indexOf(checkSong)});
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


        } catch (Exception ignored) {
        }


    }


}
