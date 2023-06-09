package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.SpeedPlayItemBinding

class SpeedPlayAdapter(
    context: Context,
    list: List<String>
) : BaseRvAdapter<String, SpeedPlayAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: SpeedPlayItemBinding) : BaseRvViewHolder(binding) {
        fun bind(t: String) {
            binding.titleTv.text = t
        }

    }

    override fun onBindData(holder: MyHolder, t: String) {
        holder.bind(t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            SpeedPlayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
