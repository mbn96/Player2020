//package com.br.mreza.musicplayer;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//class ListMaker_BackUp {
//
//
////    public static List<ListActivity.MbnSong> allSongsRealFile = new ArrayList<>();
////
////    public static List<String> allSongsNames = new ArrayList<>();
//
//    static List<String> albums = new ArrayList<>();
//
//    static List<ArrayList<ListActivity.MbnSong>> albumContents = new ArrayList<>();
//
//
//    public static List<ListActivity.MbnSong> current = new ArrayList<>();
//
//
//    static void listReceiver(List<ListActivity.MbnSong> allSongs) {
//
//
////        albums.add("Album Unknown");
//
//
//        for (ListActivity.MbnSong song : allSongs) {
//
//            boolean isNew = true;
//
//            for (int i = 0; i < albums.size(); i++) {
//
//
//                if (albums.get(i).equals(song.getAlbumTitle())) {
//
//
//                    albumContents.get(i).add(song);
//                    isNew = false;
//                    break;
//
//
//                }
//
//
//            }
//
//
//            if (isNew) {
//
//                albums.add(song.getAlbumTitle());
//
//                ArrayList<ListActivity.MbnSong> a = new ArrayList<>();
//                a.add(song);
//
//                albumContents.add(a);
//
//
//            }
//
//
//        }
//
//
////        System.out.println(albums);
////        System.out.println(albumContents);
//
//
//    }
//
//
//}
