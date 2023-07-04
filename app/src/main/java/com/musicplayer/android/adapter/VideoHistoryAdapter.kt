package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.ItemVideoHistoryBinding
import com.musicplayer.android.model.formatDuration
import com.musicplayer.android.room.data.VideoHistoryData

class VideoHistoryAdapter(context: Context, list: List<VideoHistoryData>) :
    BaseRvAdapter<VideoHistoryData, VideoHistoryAdapter.MyHolder>(
        context, list
    ) {


    /* @SuppressLint("SuspiciousIndentation")
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
     val view=LayoutInflater.from(parent.context).inflate(R.layout.item_video_history,parent,false)
         return vh(view)
     }*/


    override fun onBindData(holder: MyHolder, t: VideoHistoryData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemVideoHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class MyHolder(val binding: ItemVideoHistoryBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, t: VideoHistoryData) {
            binding.imgVideoHistory.apply {
                Glide.with(context).load(t.path).into(this)
            }
            binding.videoHistoryDuration.text= formatDuration(t.duration)
        }

    }
}