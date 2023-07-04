package com.musicplayer.android.utils

class DataSet {
    companion object{
        final val REF: String = "reference"
        final val TITLE: String = "title"
        final val PLAYLIST_ID: String = "playlistId"
        final val PLAYLIST_NAME: String = "playlistName"
        final val ALBUM_ID: String = "albumId"
        final val ARTIST_ID: String = "artistId"
        final val FOLDER_ID: String = "folderId"

    }
    final class Reference{
        companion object {
            final val FAVORITE: Int = 1
            final val PLAYLIST: Int = 2

            //for music
            final val MUSIC_PLAYLIST: Int = 3
            final val MUSIC_ALBUM: Int = 4
            final val MUSIC_ARTIST: Int = 5
            final val MUSIC_FOLDER: Int = 6
            final val MUSIC_FAVORITE: Int = 7
            final val MUSIC_RECENT: Int = 8
            final val MUSIC_ALL_PL: Int = 9
            final val MUSIC_ALL: Int = 10
            final val MUSIC_NOW_PLAY: Int = 11
        }
    }
}