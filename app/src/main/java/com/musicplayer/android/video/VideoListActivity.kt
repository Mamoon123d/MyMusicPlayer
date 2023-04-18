package com.musicplayer.android.video

import android.app.Dialog
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.musicplayer.android.R
import com.musicplayer.android.adapter.PlAdapter
import com.musicplayer.android.adapter.SheetAdapter
import com.musicplayer.android.adapter.VideoAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.*
import com.musicplayer.android.model.OptionSheetData
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.room.data.FavoriteData
import com.musicplayer.android.room.data.PlayListData
import com.musicplayer.android.room.data.VideoItemPlData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.DataSet
import com.musicplayer.android.utils.MyIntent
import com.musicplayer.android.utils.SendTo
import java.io.File

class VideoListActivity : BaseActivity<VideoListHomeBinding>() {
    private lateinit var mainViewModel: MainViewModel
    private var vidAdapter: VideoAdapter? = null
    var forPlaylist: Boolean = false
    override fun setLayoutId(): Int {
        return R.layout.video_list_home
    }

    companion object {
        // var list:MutableList<VideoMainData>?= null
        lateinit var videoList: ArrayList<VideoMainData>

    }

    override fun initM() {
        setContent()
    }

    private fun setContent() {
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        initDb()
        val ref = intent.getIntExtra(DataSet.REF, -1)
        if (ref == DataSet.Reference.FAVORITE) {
            forPlaylist = false
            //for favorite videos
            binding.title.text = "My Favorite Videos"
            setFavList()
        } else if (ref == DataSet.Reference.PLAYLIST) {
            //for playlist videos
            forPlaylist = true
            val title = intent.getStringExtra(DataSet.TITLE)
            val plId = intent.getStringExtra(DataSet.PLAYLIST_ID)
            binding.title.text = title
            setPlaylist(plId)
        }
    }

    private fun setPlaylist(plId: String?) {
        mainViewModel.getPlItemList(plId = plId!!.trim().toInt()).observe(this) { playlist ->
            videoList = ArrayList()
            for (data in playlist) {
                val file = File(data.path)
                if (file.exists()) {
                    val artUriC = Uri.fromFile(file)
                    videoList.add(
                        VideoMainData(
                            id = data.videoId.toString(),
                            title = data.title!!,
                            duration = data.duration,
                            folderName = data.folderName,
                            size = data.size,
                            path = data.path,
                            artUri = artUriC,
                            pixels = data.pixels,
                            isFavourite = data.isFavorite,
                            plVideoId = data.id

                        )
                    )
                } else {
                    mainViewModel.deleteVideoPl(data)
                }
            }
            setRecycler(videoList!!)

        }
    }

    private fun initDb() {
        val dao = Db.getDatabase(mActivity).myDao()
        val repository = Repository(dao)
        mainViewModel =
            ViewModelProvider(this, MainMvFactory(repository))[MainViewModel::class.java]

    }

    private fun setFavList() {
        mainViewModel.getFavorites().observe(this) { favList ->
            videoList = ArrayList()

            for (data in favList) {
                val file = File(data.path)
                if (file.exists()) {
                    val artUriC = Uri.fromFile(file)
                    videoList.add(
                        VideoMainData(
                            id = data.videoId.toString(),
                            title = data.title!!,
                            duration = data.duration,
                            folderName = data.folderName,
                            size = data.size,
                            path = data.path,
                            artUri = artUriC,
                            pixels = data.pixels,
                            isFavourite = data.isFavorite
                        )
                    )
                } else {
                    mainViewModel.removeFavorite(data)
                }
            }

            setRecycler(videoList)
        }
    }

    private fun setRecycler(list: MutableList<VideoMainData>) {
        val rv = binding.rv
        rv.apply {
            vidAdapter = VideoAdapter(mActivity, list, lifecycleOwner = this@VideoListActivity)
            adapter = vidAdapter?.apply {
                setOnMoreOptionClickListenerX(object : VideoAdapter.OnMoreOptionClickListener {
                    override fun onMoreOptionClick(data: VideoMainData, position: Int) {
                        showMoreOptions(data, position)

                    }

                })
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        MyIntent.goIntent(mActivity, position, "VideoListActivity")

                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }

    }

    private fun showMoreOptions(data: VideoMainData, vPos: Int) {
        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = OptionSheetBinding.inflate(LayoutInflater.from(mActivity))
        val rv = binding_sheet.rvSheet
        val list = mutableListOf<OptionSheetData>().apply {
            add(OptionSheetData(-1, "Favorite", R.drawable.ic_favorite))
            add(OptionSheetData(-2, "Add to playlist", R.drawable.ic_playlist))
            add(OptionSheetData(1, "Rename", R.drawable.ic_rename))
            add(OptionSheetData(2, "Remove", R.drawable.ic_delete))
            add(OptionSheetData(3, "File Info", R.drawable.ic_info))
            add(OptionSheetData(4, "Mute Play", R.drawable.ic_mute))
            add(OptionSheetData(5, "Share", R.drawable.ic_share))

        }

        rv.apply {
            adapter =
                SheetAdapter(mActivity, list, this@VideoListActivity, videoId = data.id).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            when (list[position].id) {
                                -1 -> if (!data.isFavourite!!) addFavorite(
                                    data,
                                    vPos
                                ) else removeFavorite(data, vPos)
                                -2 -> addInPlaylist(data)
                                1 -> renameFile(data, vPos)
                                2 -> removeFile(data, vPos)
                                3 -> info(data)
                                4 -> mutePlay(vPos)
                                5 -> shareFile(data.path)
                            }
                            bs.dismiss()
                        }

                    })
                }
            layoutManager = LinearLayoutManager(mActivity)
        }
        bs.setContentView(binding_sheet.root)
        bs.show()
    }


    private fun addFavorite(data: VideoMainData, vPos: Int) {
        // mainViewModel.removeFavorite(FavoriteData(videoId = videoId.trim().toLong()))
        val videoId = data.id.trim().toLong()
        mainViewModel.addFavorite(
            FavoriteData(
                id = videoId,
                title = data.title,
                videoId = videoId,
                folderName = data.folderName,
                duration = data.duration,
                size = data.size,
                path = data.path,
                pixels = data.pixels
            )
        )
        vidAdapter!!.notifyItemChanged(vPos)

    }

    private fun removeFavorite(data: VideoMainData, vPos: Int) {
        val videoId = data.id.trim().toLong()
        mainViewModel.removeFavorite(
            FavoriteData(
                id = videoId,
                title = data.title,
                videoId = videoId,
                folderName = data.folderName,
                duration = data.duration,
                size = data.size,
                path = data.path,
                pixels = data.pixels
            )
        )
        if (!forPlaylist) {
            //for favorite
            vidAdapter!!.removeItem(vPos)
            vidAdapter!!.notifyItemRemoved(vPos)
        } else {
            //for playlist
            vidAdapter!!.notifyItemChanged(vPos)
        }
    }

    private fun addInPlaylist(data: VideoMainData) {

        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = PlaylistDialogBinding.inflate(LayoutInflater.from(mActivity))
        bs.setContentView(binding_sheet.root)


        mainViewModel.getPlayList().observe(this) {
            binding_sheet.plRv.apply {
                adapter = PlAdapter(mActivity, it).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            showMsg("Add video in playlist")
                            mainViewModel.insertVideoPl(
                                VideoItemPlData(
                                    title = data.title,
                                    plId = it[position].id,
                                    videoId = data.id,
                                    folderName = data.folderName,
                                    duration = data.duration,
                                    size = data.size,
                                    path = data.path,
                                    //   artUri = data.artUri,
                                    pixels = data.pixels,

                                    )
                            )
                            bs.dismiss()
                        }

                    })
                }
                layoutManager = LinearLayoutManager(mActivity)
            }
        }
        binding_sheet.addListCon.setOnClickListener {
            bs.dismiss()
            insertPL(data)
        }
        bs.show()

    }

    private fun insertPL(data: VideoMainData) {
        val dialog = Dialog(mActivity)
        val binding_sheet = AddPlDialogBinding.inflate(LayoutInflater.from(mActivity))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)

        dialog.window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)

        }
        dialog.setCancelable(false)
        val et = binding_sheet.addPlEt
        binding_sheet.posBtn.setOnClickListener {
            val plName = et.text.toString().trim()
            if (plName.isBlank()) {
                et.error = "name is empty"
            } else {
                val plData = PlayListData(title = plName)
                mainViewModel.insertPlItem(plData)
                showMsg("Playlist has been created")
                var plId: Long = 1
                mainViewModel.getPlId()!!.observe(this) {
                    if (it != null) {
                        plId = it!!
                    }
                }
                Handler(Looper.myLooper()!!).postDelayed({
                    mainViewModel.insertVideoPl(
                        VideoItemPlData(
                            title = data.title,
                            plId = plId,
                            videoId = data.id,
                            folderName = data.folderName,
                            duration = data.duration,
                            size = data.size,
                            path = data.path,
                            //   artUri = data.artUri,
                            pixels = data.pixels,
                        )
                    )
                }, 200)
                dialog.dismiss()
            }
            //done

        }
        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun renameFile(data: VideoMainData, vPos: Int) {
        val dialog = Dialog(mActivity)
        val binding_sheet = RenameDialogBinding.inflate(LayoutInflater.from(mActivity))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)

        dialog.window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)

        }
        dialog.setCancelable(false)
        val renameEt = binding_sheet.renameEt
        renameEt.apply {
            setText(data.title.substring(0, data.title.lastIndexOf(".")))
        }
        binding_sheet.posBtn.setOnClickListener {
            val text = renameEt.text.toString().trim()
            if (text.isBlank()) {
                renameEt.error = "name is empty"
            } else {
                val vFile = File(data.path)
                if (vFile.exists()) {
                    val extension = data.path.substring(data.path.lastIndexOf("."))
                    val pathOnly = data.path.substringBeforeLast(File.separator)

                    val completePath = buildString {
                        append(pathOnly)
                        append(File.separator)
                        append(text)
                        append(extension)
                    }
                    Log.d("more_option", "renameFile: " + completePath)
                    val nFile = File(completePath)
                    val success = vFile.renameTo(nFile)
                    if (success) {
                        data.title = "$text$extension"
                        val file = File(completePath)
                        val artUriC = Uri.fromFile(file)
                        data.path = completePath
                        data.artUri = artUriC
                        // MainActivity.videoList[vPos].path=completePath
                        dialog.dismiss()

                        Handler(Looper.myLooper()!!).postDelayed({
                            vidAdapter!!.notifyItemChanged(vPos)
                        }, 100)

                    }

                }
            }
        }
        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun removeFile(data: VideoMainData, vPos: Int) {
        val dialog = Dialog(mActivity)
        val binding_sheet = RemoveDialogBinding.inflate(LayoutInflater.from(mActivity))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)
        val cb = binding_sheet.deleteCh
        dialog.window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialog.setCancelable(false)

        binding_sheet.posBtn.setOnClickListener {

            if (forPlaylist) {
                //playlist
                val plId = intent.getStringExtra(DataSet.PLAYLIST_ID)

                mainViewModel.deleteVideoPl(
                    VideoItemPlData(
                        id=data.plVideoId,
                        title = data.title,
                        plId = plId!!.trim().toLong(),
                        videoId = data.id,
                        folderName = data.folderName,
                        duration = data.duration,
                        size = data.size,
                        path = data.path,
                        //   artUri = data.artUri,
                        pixels = data.pixels,
                        isFavorite = data.isFavourite
                        )
                )
            } else {
                //favorite
                mainViewModel.removeFavorite(
                    FavoriteData(
                        id = data.id.trim().toLong(),
                        title = data.title,
                        videoId = data.id.trim().toLong(),
                        folderName = data.folderName,
                        duration = data.duration,
                        size = data.size,
                        path = data.path,
                        pixels = data.pixels
                    )
                )
            }
            if (cb.isChecked) {
                //delete file
                val vFile = File(data.path)
                if (vFile.exists()) {
                    val success = vFile.delete()
                    if (success) {
                        dialog.dismiss()
                        vidAdapter!!.notifyItemRemoved(vPos)
                    }
                } else {
                    showMsg("file not exist")
                }
            } else {
                vidAdapter?.let {
                    it.removeItem(vPos)
                    it.notifyItemRemoved(vPos)
                }
                dialog.dismiss()
            }


        }
        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun info(data: VideoMainData) {
        val dialog = Dialog(mActivity)
        val binding_sheet = InfoDialogBinding.inflate(LayoutInflater.from(mActivity))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)
        dialog.setCancelable(false)
        dialog.window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        val extension = data.path.substring(data.path.lastIndexOf("."))
        val vFile = File(data.path)

        binding_sheet.titleTv.text = data.title
        binding_sheet.resoTv.text = data.pixels
        binding_sheet.sizeTv.text = data.size
        binding_sheet.formatTv.text = "video/$extension"
        binding_sheet.pathTv.text = data.path
        binding_sheet.durationTv.text = DateUtils.formatElapsedTime(data.duration / 1000)
        binding_sheet.dateTv.text = vFile.lastModified().toString()

        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun mutePlay(vPos: Int) {
        MyIntent.goIntent(mActivity, vPos, "MutePlay")
    }

    private fun shareFile(path: String) {
        val vFile = File(path)
        if (vFile.exists()) {
            SendTo(mActivity).sendVideoFile(path)
        }

    }
}