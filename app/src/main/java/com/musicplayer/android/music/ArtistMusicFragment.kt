package com.musicplayer.android.music

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicArtistAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.ArtistMusicFragBinding
import com.musicplayer.android.model.MusicArtistData
import com.musicplayer.android.utils.DataSet


class ArtistMusicFragment : BaseFragment<ArtistMusicFragBinding>() {
    override fun initM() {
        setArtistList()
    }

    companion object {
        lateinit var artistList: ArrayList<MusicArtistData>
    }

    override fun setLayoutId(): Int {
        return R.layout.artist_music_frag
    }

    private fun setArtistList() {
        artistList = MainActivity.musicArtistList
        binding.artisRv.apply {
            adapter = MusicArtistAdapter(mActivity, artistList).apply {
                setOnItemClickListener(object:BaseRvAdapter2.OnItemClickListener{
                    override fun onItemClick(v: View?, position: Int) {
                        val data= artistList[position]
                        val b= Bundle()
                        b.putInt(DataSet.REF, DataSet.Reference.MUSIC_ARTIST)
                        b.putString(DataSet.TITLE,data.artists)
                        b.putString(DataSet.ARTIST_ID,data.artistId)
                        goActivity(MusicListHome(),b)
                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

}