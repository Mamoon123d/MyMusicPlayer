package com.musicplayer.android.music

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MPlaylistAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.MusicPlaylistFragBinding
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.DataSet


class MusicPlaylistFrag : BaseFragment<MusicPlaylistFragBinding>() {
    private var mainViewModel: MainViewModel? = null
    override fun setLayoutId(): Int {
        return R.layout.music_playlist_frag
    }

    override fun initM() {

        initDb()
        initializeLayout()
        binding.allCon.setOnClickListener {
            //showMsg("all songs")
            val b = Bundle()
            b.putInt(DataSet.REF, DataSet.Reference.MUSIC_ALL_PL)
            goActivity(MusicPlaylistView(), b)
        }
        binding.recentCon.setOnClickListener {
            val b = Bundle()
            b.putInt(DataSet.REF, DataSet.Reference.MUSIC_RECENT)
            goActivity(MusicPlaylistView(), b)
        }
        binding.favCon.setOnClickListener {
            val b = Bundle()
            b.putInt(DataSet.REF, DataSet.Reference.MUSIC_FAVORITE)
            goActivity(MusicPlaylistView(), b)
        }

        setOtherPl()
    }

    private fun setOtherPl() {

        mainViewModel!!.getMusicPl().observe(this){list->
        binding.rvPl.apply {
           adapter= MPlaylistAdapter(context,list,mainViewModel).apply {
               setOnItemClickListener(object:BaseRvAdapter.OnItemClickListener{
                   override fun onItemClick(v: View?, position: Int) {
                       val b = Bundle()
                       b.putInt(DataSet.REF, DataSet.Reference.MUSIC_PLAYLIST)
                       b.putLong(DataSet.PLAYLIST_ID,list[position].id!!)
                       b.putString(DataSet.PLAYLIST_NAME,list[position].title)

                       goActivity(MusicPlaylistView(), b)
                   }
               })
           }
            layoutManager=LinearLayoutManager(mActivity)
        }
        }

    }

    private fun initDb() {
        val dao = Db.getDatabase(mActivity).myDao()
        val repository = Repository(dao)
        mainViewModel =
            ViewModelProvider(this, MainMvFactory(repository))[MainViewModel::class.java]

    }

    private fun initializeLayout() {
        binding.totalSongTv.text = buildString {
            val list = MainActivity.audioList
            append(list.size)
            if (list.size > 1) append(" Songs")
            else append(" Song")

        }

        binding.favSongsTv.apply {
            mainViewModel!!.getMusicFavorites().observe(this@MusicPlaylistFrag) { list ->
                text = buildString {
                    append(list.size)
                    if (list.size > 1) append(" Songs")
                    else append(" Song")
                }
            }
        }

        binding.rpSongsTv.apply {
            mainViewModel!!.recentMusics().observe(this@MusicPlaylistFrag) { list ->
                text = buildString {
                    append(list.size)
                    if (list.size > 1) append(" Songs")
                    else append(" Song")
                }
            }
        }
    }

}