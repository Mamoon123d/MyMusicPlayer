package com.musicplayer.android.video.folder

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.FolderAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.FragmentFolderVideoBinding
import com.musicplayer.android.model.VideoFolderData

class FolderFragment : BaseFragment<FragmentFolderVideoBinding>() {

    companion object {
        var folderList: ArrayList<VideoFolderData>? = null

    }


    override fun initM() {
        setFolderGrid()
    }

    private fun setFolderGrid() {
      //  folderList = getAllVideosFromFolder(mActivity)
        val list = MainActivity.folderList

        /*val fList=ArrayList<Folder2>()

        for (data in list){
            val pathBlocks = data.path!!.split("/")
            val dir = pathBlocks[4]
            var isAdded=false
            list.filter { it.path!!.contains("/${dir}/") }.forEach {folder->
                if (!isAdded) {
                    Log.d("folder", "folder dir : "+dir+" path : "+folder.path)
                    fList.add(folder.apply {
                        folderName=dir
                    })
                    isAdded=true

                }
            }
        }*/


        binding.videoRecyclerFolder.apply {
            adapter = FolderAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        val data = list[position]
                        val b = Bundle()
                        b.putString("folder_id", data.id)
                        b.putString("folder_name", data.folderName)
                        goActivity(FolderActivity(), b)
                    }
                })
            }
            layoutManager = GridLayoutManager(mActivity, 3, RecyclerView.VERTICAL, false)

        }

        // binding.videoRecyclerFolder.layoutManager = GridLayoutManager(mActivity, 3)
        // binding.videoRecyclerFolder.adapter = VideoFolderAdapter(mActivity, folderList)

    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_folder_video
    }


}