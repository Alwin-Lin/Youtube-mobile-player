package com.alwin.youtubemobileplayer.videoList

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alwin.youtubemobileplayer.R
import com.alwin.youtubemobileplayer.storage.VideoDao

class VideoListActivity : AppCompatActivity() {
    private lateinit var videoDao: VideoDao
    private val newVideoActivityRequstCode = 1
    private val videoListViewModel by viewModels<VideoListViewModel> {
        ViewModelFactory(videoDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }
}