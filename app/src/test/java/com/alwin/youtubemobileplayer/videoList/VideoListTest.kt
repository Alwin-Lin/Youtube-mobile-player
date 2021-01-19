package com.alwin.youtubemobileplayer.videoList

import com.alwin.youtubemobileplayer.videoListUI.VideoAdditionViewModel
import com.alwin.youtubemobileplayer.videoModel.VideoDao
import org.junit.Test

class VideoListTest {
    private lateinit var VideoAdditionViewModel: VideoAdditionViewModel
    private lateinit var videoDao: VideoDao

    @Test
    fun addVideoTest() {
        if (this::videoDao.isInitialized && this::VideoAdditionViewModel.isInitialized) {
            VideoAdditionViewModel = VideoAdditionViewModel(videoDao)
            val testingVideoName = "Add video Test Name"
            val testingVideoUrl = "Add video Test url"
            VideoAdditionViewModel.addVideo(
                    1,
                    testingVideoName,
                    testingVideoUrl
            ) {
            }
            var testingVideo = VideoAdditionViewModel.get(1)
            assert(testingVideo == VideoAdditionViewModel.get(1))
         }
    }
}