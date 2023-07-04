package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.FolderItemBinding
import com.musicplayer.android.model.Folder2
import com.musicplayer.android.model.getAllFolderVideo
import com.musicplayer.android.utils.Extension.Companion.gone

class FolderAdapter(context: Context, list: ArrayList<Folder2>) :
    BaseRvAdapter2<Folder2, FolderAdapter.MyHolder>(context, list) {
    //lateinit var mainVm: MainViewModel

    init {
      //  val dao= Db.getDatabase(context).myDao()
       // val repo=Repository(dao)
       //  mainVm=ViewModelProvider(context as ViewModelStoreOwner,MainMvFactory(repo))[MainViewModel::class.java]
    }
    class MyHolder(val binding: FolderItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: Folder2, list: MutableList<Folder2>) {
            binding.folderTv.text=data.folderName

            val noOfVideos=getAllFolderVideo(context = context,data.id.trim()).size
            binding.totalVideo.text="${noOfVideos} videos"

            binding.coverIcon.gone()

            if (noOfVideos==0){
                list.remove(data)
            }




        }

    }

    override fun onBindData(holder: MyHolder, t: Folder2) {
        holder.bind(context,t,list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            FolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}
