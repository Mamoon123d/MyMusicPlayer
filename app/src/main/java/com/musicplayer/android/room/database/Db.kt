package com.musicplayer.android.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.musicplayer.android.room.data.FavoriteData
import com.musicplayer.android.room.data.PlayListData
import com.musicplayer.android.room.data.VideoItemPlData

@Database(entities = [PlayListData::class,VideoItemPlData::class,FavoriteData::class], version = 1, exportSchema = false)
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