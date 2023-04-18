package com.musicplayer.android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.musicplayer.android.music.*

class MusicAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllSongMusicFragment()
            }
            1 -> {
                PlaylistMusicFragment()
            }
            2 -> {
                FolderMusicFragment()
            }
            3 -> {
                AlbumFragment()
            }
            4 -> {
                ArtistMusicFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}