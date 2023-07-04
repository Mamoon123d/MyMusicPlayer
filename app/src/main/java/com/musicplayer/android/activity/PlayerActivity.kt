package com.musicplayer.android.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.media.AudioManager
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.TimeBar
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.AskNextVideoBinding
import com.musicplayer.android.databinding.PlayerActivityBinding
import com.musicplayer.android.fragment.History
import com.musicplayer.android.model.VideoMainData
import com.musicplayer.android.room.data.VideoHistoryData
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.visible
import com.musicplayer.android.utils.MyIntent
import com.musicplayer.android.video.VideoDashFrag
import com.musicplayer.android.video.VideoListActivity
import com.musicplayer.android.video.folder.FolderActivity
import java.io.File
import java.util.*
import kotlin.math.abs

class PlayerActivity : BaseActivity<PlayerActivityBinding>(isFullScreen = true),
    AudioManager.OnAudioFocusChangeListener,
    GestureDetector.OnGestureListener {
    override fun setLayoutId(): Int {
        return R.layout.player_activity
    }

    private lateinit var mainMv: MainViewModel

    //------------------------------------------------
    private lateinit var playPauseBtn: ImageButton
    private lateinit var fullScreenBtn: ImageButton
    private lateinit var videoTitle: TextView
    private lateinit var gestureDetectorCompat: GestureDetectorCompat
    private var minSwipeY: Float = 0f

    companion object {
        private var audioManager: AudioManager? = null
        private var timer: Timer? = null
        private lateinit var player: ExoPlayer
        lateinit var playerList: ArrayList<VideoMainData>
        var position: Int = -1
        private var repeat: Boolean = false
        private var isFullscreen: Boolean = false
        private var isLocked: Boolean = false
        private lateinit var trackSelector: DefaultTrackSelector
        private lateinit var loudnessEnhancer: LoudnessEnhancer
        private var speed: Float = 1.0f
        var pipStatus: Int = 0
        var nowPlayingId: String = ""
        private var brightness: Int = 0
        private var volume: Int = 0
        private var isSpeedChecked: Boolean = false
    }

    override fun initM() {
        initDb()
        setMyPlayer()
        //setAd()
    }

    private fun setAd() {

    }

    private fun initDb() {
        val dao = Db.getDatabase(mActivity).myDao()
        val repo = Repository(dao)
        mainMv = ViewModelProvider(this, MainMvFactory(repo))[MainViewModel::class.java]
    }

    private fun createPlayer() {
        try {
            player.release()
        } catch (e: Exception) {
        }
        if (!isSpeedChecked) speed = 1.0f
        trackSelector = DefaultTrackSelector(this)
        videoTitle.text = playerList[position].title
        videoTitle.isSelected = true
        player = ExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        doubleTapEnable()
        val mediaItem = MediaItem.fromUri(playerList[position].artUri)
        player.setMediaItem(mediaItem)

        player.setPlaybackSpeed(speed)
        player.prepare()
        playVideo()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) {
                    //complete video here
                    //show dialog for asking to play next video
                    // or auto play after 500ms
                    askNextVideo()
                }
            }
        })
        playInFullscreen(enable = isFullscreen)
        loudnessEnhancer = LoudnessEnhancer(player.audioSessionId)
        loudnessEnhancer.enabled = true
        nowPlayingId = playerList[position].id
        seekBarFeature()
        mainMv.isHistoryExists(playerList[position].id.trim()).observe(this) {
            val vData = playerList[position]
            val data = VideoHistoryData(
                id = vData.id.trim().toLong(),
                videoId = vData.id.trim().toLong(),
                path = vData.path,
                duration = vData.duration,
                recentTime = Date().time.toString(),
                isFavorite = vData.isFavourite
            )

            if (it > 1) {
                mainMv.removeVideoInHistory(data)
            }
            val isExist = it > 0

            if (isExist) {
                //exist
                mainMv.updateVideoInHistory(data)
            } else {
                mainMv.addVideoInHistory(data)
                // not exist
            }
        }
    }

    private fun askNextVideo() {
        if (playerList.size > 1) {
            val dialog = Dialog(mActivity)
            val binding_sheet = AskNextVideoBinding.inflate(LayoutInflater.from(mActivity))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding_sheet.root)

            dialog.window!!.apply {
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(android.R.color.transparent)
            }
            dialog.setCancelable(true)
            if (playerList.size > position + 1) {
                val data = playerList[position + 1]
                if (data != null) {
                    try {
                        Glide.with(mActivity).load(data.artUri).into(binding_sheet.nvThumb)
                        binding_sheet.nvTitle.text = data.title
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                val progress = binding_sheet.progress
                progress.progress = 0
                var i = 1
                val cd = object : CountDownTimer(5000, 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        i++
                        progress.progress = i * 100 / (5000 / 100)
                    }

                    override fun onFinish() {
                        progress.progress = 100
                        dialog.dismiss()
                        nextPrevVideo()
                    }

                }
                cd.start()
            }
            binding_sheet.nextBtn.setOnClickListener {
                //next button
                dialog.dismiss()
                nextPrevVideo()
            }
            binding_sheet.replayBtn.setOnClickListener {
                //replay video
                dialog.dismiss()
                replayVideo()
            }
            if (!(mActivity as Activity).isFinishing) {
                //show dialog
                dialog.show()

            }
        }

    }

    private fun createPlayerWithMute() {
        try {
            player.release()
        } catch (e: Exception) {

        }

        if (!isSpeedChecked) speed = 1.0f
        trackSelector = DefaultTrackSelector(this)
        videoTitle.text = playerList[position].title
        videoTitle.isSelected = true
        player = ExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        doubleTapEnable()
        val mediaItem = MediaItem.fromUri(playerList[position].artUri)
        player.setMediaItem(mediaItem)

        player.setPlaybackSpeed(speed)
        player.prepare()
        player.volume = 0f
        playVideo()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) nextPrevVideo()
            }
        })
        playInFullscreen(enable = isFullscreen)
        loudnessEnhancer = LoudnessEnhancer(player.audioSessionId)
        loudnessEnhancer.enabled = true
        nowPlayingId = playerList[position].id
        seekBarFeature()
    }

    // SuppressLint("ClickableViewAccessibility")
    @SuppressLint("ClickableViewAccessibility")
    private fun doubleTapEnable() {
        binding.playerView.player = player
        /* binding.ytOverlay.performListener(object: YouTubeOverlay.PerformListener{
             override fun onAnimationEnd() {
                 binding.ytOverlay.visibility = View.GONE
             }

             override fun onAnimationStart() {
                 binding.ytOverlay.visibility = View.VISIBLE
             }
         })*/
        //  binding.ytOverlay.player(player)
        binding.playerView.setOnTouchListener { _, motionEvent ->
            binding.playerView.isDoubleTapEnabled = false
            if (!isLocked) {
                binding.playerView.isDoubleTapEnabled = true
                gestureDetectorCompat.onTouchEvent(motionEvent)
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    binding.brightnessIcon.gone()
                    binding.volumeIcon.gone()
                    //for immersive mode
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    WindowInsetsControllerCompat(window, binding.root).let { controller ->
                        controller.hide(WindowInsetsCompat.Type.systemBars())
                        controller.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun setMyPlayer() {
        videoTitle = findViewById(R.id.videoTitle)
        playPauseBtn = findViewById(R.id.playPauseBtn)
        fullScreenBtn = findViewById(R.id.fullScreenBtn)
        gestureDetectorCompat = GestureDetectorCompat(mActivity, this)
        try {
            //for handling video file intent (Improved Version)
            if (intent.data?.scheme.contentEquals("content")) {
                playerList = ArrayList()
                position = 0
                val cursor = contentResolver.query(
                    intent.data!!, arrayOf(MediaStore.Video.Media.DATA), null, null,
                    null
                )
                cursor?.let {
                    it.moveToFirst()
                    try {
                        val path =
                            it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                        val file = File(path)
                        val video = VideoMainData(
                            id = "",
                            title = file.name,
                            duration = 0L,
                            artUri = Uri.fromFile(file),
                            path = path,
                            size = "",
                            folderName = "",
                            pixels = ""
                        )
                        playerList.add(video)
                        cursor.close()
                    } catch (e: Exception) {
                        val tempPath = getPathFromURI(context = this, uri = intent.data!!)
                        val tempFile = File(tempPath)
                        val video = VideoMainData(
                            id = "",
                            title = tempFile.name,
                            duration = 0L,
                            artUri = Uri.fromFile(tempFile),
                            path = tempPath,
                            size = "",
                            folderName = "",
                            pixels = ""
                        )
                        playerList.add(video)
                        cursor.close()
                    }
                }
                createPlayer()
                initializeBinding()
            } else {
                initializeLayout()
                initializeBinding()
            }

        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    private fun initializeLayout() {
        when (intent.getStringExtra(MyIntent.CLASS)) {
            "VideoFragment" -> {
                playerList = ArrayList()
                playerList.addAll(MainActivity.videoList)
                createPlayer()
            }
            "MutePlay" -> {
                playerList = ArrayList()
                playerList.addAll(MainActivity.videoList)
                createPlayerWithMute()
            }
            "VideoListActivity" -> {
                playerList = ArrayList()
                playerList.addAll(VideoListActivity.videoList)
                createPlayer()

            }
            "VideoHistory" -> {
                playerList = ArrayList()
                playerList.addAll(VideoDashFrag.videoList)
                createPlayer()

            }
            "FolderVideos" -> {
                playerList = ArrayList()
                playerList.addAll(FolderActivity.videoList)
                createPlayer()

            }
            "History" -> {
                playerList = ArrayList()
                playerList.addAll(History.vList)
                createPlayer()

            }

            /*  "FolderActivity" -> {
                  playerList = ArrayList()
                  playerList.addAll(FoldersActivity.currentFolderVideos)
                  createPlayer()

              }
              "SearchedVideos" ->{
                  playerList = ArrayList()
                  playerList.addAll(MainActivity.searchList)
                  createPlayer()
              }
              "NowPlaying" ->{
                  speed = 1.0f
                  videoTitle.text = playerList[position].title
                  videoTitle.isSelected = true
                  doubleTapEnable()
                  playVideo()
                  playInFullscreen(enable = isFullscreen)
                  seekBarFeature()
              }*/
        }
        // if(repeat) findViewById<ImageButton>(R.id.repeatBtn).setImageResource(R.drawable.exo_controls_repeat_all)
        // else findViewById<ImageButton>(R.id.repeatBtn).setImageResource(R.drawable.exo_controls_repeat_off)
    }


    private fun initializeBinding() {
        findViewById<ImageButton>(R.id.orientationBtn).setOnClickListener {
            requestedOrientation =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                else
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
        findViewById<ImageButton>(R.id.backBtn).setOnClickListener {
            finish()
        }

        playPauseBtn.setOnClickListener {
            if (player.isPlaying) pauseVideo()
            else playVideo()
        }
        findViewById<ImageButton>(R.id.nextBtn).setOnClickListener { nextPrevVideo() }
        findViewById<ImageButton>(R.id.prevBtn).setOnClickListener { nextPrevVideo(isNext = false) }
        findViewById<ImageButton>(R.id.repeatBtn).setOnClickListener {
            if (repeat) {
                repeat = false
                player.repeatMode = Player.REPEAT_MODE_OFF
                // findViewById<ImageButton>(R.id.repeatBtn).setImageResource(R.drawable.exo_controls_repeat_off)
            } else {
                repeat = true
                player.repeatMode = Player.REPEAT_MODE_ONE
                //findViewById<ImageButton>(R.id.repeatBtn).setImageResource(R.drawable.exo_controls_repeat_all)
            }
        }

        fullScreenBtn.setOnClickListener {
            if (isFullscreen) {
                isFullscreen = false
                playInFullscreen(enable = false)
            } else {
                isFullscreen = true
                playInFullscreen(enable = true)
            }
        }

        binding.lockButton.setOnClickListener {
            if (!isLocked) {
                //for hiding
                isLocked = true
                binding.playerView.hideController()
                binding.playerView.useController = false
                binding.lockButton.setImageResource(R.drawable.close_lock_icon)
            } else {
                //for showing
                isLocked = false
                binding.playerView.useController = true
                binding.playerView.showController()
                binding.lockButton.setImageResource(R.drawable.lock_open_icon)
            }
        }

        //for auto hiding & showing lock button
        binding.playerView.setControllerVisibilityListener {
            when {
                isLocked -> binding.lockButton.visibility = View.VISIBLE
                binding.playerView.isControllerVisible -> binding.lockButton.visibility =
                    View.VISIBLE
                else -> binding.lockButton.visibility = View.INVISIBLE
            }
        }
        /*findViewById<ImageButton>(R.id.moreFeaturesBtn).setOnClickListener {
            pauseVideo()
            val customDialog = LayoutInflater.from(this).inflate(R.layout.more_features, binding.root, false)
            val bindingMF = MoreFeaturesBinding.bind(customDialog)
            val dialog = MaterialAlertDialogBuilder(this).setView(customDialog)
                .setOnCancelListener { playVideo() }
                .setBackground(ColorDrawable(0x803700B3.toInt()))
                .create()
            dialog.show()

            bindingMF.audioTrack.setOnClickListener {
                dialog.dismiss()
                playVideo()
                val audioTrack = ArrayList<String>()
                val audioList = ArrayList<String>()
                for(group in player.currentTracksInfo.trackGroupInfos){
                    if(group.trackType == C.TRACK_TYPE_AUDIO){
                        val groupInfo = group.trackGroup
                        for (i in 0 until groupInfo.length){
                            audioTrack.add(groupInfo.getFormat(i).language.toString())
                            audioList.add("${audioList.size + 1}. " + Locale(groupInfo.getFormat(i).language.toString()).displayLanguage
                                    + " (${groupInfo.getFormat(i).label})")
                        }
                    }
                }

                if(audioList[0].contains("null")) audioList[0] = "1. Default Track"

                val tempTracks = audioList.toArray(arrayOfNulls<CharSequence>(audioList.size))
                val audioDialog = MaterialAlertDialogBuilder(this, R.style.alertDialog)
                    .setTitle("Select Language")
                    .setOnCancelListener { playVideo() }
                    .setPositiveButton("Off Audio"){ self, _ ->
                        trackSelector.setParameters(trackSelector.buildUponParameters().setRendererDisabled(
                            C.TRACK_TYPE_AUDIO, true
                        ))
                        self.dismiss()
                    }
                    .setItems(tempTracks){_, position ->
                        Snackbar.make(binding.root, audioList[position] + " Selected", 3000).show()
                        trackSelector.setParameters(trackSelector.buildUponParameters()
                            .setRendererDisabled(C.TRACK_TYPE_AUDIO, false)
                            .setPreferredAudioLanguage(audioTrack[position]))
                    }
                    .create()
                audioDialog.show()
                audioDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
                audioDialog.window?.setBackgroundDrawable(ColorDrawable(0x99000000.toInt()))
            }*/


    }

    private fun playVideo() {
        playPauseBtn.setImageResource(R.drawable.pause_icon)
        player.play()
    }

    private fun pauseVideo() {
        playPauseBtn.setImageResource(R.drawable.play_icon)
        player.pause()
    }

    private fun replayVideo() {
        createPlayer()
    }

    private fun nextPrevVideo(isNext: Boolean = true) {
        if (isNext) setPosition()
        else setPosition(isIncrement = false)
        createPlayer()
    }

    private fun setPosition(isIncrement: Boolean = true) {
        if (!repeat) {
            if (isIncrement) {
                if (playerList.size - 1 == position)
                    position = 0
                else ++position
            } else {
                if (position == 0)
                    position = playerList.size - 1
                else --position
            }
        }
    }

    private fun changeSpeed(isIncrement: Boolean) {
        if (isIncrement) {
            if (speed <= 2.9f) {
                speed += 0.10f //speed = speed + 0.10f
            }
        } else {
            if (speed > 0.20f) {
                speed -= 0.10f
            }
        }

        player.setPlaybackSpeed(speed)
    }

    private fun changeSpeedManual(speed_: Float) {
        speed = speed_
        player.setPlaybackSpeed(speed)
    }

    private fun playInFullscreen(enable: Boolean) {
        if (enable) {
            binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            fullScreenBtn.setImageResource(R.drawable.fullscreen_exit_icon)
        } else {
            binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            fullScreenBtn.setImageResource(R.drawable.fullscreen_icon)
        }
    }

    private fun setScreenBrightness(value: Int) {
        val d = 1.0f / 30
        val lp = this.window.attributes
        lp.screenBrightness = d * value
        this.window.attributes = lp
    }

    private fun seekBarFeature() {
        findViewById<DefaultTimeBar>(R.id.exo_progress).addListener(object :
            TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                pauseVideo()
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                player.seekTo(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                playVideo()
            }

        })

    }


    //used to get path of video selected by user (if column data fails to get path)
    private fun getPathFromURI(context: Context, uri: Uri): String {
        var filePath = ""
        // ExternalStorageProvider
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(':')
        val type = split[0]

        return if ("primary".equals(type, ignoreCase = true)) {
            "${Environment.getExternalStorageDirectory()}/${split[1]}"
        } else {
            //getExternalMediaDirs() added in API 21
            val external = context.externalMediaDirs
            if (external.size > 1) {
                filePath = external[1].absolutePath
                filePath = filePath.substring(0, filePath.indexOf("Android")) + split[1]
            }
            filePath
        }
    }


    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) pauseVideo()
    }

    override fun onDown(e: MotionEvent): Boolean {
        minSwipeY = 0f
        return false
    }

    override fun onShowPress(e: MotionEvent) = Unit

    override fun onSingleTapUp(e: MotionEvent): Boolean = false
    override fun onLongPress(e: MotionEvent) = Unit
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean = false

    override fun onScroll(
        event: MotionEvent,
        event1: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        minSwipeY += distanceY

        val sWidth = Resources.getSystem().displayMetrics.widthPixels
        val sHeight = Resources.getSystem().displayMetrics.heightPixels

        val border = 100 * Resources.getSystem().displayMetrics.density.toInt()
        if (event!!.x < border || event.y < border || event.x > sWidth - border || event.y > sHeight - border)
            return false

        //minSwipeY for slowly increasing brightness & volume on swipe --> try changing 50 (<50 --> quick swipe & > 50 --> slow swipe
        Log.d("onVideoScroll", "onScroll: " + sHeight)
        // & test with your custom values
        if (abs(distanceX) < abs(distanceY) && abs(minSwipeY) > 50) {
            if (event.x < sWidth / 2) {
                //brightness
                binding.brightnessIcon.visible()
                binding.volumeIcon.gone()
                val increase = distanceY > 0
                val newValue = if (increase) brightness + 1 else brightness - 1
                if (newValue in 0..30) brightness = newValue
                binding.brightnessIcon.text = brightness.toString()
                setScreenBrightness(brightness)
            } else {
                //volume
                binding.brightnessIcon.gone()
                binding.volumeIcon.visible()
                val maxVolume = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                val increase = distanceY > 0
                val newValue = if (increase) volume + 1 else volume - 1
                if (newValue in 0..maxVolume) volume = newValue
                binding.volumeIcon.text = volume.toString()
                audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
            }
            minSwipeY = 0f
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        player.pause()
        audioManager?.abandonAudioFocus(this)
    }

    override fun onResume() {
        super.onResume()
        if (audioManager == null) audioManager =
            getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager!!.requestAudioFocus(
            this,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        if (brightness != 0) setScreenBrightness(brightness)
    }

}