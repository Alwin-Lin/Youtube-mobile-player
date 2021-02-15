package com.alwin.youtubemobileplayer.videoModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alwin.youtubemobileplayer.videoModel.Video
import com.alwin.youtubemobileplayer.videoModel.VideoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Called when deleting video
 */

class VideoDeleteViewModel(private val videoDao: VideoDao) : ViewModel() {
    private val TAG = "com.alwin.youtubemobileplayer.VideoListModel"

    // Users of this ViewModel will observe changes to its donuts list to know when
    // to redisplay those changes
    val videos: LiveData<List<Video>> = videoDao.getAll()

    @SuppressLint("LongLogTag")
    fun delete(video: Video) = viewModelScope.launch(Dispatchers.IO) {
        videoDao.delete(video)
        var name = video.videoName
        var url = video.videoUrl
        Log.i(TAG,"Video deleted, name: $name, url: $url")
    }
}


