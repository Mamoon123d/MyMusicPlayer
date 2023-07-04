package com.musicplayer.android.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.musicplayer.android.room.data.*

@Database(
    entities = [PlayListData::class, VideoItemPlData::class, FavoriteData::class, VideoHistoryData::class, MusicFavoriteData::class, MusicPlayListData::class, MusicItemPlData::class, RecentMusicItemData::class],
    version = 1,
    exportSchema = false
)
abstract class Db : RoomDatabase() {
    abstract fun myDao(): MyDao
    companion object {
        private var INSTANCE: Db? = null
        fun getDatabase(context: Context): Db {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context,
                            Db::class.java,
                            "myMedia_database")
                            .build()
                }
            }

            return INSTANCE!!
        }
    }
}