package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.GameItemBinding
import com.musicplayer.android.model.GameData

class GameAdapter(
    context: Context,
    list: List<GameData>
) : BaseRvAdapter<GameData, GameAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: GameItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: GameData) {
            binding.tvGameName.text = data.gameName
            Glide.with(context).load(data.img)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.ivGame)

        }

    }

    override fun onBindData(holder: MyHolder, t: GameData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

}
