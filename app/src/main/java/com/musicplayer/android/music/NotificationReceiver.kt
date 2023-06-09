package com.musicplayer.android.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.musicplayer.android.MyApplication
import com.musicplayer.android.R
import com.musicplayer.android.model.exitApplication
import com.musicplayer.android.model.setMusicPosition

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, i: Intent?) {
        when (i?.action) {
            MyApplication.PLAY -> if (MPlayerActivity.isPlaying) pauseMusic() else playMusic()
            MyApplication.PREVIOUS -> nextPrevMusic(increment = false, context!!)
            MyApplication.NEXT -> nextPrevMusic(increment = true, context!!)
            MyApplication.EXIT -> {
               exitApplication()
            }
        }
    }

    private fun playMusic() {
        MPlayerActivity.isPlaying = true
        MPlayerActivity.musicService!!.mediaPlayer!!.start()
        MPlayerActivity.musicService!!.showNotification(R.drawable.ic_pause_circle)
        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
        NowPlayingFrag.nowPlayBinding.playPauseBt.setImageResource(R.drawable.pause_icon)
    }

    private fun pauseMusic() {
        MPlayerActivity.isPlaying = false
        MPlayerActivity.musicService!!.mediaPlayer!!.pause()
        MPlayerActivity.musicService!!.showNotification(R.drawable.ic_play_circle)
        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_play_circle)
        NowPlayingFrag.nowPlayBinding.playPauseBt.setImageResource(R.drawable.play_icon)

    }

    private fun nextPrevMusic(increment: Boolean, context: Context) {
        setMusicPosition(increment = increment)
        MPlayerActivity.musicService!!.createMediaPlayer()
        MPlayerActivity.mpBinding.albumCover.apply {
            // setImageBitmap(bitmap)
            Glide.with(context).asBitmap()
                .load(MPlayerActivity.audioList[MPlayerActivity.songPosition].artUri).into(this)
        }
        MPlayerActivity.mpBinding.tvSongName.text =
            MPlayerActivity.audioList[MPlayerActivity.songPosition].title
        playMusic()

    }
}