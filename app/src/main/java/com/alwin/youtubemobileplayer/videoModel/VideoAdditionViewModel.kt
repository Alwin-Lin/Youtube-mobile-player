package com.alwin.youtubemobileplayer.videoListUI


import android.util.Log
import androidx.lifecycle.*
import com.alwin.youtubemobileplayer.videoModel.Video
import com.alwin.youtubemobileplayer.videoModel.VideoDao
import com.alwin.youtubemobileplayer.videoModel.VideoDeleteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** This class interacts with videoLiveData, handles the adding and deleting of data
 * The id, name, and the descriptions will be passed to addData
 */

class VideoAdditionViewModel(private val videoDao: VideoDao) : ViewModel() {

    private val TAG = "com.alwin.youtubemobileplayer.VideoAdditionViewModel"
    private var videoLiveData: LiveData<Video>? = null

    fun get(id: Long): LiveData<Video> {
        return videoLiveData ?: liveData {
            emit(videoDao.get(id))
        }.also {
            videoLiveData = it
        }
    }

    fun addVideo(
            id: Long,
            name: String,
            url: String,
            setupNotification: (Long) -> Unit
    ) {
        val video = Video(id, name, url)
        CoroutineScope(Dispatchers.Main.immediate).launch {
            var actualId = id

            if (id > 0) {
                update(video)
            } else {
                actualId = insert(video)
            }
            setupNotification(actualId)
        }
        Log.i(TAG, "New video added, name: $name, url: $url")
    }

    private suspend fun insert(video: Video): Long {
        return videoDao.insert(video)
    }

    private fun update(video: Video) = viewModelScope.launch(Dispatchers.IO) {
        videoDao.update(video)
        Log.i(TAG, "List updated")
    }
}

class ViewModelFactory(private val videoDao: VideoDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoDeleteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoDeleteViewModel(videoDao) as T
        } else if (modelClass.isAssignableFrom(VideoAdditionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoAdditionViewModel(videoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
