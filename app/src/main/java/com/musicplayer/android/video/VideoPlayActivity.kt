package com.musicplayer.android.video

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.musicplayer.android.databinding.ActivityVideoPlayBinding

class VideoPlayActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoPlayBinding
    private var exoPlayer: ExoPlayer? =null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,com.musicplayer.android.R.layout.activity_video_play)
        window.addFlags(LayoutParams.FLAG_LAYOUT_NO_LIMITS)

       playVideo()
    }

    private fun playVideo() {
        exoPlayer=ExoPlayer.Builder(this).build()
        binding.playerView.player =exoPlayer
        val data=intent.getStringExtra("uri")
        val dataSourceFactory= DefaultDataSource.Factory(this)
        val mediaSource= ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(data)))
        exoPlayer!!.setMediaSource(mediaSource)
            exoPlayer!!.trackSelectionParameters
                .buildUpon()
                .setMaxVideoSizeSd()
                .setPreferredAudioLanguage("hu")
                .build()
        exoPlayer!!
            .seekToDefaultPosition()
        exoPlayer!!.prepare()
        exoPlayer!!.playWhenReady=true
        exoPlayer!!.play()

        //exoPlayer!!.stop()

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("uri", exoPlayer!!.contentPosition)
        outState.putBoolean("true", exoPlayer!!.playWhenReady)
    }

    /*override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            exoPlayer!!.seekTo(it.getLong("uri"))
            exoPlayer!!.playWhenReady = it.getBoolean("true")
        }
    }*/

    private fun resumePlaybackFromStateBundle(inState: Bundle?): Boolean {
        if (inState != null) {
            exoPlayer!!.playWhenReady = inState.getBoolean("true")
            exoPlayer!!.seekTo(inState.getLong("uri"))
            return true
        }
        return false
    }
    companion object{
        const val URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer!!.stop()
    }
}