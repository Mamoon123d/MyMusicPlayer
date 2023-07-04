package com.musicplayer.android.room.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicplayer.android.room.data.*
import com.musicplayer.android.room.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repository) : ViewModel() {
    fun getPlayList(): LiveData<List<PlayListData>> {
        return repo.getList()
    }

    fun getPlId():LiveData<Long>?{
        return repo.getPlId()
    }
    fun insertPlItem(data: PlayListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertPlItem(data)
        }
    }

    fun deletePl(data: PlayListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePlItem(data)
        }
    }

    fun updateItem(data: PlayListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updatePlItem(data)
        }
    }

    //-------------------------Videos in playlist-------------------------
    fun getPlItemList(plId: Int): LiveData<List<VideoItemPlData>> {
        return repo.getPlItemList(plId)
    }

    fun insertVideoPl(data: VideoItemPlData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertVideoPl(data)
        }
    }
 fun deleteVideoPl(data: VideoItemPlData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteVideoPl(data)
        }
    }

    //-----------------------favorite-------------------------------
    fun getFavorites(): LiveData<List<FavoriteData>> {
        return repo.getFavorites()
    }

    fun addFavorite(data: FavoriteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addFavorite(data)
        }
    }

    fun removeFavorite(data: FavoriteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeFavorite(data)
        }
    }

    fun isFavoriteExists(videoId: String): LiveData<Int> {

        return repo.isFavorite(videoId)
    }

    fun totalFavorite(): LiveData<Long> {
        return repo.totalFavorite()
    }


    //============ Video History ==============================
    fun getVideosInHistory(): LiveData<List<VideoHistoryData>>{
        return repo.getVideosInHistory()
    }

     fun addVideoInHistory(data: VideoHistoryData){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addVideoInHistory(data)
        }
    }

    fun removeVideoInHistory(data: VideoHistoryData){
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeVideoInHistory(data)
        }
    }

     fun updateVideoInHistory(data: VideoHistoryData){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateVideoInHistory(data)
        }
    }


    fun isHistoryExists(videoId: String): LiveData<Int>{
        return repo.isHistoryExists(videoId)
    }

    //================== music favorite ================

    fun getMusicFavorites(): LiveData<List<MusicFavoriteData>> {
        return repo.getMusicFavorites()
    }

    fun addMusicFavorite(data: MusicFavoriteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addMusicFavorite(data)
        }
    }

    fun removeMusicFavorite(data: MusicFavoriteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeMusicFavorite(data)
        }
    }

    fun isMusicFavoriteExists(musicId: String): LiveData<Int> {
        return repo.isMusicFavorite(musicId)
    }

    fun totalMusicFavorite(): LiveData<Long> {
        return repo.totalMusicFavorite()
    }

    //===================== Music Playlist =============================
    fun getMusicPl(): LiveData<List<MusicPlayListData>> {
        return repo.getMusicPl()
    }

    fun getMusicPlId():LiveData<Long>?{
        return repo.getMusicPlId()
    }
     fun addMusicPl(data: MusicPlayListData) {
        viewModelScope.launch(Dispatchers.IO) {
         repo.addMusicPl(data)
        }
    }

     fun removeMusicPl(data: MusicPlayListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeMusicPl(data)
        }
    }

     fun updateMusicPl(data: MusicPlayListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateMusicPl(data)
        }
    }

    //=============== Music item in playlist ================
    fun getMusicsInPl(plId: Int): LiveData<List<MusicItemPlData>> {
        return repo.getMusicsInPl(plId)
    }

    fun addMusicInPl(data: MusicItemPlData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addMusicInPl(data)
        }
    }

    fun removeMusicInPl(data: MusicItemPlData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeMusicInPl(data)
        }
    }

    //==================== recent music =======================
    fun recentMusics(): LiveData<List<RecentMusicItemData>> {
        return repo.recentMusics()
    }

    fun addRecentMusic(data: RecentMusicItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addRecentMusic(data)
        }
    }

    fun removeRecentMusic(data: RecentMusicItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeRecentMusic(data)
        }
    }

    fun updateRecentMusic(data: RecentMusicItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateMRecentMusic(data)
        }
    }

    fun isRecentMusicExists(musicId: String): LiveData<Int> {
        return repo.isRecentMusicExists(musicId)
    }


}