package com.musicplayer.android.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pl_video")
data class VideoItemPlData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "pl_id") val plId: Long? = null,
    @ColumnInfo(name = "video_id") val videoId: String,
    @ColumnInfo(name = "folder_name") val folderName: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "pixels") val pixels: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean?=false,
    ) {


}