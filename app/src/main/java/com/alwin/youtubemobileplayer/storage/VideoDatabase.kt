package com.alwin.youtubemobileplayer.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alwin.youtubemobileplayer.data.Video

/**
 * The underlying database where information about the videos is stored.
 */
@Database(entities = arrayOf(Video::class), version = 1)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getDatabase(context: Context): VideoDatabase {
            val tempInstance =
                    INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context,
                        VideoDatabase::class.java,
                        "video_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}