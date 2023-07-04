package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.TaskItemBinding

import com.musicplayer.android.model.TaskData

class TaskAdapter(
    context: Context,
    list: List<TaskData>
) : BaseRvAdapter<TaskData, TaskAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: TaskItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: TaskData) {
            Glide.with(context).load(data.taskImg).into(binding.ivOffer)
            binding.tvTitle.text = data.title
            binding.tvSubtitle.text = data.subtitle
        }

    }

    override fun onBindData(holder: MyHolder, t: TaskData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
