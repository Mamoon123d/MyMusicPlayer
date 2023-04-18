package com.musicplayer.android.model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.musicplayer.android.MainActivity
import com.musicplayer.android.room.database.Db
import com.musicplayer.android.room.repo.Repository
import com.musicplayer.android.room.vm.MainViewModel
import java.io.File
import java.text.DecimalFormat


data class VideoMainData(
    val id: String,
    var title: String,
    val duration: Long = 0,
    val folderName: String,
    val size: String,
    var path: String,
    var artUri: Uri,
    var pixels: String,
    var isFavourite: Boolean? = false,
    var plVideoId:Long?=0
)

data class Folder2(val id: String, val folderName: String, var numberVideos: Int = 0)

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllVideos(context: Context): ArrayList<VideoMainData> {
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    MainActivity.sortValue = sortEditor.getInt("sortValue", 0)

    //for avoiding duplicate folders
    MainActivity.folderList = ArrayList()


    val dao = Db.getDatabase(context).myDao()
    val repository = Repository(dao)
    val mainVm = MainViewModel(repository)

    val tempList = ArrayList<VideoMainData>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.RESOLUTION
    )
    val cursor = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        MainActivity.sortList[MainActivity.sortValue]
    )
    if (cursor != null)
        if (cursor.moveToNext())
            do {
                //checking null safety with ?: operator
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    ?: "Unknown"
                val idC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)) ?: "Unknown"
                val folderC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        ?: "Internal Storage"
                val folderIdC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                        ?: "Unknown"
                val sizeC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)) ?: "0"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    ?: "Unknown"
                //just add null checking in end, this 0L is alternative value if below function returns a null value
                val durationC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                        ?.toLong() ?: 0L

                val pixelsC = try {
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))
                        ?: "Unknown"
                } catch (e: Exception) {
                    "Unknown"
                }
                try {
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    //  Log.d("Video", "video path: " + pathC)


                    val video = VideoMainData(
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
                        isFavourite = false
                    )
                    if (file.exists()) tempList.add(video)

                    //for adding folders
                    if (!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")) {
                        tempFolderList.add(folderC)
                        MainActivity.folderList.add(
                            Folder2(
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
//-----------------------------------------------------

@SuppressLint("InlinedApi", "Recycle", "Range")
fun getAllFolderVideo(context: Context, folderId: String): ArrayList<VideoMainData> {
    val sortEditor = context.getSharedPreferences("Sorting", AppCompatActivity.MODE_PRIVATE)
    MainActivity.sortValue = sortEditor.getInt("sortValue", 0)
    val selection = MediaStore.Video.Media.BUCKET_ID + "=?"

    //for avoiding duplicate folders
    // MainActivity.folderList = ArrayList()

    val tempList = ArrayList<VideoMainData>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.RESOLUTION
    )
    /*MediaStore.Video.Media.TITLE,
    MediaStore.Video.Media.SIZE,
    MediaStore.Video.Media._ID,
    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
    MediaStore.Video.Media.DATA,
    MediaStore.Video.Media.DATE_ADDED,
    MediaStore.Video.Media.DURATION,*/
    val cursor = (context as Activity).contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection, selection, arrayOf(folderId),
        null

    )
    if (cursor != null)
        if (cursor.moveToNext())
            do {
                //checking null safety with ?: operator
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    ?: "Unknown"
                val idC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)) ?: "Unknown"
                val folderC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        ?: "Internal Storage"
                val folderIdC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                        ?: "Unknown"
                val sizeC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)) ?: "0"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    ?: "Unknown"
                //just add null checking in end, this 0L is alternative value if below function returns a null value
                val durationC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                        ?.toLong() ?: 0L

                val pixelsC = try {
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))
                } catch (e: Exception) {
                    "Unknown"
                }
                val dateC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                        ?: "Unknown"

                try {
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    //  Log.d("Video", "video path: " + pathC)


                    val video = VideoMainData(
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
                        pixels = pixelsC
                    )
                    if (file.exists()) tempList.add(video)

                    //for adding folders
                    /* if (!tempFolderList.contains(folderC) && !folderC.contains("Internal Storage")) {
                         tempFolderList.add(folderC)
                         MainActivity.folderList.add(Folder2(id = folderIdC, folderName = folderC))
                         *//*     var isDownload=false

                          if (folderC.contains("/Download/")){
                              if (!isDownload){
                                  MainActivity.folderList.add(Folder2(id = folderIdC, folderName = folderC))
                              }
                              isDownload=true
                          }else{
                           //   MainActivity.folderList.add(Folder2(id = folderIdC, folderName = folderC))
                          }*//*
                    }*/


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
    cursor?.close()
    return tempList
}


fun formatFileSize(size: Long): String? {
    var hrSize: String? = null
    val b = size.toDouble()
    val k = size / 1024.0
    val m = size / 1024.0 / 1024.0
    val g = size / 1024.0 / 1024.0 / 1024.0
    val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0
    val dec = DecimalFormat("0.00")
    hrSize = if (t > 1) {
        dec.format(t).plus(" TB")
    } else if (g > 1) {
        dec.format(g).plus(" GB")
    } else if (m > 1) {
        dec.format(m).plus(" MB")
    } else if (k > 1) {
        dec.format(k).plus(" KB")
    } else {
        dec.format(b).plus(" Bytes")
    }
    return hrSize
}

/*fun getPixels(path: String): Int {
    var retriever: MediaMetadataRetriever? = null
    val bmp: Bitmap? = null
    val inputStream: FileInputStream? = null
    val mWidthHeight = 0
    try {
        retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val width =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                ?.let { Integer.valueOf(it) }
        val height =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                ?.let { Integer.valueOf(it) }

        retriever.release()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: java.lang.RuntimeException) {
        e.printStackTrace()
    } finally {
        if (retriever != null) {
            retriever.release()
        }
    }
    return mWidthHeight

}*/

@SuppressLint("Range", "Recycle")
fun getAllVideo(context: Context, folderId: String): ArrayList<VideoMainData> {
    val tempList = ArrayList<VideoMainData>()
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

    val cursor = context.contentResolver.query(
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
                val dateC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                val folderC =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
//                  val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()
                try {
                    //val pathC ="/storage/emulator/0/Download"
                    val file = File(pathC)
                    val artUriC = Uri.fromFile(file)
                    val video = VideoMainData(
                        // date = dateC,
                        title = titleC,
                        id = idC,
                        folderName = folderC,
                        duration = 0,
                        size = sizeC,
                        path = pathC,
                        artUri = artUriC,
                        pixels = ""
                    )
                    if (file.exists()) tempList.add(video)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
    cursor!!.close()
    return tempList
}



