package com.musicplayer.android.video

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.VideoHistoryAdapter
import com.musicplayer.android.adapter.VpVideoAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.RemoveDialogBinding
import com.musicplayer.android.databinding.VideoDashFragBinding
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.room.data.VideoHistoryData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.visible
import com.musicplayer.android.utils.MyIntent

class VideoDashFrag : BaseFragment<VideoDashFragBinding>() {

    private var mainVm: MainViewModel? = null

    companion object {
        lateinit var videoList: ArrayList<VideoMainData>
    }


    override fun initM() {
        setVideos()
    }

    private fun setVideos() {
        initDb()
        setVideoHistory()
        binding.sortBtn.gone()
        binding.gridBt.gone()
        val vpa = VpVideoAdapter(childFragmentManager, lifecycle)
        binding.vp.adapter = vpa
        TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Video"
                }
                1 -> {
                    tab.text = "Folder"
                }
                2 -> {
                    tab.text = "Playlist"
                }
            }
        }.attach()
        binding.sortBtn.setOnClickListener {
            val menuList = arrayOf("Recently Added", "Name", "Size", "Default Order")
            var currentSort = MainActivity.sortOrder
            val builder = MaterialAlertDialogBuilder(mActivity)
            builder.setTitle("Sort by")
                .setPositiveButton("Yes") { _, _ ->
                    val editor = mActivity.getSharedPreferences("SORTING", MODE_PRIVATE).edit()
                    editor.putInt("sortOrder", currentSort)
                    editor.apply()
                }
                .setSingleChoiceItems(menuList, currentSort) { _, which ->
                    currentSort = which
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }

    }

    private fun initDb() {
        val myDao = Db.getDatabase(mActivity).myDao()
        val repo = Repository(dao = myDao)
        mainVm = ViewModelProvider(this, MainMvFactory(repo))[MainViewModel::class.java]

    }

    private fun removeFile(vidList: List<VideoHistoryData>, adapter: VideoHistoryAdapter) {
        val dialog = Dialog(mActivity)
        val binding_sheet = RemoveDialogBinding.inflate(LayoutInflater.from(mActivity))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)
        val cb = binding_sheet.deleteCh
        val titleTv = binding_sheet.titleTv
        cb.text = "Don't showing History at homepage again"
        cb.gone()
        titleTv.text = "Clear history of this video ?"
        dialog.window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialog.setCancelable(false)

        binding_sheet.posBtn.setOnClickListener {
            for (it in vidList) {
                mainVm!!.removeVideoInHistory(it)
            }
            dialog.dismiss()
            adapter.notifyDataSetChanged()
            binding.historyRl.gone()
        }


        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun setVideoHistory() {
        videoList = ArrayList()
        mainVm!!.getVideosInHistory().observe(this) { vidList ->
            /*MainActivity.videoList.forEach { vidData ->
                vidList.filter { it.videoId == vidData.id.trim().toLong() }.forEach {
                    videoList.add(
                        VideoMainData(
                            vidData.id,
                            vidData.title,
                            vidData.duration,
                            vidData.folderName,
                            vidData.size,
                            vidData.path,
                            vidData.artUri,
                            vidData.pixels,
                            vidData.isFavourite,
                            vidData.plVideoId,
                            vidData.type
                        )
                    )
                }
            }*/
            if (vidList.size > 0) {
                binding.historyRl.visible()
            } else {
                binding.historyRl.gone()
            }
            binding.videoHistoryRecycler.apply {
                val vhAdapter = VideoHistoryAdapter(mActivity, vidList)
                adapter = vhAdapter.apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            val vData = MainActivity.videoList.filter {
                                it.id.trim().toLong() == vidList[position].videoId
                            }
                            videoList.add(vData[0])
                            MyIntent.goIntent(mActivity, position, "VideoHistory")

                        }

                    })
                }
                layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
                setItemViewCacheSize(10)
                binding.delete.setOnClickListener {
                    removeFile(vidList, vhAdapter)
                }
            }


        }

        /* val data = ArrayList<VideoData>()

         for (i in 1..20) {
             data.add(VideoData(R.drawable.youtube_cover, "Item $i"))
         }*/

    }


    override fun setLayoutId(): Int {
        return R.layout.video_dash_frag
    }

}


