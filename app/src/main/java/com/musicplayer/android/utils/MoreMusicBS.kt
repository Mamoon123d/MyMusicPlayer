package com.musicplayer.android.utils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicMoreBsAdapter
import com.musicplayer.android.adapter.MusicPlAdapter
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.*
import com.musicplayer.android.model.MainMusicData
import com.musicplayer.android.model.OptionSheetData
import com.musicplayer.android.model.formatDate
import com.musicplayer.android.music.MPlayerActivity
import com.musicplayer.android.room.data.MusicFavoriteData
import com.musicplayer.android.room.data.MusicItemPlData
import com.musicplayer.android.room.data.MusicPlayListData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.visible
import java.io.File

class MoreMusicBS(
    private val context: Context,
    val musicData: MainMusicData,
    val musicAdapter: RecyclerView.Adapter<ViewHolder>?,
    music_position: Int, ref: Int
) {
    lateinit var mainVm: MainViewModel

    init {
        fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
            return operation(a, b)
        }

      //  val minus = calculate(1, 2) { a, b -> a-b }
       // val plus = calculate(1,2){a,b-> a+b}
        val dao = Db.getDatabase(context).myDao()
        val repository = Repository(dao)
        mainVm = ViewModelProvider(
            context as ViewModelStoreOwner,
            MainMvFactory(repository)
        )[MainViewModel::class.java]
        val bs = BottomSheetDialog(context)
        val binding = OptionSheetBinding.inflate(LayoutInflater.from(context))
        bs.setContentView(binding.root)
        val rv = binding.rvSheet
        val list = mutableListOf<OptionSheetData>().apply {

            add(OptionSheetData(1, "Play Now", R.drawable.player_play))
            add(OptionSheetData(2, "Set as ringtone", R.drawable.ic_ring))
            add(OptionSheetData(3, "Add to playlist", R.drawable.ic_playlist))
            add(OptionSheetData(4, "Favorite", R.drawable.ic_favorite))
            add(OptionSheetData(5, "Rename", R.drawable.ic_rename))
            add(OptionSheetData(6, "Delete", R.drawable.ic_delete))
            add(OptionSheetData(7, "File Info", R.drawable.ic_info))
            add(OptionSheetData(8, "Share", R.drawable.ic_share))
        }
        rv.apply {
            adapter = MusicMoreBsAdapter(
                context,
                list,
                mainVm,
                context as LifecycleOwner,
                musicData.id
            ).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        when (list[position].id) {
                            1 -> MyIntent.goMusicIntent(context, music_position, ref)
                            2 -> showRingtoneOpt()
                            3 -> addPlaylist(musicData)
                            4 -> addFavorite(musicData)
                            5 -> renameFile(musicData, music_position)
                            6 -> deleteFile(musicData, music_position)
                            7 -> info(musicData)
                            8 -> shareFile(musicData)
                        }
                        bs.dismiss()
                    }

                })
            }
            layoutManager = LinearLayoutManager(context)
        }
        bs.show()
    }

    private fun addFavorite(musicData: MainMusicData) {
        val data = musicData
        val favData = MusicFavoriteData(
            id = data.id.trim().toLong(),
            music_id = data.id.trim().toLong(),
            title = data.title,
            duration = data.duration.toString(),
            folderName = data.folderName,
            size = data.size,
            path = data.path,
            artUri = data.artUri,
            pixels = data.pixels,
            album = data.album,
            artist = data.artist,
            albumId = data.albumId,
            isFavourite = data.isFavourite
        )
        if (MPlayerActivity.isFavorite) {
            mainVm.removeMusicFavorite(favData)
        } else {
            mainVm.addMusicFavorite(favData)
        }
    }

    private fun addPlaylist(musicData: MainMusicData) {
        val bs = BottomSheetDialog(context)
        val binding_sheet = PlaylistDialogBinding.inflate(LayoutInflater.from(context))
        bs.setContentView(binding_sheet.root)
        mainVm!!.getMusicPl().observe(context as LifecycleOwner) { pl ->
            binding_sheet.plRv.apply {
                adapter = MusicPlAdapter(context, pl).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            //showMsg("Add music in playlist")
                            Toast.makeText(context, "Add music in playlist", Toast.LENGTH_SHORT)
                                .show()
                            val data = musicData
                            mainVm!!.addMusicInPl(
                                MusicItemPlData(
                                    music_id = data.id.trim().toLong(),
                                    pl_id = pl[position].id,
                                    title = data.title,
                                    folderName = data.folderName,
                                    duration = data.duration.toString(),
                                    size = data.size,
                                    path = data.path,
                                    pixels = data.pixels,
                                    artUri = data.artUri,
                                    album = data.album,
                                    albumId = data.albumId,
                                    artist = data.artist,
                                    isFavourite = data.isFavourite
                                )
                            )

                            bs.dismiss()
                        }

                    })
                }
                layoutManager = LinearLayoutManager(context)
            }

        }

        binding_sheet.addListCon.setOnClickListener {
            bs.dismiss()
            insertPL()
        }

        bs.show()
    }

    private fun insertPL() {
        val dialog = Dialog(context)
        val binding_sheet = AddPlDialogBinding.inflate(LayoutInflater.from(context))
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
                val plData = MusicPlayListData(title = plName)
                mainVm.addMusicPl(plData)
                Toast.makeText(context, "Playlist has been created", Toast.LENGTH_SHORT).show()
                //showMsg("Playlist has been created")
                var plId: Long = 1
                mainVm.getMusicPlId()!!.observe(context as LifecycleOwner) {
                    if (it != null) {
                        plId = it!!
                    }
                }
                Handler(Looper.myLooper()!!).postDelayed({
                    val data = MPlayerActivity.audioList[MPlayerActivity.songPosition]
                    mainVm.addMusicInPl(
                        MusicItemPlData(
                            music_id = data.id.trim().toLong(),
                            pl_id = plId,
                            title = data.title,
                            folderName = data.folderName,
                            duration = data.duration.toString(),
                            size = data.size,
                            path = data.path,
                            pixels = data.pixels,
                            artUri = data.artUri,
                            album = data.album,
                            albumId = data.albumId,
                            artist = data.artist,
                            isFavourite = data.isFavourite
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

    private fun renameFile(data: MainMusicData, vPos: Int) {
        val dialog = Dialog(context)
        val binding_sheet = RenameDialogBinding.inflate(LayoutInflater.from(context))
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
                        // val artUriC = Uri.fromFile(file)
                        data.path = completePath
                        val uri = Uri.parse("content://media/external/audio/albumart")
                        val artUriC = Uri.withAppendedPath(uri, data.albumId).toString()

                        data.artUri = artUriC
                        // MainActivity.videoList[vPos].path=completePath
                        dialog.dismiss()

                        Handler(Looper.myLooper()!!).postDelayed({
                            musicAdapter!!.notifyItemChanged(vPos)
                            if (context is MainActivity) {
                                (context as MainActivity).updateAlbumList()
                            }
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

    private fun deleteFile(data: MainMusicData, vPos: Int) {
        val dialog = Dialog(context)
        val binding_sheet = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
                    musicAdapter!!.notifyItemRemoved(vPos)
                }
            } else {
                showMsg("file not exist")
            }


        }
        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun info(data: MainMusicData) {

        val dialog = Dialog(context)
        val binding_sheet = InfoDialogBinding.inflate(LayoutInflater.from(context))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding_sheet.root)
        dialog.setCancelable(false)
        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        val extension = data.path.substring(data.path.lastIndexOf("."))
        val vFile = File(data.path)

        binding_sheet.titleTv.text = data.title
        binding_sheet.resoCon.gone()
        binding_sheet.albumCon.visible()
        binding_sheet.artistCon.visible()
        binding_sheet.sizeTv.text = data.size
        binding_sheet.formatTv.text = "audio/$extension"
        binding_sheet.pathTv.text = data.path
        binding_sheet.artistTv.text = data.artist
        binding_sheet.albumTv.text = data.album

        binding_sheet.durationTv.text = DateUtils.formatElapsedTime(data.duration / 1000)
        binding_sheet.dateTv.text = formatDate(vFile.lastModified())

        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun shareFile(data: MainMusicData) {


        val uri = Uri.parse(data.path)
        val share = Intent(Intent.ACTION_SEND)
        share.type = "audio/*"
        share.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(share, "Share Sound File"))
    }

    private fun showRingtoneOpt() {
        val bs = BottomSheetDialog(context)
        val binding_sheet = RingtoneLayBinding.inflate(LayoutInflater.from(context))
        bs.setContentView(binding_sheet.root)
        binding_sheet.phoneCon.setOnClickListener {

            try {
                if (checkSystemWritePermission()) {
                    resetRingParam()
                    MPlayerActivity.isForPhone = true
                    setRingtone(MPlayerActivity.PHONE_RINGTONE)
                    //  showMsg("Set as ringtoon successfully ")
                } else {
                    showMsg("Allow modify system settings ==> ON ")

                }
            } catch (e: Exception) {
                Log.i("ringtone", e.toString())
                showMsg("unable to set as Ringtone ")
            }

        }
        binding_sheet.alarmCon.setOnClickListener {
            try {
                if (checkSystemWritePermission()) {
                    resetRingParam()
                    MPlayerActivity.isForAlarm = true
                    setRingtone(MPlayerActivity.ALARM_RINGTONE)
                    //  showMsg("Set as ringtoon successfully ")
                } else {
                    showMsg("Allow modify system settings ==> ON ")

                }
            } catch (e: Exception) {
                Log.i("ringtone", e.toString())
                showMsg("unable to set as Ringtone ")
            }

        }
        binding_sheet.notiCon.setOnClickListener {
            try {
                if (checkSystemWritePermission()) {
                    resetRingParam()
                    MPlayerActivity.isForNoti = true
                    setRingtone(MPlayerActivity.NOTIFICATION_RINGTONE)
                    //  showMsg("Set as ringtoon successfully ")
                } else {
                    showMsg("Allow modify system settings ==> ON ")

                }
            } catch (e: java.lang.Exception) {
                Log.i("ringtone", e.toString())
                showMsg("unable to set as Ringtone ")
            }
        }

        bs.show()
    }


    private fun checkSystemWritePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context))
                return true
            else openAndroidPermissionsMenu()
        }
        return false
    }

    private fun showMsg(message: String) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show()

    }

    private fun openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val i = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
            i.data = Uri.parse("package:" + context.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            (context as Activity).startActivityForResult(
                i,
                MPlayerActivity.CODE_WRITE_SETTINGS_PERMISSION
            )
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_SETTINGS),
                MPlayerActivity.CODE_WRITE_SETTINGS_PERMISSION
            )
        }
    }

    private fun resetRingParam() {
        MPlayerActivity.isForPhone = false
        MPlayerActivity.isForAlarm = false
        MPlayerActivity.isForNoti = false
    }

    private fun setRingtone(type: Int) {
        val data = MPlayerActivity.audioList[MPlayerActivity.songPosition]
        val k = File(data.path) // path is a file to /sdcard/media/ringtone
        Log.d("ringtone :", "path: " + data.path)

        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DATA, k.absolutePath)
        values.put(MediaStore.MediaColumns.TITLE, data.title)
        values.put(MediaStore.MediaColumns.SIZE, k.length())
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
        // values.put(MediaStore.Audio.Media.ARTIST, data.artist)
        //values.put(MediaStore.Audio.Media.DURATION, data.duration)
        values.put(MediaStore.Audio.Media.IS_MUSIC, false)


//Insert it into the database

//Insert it into the database
        val uri = MediaStore.Audio.Media.getContentUriForPath(k.absolutePath)
        //val newUri = this.contentResolver.insert(uri!!, values)

        //val cursor: Cursor? = mActivity.contentResolver.query(uri!!, null, MediaStore.MediaColumns.DATA + "=?", arrayOf<String>(data.path), null)
        //if (cursor != null && cursor.moveToFirst() && cursor.count > 0) {
        //  val id: String = cursor.getString(0)
        values.put(MediaStore.Audio.Media.IS_RINGTONE, type == MPlayerActivity.PHONE_RINGTONE)
        values.put(
            MediaStore.Audio.Media.IS_NOTIFICATION,
            type == MPlayerActivity.NOTIFICATION_RINGTONE
        )
        values.put(MediaStore.Audio.Media.IS_ALARM, type == MPlayerActivity.ALARM_RINGTONE)
        //values.put(MediaStore.Audio.Media.IS_MUSIC, false)

        context.contentResolver.update(
            uri!!,
            values,
            MediaStore.MediaColumns.DATA + "=?",
            arrayOf<String>(k.absolutePath)
        )
        val newuri = ContentUris.withAppendedId(uri, data.id.trim().toLong())
        try {
            RingtoneManager.setActualDefaultRingtoneUri(
                context,
                RingtoneManager.TYPE_RINGTONE,
                newuri
            )
            when (type) {
                MPlayerActivity.PHONE_RINGTONE -> showMsg("successfully set as Phone ringtone")
                MPlayerActivity.ALARM_RINGTONE -> showMsg("successfully set as Alarm ringtone")
                MPlayerActivity.NOTIFICATION_RINGTONE -> showMsg("successfully set as Notification ringtone")
            }

        } catch (t: Throwable) {
            t.printStackTrace()
        }
        //  cursor.close()
        // }

    }

}