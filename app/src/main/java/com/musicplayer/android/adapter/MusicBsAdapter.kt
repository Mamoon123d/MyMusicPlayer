package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.MusicItemBinding
import com.musicplayer.android.model.MainMusicData
import com.musicplayer.android.music.MPlayerActivity

class MusicBsAdapter(
    context: Context, list: ArrayList<MainMusicData>
) : BaseRvAdapter2<MainMusicData, MusicBsAdapter.MyHolder>(context, list) {
    private var onItemCloseListener: OnItemCloseListener? = null

    class MyHolder(val binding: MusicItemBinding) : BaseRvViewHolder(binding) {
        var isPlaying: Boolean = false
        fun bind(
            context: Context,
            data: MainMusicData,
            itemPosition: Int,
            onItemCloseListener: OnItemCloseListener?
        ) {
            isPlaying = itemPosition == MPlayerActivity.songPosition
            binding.tvOrder.apply {
                text = buildString {
                    append(itemPosition + 1)

                }
                /*if (isPlaying) {
                    this.gone()
                    val bar = binding.bar
                    bar.visible()
                    bar.isEnabled = true
                    // val audioSessionId: Int = MPlayerActivity.mediaPlayer!!.audioSessionId?:-1
                     //if (audioSessionId != -1) bar.setAudioSessionId(audioSessionId)
                } else {
                    this.visible()
                }*/

                setTextColor(if (isPlaying) context.getColor(R.color.text_2) else context.getColor(R.color.text_1))

            }
            binding.tvSongName.apply {
                text = data.title
                if (isPlaying) setTextColor(context.getColor(R.color.text_2))
                else setTextColor(context.getColor(R.color.text_1))
            }
            binding.tvArtistAndAlbum.apply {
                text = buildString {
                    append(data.artist)
                    append("-")
                    append(data.album)
                }
                if (isPlaying) setTextColor(context.getColor(R.color.text_2))
                else setTextColor(context.getColor(R.color.text_1))
            }
            binding.ivClose.setOnClickListener {
                onItemCloseListener!!.onItemClose(itemPosition)
            }
        }

    }

    override fun onBindData(holder: MyHolder, t: MainMusicData) {
        holder.bind(context, t, itemPosition, onItemCloseListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            MusicItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    interface OnItemCloseListener {
        fun onItemClose(position: Int)
    }

    fun setOnItemCloseListener(onItemCloseListener: OnItemCloseListener) {
        this.onItemCloseListener = onItemCloseListener
    }

}
