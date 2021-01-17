package com.alwin.youtubemobileplayer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val videoName: String,
        val videoUrl: String = "",
)