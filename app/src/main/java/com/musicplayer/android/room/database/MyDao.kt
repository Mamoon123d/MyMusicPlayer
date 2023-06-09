package com.musicplayer.android.room.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.musicplayer.android.room.data.*

@Dao
interface MyDao {

    @Query("select * from playlist ")
    fun getList(): LiveData<List<PlayListData>>

    @Query("select id from playlist order by id DESC limit 1")
    fun getPlId():LiveData<Long>?


    // @Insert(onConflict = REPLACE)
    @Insert()
    suspend fun insertPlItem(data: PlayListData)

    @Delete
    suspend fun deletePlItem(data: PlayListData)

    @Update
    suspend fun updatePlItem(data: PlayListData)

    //----------------------videos in playlist----------------
    @Query("select * from PL_VIDEO where pl_id =:plId")
    fun getPlItemList(plId: Int): LiveData<List<VideoItemPlData>>

    @Insert()
    suspend fun insertVideoPl(data: VideoItemPlData)

    @Delete
    suspend fun deleteVideoPl(data: VideoItemPlData)

    //---------------Favorite----------------------
    @Query("select * from favorite ")
    fun getFavList(): LiveData<List<FavoriteData>>

    @Insert()
    suspend fun insertFavorite(data: FavoriteData)

    @Delete
    suspend fun deleteFavorite(data: FavoriteData)

    @Query("SELECT COUNT() FROM favorite WHERE video_id = :id")
    fun isFavoriteExists(id: String): LiveData<Int>

    @Query("SELECT COUNT() FROM favorite")
    fun totalFavorites(): LiveData<Long>

    //============ music favorite ==============================
    @Query("select * from music_favorite ")
    fun getMusicFavList(): LiveData<List<MusicFavoriteData>>

    @Insert()
    suspend fun insertMusicFavorite(data: MusicFavoriteData)

    @Delete
    suspend fun deleteMusicFavorite(data: MusicFavoriteData)

    @Query("SELECT COUNT() FROM music_favorite WHERE music_id = :id")
    fun isMusicFavoriteExists(id: String): LiveData<Int>

    @Query("SELECT COUNT() FROM music_favorite")
    fun totalMusicFavorites(): LiveData<Long>

    //=================== Music Playlist ==========================
    @Query("select * from music_playlist ")
    fun getMusicPlayList(): LiveData<List<MusicPlayListData>>

    @Query("select id from music_playlist order by id DESC limit 1")
    fun getMusicPlaylistId(): LiveData<Long>?

    @Insert()
    suspend fun insertMusicPlaylist(data: MusicPlayListData)

    @Delete
    suspend fun deleteMusicPlaylist(data: MusicPlayListData)

    @Update
    suspend fun updateMusicPlaylist(data: MusicPlayListData)

    //==================== music item in playlist =======================

    @Query("select * from PL_MUSIC where pl_id =:plId")
    fun getMusicsInPl(plId: Int): LiveData<List<MusicItemPlData>>

    @Insert()
    suspend fun addMusicInPl(data: MusicItemPlData)

    @Delete
    suspend fun removeMusicInPl(data: MusicItemPlData)
}