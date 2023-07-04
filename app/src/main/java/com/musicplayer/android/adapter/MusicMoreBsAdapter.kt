package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.OSheetItemBinding
import com.musicplayer.android.model.OptionSheetData
import com.musicplayer.android.room.vm.MainViewModel

class MusicMoreBsAdapter(
    context: Context,
    list: List<OptionSheetData>,
    val mainVm: MainViewModel,
    val lifecycleOwner: LifecycleOwner,
    val musicId: String
) : BaseRvAdapter<OptionSheetData, MusicMoreBsAdapter.MyHolder>(context, list) {

    class MyHolder(val binding: OSheetItemBinding) : BaseRvViewHolder(binding) {
        fun bind(
            context: Context,
            data: OptionSheetData,
            mainVm: MainViewModel,
            lifecycleOwner: LifecycleOwner,
            musicId: String
        ) {
            if (data.id == 4) {
                mainVm.isMusicFavoriteExists(musicId).observe(lifecycleOwner) {
                    if (it > 0) {
                        Glide.with(context).load(R.drawable.fill_favorite).into(binding.oImg)
                        binding.oTitle.text = "Remove Favorite"
                    } else {
                        Glide.with(context).load(data.image).into(binding.oImg)
                        binding.oTitle.text = data.title
                    }
                }
            } else {
                Glide.with(context).load(data.image).into(binding.oImg)
                binding.oTitle.text = data.title
            }
        }

    }

    override fun onBindData(holder: MyHolder, t: OptionSheetData) {
        holder.bind(context, t, mainVm, lifecycleOwner, musicId)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            OSheetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
