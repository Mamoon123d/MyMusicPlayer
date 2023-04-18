package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.FolderItemBinding
import com.musicplayer.android.model.Folder2

class FolderAdapter(context: Context, list: List<Folder2>) :
    BaseRvAdapter<Folder2, FolderAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: FolderItemBinding) : BaseRvViewHolder(binding) {
        fun bind(data: Folder2) {
            binding.folderTv.text=data.folderName
            binding.totalVideo.text="2 videos"


        }

    }

    override fun onBindData(holder: MyHolder, t: Folder2) {
        holder.bind(t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            FolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}
