package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.ItemArtistListBinding
import com.musicplayer.android.model.MusicArtistData

class MusicArtistAdapter(context: Context, list: ArrayList<MusicArtistData>) :
    BaseRvAdapter2<MusicArtistData, MusicArtistAdapter.MyHolder>(
        context, list
    ) {
    class MyHolder(val binding: ItemArtistListBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: MusicArtistData) {
            binding.artistTv.text = data.artists
            binding.albumSongsTv.text = buildString {
                append(data.trackNumber)
                append(" Songs")
            }
        }
    }

    override fun onBindData(holder: MyHolder, t: MusicArtistData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemArtistListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
