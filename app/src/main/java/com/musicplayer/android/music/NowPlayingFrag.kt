package com.musicplayer.android.music

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicBsAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.NowPlayingBinding
import com.musicplayer.android.databinding.SonglistBsBinding
import com.musicplayer.android.model.formatDuration
import com.musicplayer.android.utils.DataSet
import com.musicplayer.android.utils.Extension.Companion.gone
import com.musicplayer.android.utils.Extension.Companion.invisible
import com.musicplayer.android.utils.Extension.Companion.visible
import com.musicplayer.android.utils.MyIntent

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
                pauseMusic()
                // MPlayerActivity.musicService!!.mediaPlayer!!.release()

                //  exitProcess(1)
                binding.root.gone()
            }
        }

        binding.songListBt.setOnClickListener {
            openSongList()
        }

        binding.root.setOnClickListener {
            MyIntent.goMusicIntent(
                mActivity,
                MPlayerActivity.songPosition,
                DataSet.Reference.MUSIC_NOW_PLAY
            )
        }

    }

    private fun openSongList() {
        val bs = BottomSheetDialog(mActivity)
        val binding_sheet = SonglistBsBinding.inflate(LayoutInflater.from(mActivity))
        bs.setContentView(binding_sheet.root)
        binding_sheet.title.text = "${MPlayerActivity.audioList.size} Songs"
        binding_sheet.rv.apply {
            adapter = MusicBsAdapter(mActivity, MPlayerActivity.audioList).apply {
                adapter
                setOnItemCloseListener(object : MusicBsAdapter.OnItemCloseListener {
                    override fun onItemClose(position: Int) {
                        if (MPlayerActivity.audioList.size > 1) {
                            MPlayerActivity.audioList.removeAt(position)
                            MPlayerActivity.songPosition = 0
                            adapter!!.notifyItemRemoved(position)
                            setLayout()
                            createMediaPlayer()
                            playMusic()
                            adapter!!.notifyDataSetChanged()

                        } else {
                            // exitApplication()
                            //  closePlayer()
                            bs.dismiss()

                        }
                    }

                })
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        MPlayerActivity.songPosition = position
                        setLayout()
                        createMediaPlayer()
                        playMusic()
                        adapter!!.notifyDataSetChanged()

                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
        bs.show()


    }

    fun createMediaPlayer() {
        try {
            if (MPlayerActivity.musicService!!.mediaPlayer == null) MPlayerActivity.musicService!!.mediaPlayer =
                MediaPlayer()

            MPlayerActivity.musicService!!.mediaPlayer!!.reset()
            MPlayerActivity.musicService!!.mediaPlayer!!.setDataSource(MPlayerActivity.audioList[MPlayerActivity.songPosition].path)
            MPlayerActivity.musicService!!.mediaPlayer!!.prepare()
            MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
            MPlayerActivity.musicService!!.showNotification(MusicService.PAUSE_IMAGE)

            MPlayerActivity.mpBinding.remainTime.text =
                formatDuration(MPlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
            MPlayerActivity.mpBinding.totalTime.text =
                formatDuration(MPlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
            MPlayerActivity.mpBinding.seekBar.progress = 0
            MPlayerActivity.mpBinding.seekBar.max =
                MPlayerActivity.musicService!!.mediaPlayer!!.duration

            //  NowPlayingFrag.nowPlayBinding.progressBar.progress=0
            // NowPlayingFrag.nowPlayBinding.progressBar.max = MPlayerActivity.musicService!!.mediaPlayer!!.duration

        } catch (e: Exception) {
            return
        }
    }

    private fun closePlayer() {
        if (MPlayerActivity.musicService != null) {
            MPlayerActivity.musicService!!.stopForeground(true)
            MPlayerActivity.musicService!!.mediaPlayer!!.release()
            MPlayerActivity.musicService = null
            binding.root.gone()

            // exitProcess(1)


        }

    }

    fun setLayout() {
        if (MPlayerActivity.musicService != null) {
            binding.root.visible()
            binding.albumArt.apply {
                // setImageBitmap(bitmap)
                Glide.with(context).asBitmap().circleCrop()
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

    override fun onResume() {
        super.onResume()
        setLayout()
    }

    private fun playMusic() {
        MPlayerActivity.musicService!!.mediaPlayer!!.start()
        binding.playPauseBt.setImageResource(R.drawable.pause_icon)
        MPlayerActivity.musicService!!.showNotification(MusicService.PAUSE_IMAGE)
        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_pause_circle)
        MPlayerActivity.isPlaying = true
    }

    private fun pauseMusic() {
        MPlayerActivity.musicService!!.mediaPlayer!!.pause()
        binding.playPauseBt.setImageResource(R.drawable.play_icon)
        MPlayerActivity.musicService!!.showNotification(MusicService.PLAY_IMAGE)
        MPlayerActivity.mpBinding.playPauseBt.setImageResource(R.drawable.ic_play_circle)
        MPlayerActivity.isPlaying = false
    }
}