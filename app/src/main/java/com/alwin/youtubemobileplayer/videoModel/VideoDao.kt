package com.alwin.youtubemobileplayer.videoModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * The Data Access Object used to retrieve and store data from/to the [VideoDatabase].
 */
@Dao
interface VideoDao {
    @Query("SELECT * FROM video")
    fun getAll(): LiveData<List<Video>>

    @Query("SELECT * FROM video WHERE id = :id")
    suspend fun get(id: Long): Video

    @Insert
    suspend fun insert(video: Video): Long

    @Delete
    suspend fun delete(video: Video)

    @Update
    suspend fun update(video: Video)
}
