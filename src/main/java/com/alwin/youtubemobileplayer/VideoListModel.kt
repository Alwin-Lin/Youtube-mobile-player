package com.alwin.youtubemobileplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alwin.youtubemobileplayer.storage.VideoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoListViewModel(private val videoDao: VideoDao) : ViewModel() {

    // Users of this ViewModel will observe changes to its donuts list to know when
    // to redisplay those changes
    val videos: LiveData<List<Video>> = videoDao.getAll()

    fun delete(video: Video) = viewModelScope.launch(Dispatchers.IO) {
        videoDao.delete(video)
    }
}
