package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.TimeItemBinding

class TimeAdapter(context: Context,
                  list: List<String>
) :BaseRvAdapter<String,TimeAdapter.MyHolder>(context, list){
    class MyHolder(val binding: TimeItemBinding) : BaseRvViewHolder(binding) {
        fun bind(t: String) {
            binding.titleTv.text=t
        }

    }

    override fun onBindData(holder: MyHolder, t: String) {
        holder.bind(t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(TimeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}
