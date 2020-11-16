package com.alwin.youtubemobileplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alwin.youtubemobileplayer.storage.VideoDao

class ViewModelFactory(private val videoDao: VideoDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoListViewModel(videoDao) as T
        } else if (modelClass.isAssignableFrom(VideoEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoEntryViewModel(videoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
