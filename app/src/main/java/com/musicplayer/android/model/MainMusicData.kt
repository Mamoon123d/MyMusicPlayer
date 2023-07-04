package com.musicplayer.android.model

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.musicplayer.android.MainActivity
import com.musicplayer.android.music.MPlayerActivity
import java.io.File
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

data class MainMusicData(
    val id: String,
    var title: String,
    val duration: Long = 0,
    val folderName: String,
    val size: String,
    var path: String,
    var artUri: String,
    var pixels: String,
    var album: String,
    var artist: String,
    var albumId: String,
    var artistId: String,
    //  var folderId: String,
    var isFavourite: Boolean? = false
)

data class MusicFolder(
    val id: String, val folderName: String, val folderPath: String, var numberVideos: Int = 0
)


@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllMusic(context: Context): ArrayList<MainMusicData> {
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    MainActivity.sortValue = sortEditor.getInt("sortValue", 0)

    //for avoiding duplicate folders
    MainActivity.audioFolders = ArrayList()


    //val dao = Db.getDatabase(context).myDao()
    //val repository = Repository(dao)
    //val mainVm = MainViewModel(repository)

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
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ALBUM_ID,


        MediaStore.Audio.Media.COMPOSER,

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

                val albumIdC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)) ?: "0"

                val artistIdC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)) ?: "0"

                val composerC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER)) ?: null
                Log.d("getAllMusic", "getAllMusic: " + composerC)

                try {
                    val file = File(pathC)
                    //val artUriC = Uri.fromFile(file)
                    //  Log.d("Audio", "Audio path: " + pathC)
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()

                    val Audio = MainMusicData(
                        title = if (titleC.matches(Regex("Unknown"))) titleC else "${
                            Uri.fromFile(file).lastPathSegment
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
                        albumId = albumIdC,
                        artistId = artistIdC,
                        //  folderId = folderIdC
                    )
                    if (file.exists()) {
                        //val albumIdTemp = albumIdC.trim().toLong()
                        if (composerC != null) {
                            tempList.add(Audio)
                        }
                    }

                    //for adding folders
                    if (!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")) {
                        tempFolderList.add(folderC)
                        val folderPath = file.parent
                        if (composerC != null) {
                            MainActivity.audioFolders.add(
                                MusicFolder(
                                    id = folderIdC, folderName = folderC, folderPath = folderPath
                                )
                            )
                        }

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
    cursor?.close()
    return tempList
}

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAlbumList(context: Context): ArrayList<MusicAlbumData> {
    val projection = arrayOf(
        MediaStore.Audio.Albums.ALBUM_ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
        MediaStore.Audio.Albums.ARTIST,
    )
    val tempList = ArrayList<MusicAlbumData>()

    val cursor = context.contentResolver.query(
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, null, null, null

    )
    if (cursor != null) {
        if (cursor.moveToNext()) do {
            //checking null safety with ?: operator
            //just add null checking in end, this 0L is alternative value if below function returns a null value
            // val albumIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)) ?: "0"
            val albumIdC =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)) ?: "0"
            val albumC =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)) ?: "Unknown"
            val noOFSongsC =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
                    ?: "0"
            val artistC =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)) ?: null

            //val albumArtC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)) ?: "Unknown"
            val uri = Uri.parse("content://media/external/audio/albumart")
            val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()

            try {
                if (artistC != null) {
                    tempList.add(
                        MusicAlbumData(
                            albumId = albumIdC,
                            albumName = albumC,
                            artUri = artUriC,
                            totalTrack = noOFSongsC
                        )
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } while (cursor.moveToNext())
    }
    cursor?.close()
    return tempList
}

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getArtistList(context: Context): ArrayList<MusicArtistData> {
    val projection = arrayOf(
        MediaStore.Audio.Artists._ID,
        MediaStore.Audio.Artists.ARTIST,
        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,


        )
    val tempList = ArrayList<MusicArtistData>()

    val cursor = context.contentResolver.query(
        MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, null, null, null

    )
    if (cursor != null) {
        if (cursor.moveToNext()) do {
            //checking null safety with ?: operator
            //just add null checking in end, this 0L is alternative value if below function returns a null value
            // val albumIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)) ?: "0"
            val artistIdC =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)) ?: "0"
            val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                ?: "Unknown"
            val noOFSongsC =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
                    ?: "0"

            try {
                tempList.add(
                    MusicArtistData(
                        artistId = artistIdC, artists = artistC, trackNumber = noOFSongsC
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } while (cursor.moveToNext())
    }
    cursor?.close()
    return tempList
}

fun getImageArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

public fun setMusicPosition(increment: Boolean) {
    if (!MPlayerActivity.isRepeat) {
        if (increment) {
            if (MPlayerActivity.audioList.size - 1 == MPlayerActivity.songPosition)
                MPlayerActivity.songPosition = 0
            else
                ++MPlayerActivity.songPosition
        } else {
            if (0 == MPlayerActivity.songPosition)
                MPlayerActivity.songPosition = MPlayerActivity.audioList.size - 1
            else
                --MPlayerActivity.songPosition

        }
    }
}

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(
        duration, TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

fun formatDate(time: Long): String {
    return DateFormat.getDateTimeInstance(
        DateFormat.SHORT, DateFormat.MEDIUM
    ).format(time)
}

fun exitApplication() {
    if (MPlayerActivity.musicService != null) {
        MPlayerActivity.musicService!!.stopForeground(true)
        MPlayerActivity.musicService!!.mediaPlayer!!.release()
        MPlayerActivity.musicService = null
        exitProcess(1)


    }
}
