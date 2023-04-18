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
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel

class SheetAdapter(
    context: Context,
    list: List<OptionSheetData>,
   private val lifecycleOwner: LifecycleOwner?=null,
    private val videoId:String

) : BaseRvAdapter<OptionSheetData, SheetAdapter.MyHolder>(context, list) {
    private lateinit var mainVm: MainViewModel

    init {
        val dao = Db.getDatabase(context).myDao()
        val repository = Repository(dao)
        mainVm = MainViewModel(repository)
    }
    class MyHolder(val binding: OSheetItemBinding) : BaseRvViewHolder(binding) {

        fun bind(
            context: Context,
            data: OptionSheetData,
            mainVm: MainViewModel,
            lifecycleOwner: LifecycleOwner,
            videoId: String
        ) {

            if (position==0){
                  mainVm.isFavoriteExists(videoId =videoId ).observe(lifecycleOwner){
                      if (it>0){
                          Glide.with(context).load(R.drawable.fill_favorite).into(binding.oImg)
                          binding.oTitle.text = "Remove Favorite"
                      }else{
                          Glide.with(context).load(data.image).into(binding.oImg)
                          binding.oTitle.text = data.title
                      }
                  }
            }else{
                Glide.with(context).load(data.image).into(binding.oImg)
                binding.oTitle.text = data.title
            }
        }

    }

    override fun onBindData(holder: MyHolder, t: OptionSheetData) {
        holder.bind(context, t,mainVm,lifecycleOwner!!,videoId)
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
