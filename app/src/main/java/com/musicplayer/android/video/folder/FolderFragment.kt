package com.musicplayer.android.video.folder

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.FolderAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
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
        folderList = getAllVideo()
        val list = MainActivity.folderList
        binding.videoRecyclerFolder.apply {
            adapter = FolderAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
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

    @SuppressLint("Range", "Recycle")
    private fun getAllVideo(): ArrayList<VideoFolderData> {
        val tempFolderList = ArrayList<VideoFolderData>()
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_ID
        )

        val cursor = mActivity.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        //sort Order=MediaStore.Video.Media.DATE_TAKEN + "DESC"
        Log.d("TAG", "getAllVideo: $cursor")
        if (cursor != null)
            if (cursor.moveToNext())
                do {
                    val dateC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
//                  val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()
                    try {
                        if (tempFolderList.add(VideoFolderData(id = folderIdC, folderName = folderC))){

                        }
                       // if (folderList!!.add(VideoFolderData(id = folderIdC, folderName = folderC))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
        cursor!!.close()
        return tempFolderList
    }

}