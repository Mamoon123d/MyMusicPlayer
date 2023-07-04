package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.VideoItemBinding
import com.musicplayer.android.room.data.VideoHistoryData

class HistoryAdapter(context: Context, list: ArrayList<VideoHistoryData>) :BaseRvAdapter2<VideoHistoryData,HistoryAdapter.MyHolder>(
    context, list) {
    class MyHolder(binding: ViewBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, t: VideoHistoryData) {

        }

    }

    override fun onBindData(holder: MyHolder, t: VideoHistoryData) {
        holder.bind(context,t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}
