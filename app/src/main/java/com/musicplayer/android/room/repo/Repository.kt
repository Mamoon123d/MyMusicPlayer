package com.musicplayer.android.room.repo

import androidx.lifecycle.LiveData
import com.musicplayer.android.room.data.FavoriteData
import com.musicplayer.android.room.data.PlayListData
import com.musicplayer.android.room.data.VideoItemPlData
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
    //---------------Favorite----------------------

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
}