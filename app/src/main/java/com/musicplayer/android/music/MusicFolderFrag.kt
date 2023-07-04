package com.musicplayer.android.music

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicFolderAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.FolderMusicFragBinding
import com.musicplayer.android.utils.DataSet


class MusicFolderFrag : BaseFragment<FolderMusicFragBinding>() {


    override fun setLayoutId(): Int {
        return R.layout.folder_music_frag
    }

    override fun initM() {

        setFolderList()
    }

    private fun setFolderList() {
        binding.rv.apply {
            adapter = MusicFolderAdapter(mActivity, MainActivity.audioFolders).apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        val data= MainActivity.audioList[position]
                        val b= Bundle()
                        b.putInt(DataSet.REF, DataSet.Reference.MUSIC_FOLDER)
                        b.putString(DataSet.TITLE,data.folderName)
                        b.putString(DataSet.FOLDER_ID,data.id)
                        goActivity(MusicListHome(),b)
                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

}