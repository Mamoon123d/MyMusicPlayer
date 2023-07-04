package com.musicplayer.android.music

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.AllSMAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.MusicListHomeBinding
import com.musicplayer.android.model.MainMusicData
import com.musicplayer.android.utils.DataSet
import com.musicplayer.android.utils.MoreMusicBS
import com.musicplayer.android.utils.MyIntent

class MusicListHome : BaseActivity<MusicListHomeBinding>() {

    private var ref: Int = -1

    companion object {
        public lateinit var audioList: ArrayList<MainMusicData>
    }

    override fun setLayoutId(): Int {
        return R.layout.music_list_home
    }

    override fun initM() {
        initializeLayout()
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initializeLayout() {
        ref = intent.getIntExtra(DataSet.REF, -1)
        when (ref) {
            DataSet.Reference.MUSIC_ALBUM -> {
                val albumId = intent.getStringExtra(DataSet.ALBUM_ID)!!.trim().toLong()
                val albumName = intent.getStringExtra(DataSet.TITLE)
                binding.titleTv.text = albumName
                audioList = ArrayList()
                audioList.addAll(MainActivity.audioList.filter {
                    it.albumId.trim().toLong() == albumId
                })
                setAudioList()
            }
            DataSet.Reference.MUSIC_ARTIST -> {
                val artistId = intent.getStringExtra(DataSet.ARTIST_ID)!!.trim().toLong()
                val artist = intent.getStringExtra(DataSet.TITLE)
                binding.titleTv.text = artist
                audioList = ArrayList()
                audioList.addAll(MainActivity.audioList.filter {
                    it.artistId.trim().toLong() == artistId
                })
                setAudioList()
            }
            DataSet.Reference.MUSIC_FOLDER -> {
                val folderId = intent.getStringExtra(DataSet.FOLDER_ID)!!.trim().toLong()
                val folderName = intent.getStringExtra(DataSet.TITLE)!!.trim()
                binding.titleTv.text = folderName
                audioList = ArrayList()
                audioList.addAll(MainActivity.audioList.filter {
                    it.folderName.trim().matches(Regex(folderName))
                    // it.folderName.trim()==folderName
                })
                setAudioList()
            }

        }
    }

    private fun setAudioList() {
        binding.musicRv.apply {
            adapter = AllSMAdapter(mActivity, audioList).apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        MyIntent.goMusicIntent(mActivity, position, ref)
                    }

                })

                setOnMoreOptionClickListenerX(object : AllSMAdapter.OnMoreOptionClickListener {
                    override fun onMoreOptionClick(data: MainMusicData, position: Int) {
                        //  showMsg("more")
                        MoreMusicBS(
                            context = mActivity,
                            musicData = data,
                            musicAdapter = adapter,
                            music_position = position,
                            ref
                        )
                    }
                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

}
