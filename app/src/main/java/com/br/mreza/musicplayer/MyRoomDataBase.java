package com.br.mreza.musicplayer;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.br.mreza.musicplayer.newmodel.artwork.ArtCachePath;
import com.br.mreza.musicplayer.newmodel.database.CurrentListElement;
import com.br.mreza.musicplayer.p2020.management.MostPlayedItem;

@Database(entities = {DataSong.class, DataBaseAlbum.class, DataBaseCurrentList.class, DataBaseArtists.class, DataBasePlayList.class, CurrentListElement.class, ArtCachePath.class, MostPlayedItem.class}, version = 1, exportSchema = false)
public abstract class MyRoomDataBase extends RoomDatabase {

    public abstract MySongDAO getSongDAO();

}
