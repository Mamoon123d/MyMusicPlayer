package com.musicplayer.android.video.folder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.adapter.VideoAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.AllFolderVideosBinding
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.utils.MyIntent

class AllFolderVideosFrag : BaseFragment<AllFolderVideosBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.all_folder_videos
    }

    override fun initM() {
        setVideos()
    }

    private fun setVideos() {
        binding.rvVideo.apply {
            adapter = VideoAdapter(
                mActivity,
                FolderActivity.videoList,
                lifecycleOwner = mActivity
            ).apply {
                adapter
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        MyIntent.goIntent(mActivity, position, "FolderVideos")
                    }

                })
                setOnMoreOptionClickListenerX(object : VideoAdapter.OnMoreOptionClickListener {
                    override fun onMoreOptionClick(data: VideoMainData, position: Int) {
                        //MoreVideoBS(mActivity,data,position,this@apply)
                    }

                })

            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }
}