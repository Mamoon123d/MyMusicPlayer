package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.SettingItemBinding
import com.musicplayer.android.model.SettingData

class SettingAdapter(
    context: Context,
    list: List<SettingData>
) : BaseRvAdapter<SettingData, SettingAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: SettingItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: SettingData) {
            binding.sImg.setImageResource(data.img)
            binding.sTitle.text = data.title
            binding.sSubtitle.text = data.subtitle
        }
    }

    override fun onBindData(holder: MyHolder, t: SettingData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            SettingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
