package com.musicplayer.android.room.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicplayer.android.room.data.FavoriteData
import com.musicplayer.android.room.data.PlayListData
import com.musicplayer.android.room.data.VideoItemPlData
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

    fun isFavoriteExists(videoId:String):LiveData<Int>{

        return repo.isFavorite(videoId)
    }

    fun totalFavorite():LiveData<Long>{
        return repo.totalFavorite()
    }

/*
     fun isExistsTD(td: Int): Boolean {
         var isExtsts: Boolean? = false
         viewModelScope.launch(Dispatchers.IO) {
             isExtsts = repo.isExistsTd(td)
             Log.d("isExistsTD","is exists : ${repo.isExistsTd(td)}")
         }
         return isExtsts!!
     }*/

}