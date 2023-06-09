package com.musicplayer.android.music

import com.bumptech.glide.Glide
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.databinding.NowPlayingBinding
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.invisible
import com.musicplayer.android.utils.Extension.Companion.visible

class NowPlayingFrag : BaseFragment<NowPlayingBinding>() {
    companion object {
        lateinit var nowPlayBinding: NowPlayingBinding
    }

    override fun setLayoutId(): Int {
        return R.layout.now_playing
    }

    override fun initM() {
        nowPlayBinding = binding
        binding.root.invisible()
        //binding.progressBar.progress = 0
        //binding.progressBar.max = MPlayerActivity.musicService!!.mediaPlayer!!.duration

        binding.playPauseBt.setOnClickListener {
            if (MPlayerActivity.isPlaying) pauseMusic() else playMusic()
        }
        binding.closeBt.setOnClickListener {

            if (MPlayerActivity.musicService != null) {
                MPlayerActivity.musicService!!.stopForeground(true)
                MPlayerActivity.musicService!!.mediaPlayer!!.release()
                MPlayerActivity.musicService = null
                binding.root.gone()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (MPlayerActivity.musicService != null) {
            binding.root.visible()
            binding.albumArt.apply {
                // setImageBitmap(bitmap)
                Glide.with(context).asBitmap()
                    .load(MPlayerActivity.audioList[MPlayerActivity.songPosition].artUri).into(this)
            }
            binding.tvSongName.text = MPlayerActivity.audioList[MPlayerActivity.songPosition].title
            binding.tvArtistAndAlbum.text =
                MPlayerActivity.audioList[MPlayerActivity.songPosition].artist
            if (MPlayerActivity.isPlaying) {
                binding.playPauseBt.setImageResource(R.drawable.pause_icon)
            } else {
                binding.playPauseBt.setImageResource(R.drawable.play_icon)
            }

        }
    }

    private fun playMusic() {
        MPlayerActivity.musicService!!.mediaPlayer!!.start()
        binding.playPauseBt.setImageResource(R.drawable.pause_icon)
        MPlayerActivity.musicService!!.showNotification(R.drawable.ic_pause_circle)
        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
        MPlayerActivity.isPlaying = true
    }

    private fun pauseMusic() {
        MPlayerActivity.musicService!!.mediaPlayer!!.pause()
        binding.playPauseBt.setImageResource(R.drawable.play_icon)
        MPlayerActivity.musicService!!.showNotification(R.drawable.ic_play_circle)
        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_play_circle)
        MPlayerActivity.isPlaying = false
    }
}