package com.musicplayer.android.music

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.AllSMAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.MusicPlaylistViewBinding
import com.musicplayer.android.model.MainMusicData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.DataSet
import com.musicplayer.android.utils.MoreMusicBS
import com.musicplayer.android.utils.MyIntent

class MusicPlaylistView : BaseActivity<MusicPlaylistViewBinding>() {
    private var ref: Int? = null
    private lateinit var mainViewModel: MainViewModel

    companion object {
        lateinit var audioList: ArrayList<MainMusicData>
    }

    override fun setLayoutId(): Int {
        return R.layout.music_playlist_view
    }


    override fun initM() {
        initDb()
        initializeLayout()
        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

    }

    private fun initDb() {
        val dao = Db.getDatabase(mActivity).myDao()
        val repo = Repository(dao = dao)
        mainViewModel = ViewModelProvider(this, MainMvFactory(repo))[MainViewModel::class.java]

    }

    private fun initializeLayout() {
        ref = intent.getIntExtra(DataSet.REF, -1)
        when (ref) {
            DataSet.Reference.MUSIC_ALL_PL -> {
                audioList = ArrayList()
                audioList = MainActivity.audioList
                setTb("All")
                binding.listImg.setImageResource(R.drawable.all_img)
                binding.bgApp.setBackgroundResource(R.drawable.all_banner)
                setSongsList()
            }
            DataSet.Reference.MUSIC_FAVORITE -> {
                audioList = ArrayList()
                mainViewModel.getMusicFavorites().observe(this) { list ->
                    list.forEach { data ->
                        audioList.add(
                            MainMusicData(
                                id = data.music_id.toString(),
                                title = data.title,
                                duration = data.duration.trim().toLong(),
                                folderName = data.folderName,
                                size = data.size,
                                path = data.path,
                                artUri = data.artUri,
                                pixels = data.pixels,
                                album = data.album,
                                artist = data.artist,
                                albumId = data.albumId.toString(),
                                artistId = data.artistId.toString(),
                                isFavourite = data.isFavourite
                            )
                        )
                    }

                    setSongsList()
                }
                setTb(mActivity.getString(R.string.favorite))
                binding.listImg.setImageResource(R.drawable.fav_img)
                binding.bgApp.setBackgroundResource(R.drawable.fav_banner)

                // audioList = MainActivity.audioList

            }
            DataSet.Reference.MUSIC_RECENT -> {
                setTb(mActivity.getString(R.string.recently_played))
                binding.listImg.setImageResource(R.drawable.new_img)
                binding.bgApp.setBackgroundResource(R.drawable.recent_banner)
                audioList = ArrayList()
                mainViewModel.recentMusics().observe(this) { list ->
                    list.forEach { data ->
                        MainActivity.audioList.filter { data.music_id == it.id.trim().toLong() }
                            .forEach { mData ->
                                audioList.add(mData)
                            }
                    }

                    setSongsList()

                }


            }
            DataSet.Reference.MUSIC_PLAYLIST -> {
                val title = intent.getStringExtra(DataSet.PLAYLIST_NAME)
                val plId = intent.getLongExtra(DataSet.PLAYLIST_ID, 0)
                binding.listImg.setImageResource(R.drawable.music_ph)
                binding.bgApp.setBackgroundResource(R.drawable.banner_playlist)
                setTb(title!!)
                audioList = ArrayList()
                mainViewModel.getMusicsInPl(plId = plId.toInt()).observe(this) { list ->
                    list.forEach { data ->
                        MainActivity.audioList.filter { data.music_id == it.id.trim().toLong() }
                            .forEach { mData ->
                                audioList.add(mData)
                            }
                    }

                    setSongsList()

                }

            }
        }
    }

    private fun setTb(title: String) {
        val titleTv = binding.title
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (Math.abs(verticalOffset) - appBarLayout!!.totalScrollRange == 0) {
                    //  Collapsed
                    titleTv.text = title
                } else {
                    //Expanded
                    titleTv.text = "Playlist"
                }
            }

        })
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setSongsList() {
        binding.rv.apply {
            adapter = AllSMAdapter(mActivity, audioList).apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        MyIntent.goMusicIntent(mActivity, position, ref!!)
                    }

                })

                setOnMoreOptionClickListenerX(object : AllSMAdapter.OnMoreOptionClickListener {
                    override fun onMoreOptionClick(data: MainMusicData, position: Int) {
                        // this@MusicPlaylistView.showMsg("more option")
                        MoreMusicBS(
                            context = mActivity,
                            musicData = data,
                            musicAdapter = adapter,
                            music_position = position,
                            ref!!
                        )
                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

}
