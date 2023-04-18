package com.musicplayer.android.model

import android.net.Uri

data class VideoContentData(
    val date: String,
    val id: String,
    val title:String,
    val duration: Long=0,
    val folderName: String,
    val size: String,
    val path: String,
    val artURi: Uri
)
data class VideoFolderData(val id : String, val folderName: String)

