package com.musicplayer.android.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.PlaylistVideoItemBinding
import com.musicplayer.android.room.data.PlayListData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import java.io.File

class PlaylistAdapter(
    context: Context,
    list: List<PlayListData>, private val lifecycleOwner: LifecycleOwner
) : BaseRvAdapter<PlayListData, PlaylistAdapter.MyHolder>(context, list) {
    private lateinit var mainVm: MainViewModel

    init {
        val dao = Db.getDatabase(context).myDao()
        val repository = Repository(dao)
        mainVm = MainViewModel(repository)
    }

    class MyHolder(val binding: PlaylistVideoItemBinding) : BaseRvViewHolder(binding) {
        fun bind(
            context: Context,
            t: PlayListData,
            mainVm: MainViewModel,
            lifecycleOwner: LifecycleOwner
        ) {
            binding.vTitle.text = t.title

            mainVm.getPlItemList(t.id!!.toInt()).observe(lifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    binding.vSubtitle.text = "${list.size} Videos"

                    try {
                        val file = File(list[0].path)
                        val artUri = Uri.fromFile(file)
                        if (artUri != null) {

                            Glide.with(context).asBitmap().load(artUri).apply(
                                RequestOptions().placeholder(R.drawable.about).centerCrop()
                            ).into(binding.vThumbnail)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    mainVm.deletePl(t)
                  /*  binding.vSubtitle.text = "no video"
                    Glide.with(context).asBitmap().load(R.drawable.music_color_icon).apply(
                        RequestOptions().placeholder(R.drawable.about).centerCrop()
                    ).into(binding.vThumbnail)*/
                }
            }


        }

    }

    override fun onBindData(holder: MyHolder, t: PlayListData) {
        holder.bind(context, t, mainVm, lifecycleOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            PlaylistVideoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
