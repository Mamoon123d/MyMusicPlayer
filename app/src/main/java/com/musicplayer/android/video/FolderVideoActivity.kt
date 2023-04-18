package com.musicplayer.android.video

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.adapter.VideoSubAdapter
import com.musicplayer.android.databinding.ActivityFolderVideoBinding
import com.musicplayer.android.model.VideoContentData
import com.musicplayer.android.video.folder.FolderFragment
import java.io.File

class FolderVideoActivity : AppCompatActivity() {

    companion object{
        var currentFolderVideos :ArrayList<VideoContentData>?=null
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFolderVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val templist = ArrayList<VideoContentData>()
        templist.add(
            VideoContentData(
                "",
                "1",
                "ScreenShot",
                0,
                "ScreenShot",
                "2",
                "",
                Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
            )
        )
        templist.add(
            VideoContentData(
                "",
                "1",
                "ScreenShot",
                0,
                "ScreenShot",
                "2",
                "",
                Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
            )
        )
        templist.add(
            VideoContentData(
                "",
                "1",
                "ScreenShot",
                0,
                "ScreenShot",
                "2",
                "",
                Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg")
            )
        )
        templist.add(
            VideoContentData(
                "",
                "1",
                "ScreenShot",
                0,
                "ScreenShot",
                "2",
                "",
                Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg")
            )
        )

        val position = intent.getIntExtra("position", 0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = FolderFragment.folderList!![position].folderName
//        currentFolderVideos=getAllVideo(FolderVideoFragment.folderList!![position].id)
        binding.videoRecyclerFA.layoutManager = LinearLayoutManager(this)
        binding.videoRecyclerFA.adapter = VideoSubAdapter(this, templist)
        binding.totalVideo.text = buildString {
            append("Total Videos : ")
            append(templist.size)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
    @SuppressLint("Range", "Recycle")
    private fun getAllVideo(folderId: String): ArrayList<VideoContentData> {
        val tempList = ArrayList<VideoContentData>()
        val selection = MediaStore.Video.Media.BUCKET_ID
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            selection
        )

        val cursor = this@FolderVideoActivity.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(folderId),
            null
        )
        //sort Order=MediaStore.Video.Media.DATE_TAKEN + "DESC"
        Log.d("TAG", "getAllVideo: $cursor")
        if (cursor != null)
            if (cursor.moveToNext())
                do {
                    val dateC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
//                  val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()
                    try {
                        //val pathC ="/storage/emulator/0/Download"
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = VideoContentData(
                            date = dateC,
                            title = titleC,
                            id = idC,
                            folderName = folderC,
                            duration = 0,
                            size = sizeC,
                            path = pathC,
                            artURi = artUriC,
                        )
                        if (file.exists()) tempList.add(video)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
        cursor!!.close()
        return tempList
    }
}