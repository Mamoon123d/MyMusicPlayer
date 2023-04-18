package com.musicplayer.android.video.folder

import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.adapter.VideoAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.databinding.AllFolderVideosBinding

class AllFolderVideosFrag : BaseFragment<AllFolderVideosBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.all_folder_videos
    }

    override fun initM() {
        setVideos()
    }

    private fun setVideos() {
        binding.rvVideo.apply {
            adapter = VideoAdapter(mActivity, FolderActivity.videoList).apply {
               // setOnItemClickListener()
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }
}