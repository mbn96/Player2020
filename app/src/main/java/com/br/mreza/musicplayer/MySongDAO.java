package com.br.mreza.musicplayer;


import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.br.mreza.musicplayer.newmodel.artwork.ArtCachePath;
import com.br.mreza.musicplayer.newmodel.database.CurrentListElement;
import com.br.mreza.musicplayer.p2020.management.MostPlayedItem;

import java.util.List;
@Keep
@Dao
public interface MySongDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSongs(List<DataSong> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSong(DataSong song);

    @Query("SELECT * FROM songs ORDER BY title ASC")
    List<DataSong> loadAllSongs();

    @Query("SELECT id FROM songs")
    List<Long> loadAllSongsIds();

    @Query("SELECT id FROM songs WHERE albumID = :abID")
    List<Long> loadAllSongsIdsForAlbum(long abID);

    @Query("SELECT id FROM songs WHERE artistID = :arID")
    List<Long> loadAllSongsIdsForArtist(long arID);

    @Query("SELECT id FROM songs ORDER BY dateAdded DESC")
    List<Long> loadAllSongsIdsOrderedByTime();

    @Query("SELECT id FROM songs ORDER BY title ASC")
    List<Long> loadAllSongsIdsOrderedByName();

    @Query("SELECT id FROM songs WHERE title LIKE :sch" +
            " OR albumTitle LIKE :sch" +
            " OR artistTitle LIKE :sch" +
            " ORDER BY title ASC")
    List<Long> searchSongs(String sch);

    @Query("SELECT * FROM songs WHERE path IN (:data)")
    List<DataSong> loadSongs(List<String> data);

    @Query("SELECT * FROM songs WHERE id IN (:ids)")
    List<DataSong> loadSongsWithIDs(List<Long> ids);

    @Query("SELECT * FROM songs WHERE albumID = :album_name ORDER BY title ASC")
    List<DataSong> loadAlbum(long album_name);

    @Query("DELETE FROM songs")
    void clearSongsTable();

    @Query("DELETE FROM songs WHERE id NOT IN (:ids)")
    void removeOldSongs(List<Long> ids);

    @Query("DELETE FROM songs WHERE id IN (:ids)")
    void removeOldSongsAfterCheck(List<Long> ids);

    @Query("UPDATE songs SET isChecked = 0")
    void setAllSongsUnchecked();

    @Query("UPDATE songs SET isChecked = 1 WHERE id = :id")
    void setSongChecked(long id);

    @Query("SELECT * FROM songs WHERE id = :id")
    DataSong getSong(long id);

    @Query("DELETE FROM songs WHERE isChecked = 0")
    void removeUncheckedSongs();

    /*-------------------  ARTWORK MANAGEMENT  --------------*/

    @Query("UPDATE songs SET artworkStatus = :status WHERE id = :id")
    void setSongArtworkStatus(long id, int status);

//    @Query("UPDATE songs SET downloadedArtPath = :path WHERE id = :id")
//    void setSongDownloadedArtworkPath(long id, String path);

    @Query("SELECT artworkStatus FROM songs WHERE id = :id")
    int getSongArtworkStatus(long id);

    @Query("SELECT id FROM songs WHERE artworkStatus = 2")
    List<Long> getSongIDsForSearch();

    @Query("SELECT id FROM songs WHERE artworkStatus = :artStatus")
    List<Long> getSongIDsForArtStatus(int artStatus);

    @Query("SELECT id FROM songs WHERE artworkStatus = 0")
    List<Long> getSongIDsArtUnknown();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addArtCache(ArtCachePath cache);

    @Query("SELECT * FROM art_paths WHERE id = :id")
    ArtCachePath getArtCache(long id);

    @Query("SELECT path FROM songs WHERE id = :id")
    String getSongPath(long id);

    /*------------------- Play Time Management --------*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void putPlayTime(MostPlayedItem item);

    @Query("SELECT * FROM most_played WHERE id = :id")
    MostPlayedItem getPlayTime(long id);

    @Query("SELECT * FROM most_played ORDER BY lastPlay DESC")
    List<MostPlayedItem> getRecentlyPlayed();

    @Query("SELECT * FROM most_played ORDER BY totalPlay DESC")
    List<MostPlayedItem> getMostPlayed();

    /*-------------------   ALBUMS   ------------------*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAlbum(DataBaseAlbum album);

    @Query("UPDATE albums SET isChecked = 0")
    void setAllAlbumsUnchecked();

    @Query("UPDATE albums SET isChecked = 1 WHERE id = :id")
    void setAlbumChecked(long id);

    @Query("UPDATE albums SET numberOfTracks = :num WHERE id = :id")
    void setAlbumNumberOfTracks(long id, int num);

    @Query("DELETE FROM albums WHERE isChecked = 0")
    void removeUncheckedAlbums();

    @Query("SELECT * FROM albums WHERE id = :id")
    DataBaseAlbum getAlbum(long id);

    @Query("SELECT * FROM albums WHERE name LIKE :sth")
    List<DataBaseAlbum> searchAlbums(String sth);

    @Query("SELECT * FROM albums ORDER BY name ASC")
    List<DataBaseAlbum> loadAllAlbums();

    @Query("DELETE FROM albums")
    void clearAlbumsTable();

    @Query("SELECT SUM(duration) FROM songs WHERE albumID = :albumId")
    int albumDuration(long albumId);

    /*--------------------- ARTISTS  --------------------*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addArtists(List<DataBaseArtists> artists);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addArtist(DataBaseArtists artist);

    @Query("DELETE FROM artists")
    void clearArtistTable();

    @Query("SELECT * FROM artists ORDER BY name ASC")
    List<DataBaseArtists> loadAllArtists();

    @Query("SELECT * FROM artists WHERE id = :id")
    DataBaseArtists getArtist(long id);

    @Query("SELECT * FROM artists WHERE name LIKE :sth")
    List<DataBaseArtists> searchArtists(String sth);

    /*--------------------- CURRENT_LIST  ---------------*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setCurrent(List<DataBaseCurrentList> list);

    @Query("SELECT * FROM current")
    List<DataBaseCurrentList> getCurrentList();

    @Query("SELECT * FROM current WHERE path LIKE :data")
    List<DataBaseCurrentList> getCurrent(String data);

    @Query("DELETE FROM current")
    void clearCurrentList();

    /*------------- New current model ---------------*/


    @Query("SELECT id FROM current_table ORDER BY numberInRow ASC")
    List<Long> currentIds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setCurrentNew(List<CurrentListElement> elements);

    @Query("DELETE FROM current_table")
    void clearNewCurrent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToCurrent(CurrentListElement element);

    @Query("SELECT id FROM songs WHERE id IN (:ids)")
    List<Long> methodToCheckCurrent(List<Long> ids);

    /*----------------------- PLAY LISTS -----------------------------*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPlayList(DataBasePlayList playList);

    @Update
    void updatePlayList(DataBasePlayList playList);

    @Query("SELECT * FROM playLists")
    List<DataBasePlayList> getAllPlayLists();


}
