package com.alwin.youtubemobileplayer.videoModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val videoName: String,
        val videoUrl: String = "",
)