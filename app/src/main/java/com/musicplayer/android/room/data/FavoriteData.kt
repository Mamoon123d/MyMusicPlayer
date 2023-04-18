package com.musicplayer.android.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteData(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "video_id") val videoId: Long? = null,
    @ColumnInfo(name = "folder_name") val folderName: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "pixels") val pixels: String,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean?=false,
    ) {
}

