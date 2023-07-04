package com.musicplayer.android.room.repo

import androidx.lifecycle.LiveData
import com.musicplayer.android.room.data.*
import com.musicplayer.android.room.database.MyDao

class Repository(private val dao: MyDao) {
    fun getList(): LiveData<List<PlayListData>> {
        return dao.getList()
    }

    fun getPlId():LiveData<Long>?{
        return dao.getPlId()
    }
    suspend fun insertPlItem(data: PlayListData) {
        return dao.insertPlItem(data)
    }

    suspend fun deletePlItem(data: PlayListData) {
        return dao.deletePlItem(data)
    }

    suspend fun updatePlItem(data: PlayListData) {
        return dao.updatePlItem(data)
    }


    /*
        suspend fun isExistsTd(td: Int): Boolean {
            return dao.isExists(td)
        }*/
    //------------------------------videos in playlist-------------------
    fun getPlItemList(plId: Int): LiveData<List<VideoItemPlData>> {
        return dao.getPlItemList(plId)
    }

    suspend fun insertVideoPl(data: VideoItemPlData) {
        return dao.insertVideoPl(data)
    }
    suspend fun deleteVideoPl(data: VideoItemPlData) {
        return dao.deleteVideoPl(data)
    }




    //--------------- video Favorite----------------------


    fun getFavorites(): LiveData<List<FavoriteData>> {
        return dao.getFavList()
    }

     fun isFavorite(videoId: String): LiveData<Int> {
        return dao.isFavoriteExists(videoId)
    }

    suspend fun addFavorite(data: FavoriteData) {
        return dao.insertFavorite(data)
    }

    suspend fun removeFavorite(data: FavoriteData) {
        return dao.deleteFavorite(data)
    }
     fun totalFavorite():LiveData<Long> {
        return dao.totalFavorites()
    }


    //============ Video History ==============================
    fun getVideosInHistory(): LiveData<List<VideoHistoryData>>{
        return dao.getVideosInHistory()
    }

    suspend fun addVideoInHistory(data: VideoHistoryData){
        return dao.addVideoInHistory(data)
    }

    suspend fun removeVideoInHistory(data: VideoHistoryData){
        return dao.removeVideoInHistory(data)
    }

    suspend fun updateVideoInHistory(data: VideoHistoryData){
        return dao.updateVideoInHistory(data)
    }


    fun isHistoryExists(id: String): LiveData<Int>{
        return dao.isHistoryExists(id)
    }


    //-------------------music favorite----------------------

    fun getMusicFavorites(): LiveData<List<MusicFavoriteData>> {
        return dao.getMusicFavList()
    }

    fun isMusicFavorite(musicId: String): LiveData<Int> {
        return dao.isMusicFavoriteExists(musicId)
    }

    suspend fun addMusicFavorite(data: MusicFavoriteData) {
        return dao.insertMusicFavorite(data)
    }

    suspend fun removeMusicFavorite(data: MusicFavoriteData) {
        return dao.deleteMusicFavorite(data)
    }
    fun totalMusicFavorite():LiveData<Long> {
        return dao.totalMusicFavorites()
    }

    //================== Music Playlist ========================
    fun getMusicPl(): LiveData<List<MusicPlayListData>> {
        return dao.getMusicPlayList()
    }

    fun getMusicPlId():LiveData<Long>?{
        return dao.getMusicPlaylistId()
    }
    suspend fun addMusicPl(data: MusicPlayListData) {
        return dao.insertMusicPlaylist(data)
    }

    suspend fun removeMusicPl(data: MusicPlayListData) {
        return dao.deleteMusicPlaylist(data)
    }

    suspend fun updateMusicPl(data: MusicPlayListData) {
        return dao.updateMusicPlaylist(data)
    }

    //=============== Music item in playlist ================
    fun getMusicsInPl(plId: Int): LiveData<List<MusicItemPlData>>{
        return dao.getMusicsInPl(plId)
    }

    suspend fun addMusicInPl(data: MusicItemPlData) {
        return dao.addMusicInPl(data)
    }

    suspend fun removeMusicInPl(data: MusicItemPlData) {
        return dao.removeMusicInPl(data)
    }

    //==================== recent music =======================
    fun recentMusics(): LiveData<List<RecentMusicItemData>> {
        return dao.recentMusics()
    }

    suspend fun addRecentMusic(data: RecentMusicItemData) {
        return dao.addRecentMusic(data)
    }

    suspend fun removeRecentMusic(data: RecentMusicItemData) {
        return dao.removeRecentMusic(data)
    }

    suspend fun updateMRecentMusic(data: RecentMusicItemData) {
        return dao.updateMRecentMusic(data)
    }

    fun isRecentMusicExists(musicId:String) :LiveData<Int>{
        return dao.isRecentMusicExists(musicId)
    }

}