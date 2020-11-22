package com.alwin.youtubemobileplayer


import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.alwin.youtubemobileplayer.storage.VideoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** This class interacts with videoLiveData, handles the adding and deleting of data
 * The id, name, and the discription will be passed to addData
 */

class VideoEntryViewModel(private val videoDao: VideoDao) : ViewModel() {

    private val TAG = "com.alwin.youtubemobileplayer"
    private var videoLiveData: LiveData<Video>? = null

    fun get(id: Long): LiveData<Video> {
        return videoLiveData ?: liveData {
            emit(videoDao.get(id))
        }.also {
            videoLiveData = it
        }
    }

    fun addData(
            id: Long,
            name: String,
            description: String,
            setupNotification: (Long) -> Unit
    ) {
        val video = Video(id, name, description)

        CoroutineScope(Dispatchers.Main.immediate).launch {
            var actualId = id

            if (id > 0) {
                update(video)
            } else {
                actualId = insert(video)
            }
            setupNotification(actualId)
        }
        Log.i(TAG, "VideoEntryModel: Adding video entry Name: $name, URL: $description")
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
