package com.musicplayer.android.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.RewardItemBinding
import com.musicplayer.android.model.RewardData

class RewardAdapter(
    context: Context,
    list: List<RewardData>
) : BaseRvAdapter<RewardData, RewardAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: RewardItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: RewardData) {
         binding.titleTv.text=data.title
            binding.subtitleTv.text=data.subtitle

            binding.titleIv.apply {
                Glide.with(context).load(data.titleImg).into(this)
            }
            binding.bottomCon.apply {
                setColorFilter(Color.parseColor(data.bgColor))
            }
        }

    }

    override fun onBindData(holder: MyHolder, t: RewardData) {
        holder.bind(context,t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            RewardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
