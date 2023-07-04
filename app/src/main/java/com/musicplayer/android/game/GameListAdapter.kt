package com.musicplayer.android.game

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.GameItem2Binding
import com.musicplayer.android.model.GameData

class GameListAdapter(context: Context,
                      list: List<GameData>
):BaseRvAdapter<GameData,GameListAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: GameItem2Binding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, t: GameData) {
            binding.gameName.text=t.gameName
            Glide.with(context).load(t.img).into(binding.ivGame)

        }

    }

    override fun onBindData(holder: MyHolder, t: GameData) {
        holder.bind(context,t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(GameItem2Binding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}
