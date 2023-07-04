package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.ItemAudioListBinding
import com.musicplayer.android.model.MainMusicData

class AllSMAdapter(
    context: Context,
    list: MutableList<MainMusicData>
) : BaseRvAdapter2<MainMusicData, AllSMAdapter.MyHolder>(context, list) {

    var onMoreOptionClickListener: OnMoreOptionClickListener? = null

    override fun onBindData(holder: MyHolder, t: MainMusicData) {
        holder.bind(context, t, itemPosition, onMoreOptionClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemAudioListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class MyHolder(val binding: ItemAudioListBinding) : BaseRvViewHolder(binding) {
        fun bind(
            context: Context,
            data: MainMusicData,
            itemPosition: Int,
            onMoreOptionClickListener: OnMoreOptionClickListener?
        ) {
            binding.tvOrder.text = buildString {
                append(itemPosition + 1)
            }
            binding.tvSongName.text = data.title
            binding.tvArtistAndAlbum.text = buildString {
                append(data.artist)
                append("-")
                append(data.album)
            }
            binding.ivMore.setOnClickListener {
                onMoreOptionClickListener!!.onMoreOptionClick(data, position)
            }

        }

    }

    interface OnMoreOptionClickListener {
        fun onMoreOptionClick(data: MainMusicData, position: Int)
    }

    fun setOnMoreOptionClickListenerX(onMoreOptionClickListener: OnMoreOptionClickListener) {
        this.onMoreOptionClickListener = onMoreOptionClickListener
    }
}