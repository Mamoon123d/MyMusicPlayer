package com.musicplayer.android.video

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.VideoHistoryAdapter
import com.musicplayer.android.adapter.VpVideoAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.databinding.VideoDashFragBinding
import com.musicplayer.android.model.VideoData

class VideoDashFrag : BaseFragment<VideoDashFragBinding>() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }


    override fun initM() {
        setVideos()
    }

    private fun setVideos() {
        val data = ArrayList<VideoData>()
        for (i in 1..20) {
            data.add(VideoData(R.drawable.youtube_cover, "Item $i"))
        }
        binding.videoHistoryRecycler.setHasFixedSize(true)
        binding.videoHistoryRecycler.setItemViewCacheSize(10)
        val adapter = context?.let { VideoHistoryAdapter(it, data) }
        binding.videoHistoryRecycler.adapter = adapter
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

    override fun setLayoutId(): Int {
        return R.layout.video_dash_frag
    }

}


