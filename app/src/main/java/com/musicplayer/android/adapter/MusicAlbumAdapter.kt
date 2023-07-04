package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.ItemAlbumListBinding
import com.musicplayer.android.model.MusicAlbumData

class MusicAlbumAdapter(context: Context, list: ArrayList<MusicAlbumData>) :
    BaseRvAdapter2<MusicAlbumData, MusicAlbumAdapter.MyHolder>(
        context, list
    ) {
    class MyHolder(val binding: ItemAlbumListBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: MusicAlbumData) {
            binding.albumTv.text = data.albumName
            binding.albumSongsTv.text = buildString {
                append(data.totalTrack)
                append(" Songs")
            }
            binding.ivCover.apply {
                if (data.artUri != null) {
                    Glide.with(context).load(data.artUri).placeholder(R.drawable.music_color_icon).into(this)
                }
            }
        }

    }

    override fun onBindData(holder: MyHolder, t: MusicAlbumData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemAlbumListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
