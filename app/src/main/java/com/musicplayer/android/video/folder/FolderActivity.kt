package com.musicplayer.android.video.folder

import android.os.Build
import android.os.Environment
import com.google.android.material.tabs.TabLayoutMediator
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.MyPageAdapter
import com.musicplayer.android.databinding.FolderActivityBinding
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.model.getAllFolderVideo

class FolderActivity : BaseActivity<FolderActivityBinding>() {
    companion object{
        lateinit var videoList: ArrayList<VideoMainData>

    }
    override fun setLayoutId(): Int {
        return R.layout.folder_activity
    }

    override fun initM() {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.isExternalStorageManager()

            } else {
                TODO("VERSION.SDK_INT < R")
            }
        ) {
            //    showMsg("granted")
            // Permission granted. Now resume your workflow.
            val folderId = intent.getStringExtra("folder_id")
           // val folderName = intent.getStringExtra("folder_name")

            videoList = getAllFolderVideo(mActivity,folderId!!)

            setTabs()
        } else {
            videoList = ArrayList()
        }
        setTb()

    }

    private fun setTb() {
        val folderName = intent.getStringExtra("folder_name")
        binding.title.text = folderName
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setTabs() {
        val vpa = MyPageAdapter(supportFragmentManager, lifecycle).apply {
            addFragment(AllFolderVideosFrag(),"Video")
            addFragment(AllFolderVideosFrag(),"Folder")
        }
        binding.vp.adapter = vpa
        TabLayoutMediator(binding.tabs, binding.vp) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Video"
                }
                1 -> {
                    tab.text = "Folder"
                }

            }
        }.attach()

    }
}