package com.musicplayer.android.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_history")
data class VideoHistoryData (
     @PrimaryKey(autoGenerate = false)
     @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "videoId") val videoId: Long? = null,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "time") val recentTime: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean?=false,

    ){
}
