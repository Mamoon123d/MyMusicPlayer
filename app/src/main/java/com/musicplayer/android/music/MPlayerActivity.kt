package com.musicplayer.android.music

import android.Manifest
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicBsAdapter
import com.musicplayer.android.adapter.MusicPlAdapter
import com.musicplayer.android.adapter.SpeedPlayAdapter
import com.musicplayer.android.adapter.TimeAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.*
import com.musicplayer.android.model.*
import com.musicplayer.android.room.data.MusicFavoriteData
import com.musicplayer.android.room.data.MusicItemPlData
import com.musicplayer.android.room.data.MusicPlayListData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.view.visualizer.CircleVisualizer
import java.io.File


@Suppress("DEPRECATION")
class MPlayerActivity : BaseActivity<MplayerActivityBinding>(), ServiceConnection,
    MediaPlayer.OnCompletionListener {

    private var mainViewModel: MainViewModel? = null

    companion object {
        lateinit var audioList: ArrayList<MainMusicData>
        public var isPlaying: Boolean = false
        public var songPosition: Int = -1

        // var mediaPlayer: MediaPlayer? = null
        var musicService: MusicService? = null
        lateinit var mpBinding: MplayerActivityBinding
        public var isRepeat = false
        var min15: Boolean = false
        lateinit var audioVisual: CircleVisualizer
        var isFavorite: Boolean = false

        //ringtone
        private const val PHONE_RINGTONE: Int = 1
        private const val ALARM_RINGTONE: Int = 2
        private const val NOTIFICATION_RINGTONE: Int = 3
        private var isForPhone: Boolean = false
        private var isForAlarm: Boolean = false
        private var isForNoti: Boolean = false

        private const val CODE_WRITE_SETTINGS_PERMISSION: Int = 124


    }

    override fun setLayoutId(): Int {
        return R.layout.mplayer_activity
    }

    override fun initM() {
        //for db
        initializeDb()
        setTb()
        initializeLayout()

        //for share file
        binding.shareImg.setOnClickListener {
            shareFile()
        }

        //for start service
        binding.playPauseBt.setOnClickListener {
            if (isPlaying)
                pauseMusic()
            else
                playMusic()
        }

        binding.nextBt.setOnClickListener {
            //for next music

            prevNextMusic(true)

        }
        binding.backBt.setOnClickListener {
            //for previous music
            prevNextMusic(false)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekB: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService!!.mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })

        binding.repeatBt.setOnClickListener {
            if (!isRepeat) {
                isRepeat = true
            } else {
                isRepeat = false
            }
        }

        /* binding.listenTimeBt.setOnClickListener {
             //set 15 minutes
             //set 30 minutes
             //after that stop music
             //if 15 min true

         }*/
        binding.favMusicBt.setOnClickListener {
            val data = audioList[songPosition]
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
            if (isFavorite) {
                mainViewModel!!.removeMusicFavorite(favData)
            } else {
                mainViewModel!!.addMusicFavorite(favData)
            }
        }
        binding.addPl.setOnClickListener {
            addPlaylist()
        }

        binding.listenTimeBt.setOnClickListener {
            val bs = BottomSheetDialog(mActivity)
            val binding_sheet = TimeSheetBinding.inflate(LayoutInflater.from(mActivity))
            bs.setContentView(binding_sheet.root)
            val list = mutableListOf<String>(
                "Stop after this track",
                "15 minutes later",
                "30 minutes later",
                "45 minutes later",
                "60 minutes later",
                "Custom"
            )
            binding_sheet.rv.apply {
                adapter = TimeAdapter(mActivity, list).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            showMsg(list[position])

                            when (position) {
                                0 -> {
                                    //stop after track
                                    /*Thread {
                                        Thread.sleep(15 * 60000)
                                        if (min15) exitApplication()
                                    }.start()*/
                                }
                                1 -> {
                                    min15 = true
                                    Thread {
                                        Thread.sleep(15 * 60000)
                                        if (min15) exitApplication()
                                    }.start()
                                }

                            }
                            bs.dismiss()
                        }

                    })
                }
                layoutManager = LinearLayoutManager(mActivity)
            }
            binding_sheet.back.setOnClickListener {
                bs.dismiss()
            }
            bs.show()

        }

        binding.echoBt.setOnClickListener {
            /*  val bs = BottomSheetDialog(mActivity)
              val binding_sheet = EchoLayoutBinding.inflate(LayoutInflater.from(mActivity))
              bs.setContentView(binding_sheet.root)*/

            // val i =  Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
            // i.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
            // i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, musicService!!.mediaPlayer!!.audioSessionId)
            //startActivity(i)


        }

        binding.moreOptionBt.setOnClickListener {
            val bs = BottomSheetDialog(mActivity)
            val binding_sheet = MoreOptionLayBinding.inflate(LayoutInflater.from(mActivity))
            bs.setContentView(binding_sheet.root)
            binding_sheet.ringCon.setOnClickListener {
                showRingtoneOpt()

                bs.dismiss()
            }

            binding_sheet.infoCon.setOnClickListener {
                info()
            }

            binding_sheet.speedCon.setOnClickListener {
                showSpeed()
            }

            bs.show()

        }
        binding.songListBt.setOnClickListener {
            openSongList()
        }
        addMusicService()
    }

    private fun shareFile() {


        val uri = Uri.parse(audioList[songPosition].path)
        val share = Intent(Intent.ACTION_SEND)
        share.type = "audio/*"
        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, "Share Sound File"))
    }

    private fun openSongList() {
        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = SonglistBsBinding.inflate(LayoutInflater.from(mActivity))
        bs.setContentView(binding_sheet.root)
        binding_sheet.title.text = "${audioList.size} Songs"
        binding_sheet.rv.apply {
            adapter = MusicBsAdapter(mActivity, audioList).apply {
                adapter
                setOnItemCloseListener(object : MusicBsAdapter.OnItemCloseListener {
                    override fun onItemClose(position: Int) {
                        audioList.removeAt(position)
                        adapter!!.notifyItemRemoved(position)
                    }

                })
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        songPosition = position
                        setLayout()
                        createMediaPlayer()
                        adapter!!.notifyDataSetChanged()

                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
        bs.show()


    }

    private fun showSpeed() {
        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = SpeedSheetBinding.inflate(LayoutInflater.from(mActivity))
        bs.setContentView(binding_sheet.root)
        binding_sheet.rv.apply {
            adapter = SpeedPlayAdapter(
                mActivity,
                listOf("2.0x", "1.5x", "1.0x", "0.75x", "0.5x", "0.25x")
            ).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        showMsg(list[position])
                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)

        }

        bs.show()


    }

    private fun checkSystemWritePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(mActivity))
                return true
            else openAndroidPermissionsMenu()
        }
        return false
    }

    private fun openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val i = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
            i.data = Uri.parse("package:" + packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(i, CODE_WRITE_SETTINGS_PERMISSION)
        } else {
            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(Manifest.permission.WRITE_SETTINGS),
                CODE_WRITE_SETTINGS_PERMISSION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(mActivity)) {
            if (isForPhone)
                setRingtone(PHONE_RINGTONE)
            else if (isForAlarm)
                setRingtone(ALARM_RINGTONE)
            else if (isForNoti)
                setRingtone(NOTIFICATION_RINGTONE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isForPhone)
                setRingtone(PHONE_RINGTONE)
            else if (isForAlarm)
                setRingtone(ALARM_RINGTONE)
            else if (isForNoti)
                setRingtone(NOTIFICATION_RINGTONE)
        }
    }

    private fun showRingtoneOpt() {
        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = RingtoneLayBinding.inflate(LayoutInflater.from(mActivity))
        bs.setContentView(binding_sheet.root)
        binding_sheet.phoneCon.setOnClickListener {

            try {
                if (checkSystemWritePermission()) {
                    resetRingParam()
                    isForPhone = true
                    setRingtone(PHONE_RINGTONE)
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
                    isForAlarm = true
                    setRingtone(ALARM_RINGTONE)
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
                    isForNoti = true
                    setRingtone(NOTIFICATION_RINGTONE)
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

    private fun resetRingParam() {
        isForPhone = false
        isForAlarm = false
        isForNoti = false
    }

    private fun setRingtone(type: Int) {
        val data = audioList[songPosition]
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
        values.put(MediaStore.Audio.Media.IS_RINGTONE, type == PHONE_RINGTONE)
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, type == NOTIFICATION_RINGTONE)
        values.put(MediaStore.Audio.Media.IS_ALARM, type == ALARM_RINGTONE)
        //values.put(MediaStore.Audio.Media.IS_MUSIC, false)

        mActivity.contentResolver.update(
            uri!!,
            values,
            MediaStore.MediaColumns.DATA + "=?",
            arrayOf<String>(k.absolutePath)
        )
        val newuri = ContentUris.withAppendedId(uri, data.id.trim().toLong())
        try {
            RingtoneManager.setActualDefaultRingtoneUri(
                mActivity,
                RingtoneManager.TYPE_RINGTONE,
                newuri
            )
            when (type) {
                PHONE_RINGTONE -> showMsg("successfully set as Phone ringtone")
                ALARM_RINGTONE -> showMsg("successfully set as Alarm ringtone")
                NOTIFICATION_RINGTONE -> showMsg("successfully set as Notification ringtone")
            }

        } catch (t: Throwable) {
            t.printStackTrace()
        }
        //  cursor.close()
        // }

    }

    private fun addPlaylist() {
        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = PlaylistDialogBinding.inflate(LayoutInflater.from(mActivity))
        bs.setContentView(binding_sheet.root)
        mainViewModel!!.getMusicPl().observe(this@MPlayerActivity) { pl ->
            binding_sheet.plRv.apply {
                adapter = MusicPlAdapter(mActivity, pl).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            showMsg("Add music in playlist")
                            val data = audioList[songPosition]
                            mainViewModel!!.addMusicInPl(
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
                layoutManager = LinearLayoutManager(mActivity)
            }

        }

        binding_sheet.addListCon.setOnClickListener {
            bs.dismiss()
            insertPL()
        }

        bs.show()
    }

    private fun info() {
        val data = audioList[songPosition]
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
        binding_sheet.formatTv.text = "audio/$extension"
        binding_sheet.pathTv.text = data.path
        binding_sheet.durationTv.text = DateUtils.formatElapsedTime(data.duration / 1000)
        binding_sheet.dateTv.text = vFile.lastModified().toString()

        binding_sheet.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun insertPL() {
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
                val plData = MusicPlayListData(title = plName)
                mainViewModel!!.addMusicPl(plData)
                showMsg("Playlist has been created")
                var plId: Long = 1
                mainViewModel!!.getMusicPlId()!!.observe(this) {
                    if (it != null) {
                        plId = it!!
                    }
                }
                Handler(Looper.myLooper()!!).postDelayed({
                    val data = audioList[songPosition]
                    mainViewModel!!.addMusicInPl(
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

    private fun initializeDb() {
        val dao = Db.getDatabase(mActivity).myDao()
        val repository = Repository(dao)
        mainViewModel =
            ViewModelProvider(this, MainMvFactory(repository))[MainViewModel::class.java]
    }


    private fun addMusicService() {
        val i = Intent(mActivity, MusicService::class.java)
        bindService(i, this, BIND_AUTO_CREATE)
        startService(i)
    }

    private fun setTb() {
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initializeLayout() {
        mpBinding = binding
        audioList = ArrayList()
        audioList = MainActivity.audioList
        songPosition = intent.getIntExtra("pos", -1)

        //from player list
        setLayout()
        createMediaPlayer()

    }


    private fun setLayout() {
        val data = audioList[songPosition]
        /*val imageArt = if (data.artUri != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt!!.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.bg_circle)
        }*/


        //  val softwareBitmap: Bitmap = imageArt.copy(Bitmap.Config.ARGB_8888, true)
        binding.mainImg.apply {

            if (data.artUri != null) {
                Glide.with(mActivity).load(data.artUri)
                    //.transform(BlurTransformation(mActivity))
                    .override(2, 2)
                    .into(this)
                val imgArt = getImageArt(data.path)

                if (imgArt != null) {
                    val bm = BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
                    val softwareBitmap: Bitmap = bm.copy(Bitmap.Config.ARGB_8888, false)

                    Palette.from(softwareBitmap).generate() { palette ->
                        val mutedDark = palette!!.darkMutedSwatch!!.rgb
                        binding.mainLay.setBackgroundColor(mutedDark)
                        // showMsg(mutedDark.toString())
                    }
                }
            }

            /*  if (softwareBitmap != null) {
                  Glide.with(mActivity).load(softwareBitmap)
                      //.transform(BlurTransformation(mActivity))
                      .override(2, 2)
                      .into(this)
                  Palette.from(softwareBitmap).generate(){ palette ->
                      val mutedDark = palette!!.darkMutedSwatch!!.rgb
                      binding.mainLay.setBackgroundColor(mutedDark)
                     // showMsg(mutedDark.toString())

                  }
              }*/
        }



        binding.tvSongName.apply {
            text = data.title
            ellipsize = TextUtils.TruncateAt.MARQUEE
            setSingleLine()
            isSelected = true
        }

        binding.tvArtistAndAlbum.text = data.artist
        binding.albumCover.apply {
            // setImageBitmap(bitmap)
            Glide.with(mActivity).asBitmap().load(data.artUri).into(binding.albumCover)
        }


        //set audio visual
        audioVisual = binding.visualizer
        audioVisual.isDrawLine = true

        if (mainViewModel != null) {
            initializeDb()
        }


        binding.favMusicBt.apply {
            mainViewModel!!.isMusicFavoriteExists(data.id.trim()).observe(this@MPlayerActivity) {
                if (it > 0) {
                    setImageResource(R.drawable.ic_star)
                    isFavorite = true
                } else {
                    setImageResource(R.drawable.ic_favorite)
                    isFavorite = false
                }
            }

        }

    }

    private fun createMediaPlayer() {


        //media player
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()

            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(audioList[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
            musicService!!.showNotification(R.drawable.ic_pause_circle)
            binding.remainTime.text =
                formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            binding.totalTime.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            binding.seekBar.progress = 0
            binding.seekBar.max = musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)

        } catch (e: Exception) {
            return
        }

    }


    fun getAlbumArtUri(albumId: Long): Uri? {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }

    private fun playMusic() {
        binding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
        musicService!!.showNotification(R.drawable.ic_pause_circle)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()

        val audioSessionId: Int = musicService!!.mediaPlayer!!.audioSessionId
        try {
            if (audioSessionId != -1) {
                audioVisual.setAudioSessionId(audioSessionId)
            }
        } catch (e: Exception) {
            audioVisual = binding.visualizer
        }

    }

    private fun pauseMusic() {
        binding.playPauseBt.setImageResource(R.drawable.ic_play_circle)
        musicService!!.showNotification(R.drawable.ic_play_circle)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()

        /*if (audioVisual != null)
            audioVisual.release();*/
    }

    private fun prevNextMusic(increment: Boolean) {
        if (increment) {
            setMusicPosition(increment = true)
            setLayout()
            createMediaPlayer()

        } else {
            setMusicPosition(increment = false)
            setLayout()
            createMediaPlayer()

        }
    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        musicService!!.showNotification(R.drawable.ic_pause_circle)
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(p0: MediaPlayer?) {
        setMusicPosition(increment = true)
        createMediaPlayer()
        try {
            setLayout()
            if (audioVisual != null)
                audioVisual.hide();
        } catch (e: Exception) {
            return
        }
    }

}
