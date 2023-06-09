package com.musicplayer.android.music

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.musicplayer.android.MyApplication
import com.musicplayer.android.R
import com.musicplayer.android.model.formatDuration
import com.musicplayer.android.model.getImageArt
import java.util.*


class MusicService :Service() {
    private var myBinder=MyBinder()
    public var mediaPlayer:MediaPlayer?=null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable
    override fun onBind(p0: Intent?): IBinder? {
        mediaSession= MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }
    inner class MyBinder:Binder(){
        fun currentService():MusicService{

            return this@MusicService
        }
    }

   private fun createNotificationChannel(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val channelId = NotificationChannel(MyApplication.CHANNEL_ID, "Channel_1", NotificationManager.IMPORTANCE_HIGH)
           channelId.description = "Channel 1 Music"
            val notificationManager = getSystemService(
               NotificationManager::class.java
           )
           notificationManager.createNotificationChannel(channelId)
       }
   }

    fun showNotification(playPauseBt:Int){
        var isAndroidS=false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            isAndroidS=true
        }
            val prevIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(MyApplication.PREVIOUS)
        val prevPendingIntent=PendingIntent.getBroadcast(baseContext,0,prevIntent,if (isAndroidS)PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(MyApplication.PLAY)
        val playPendingIntent=PendingIntent.getBroadcast(baseContext,0,playIntent,if (isAndroidS)PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(MyApplication.NEXT)
        val nextPendingIntent=PendingIntent.getBroadcast(baseContext,0,nextIntent,if (isAndroidS)PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(MyApplication.EXIT)
        val exitPendingIntent=PendingIntent.getBroadcast(baseContext,0,exitIntent,if (isAndroidS)PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)

        val imgArt= getImageArt(MPlayerActivity.audioList[MPlayerActivity.songPosition].path)
      val image=  if (imgArt!=null){
            BitmapFactory.decodeByteArray(imgArt,0,imgArt.size)
        }else{
           BitmapFactory.decodeResource(resources,R.drawable.bg_circle)
        }
        val notification=NotificationCompat.Builder(baseContext,MyApplication.CHANNEL_ID)
            .setContentTitle(MPlayerActivity.audioList[MPlayerActivity.songPosition].title)
            .setContentText(MPlayerActivity.audioList[MPlayerActivity.songPosition].artist)

            .setSmallIcon(R.drawable.ic_playlist)
            .setLargeIcon(image)

            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_icon,"Previous",prevPendingIntent)
            .addAction(R.drawable.next_icon,"Next",nextPendingIntent)
            .addAction(playPauseBt,"Play",playPendingIntent)
            .addAction(R.drawable.back_icon,"Exit",exitPendingIntent)

            .build()

        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        startForeground(m,notification)
    }
    fun createMediaPlayer(){
        try {
            if (MPlayerActivity.musicService!!.mediaPlayer == null) MPlayerActivity.musicService!!.mediaPlayer =
                MediaPlayer()

            MPlayerActivity.musicService!!.mediaPlayer!!.reset()
            MPlayerActivity.musicService!!.mediaPlayer!!.setDataSource(MPlayerActivity.audioList[MPlayerActivity.songPosition].path)
            MPlayerActivity.musicService!!.mediaPlayer!!.prepare()
            MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
            MPlayerActivity.musicService!!.showNotification(R.drawable.ic_pause_circle)

            MPlayerActivity.mpBinding.remainTime.text =
                formatDuration(MPlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
            MPlayerActivity.mpBinding.totalTime.text =
                formatDuration(MPlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
            MPlayerActivity.mpBinding.seekBar.progress = 0
            MPlayerActivity.mpBinding.seekBar.max = MPlayerActivity.musicService!!.mediaPlayer!!.duration

          //  NowPlayingFrag.nowPlayBinding.progressBar.progress=0
           // NowPlayingFrag.nowPlayBinding.progressBar.max = MPlayerActivity.musicService!!.mediaPlayer!!.duration

        } catch (e: Exception) {
            return
        }
    }


    fun seekBarSetup() {
            runnable = Runnable {

                if (MPlayerActivity.musicService!=null) {
                    MPlayerActivity.mpBinding.remainTime.text =
                        formatDuration(MPlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
                    MPlayerActivity.mpBinding.seekBar.progress = mediaPlayer!!.currentPosition
                 //   NowPlayingFrag.nowPlayBinding.progressBar.progress=mediaPlayer!!.currentPosition
                    Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
                }

            }
            Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
        }
}