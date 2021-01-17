package com.alwin.youtubemobileplayer.videoList


import android.util.Log
import androidx.lifecycle.*
import com.alwin.youtubemobileplayer.data.Video
import com.alwin.youtubemobileplayer.storage.VideoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** This class interacts with videoLiveData, handles the adding and deleting of data
 * The id, name, and the discription will be passed to addData
 */

class VideoEditViewModel(private val videoDao: VideoDao) : ViewModel() {

    private val TAG = "com.alwin.youtubemobileplayer.VideoEditViewModel"
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
        Log.i(TAG, "VideoEntryModel: Adding video entry Name: $name, url: $url")
    }

    private suspend fun insert(video: Video): Long {
        return videoDao.insert(video)
        Log.i(TAG, "VideoEntryModel: New video inserted")
    }

    private fun update(video: Video) = viewModelScope.launch(Dispatchers.IO) {
        videoDao.update(video)
        Log.i(TAG, "VideoEntryModel: List updated")
    }
}

class ViewModelFactory(private val videoDao: VideoDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoListViewModel(videoDao) as T
        } else if (modelClass.isAssignableFrom(VideoEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoEditViewModel(videoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
