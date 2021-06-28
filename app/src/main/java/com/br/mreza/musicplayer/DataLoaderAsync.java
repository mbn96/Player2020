//package com.br.mreza.musicplayer;
//
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.MediaMetadataRetriever;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.v4.content.LocalBroadcastManager;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DataLoaderAsync {
//
//
////    public static class Loader extends AsyncTask<Context, Void, Void> {
////
////
////        @Override
////        protected Void doInBackground(Context... contexts) {
////
////            finder(contexts[0]);
////
////            return null;
////        }
////
////
////    }
//
//
//    //    private static void deleteOldCovers(Context context) {
////
////        File dir = new File(context.getFilesDir(), "BlurredCovers");
////
////
////        ArrayList<String> covPaths = new ArrayList<>();
////
////        for (DataSong song : DataBaseHolder.getInstance(context).getSongDAO().loadAllSongs()) {
////            covPaths.add(song.getBlurredCover());
////        }
////
////
////        try {
////            for (File file : dir.listFiles()) {
////
////                try {
////                    if (!covPaths.contains(file.getPath())) {
////
////                        file.delete();
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////
////    }
//
//
//    private static void finder(Context context) {
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
////        Cursor albumsCursor = contentResolver.query(albumsUri, null, null, null, null);
//
////        Cursor artistCursor = contentResolver.query(artistUri, null, null, null, null);
//
////        tempDataAlbumList.clear();
//
//
////        if (albumsCursor != null && albumsCursor.getCount() > 0) {
////            while (albumsCursor.moveToNext()) {
////
////                long al_ID = albumsCursor.getLong(albumsCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
////                String al_Name = albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
////                String al_Data = albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
////
////                int num = albumsCursor.getInt(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
////
////                tempDataAlbumList.add(new DataBaseAlbum(al_Name, al_Data, al_ID, num));
////
////
////            }
////            albumsCursor.close();
////        }
//
//
////        DataBaseHolder.getInstance(context).getSongDAO().clearAlbumsTable();
////        DataBaseHolder.getInstance(context).getSongDAO().addAlbum(tempDataAlbumList);
////        tempDataAlbumList.clear();
////        tempDataAlbumList = null;
//
//
////        tempDataArtistList.clear();
//
////        if (artistCursor != null && artistCursor.getCount() > 0) {
////            while (artistCursor.moveToNext()) {
////
////                long ar_ID = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
////                String ar_name = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
////                int numberOfTrackArtist = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
////
////                tempDataArtistList.add(new DataBaseArtists(ar_ID, ar_name, numberOfTrackArtist));
////
////            }
////            artistCursor.close();
////        }
////
////
////        DataBaseHolder.getInstance(context).getSongDAO().clearArtistTable();
////        DataBaseHolder.getInstance(context).getSongDAO().addArtists(tempDataArtistList);
////        tempDataArtistList.clear();
////        tempDataArtistList = null;
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
////        ArrayList<Integer> integers = new ArrayList<>();
////
////        integers.add(569);
////        integers.add(41);
////        integers.add(9);
////        integers.add(69);
////
////        Gson gson = new Gson();
////
////        String jsonSt = "{songs=" + gson.toJson(integers) + "}";
////
////        Log.i("SONG ID", jsonSt);
////
////        try {
////            JSONObject object = new JSONObject(jsonSt);
////
////            Log.i("SONG ID", String.valueOf(object.optJSONArray("songs").optInt(1)));
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//
////        ArrayList<DataSong> temp = new ArrayList<>();
////        temp.addAll(tempDataSongList);
////
////
////        Comparator<DataSong> comparator1 = new Comparator<DataSong>() {
////            @Override
////            public int compare(DataSong o1, DataSong o2) {
////
////
////                return o1.getId() > o2.getId() ? 1 : -1;
////            }
////        };
////
////        Collections.sort(temp, comparator1);
////
////        for (DataSong song : temp) {
////
////            Log.i("SONG ID", song.getId() + " _ " + song.getTitle());
////        }
//
//
////        while (tempSongIdsList.size()>400){
////
////            Log.i("NUMBER", String.valueOf(tempSongIdsList.size()));
////
////            DataBaseHolder.getInstance(context).getSongDAO().removeOldSongs( tempSongIdsList.subList(0,400));
////
////            tempSongIdsList.subList(0,400).clear();
////
////
////        }
//
//
////        DataBaseHolder.getInstance(context).getSongDAO().removeOldSongs(tempSongIdsList);
//
////
////        File cover = new File(oldSong.getBlurredCover());
////
////        try {
////            cover.delete();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
////        tempSongIdsList.clear();
//
//
//        List<Long> fromDatabase = DataBaseHolder.getInstance(context).getSongDAO().loadAllSongsIds();
//
//        List<DataSong> toAdd;
//        List<Long> toRemove;
//
//        if (fromDatabase != null && fromDatabase.size() > 0) {
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
//            prepareTheSongs(toAdd, context);
//            DataBaseHolder.getInstance(context).getSongDAO().addSongs(toAdd);
//            toAdd.clear();
//            toRemove.clear();
//        } else {
//            prepareTheSongs(tempDataSongList, context);
//            DataBaseHolder.getInstance(context).getSongDAO().addSongs(tempDataSongList);
//        }
//
//
////        List<DataSong> songsForCompare = DataBaseHolder.getInstance(context).getSongDAO().loadAllSongs();
////        List<DataSong> songsToAdd = new ArrayList<>();
////        List<Long> songsToRemove = new ArrayList<>();
////
////        for (DataSong newSong : tempDataSongList) {
////
////            DataSong oldSong = selectSong(songsForCompare, newSong.getPath());
////            if (oldSong != null) {
////
////                if (!compare(newSong, oldSong)) {
////
////                    songsToRemove.add(oldSong.getId());
//////                    File cover = new File(oldSong.getBlurredCover());
////
//////                    try {
//////                        cover.delete();
//////                    } catch (Exception e) {
//////                        e.printStackTrace();
//////                    }
////
////                    songsToAdd.add(newSong);
////                }
////            } else {
////                songsToAdd.add(newSong);
////            }
////        }
//
//
////        DataBaseHolder.getInstance(context).getSongDAO().clearSongsTable();
//
//
////        songsToAdd.clear();
////        songsForCompare.clear();
////        songsToRemove.clear();
//        tempDataSongList.clear();
//        tempSongIdsList.clear();
//        if (fromDatabase != null) {
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
////            System.out.println("finder");
//
////            File[] files = rawFile.listFiles();
////
//////            System.out.println(files[3]);
////
////
////            if (files.length > 0) {
////
////                for (File song : files) {
////
////                    if (song.isDirectory()) {
////
////                        finder(song);
////                    } else {
////
////                        if (song.getName().toLowerCase().endsWith(pattern)) {
////
////                            list.add(new MbnSong(song));
////
////                            counter++;
////
////                            publishProgress(counter);
//////                            System.out.println(song.getName());
////
////
////                        }
////
////
////                    }
////
////
////                }
////            }
//
//
//    }
//
//    private static void prepareTheSongs(List<DataSong> songs, Context context) {
//
//
//        for (DataSong checkSong : songs) {
//
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(checkSong.getPath());
//
//                String chTitle;
//                String chArtist;
//                String chAlbum;
//                String bitRate;
//
//
//                chTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//                chArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//                chAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
//                bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
//
//                retriever.release();
//
//                if (chTitle != null) {
//                    checkSong.setTitle(chTitle);
//                }
//                if (chAlbum != null) {
//                    checkSong.setAlbumTitle(chAlbum);
//                }
//                if (chArtist != null) {
//                    checkSong.setArtistTitle(chArtist);
//                }
//                if (bitRate != null) {
//                    checkSong.setBitRate(bitRate);
//                }
//
//
////                File cover = getBlurredFile(context, checkSong.getPath());
//
////                if (cover != null) {
////
////                    if (cover.exists()) {
////                        try {
////                            cover.delete();
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    }
//
////                        System.out.println("mmmmm  " + cover.getPath());
//
////                        Intent intent = new Intent("newFile");
////                        intent.putExtra("path", data.substring(data.lastIndexOf(File.separator) + 1, data.length()));
////                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//
//
////                    getBlur(checkSong.getPath(), cover);
//
////                    checkSong.setBlurredCover(cover.getPath());
//
//
////                }
//
//
//                Intent intent = new Intent("newFile");
//                intent.putExtra("song", checkSong.getTitle());
//                intent.putExtra("loader", new int[]{songs.size(), songs.indexOf(checkSong)});
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//
//            } catch (Exception ignored) {
//            }
//
//        }
//
//
//    }
//
//    private static boolean compare(DataSong s1, DataSong s2) {
//
//
//        return s1.getDuration() == s2.getDuration() && s1.getSize() == s2.getSize();
//
//
//    }
//
//    private static DataSong selectSong(List<DataSong> list, String path) {
//
//
//        for (DataSong ds : list) {
//
//            if (ds.getPath().equals(path)) {
//                return ds;
//            }
//        }
//
//
//        return null;
//    }
//
//    private static File getBlurredFile(Context context, String path) {
//
////        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BlurredCovers");
//        File dir = new File(context.getFilesDir(), "BlurredCovers");
//        if (!dir.exists()) {
//            if (!dir.mkdirs()) {
//                return null;
//            }
//        }
//
////        System.out.println("hhhhhhhhhhhhh  " + dir.getPath());
//
//        String name = path.substring(path.lastIndexOf(File.separator), path.length()) + ".jpg";
//
//
//        return new File(dir, name);
//
//    }
//
//    private static void getBlur(String path, File imageFile) {
//
//        Bitmap bitmap;
//
//        try {
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(path);
//            byte[] coverArray = retriever.getEmbeddedPicture();
//            retriever.release();
//            BitmapFactory.Options options = new BitmapFactory.Options();
////            options.inSampleSize = 6;
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);
//            int width = options.outWidth;
//            int height = options.outHeight;
//            options.inSampleSize = Math.max(width, height) / 100;
//            options.inJustDecodeBounds = false;
//            Bitmap inComeBitmap = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options).copy(Bitmap.Config.ARGB_8888, true);
////            Bitmap inComeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
////            Rect rect = new Rect(0, 0, width, height);
////            Canvas canvas = new Canvas(inComeBitmap);
////            canvas.drawBitmap(bitmap, null, rect, null);
//
////            int[] allTogether = new int[width * height];
//
////            inComeBitmap.getPixels(allTogether, 0, width, 0, 0, width, height);
//
////            int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex, one, two, three, four, five, six, seven, eight, nine,
////                    total, r1, r2, r3, r4, r5, r6, r7, r8, r9, rt, b1, b2, b3, b4, b5, b6, b7, b8, b9, bt,
////                    g1, g2, g3, g4, g5, g6, g7, g8, g9, gt, a1, a2, a3, a4, a5, a6, a7, a8, a9, at;
////
////            for (int x = 0; x < 3; x++) {
////                for (int i = 0; i < allTogether.length; i++) {
////                    if ((i + 1) > width && (i + 1) % width > 1 && (i + 1) / width < height - 1) {
////                        oneIndex = i;
////                        try {
////                            twoIndex = oneIndex - 1;
////                            threeIndex = oneIndex + 1;
////                            forIndex = oneIndex - width;
////                            fiveIndex = oneIndex + width;
////
////                            one = allTogether[oneIndex];
////                            r1 = Color.red(one);
////                            b1 = Color.blue(one);
////                            g1 = Color.green(one);
////                            a1 = Color.alpha(one);
////
////                            two = allTogether[twoIndex];
////                            r2 = Color.red(two);
////                            b2 = Color.blue(two);
////                            g2 = Color.green(two);
////                            a2 = Color.alpha(two);
////
////                            three = allTogether[threeIndex];
////                            r3 = Color.red(three);
////                            b3 = Color.blue(three);
////                            g3 = Color.green(three);
////                            a3 = Color.alpha(three);
////
////                            four = allTogether[forIndex];
////                            r4 = Color.red(four);
////                            b4 = Color.blue(four);
////                            g4 = Color.green(four);
////                            a4 = Color.alpha(four);
////
////                            six = allTogether[forIndex + 1];
////                            r6 = Color.red(six);
////                            b6 = Color.blue(six);
////                            g6 = Color.green(six);
////                            a6 = Color.alpha(six);
////
////                            seven = allTogether[forIndex - 1];
////                            r7 = Color.red(seven);
////                            b7 = Color.blue(seven);
////                            g7 = Color.green(seven);
////                            a7 = Color.alpha(seven);
////
////                            five = allTogether[fiveIndex];
////                            r5 = Color.red(five);
////                            b5 = Color.blue(five);
////                            g5 = Color.green(five);
////                            a5 = Color.alpha(five);
////
////                            eight = allTogether[fiveIndex - 1];
////                            r8 = Color.red(eight);
////                            b8 = Color.blue(eight);
////                            g8 = Color.green(eight);
////                            a8 = Color.alpha(eight);
////
////                            nine = allTogether[fiveIndex + 1];
////                            r9 = Color.red(nine);
////                            b9 = Color.blue(nine);
////                            g9 = Color.green(nine);
////                            a9 = Color.alpha(nine);
////
////                            rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
////                            bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
////                            gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
////                            at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;
////
////                            total = Color.argb(at, rt, gt, bt);
////
////                            allTogether[oneIndex] = total;
////                            allTogether[twoIndex] = total;
////                            allTogether[threeIndex] = total;
////                            allTogether[forIndex] = total;
////                            allTogether[fiveIndex] = total;
////                            allTogether[fiveIndex - 1] = total;
////                            allTogether[fiveIndex + 1] = total;
////                            allTogether[forIndex - 1] = total;
////                            allTogether[forIndex + 1] = total;
////
////
////                        } catch (Exception ignored) {
////                        }
////                    }
////                }
////            }
////
////
////
////
////
////            Bitmap outComeBitmap = Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            inComeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
////            outComeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//
//        } catch (Exception e) {
//            String toFile = "No Image!";
//            FileOutputStream outputStream;
//            try {
//                outputStream = new FileOutputStream(imageFile);
//                outputStream.write(toFile.getBytes());
//                outputStream.flush();
//                outputStream.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//        }
//
//
//    }
//
//}
