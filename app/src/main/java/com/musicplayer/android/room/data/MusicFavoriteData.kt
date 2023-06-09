package com.musicplayer.android.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_favorite")
data class MusicFavoriteData(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "music_id") val music_id: Long? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "folderName") val folderName: String,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "artUri") val artUri: String,
    @ColumnInfo(name = "pixels") val pixels: String,
    @ColumnInfo(name = "album") val album: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "albumId") val albumId: String? = null,
    @ColumnInfo(name = "isFavourite") var isFavourite: Boolean? = false,
)
