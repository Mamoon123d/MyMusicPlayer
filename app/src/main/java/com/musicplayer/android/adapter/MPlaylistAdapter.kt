package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.Playlist2ItemBinding
import com.musicplayer.android.room.data.MusicPlayListData
import com.musicplayer.android.room.vm.MainViewModel

class MPlaylistAdapter(
    context: Context,
    list: List<MusicPlayListData>,
   val mainViewModel: MainViewModel?
) :BaseRvAdapter<MusicPlayListData,MPlaylistAdapter.MyHolder>(context, list,) {
    class MyHolder(val binding: Playlist2ItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: MusicPlayListData, mainViewModel: MainViewModel?) {
            binding.plTv.text=data.title
            mainViewModel!!.getMusicsInPl(data.id!!.toInt()).observe(context as LifecycleOwner){plList->
                binding.plSongsTv.text= buildString {
                    append(plList.size)
                    if (plList.size > 1) append(" Songs")
                    else append(" Song")
                }
            }

        }

    }

    override fun onBindData(holder: MyHolder, t: MusicPlayListData) {
        holder.bind(context,t,mainViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(Playlist2ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}
