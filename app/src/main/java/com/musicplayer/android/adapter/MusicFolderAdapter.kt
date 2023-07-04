package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.MusicFolderItemBinding
import com.musicplayer.android.model.MusicFolder

class MusicFolderAdapter(context: Context, list: ArrayList<MusicFolder>) :
    BaseRvAdapter2<MusicFolder, MusicFolderAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: MusicFolderItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: MusicFolder) {
            binding.folderNameTv.text = data.folderName
            binding.pathTv.text = data.folderPath
        }

    }

    override fun onBindData(holder: MyHolder, t: MusicFolder) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            MusicFolderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}


