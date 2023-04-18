package com.musicplayer.android.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.VideoItemBinding
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.visible

class VideoAdapter(
    context: Context,
    list: MutableList<VideoMainData>,
    val isFolder: Boolean = false,
    private val lifecycleOwner: LifecycleOwner? = null
) : BaseRvAdapter2<VideoMainData, VideoAdapter.MyHolder>(context, list) {

    private lateinit var mainVm: MainViewModel

    init {
        val dao = Db.getDatabase(context).myDao()
        val repository = Repository(dao)
        mainVm = MainViewModel(repository)
    }

    var onMoreOptionClickListener: OnMoreOptionClickListener? = null

    class MyHolder(val binding: VideoItemBinding) : BaseRvViewHolder(binding) {
        fun bind(
            context: Context,
            data: VideoMainData,
            folder: Boolean,
            onMoreOptionClickListener: OnMoreOptionClickListener?,
            mainVm: MainViewModel,
            lifecycleOwner: LifecycleOwner?,

            ) {
            binding.vTitle.text = data.title

            binding.vFolder.apply {

                if (data.path.contains("/Movies/")) {
                    text = "Movies"
                } else if (data.path.contains("/Download/")) {
                    text = "Download"
                } else if (data.path.contains("/Camera/")) {
                    text = "Camera"
                } else if (data.path.contains("/WhatsApp/")) {
                    text = "WhatsApp"
                } else {
                    text = data.folderName
                }
            }
            binding.vDuration.text = DateUtils.formatElapsedTime(
                data.duration / 1000
            )
            binding.vSubtitle.text = buildString {
                append(getPixelsString(data.pixels))
                append(" | ")
                append(data.size)
            }

            Glide.with(context)
                .asBitmap()
                .load(data.artUri)
                .apply(RequestOptions().placeholder(R.drawable.about).centerCrop())
                .into(binding.vThumbnail)

            binding.moreImg.setOnClickListener {
                if (onMoreOptionClickListener!=null) {
                    onMoreOptionClickListener!!.onMoreOptionClick(data, position)
                }
            }
            binding.starImg.apply {
                mainVm.isFavoriteExists(data.id).observe(lifecycleOwner!!) {
                    if (it > 0) {
                        this.visible()
                        data.isFavourite = true
                    } else {
                        this.gone()
                        data.isFavourite = false
                    }
                }

            }
        }

        fun getPixelsString(pixelsC: String?): String {
            if (pixelsC != null && !pixelsC!!.matches(Regex("Unknown"))) {
                val heightWidth = pixelsC!!.split("×")
                val height = heightWidth[0].trim().toInt()
                val width = heightWidth[1].trim().toInt()
                if (height < width) {
                    return "${height}p"
                } else {
                    return "${width}p"
                }
            } else {
                return "Unknown"
            }
        }

    }


    override fun onBindData(holder: MyHolder, t: VideoMainData) {
        holder.bind(context, t, isFolder, onMoreOptionClickListener, mainVm, lifecycleOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            VideoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface OnMoreOptionClickListener {
        fun onMoreOptionClick(data: VideoMainData, position: Int)
    }

    fun setOnMoreOptionClickListenerX(onMoreOptionClickListener: OnMoreOptionClickListener) {
        this.onMoreOptionClickListener = onMoreOptionClickListener
    }
}