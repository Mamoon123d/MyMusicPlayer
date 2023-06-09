package com.musicplayer.android.music

import androidx.lifecycle.ViewModelProvider
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.databinding.MusicPlaylistFragBinding
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel


class MusicPlaylistFrag : BaseFragment<MusicPlaylistFragBinding>() {
    private var mainViewModel: MainViewModel? = null
    override fun setLayoutId(): Int {
        return R.layout.music_playlist_frag
    }

    override fun initM() {

        initDb()
        initializeLayout()

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
    }

}