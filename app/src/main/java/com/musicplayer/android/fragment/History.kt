package com.musicplayer.android.fragment

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.VideoAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.HistoryPageBinding
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.MoreVideoBS
import com.musicplayer.android.utils.MyIntent
import java.io.File

class History : BaseActivity<HistoryPageBinding>() {
    private var mainVm: MainViewModel? = null

    companion object {
        lateinit var vList: ArrayList<VideoMainData>
    }

    override fun setLayoutId(): Int {
        return R.layout.history_page
    }

    override fun initM() {
        initDb()
        setHistory()
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initDb() {
        val myDao = Db.getDatabase(mActivity).myDao()
        val repo = Repository(dao = myDao)
        mainVm = ViewModelProvider(this, MainMvFactory(repo))[MainViewModel::class.java]

    }

    private fun setHistory() {
        mainVm!!.getVideosInHistory().observe(this) { vidList ->
            vList = ArrayList()
            vidList.forEach { hData ->
                val file = File(hData.path)
                if (file.exists()) {
                    MainActivity.videoList.filter { it.id.trim().toLong() == hData.videoId }
                        .forEach { d ->
                            vList.add(
                                VideoMainData(
                                    id = d.id,
                                    title = d.title,
                                    duration = d.duration,
                                    folderName = d.folderName,
                                    size = d.size,
                                    path = d.path,
                                    artUri = d.artUri,
                                    pixels = d.pixels,
                                    isFavourite = d.isFavourite,
                                    plVideoId = d.plVideoId,
                                    type = d.type
                                )
                            )
                        }
                }
            }
            binding.rvHistory.apply {
                adapter = VideoAdapter(mActivity, vList, lifecycleOwner = mActivity).apply {
                    setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            MyIntent.goIntent(mActivity, ref = "History", pos = position)
                        }

                    })
                    setOnMoreOptionClickListenerX(object : VideoAdapter.OnMoreOptionClickListener {
                        override fun onMoreOptionClick(data: VideoMainData, position: Int) {
                            MoreVideoBS(mActivity, data, position, this@apply)
                        }

                    })
                }
                layoutManager = LinearLayoutManager(mActivity)
            }
        }


    }

}
