//package com.br.mreza.musicplayer;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import java.util.ArrayList;
//
//import static com.br.mreza.musicplayer.ListMaker.allSongsMap;
//
//class MbnAlbum {
//
//    private ArrayList<String> songsCodes;
//    //    private String hashCode;
//    private String name;
//
//    public MbnAlbum(String name) {
//        this.name = name;
//        songsCodes = new ArrayList<>();
//    }
//
//    public ArrayList<String> getSongsCodes() {
//        return songsCodes;
//    }
//
//    @SuppressWarnings("unchecked")
//    public ArrayList<String> getSongsForQueue() {
//
//
//        return (ArrayList<String>) songsCodes.clone();
//    }
//
//    public void setSongsCodes(ArrayList<String> input) {
//
//        songsCodes.clear();
//
//        songsCodes.addAll(input);
//
//    }
//
//    public void clear() {
//
//        songsCodes.clear();
//    }
//
//    public String getHashCode() {
//        return name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void remove(String songCode) {
//
//        songsCodes.remove(songCode);
//    }
//
//    public void add(String songCode) {
//
//        songsCodes.add(songCode);
//
//    }
//
//    public boolean contains(String code) {
//
//        return songsCodes.contains(code);
//
//    }
//
//    public int getNumberInQueue(String songCode) {
//
//
//        return songsCodes.indexOf(songCode);
//    }
//
//    public ArrayList<MbnSong> getList() {
//
//        ArrayList<MbnSong> list = new ArrayList<>();
//
//        for (String a : songsCodes) {
//
//            list.add(allSongsMap.get(a));
//        }
//        return list;
//    }
//
//    public Bitmap getCover(Context context) {
//
//
//        return allSongsMap.get(songsCodes.get(0)).getCover(context);
//
//
//    }
//
//    public int size() {
//
//
//        return songsCodes.size();
//    }
//
//}
