package com.musicplayer.android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.musicplayer.android.video.folder.FolderFragment
import com.musicplayer.android.video.playlist.PlaylistFragment
import com.musicplayer.android.video.VideoFragment

class VpVideoAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                VideoFragment()
            }
            1->{
                FolderFragment()
            }
            2->{
                PlaylistFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}