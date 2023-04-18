package com.musicplayer.android.model

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.musicplayer.android.MainActivity
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import java.io.File

data class MainMusicData(
    val id: String,
    var title: String,
    val duration: Long = 0,
    val folderName: String,
    val size: String,
    var path: String,
    var artUri: Uri,
    var pixels: String,
    var album: String,
    var artist: String,
    var albumId: String,
    var isFavourite: Boolean? = false
)

data class MusicFolder(val id: String, val folderName: String, var numberVideos: Int = 0)


@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllMusic(context: Context): ArrayList<MainMusicData> {
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    MainActivity.sortValue = sortEditor.getInt("sortValue", 0)

    //for avoiding duplicate folders
    MainActivity.audioFolders = ArrayList()


    val dao = Db.getDatabase(context).myDao()
    val repository = Repository(dao)
    val mainVm = MainViewModel(repository)

    val tempList = ArrayList<MainMusicData>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.BUCKET_ID,
        MediaStore.Audio.Media.RESOLUTION,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID,

    )
    val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        MainActivity.sortList[MainActivity.sortValue]
    )
    if (cursor != null)
        if (cursor.moveToNext())
            do {
                //checking null safety with ?: operator
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    ?: "Unknown"
                val idC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)) ?: "Unknown"
                val folderC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
                        ?: "Internal Storage"
                val folderIdC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
                        ?: "Unknown"
                val sizeC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)) ?: "0"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    ?: "Unknown"
                //just add null checking in end, this 0L is alternative value if below function returns a null value
                val durationC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        ?.toLong() ?: 0L

                val pixelsC = try {
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RESOLUTION))
                        ?: "Unknown"
                } catch (e: Exception) {
                    "Unknown"
                }

                val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    ?: "Unknown"
                val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    ?: "Unknown"

               val albumIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)) ?: "0"


                try {
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    //  Log.d("Audio", "Audio path: " + pathC)


                    val Audio = MainMusicData(
                        title = if (titleC.matches(Regex("Unknown"))) titleC else "${
                            Uri.fromFile(
                                file
                            ).lastPathSegment
                        }",
                        id = idC,
                        folderName = folderC,
                        duration = durationC,
                        size = formatFileSize(sizeC.toLong())!!,
                        path = pathC,
                        artUri = artUriC,
                        pixels = pixelsC,
                        isFavourite = false,
                        album = albumC,
                        artist = artistC,
                       albumId = albumIdC
                    )
                    if (file.exists()) tempList.add(Audio)

                    //for adding folders
                    if (!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")) {
                        tempFolderList.add(folderC)
                        MainActivity.audioFolders.add(
                            MusicFolder(
                                id = folderIdC,
                                folderName = folderC
                            )
                        )

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
    cursor?.close()
    return tempList
}
