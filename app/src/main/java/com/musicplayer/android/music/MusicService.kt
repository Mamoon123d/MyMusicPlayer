package com.musicplayer.android.music

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import com.musicplayer.android.MyApplication
import com.musicplayer.android.R
import com.musicplayer.android.model.formatDuration
import com.musicplayer.android.model.getImageArt
import com.musicplayer.android.utils.Extension.Companion.visible
import java.util.*


class MusicService : Service(), AudioManager.OnAudioFocusChangeListener {
    private var myBinder = MyBinder()
    public var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable
    lateinit var audioManager: AudioManager

    companion object {
        const val PLAY_IMAGE: Int = 1
        const val PAUSE_IMAGE: Int = 2
    }

    override fun onBind(p0: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {

            return this@MusicService
        }
    }

    /*private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = NotificationChannel(
                MyApplication.CHANNEL_ID,
                "Channel_1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channelId.description = "Channel 1 Music"
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channelId)
        }
    }*/

    fun showNotification(playPauseBt: Int) {
        var isAndroidS = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            isAndroidS = true
        }
        val prevIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(MyApplication.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            prevIntent,
            if (isAndroidS) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(MyApplication.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            playIntent,
            if (isAndroidS) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val nextIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(MyApplication.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            nextIntent,
            if (isAndroidS) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val exitIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(MyApplication.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            exitIntent,
            if (isAndroidS) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val imgArt = getImageArt(MPlayerActivity.audioList[MPlayerActivity.songPosition].path)

        val image = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.bg_circle)
        }

        val notification = NotificationCompat.Builder(baseContext, MyApplication.CHANNEL_ID)
            .setContentTitle(MPlayerActivity.audioList[MPlayerActivity.songPosition].title)
            .setContentText(MPlayerActivity.audioList[MPlayerActivity.songPosition].artist)

            .setSmallIcon(R.drawable.ic_playlist)
            .setLargeIcon(image)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_icon, "Previous", prevPendingIntent)
            .addAction(
                if (playPauseBt == PLAY_IMAGE) R.drawable.play_icon else R.drawable.pause_icon,
                "Play",
                playPendingIntent
            )
            .addAction(R.drawable.next_icon, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_close_w, "Exit", exitPendingIntent)
            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val playBackSpeed = if (MPlayerActivity.isPlaying) 1f else 0f
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        mediaPlayer!!.duration.toLong()
                    )
                    .build()
            )
            val playBackState = PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mediaPlayer!!.currentPosition.toLong(),
                    playBackSpeed
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
            mediaSession.setPlaybackState(playBackState)
            mediaSession.setCallback(object : MediaSessionCompat.Callback() {
                //called when headphones buttons are pressed
                //currently only pause or play music on button click
                override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                    if (MPlayerActivity.isPlaying) {
                        //pause music
                        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.play_icon)
                        NowPlayingFrag.nowPlayBinding.playPauseBt.setImageResource(R.drawable.play_icon)
                        MPlayerActivity.isPlaying = false
                        mediaPlayer!!.pause()
                        showNotification(PLAY_IMAGE)
                    } else {
                        //play music
                        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.pause_icon)
                        NowPlayingFrag.nowPlayBinding.playPauseBt.setImageResource(R.drawable.pause_icon)
                        MPlayerActivity.isPlaying = true
                        mediaPlayer!!.start()
                        showNotification(PAUSE_IMAGE)
                    }
                    return super.onMediaButtonEvent(mediaButtonEvent)
                }

                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    mediaPlayer!!.seekTo(pos.toInt())
                    val playBackStateNew = PlaybackStateCompat.Builder()
                        .setState(
                            PlaybackStateCompat.STATE_PLAYING,
                            mediaPlayer!!.currentPosition.toLong(),
                            playBackSpeed
                        )
                        .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                        .build()
                    mediaSession.setPlaybackState(playBackStateNew)
                }
            })
        }

        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        startForeground(13, notification)


    }
    fun createMediaPlayer(){
        try {
            if (MPlayerActivity.musicService!!.mediaPlayer == null) MPlayerActivity.musicService!!.mediaPlayer =
                MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(MPlayerActivity.audioList[MPlayerActivity.songPosition].path)
            mediaPlayer!!.prepare()
            MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
            MPlayerActivity.musicService!!.showNotification(PAUSE_IMAGE)

            MPlayerActivity.mpBinding.remainTime.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            MPlayerActivity.mpBinding.totalTime.text = formatDuration(mediaPlayer!!.duration.toLong())
            MPlayerActivity.mpBinding.seekBar.progress = 0
            MPlayerActivity.mpBinding.seekBar.max = mediaPlayer!!.duration

            //  NowPlayingFrag.nowPlayBinding.progressBar.progress=0
            // NowPlayingFrag.nowPlayBinding.progressBar.max = MPlayerActivity.musicService!!.mediaPlayer!!.duration
           
        } catch (e: Exception) {
            return
        }

    }

    fun seekBarSetup() {
        runnable = Runnable {

            if (MPlayerActivity.musicService != null) {
                MPlayerActivity.mpBinding.remainTime.text =
                    formatDuration(MPlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
                MPlayerActivity.mpBinding.seekBar.progress = mediaPlayer!!.currentPosition
                //   NowPlayingFrag.nowPlayBinding.progressBar.progress=mediaPlayer!!.currentPosition
                Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    fun setTimer(timer: Int) {
        val times = arrayOf<Long>(15000 * 60, 30000 * 60, 45000 * 60, 60000 * 60)
        var timeInt:Long = 0
        when (timer) {
            1 -> {
                //15 minutes
                timeInt = times[0]
            }
            2->{
                //30 minutes
                timeInt = times[1]
            }
        }
      //  val handler=Handler(Looper.getMainLooper())

       /* val runnable= Runnable {
          handler.postDelayed(runnable,timeInt)
        }
        handler.postDelayed(runnable,1000)*/
        MPlayerActivity.mpBinding.tvTime.visible()
        var i=timeInt
        Timer().schedule(object:TimerTask(){
            override fun run() {
                MPlayerActivity.mpBinding.tvTime.text=" $i"
                i--
            }

        },1000,timeInt)
    }


    //for making persistent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) {
            //pause music
            MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.play_icon)
            NowPlayingFrag.nowPlayBinding.playPauseBt.setImageResource(R.drawable.play_icon)
            MPlayerActivity.isPlaying = false
            mediaPlayer!!.pause()
            showNotification(PLAY_IMAGE)

        }
        /* else{
             //play music
             MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.pause_icon)
             NowPlayingFrag.nowPlayBinding.playPauseBt.setImageResource(R.drawable.pause_icon)
             MPlayerActivity.isPlaying = true
             mediaPlayer!!.start()
             showNotification(PAUSE_IMAGE)
         }*/
    }
}