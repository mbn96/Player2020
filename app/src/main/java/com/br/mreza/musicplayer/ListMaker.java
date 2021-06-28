package com.br.mreza.musicplayer;


public class ListMaker {


//    public static List<ListActivity.MbnSong> allSongsRealFile = new ArrayList<>();
//
//    public static List<String> allSongsNames = new ArrayList<>();

//    static ArrayList<MbnAlbum> albumsList = new ArrayList<>();
//
//    static HashMap<String, MbnAlbum> albumsMap = new HashMap<>();

//    static SparseArray<ListActivity.MbnSong> allSongsMap = new SparseArray<>();

//    static HashMap<String, MbnSong> allSongsMap = new HashMap<>();
//    static ArrayList<String> allSongsCodes = new ArrayList<>();
//    static MbnAlbum allSongsAlbum = new MbnAlbum("allSongs");

//    static ArrayList<MbnAlbum> playLists = new ArrayList<>();

//    static MbnAlbum currentQueue = new MbnAlbum("nowPlaying");
    //    static String currentQueueCode;

//    public static List<DataSong> currentQueue = new ArrayList<>();

//    private static DataSong currentTrack;
//    public static boolean trackChanged = false;
//        static String currentTrackCode;
//    public static int currentTrackNumberInQueue;
//
//    public static void setCurrentTrack(DataSong currentTrack) {
//        if (ListMaker.currentTrack != null)
//            trackChanged = ListMaker.currentTrack.getId() != currentTrack.getId();
//        ListMaker.currentTrack = currentTrack;
//    }
//
//    public static DataSong getCurrentTrack() {
//        return currentTrack;
//    }
//
//    public static void listReceiver(Context context) {
//
//
////        albumsList.clear();
////        albumsMap.clear();
////        allSongsMap.clear();
////        allSongsAlbum.clear();
////        allSongsCodes.clear();
////
////
//////        albums.add("Album Unknown");
////
////
////        for (MbnSong song : allSongs) {
////
////
////            allSongsMap.put(song.getHashCode(), song);
////            allSongsCodes.add(song.getHashCode());
////            allSongsAlbum.add(song.getHashCode());
////
////            boolean isNew = true;
////
////            for (int i = 0; i < albumsList.size(); i++) {
////
////
////                if (albumsList.get(i).getName().equals(song.getAlbumTitle())) {
////
////                    albumsList.get(i).add(song.getHashCode());
//////                    albumsMap.get(albumsList.get(i).getHashCode()).add(song.getHashCode());
////
////                    isNew = false;
////                    break;
////
////
////                }
////
////
////            }
////
////
////            if (isNew) {
////
////                MbnAlbum album = new MbnAlbum(song.getAlbumTitle());
////
////                album.add(song.getHashCode());
////
////                albumsList.add(album);
////                albumsMap.put(album.getHashCode(), album);
////
////
//////                albumsMap.put(album.getHashCode(), album);
////
////
////            }
////
////
////        }
////
////
//////        System.out.println(albums);
//////        System.out.println(albumContents);
////
//
////        allSongs.clear();
//
//        addPlayList("Recently added");
//        addPlayList("Favorite");
//
////        StorageUtils.getPreferences(context);
////
////
////        findRecent();
//
//
//        getPreference(context);
//
//    }

//    private static void getPreference(final Context context) {
//
////        new AsyncTask<Void, Void, Void>() {
////            @Override
////            protected Void doInBackground(Void... voids) {
//
//        StorageUtils.getPreferences(context);
//
//        findRecent();
//
//        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(To_FRAGMENT));
////        context.sendBroadcast(new Intent(To_FRAGMENT));
//
////                return null;
////            }
////        }.execute();
//
//
//    }

//    static private void findRecent() {
//
////
////        MbnAlbum temp = getPlaylist("Recently added");
////
////        ArrayList<String> tempList = new ArrayList<>();
////
////        tempList.addAll(allSongsCodes);
////
////        Comparator<String> comparator = new Comparator<String>() {
////
////            @Override
////            public int compare(String s, String t1) {
////
////
////                if (allSongsMap.get(s).getDateAdded() > allSongsMap.get(t1).getDateAdded()) {
////
////                    return -1;
////                } else if (allSongsMap.get(s).getDateAdded() < allSongsMap.get(t1).getDateAdded()) {
////
////                    return 1;
////                }
////
////
////                return 0;
////            }
////        };
////
////
////        Collections.sort(tempList, comparator);
////
////
////        if (temp != null) {
////            temp.setSongsCodes(tempList);
////        }
//
//
//    }

//    static void addPlayList(String name) {
//
//
////        playLists.add(new MbnAlbum(name));
//
////        StorageUtils.setPlayLists(cont);
//
//
//    }

//    @Nullable
//    static MbnAlbum getPlaylist(String name) {
//
//
////        for (MbnAlbum a : playLists) {
//
////            if (a.getName().equals(name)) {
//
////                return a;
//            }

//        }

//        return null;
//    }

//    static void removeFromPlaylist(Context context, String playlistName, ArrayList<String> songCodes) {
//
//
//        MbnAlbum list = getPlaylist(playlistName);
//
//        for (String b : songCodes) {
//
//            if (list != null) {
//                list.remove(b);
//            }
//
//        }
//
//        StorageUtils.setPlayLists(context);
//
//    }

//    static void addToPlayList(Context context, String playlistName, ArrayList<String> songCodes) {
//
//
//        MbnAlbum list = getPlaylist(playlistName);
//
//        for (String b : songCodes) {
//
//            if (list != null) {
//                list.add(b);
//            }
//
//        }
//
//
//        StorageUtils.setPlayLists(context);
//
//
//    }

//    @SuppressWarnings("unchecked")
//    static void allSongsQueueMaker() {
//
////        currentQueue = new MbnAlbum("allSongs");
//
////        allSongsMap.values().iterator().
//
////
////        for (String a : allSongsCodes) {
////
////            currentQueue.add(a);
////
////        }
//
//
////        currentQueue.clear();
////        currentQueue.addAll(allSongsCodes);
//
////        currentQueue = new ArrayList<>();
////
////        for (int i = 0; i < allSongsCodes.size(); i++) {
////
////            currentQueue.add(allSongsCodes.get(i));
////
////        }
//
//
//    }


}
