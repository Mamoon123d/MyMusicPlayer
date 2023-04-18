package com.musicplayer.android.room.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel

class MainMvFactory(private val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T

    }
}