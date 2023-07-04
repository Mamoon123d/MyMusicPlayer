package com.musicplayer.android.utils

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.PlAdapter
import com.musicplayer.android.adapter.SheetAdapter
import com.musicplayer.android.adapter.VideoAdapter
import com.musicplayer.android.base.BaseRvAdapter
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
import java.io.File

class MoreVideoBS(
    private val mContext: Context,
    private val data: VideoMainData,
    private val vPos: Int,
    private val vidAdapter: VideoAdapter

) {
    lateinit var mainVm: MainViewModel

    init {
        val dao = Db.getDatabase(mContext).myDao()
        val repository = Repository(dao)
        mainVm = ViewModelProvider(
            mContext as ViewModelStoreOwner,
            MainMvFactory(repository)
        )[MainViewModel::class.java]
        val bs = BottomSheetDialog(mContext)
        val binding_sheet = OptionSheetBinding.inflate(LayoutInflater.from(mContext))
        bs.setContentView(binding_sheet.root)

        val rv = binding_sheet.rvSheet
        val list = mutableListOf<OptionSheetData>().apply {

            add(OptionSheetData(1, "Favorite", R.drawable.ic_favorite))
            add(OptionSheetData(2, "Add to playlist", R.drawable.ic_playlist))
            add(OptionSheetData(3, "Rename", R.drawable.ic_rename))
            add(OptionSheetData(4, "Delete", R.drawable.ic_delete))
            add(OptionSheetData(5, "File Info", R.drawable.ic_info))
            add(OptionSheetData(6, "Mute Play", R.drawable.ic_mute))
            add(OptionSheetData(7, "Share", R.drawable.ic_share))
        }
        rv.apply {
            adapter =
                SheetAdapter(mContext, list, mContext as LifecycleOwner, videoId = data.id).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            when (list[position].id) {
                                1 -> if (!data.isFavourite!!) addFavorite(
                                    data,
                                    vPos
                                ) else removeFavorite(data, vPos)
                                2 -> addInPlaylist(data)
                                3 -> renameFile(data, vPos)
                                4 -> deleteFile(data, vPos)
                                5 -> info(data)
                                6 -> mutePlay(vPos)
                                7 -> shareFile(data.path)
                            }
                            bs.dismiss()
                        }

                    })
                }
            layoutManager = LinearLayoutManager(mContext)
        }

        bs.show()
    }

    fun addFavorite(data: VideoMainData, vPos: Int) {
        // mainViewModel.removeFavorite(FavoriteData(videoId = videoId.trim().toLong()))
        val videoId = data.id.trim().toLong()
        mainVm.addFavorite(
            FavoriteData(
                id = videoId,
                title = data.title,
                videoId = videoId,
                folderName = data.folderName,
                duration = data.duration,
                size = data.size,
                path = data.path,
                pixels = data.pixels,
                isFavorite = data.isFavourite
            )
        )
        vidAdapter!!.notifyItemChanged(vPos)

    }

    private fun removeFavorite(data: VideoMainData, vPos: Int) {
        val videoId = data.id.trim().toLong()
        mainVm.removeFavorite(
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

    fun addInPlaylist(data: VideoMainData) {

        val bs = BottomSheetDialog(mContext)
        val binding_sheet = PlaylistDialogBinding.inflate(LayoutInflater.from(mContext))
        bs.setContentView(binding_sheet.root)


        mainVm.getPlayList().observe(mContext as LifecycleOwner) {
            binding_sheet.plRv.apply {
                adapter = PlAdapter(mContext, it).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            // showMsg("Add video in playlist")
                            Toast.makeText(mContext, "Add video in playlist", Toast.LENGTH_SHORT)
                                .show()
                            mainVm.insertVideoPl(
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
                layoutManager = LinearLayoutManager(mContext)
            }
        }
        binding_sheet.addListCon.setOnClickListener {
            bs.dismiss()
            insertPL(data)
        }

        bs.show()


    }

    private fun insertPL(data: VideoMainData) {
        val dialog = Dialog(mContext)
        val binding_sheet = AddPlDialogBinding.inflate(LayoutInflater.from(mContext))
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
                mainVm.insertPlItem(plData)
                Toast.makeText(mContext, "Playlist has been created", Toast.LENGTH_SHORT).show()
                // showMsg("Playlist has been created")
                var plId: Long = 1
                mainVm.getPlId()!!.observe(mContext as LifecycleOwner) {
                    if (it != null) {
                        plId = it!!
                    }
                }
                Handler(Looper.myLooper()!!).postDelayed({
                    mainVm.insertVideoPl(
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
        val dialog = Dialog(mContext)
        val binding_sheet = RenameDialogBinding.inflate(LayoutInflater.from(mContext))
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

    private fun deleteFile(data: VideoMainData, vPos: Int) {
        val dialog = Dialog(mContext)
        val binding_sheet = DeleteDialogBinding.inflate(LayoutInflater.from(mContext))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)

        dialog.window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialog.setCancelable(false)

        binding_sheet.posBtn.setOnClickListener {
            //delete file
            val vFile = File(data.path)
            if (vFile.exists()) {
                val success = vFile.delete()
                if (success) {
                    dialog.dismiss()
                    vidAdapter!!.notifyItemRemoved(vPos)
                }
            } else {
                // showMsg("file not exist")
                Toast.makeText(mContext, "file not exist", Toast.LENGTH_SHORT).show()
            }


        }
        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun info(data: VideoMainData) {
        val dialog = Dialog(mContext)
        val binding_sheet = InfoDialogBinding.inflate(LayoutInflater.from(mContext))
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
        MyIntent.goIntent(mContext, vPos, "MutePlay")

    }

    private fun shareFile(path: String) {
        val vFile = File(path)
        if (vFile.exists()) {
            SendTo(mContext).sendVideoFile(path)
        }

    }
}