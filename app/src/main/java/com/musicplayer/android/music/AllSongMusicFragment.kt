package com.musicplayer.android.music

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.AllSMAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.FragmentAllSongMusicBinding


class AllSongMusicFragment : BaseFragment<FragmentAllSongMusicBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun initM() {
        setALlSongs()
    }

    private fun setALlSongs() {
        // val data = ArrayList<AudioData>()

        /* for (i in 1..20) {
             data.add(AudioData(1,"Item $i","Video Detail"))
         }*/

        binding.audioRecycler.apply {
            adapter = AllSMAdapter(mActivity, MainActivity.audioList).apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        val b = Bundle()
                        b.putInt("pos", position)
                        goActivity(MPlayerActivity(), b)
                        //showMsg(MainActivity.audioList[position].artist)
                    }
                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }


    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_all_song_music
    }

}