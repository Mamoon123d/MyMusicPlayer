package com.musicplayer.android.utils

class DataSet {
    companion object{
        final val REF:String="reference"
        final val TITLE:String="title"
        final val PLAYLIST_ID:String="playlistId"

    }
    final class Reference{
        companion object {
            final val FAVORITE: Int = 1
            final val PLAYLIST: Int = 2
        }
    }
}