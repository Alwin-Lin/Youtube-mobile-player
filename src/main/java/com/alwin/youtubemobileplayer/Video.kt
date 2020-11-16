package com.alwin.youtubemobileplayer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val description: String = "",
)