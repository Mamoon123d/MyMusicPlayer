package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.PlaylistItemBinding
import com.musicplayer.android.room.data.PlayListData

class PlAdapter(context: Context,
                list: List<PlayListData>
) : BaseRvAdapter<PlayListData, PlAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: PlaylistItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, t: PlayListData) {
            binding.pTitle.text=t.title
        }

    }

    override fun onBindData(holder: MyHolder, t: PlayListData) {
        holder.bind(context,t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(PlaylistItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}
