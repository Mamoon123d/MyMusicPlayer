package com.musicplayer.android.video.playlist

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.PlaylistAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.FragmentPlaylistVideoBinding
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.factory.MainMvFactory
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import com.musicplayer.android.utils.DataSet
import com.musicplayer.android.video.VideoListActivity
import java.io.File

class PlaylistFragment : BaseFragment<FragmentPlaylistVideoBinding>() {

    private lateinit var mainViewModel: MainViewModel

    override fun initM() {
        /* val data = ArrayList<VplistData>()

         for (i in 1..20) {
             data.add(VplistData(R.drawable.youtube_cover, "Video Name", "Item $i"))
         }
         val adapter = context?.let { VplistAdapter(it, data) }
         binding.videoPlaylistRecyclerFolder.adapter = adapter*/
        setPlaylist()

    }


    private fun setPlaylist() {
        val rv = binding.videoPlaylistRecyclerFolder
        val dao = Db.getDatabase(mActivity).myDao()
        val repository = Repository(dao)
        mainViewModel = ViewModelProvider(this, MainMvFactory(repository))[MainViewModel::class.java]

        //  val list=
        mainViewModel.getPlayList().observe(this) {

            rv.apply {
                adapter = PlaylistAdapter(mActivity, it, this@PlaylistFragment).apply {
                    setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int) {
                            val data = it[position]
                            val b = Bundle()
                            b.putInt(DataSet.REF, DataSet.Reference.PLAYLIST)
                            b.putString(DataSet.TITLE, data.title)
                            b.putString(DataSet.PLAYLIST_ID, data.id.toString())
                            goActivity(VideoListActivity(), b)
                        }

                    })
                }
                layoutManager = LinearLayoutManager(mActivity)
            }
        }

        setFavorite()

    }

    private fun setFavorite() {
        mainViewModel.getFavorites().observe(this) { favoriteList ->
            if (favoriteList.isNotEmpty()) {
                binding.vSubtitle.text = "${favoriteList.size} Videos"

                try {
                    val uriArt = getVideoThumbnail(favoriteList[0].videoId.toString())
                    if (uriArt != null) {

                        Glide.with(mActivity).asBitmap().load(uriArt).apply(
                            RequestOptions().placeholder(R.drawable.about).centerCrop()
                        ).into(binding.vThumbnail)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                binding.vSubtitle.text = "no video"
                Glide.with(mActivity).asBitmap().load(R.drawable.music_color_icon).apply(
                    RequestOptions().placeholder(R.drawable.about).centerCrop()
                ).into(binding.vThumbnail)
            }
        }
        binding.favCon.setOnClickListener {
            val b = Bundle()
            b.putInt(DataSet.REF, DataSet.Reference.FAVORITE)
            goActivity(VideoListActivity(), b)
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_playlist_video
    }


    @SuppressLint("InlinedApi", "Recycle", "Range")
    fun getVideoThumbnail(videoId: String): Uri {
        val selection = MediaStore.Video.Media._ID + "=?"
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
        )
        var tempUri: Uri? = null
        val cursor = mActivity.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection, selection, arrayOf(videoId),
            MainActivity.sortList[1]


        )
        if (cursor != null)
            if (cursor.moveToNext())
                do {

                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                        ?: "Unknown"


                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        //  Log.d("Video", "video path: " + pathC)
                        tempUri = artUriC

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
        cursor?.close()
        return tempUri!!
    }

}