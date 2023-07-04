package com.musicplayer.android.music

import com.google.android.material.tabs.TabLayoutMediator
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.databinding.FragmentMusicBinding

class MusicFragment : BaseFragment<FragmentMusicBinding>() {
   

    override fun initM() {
        setTabs()
    }

    private fun setTabs() {
        val vpa = MusicAdapter(childFragmentManager,lifecycle)
        binding.vpMusic.adapter=vpa
        TabLayoutMediator(binding.tabLayoutMusic,binding.vpMusic){tab,position->
            when(position){
                0->{
                    tab.text="All Songs"
                }
                1->{
                    tab.text="Playlist"
                }
                2->{
                    tab.text="Folder"
                }
                3->{
                    tab.text="Album"
                }
                4->{
                    tab.text="Artist"
                }
            }
        }.attach()

    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_music
    }

}