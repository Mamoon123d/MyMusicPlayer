package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.PlaylistItemBinding
import com.musicplayer.android.room.data.MusicPlayListData

class MusicPlAdapter(context: Context, list: List<MusicPlayListData>):BaseRvAdapter<MusicPlayListData,MusicPlAdapter.MyHolder>(
    context, list) {
    class MyHolder(val binding: PlaylistItemBinding) : BaseRvViewHolder(binding) {
        fun bind(t: MusicPlayListData) {
            binding.pTitle.text=t.title
        }

    }

    override fun onBindData(holder: MyHolder, t: MusicPlayListData) {
        holder.bind(t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            PlaylistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

}
