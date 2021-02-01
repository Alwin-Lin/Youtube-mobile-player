package com.alwin.youtubemobileplayer

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile

class YoutubeUrlConverter:Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun getYoutubeDownloadUrl(youtubeLink: String?) {
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>, vMeta: VideoMeta) {
                if (ytFiles != null) {
                    var i = 0
                    var itag: Int
                    itag = ytFiles.keyAt(i)
                    while (itag < ytFiles.size()) {
                        itag = ytFiles.keyAt(i)
                        i++
                    }
                    val downloadUrl = ytFiles[itag].url
                } else {
                    finish()
                    return
                }
            }
        }.extract(youtubeLink, true, true)
    }
}