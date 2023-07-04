package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.TransItemBinding
import com.musicplayer.android.model.TransData

class TransAdapter(
    context: Context,
    list: List<TransData>
) : BaseRvAdapter<TransData, TransAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: TransItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, t: TransData) {
            binding.tvTitle.text = t.title
            binding.tvSubtitle.text = buildString {
                append(t.type)
                append("  ")
                append(t.date)
            }
            binding.tvCoin.text = t.coins
        }

    }

    override fun onBindData(holder: MyHolder, t: TransData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            TransItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
