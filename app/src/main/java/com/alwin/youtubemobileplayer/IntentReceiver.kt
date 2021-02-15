package com.alwin.youtubemobileplayer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.widget.Toast

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import com.alwin.youtubemobileplayer.videoPlayerUI.PlayVideoActivity

/**
 * Receives intent from other Youtube, processes url, and plays shared video
 */

const val YT_VIDEO_NAME = "com.alwin.youtubemobileplayer.VIDEO_NAME"
const val YT_VIDEO_URL = "com.alwin.youtubemobileplayer.VIDEO_URL"

class IntentReceiver: AppCompatActivity() {
    private val TAG: String = "com.alwin.youtubemobileplayer.IntentReceiver"
    private lateinit var ytUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (savedInstanceState== null && intent.type != null && "text/plain" == intent.type) {
                    handleActionSendIntent(intent)
                } else if (savedInstanceState != null) {
                    getYoutubeDownloadUrl(ytUrl)
                }
                else {
                    Log.i(TAG, "ACTION_SEND type is not text/plain")
                }
            }
            else -> {
                Log.i(TAG, "Cannot handle intent $intent")
            }
        }
        finish()
    }

    private fun handleActionSendIntent(intent: Intent) {
        val ytUrl: String = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
        if (ytUrl.contains("://youtu.be/") || ytUrl.contains("youtube.com/watch?v=")) {
            Log.i(TAG, "Received intent with ACTION_SEND from $ytUrl")
            getYoutubeDownloadUrl(ytUrl)
            finish()
        } else {
            Toast.makeText(this, "URL mismatch, please share from Youtube", Toast.LENGTH_LONG)
            Log.i(TAG, "Received URL $ytUrl, cannot process")
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun getYoutubeDownloadUrl(ytUrl: String) {
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    var i = 0
                    var itag: Int
                    itag = ytFiles.keyAt(i)
                    while (itag < ytFiles.size()) {
                        itag = ytFiles.keyAt(i)
                        i++
                    }
                    val downloadUrl = ytFiles[itag].url
                    addUrlToList(downloadUrl)
                } else {
                    finish()
                    return
                }
            }
        }.extract(ytUrl, true, true)
    }

    fun addUrlToList(downloadURL: String){
        // Get the name and url
        var convYTName = "Todo: Name"
        var convYTUrl = downloadURL
        Log.i(TAG, "Youtube video url convert complete, $convYTName, url: $convYTUrl")

        // Start Video list fragment
        val processedYTIntent = Intent(this, MainActivity()::class.java)
        processedYTIntent.putExtra(YT_VIDEO_NAME, convYTName)
        processedYTIntent.putExtra(YT_VIDEO_URL, convYTUrl)
        Log.i(TAG, "Intent sent, $intent")
        startActivity(processedYTIntent)

        val playVideo = Intent(this, PlayVideoActivity::class.java)
        playVideo.putExtra(YT_VIDEO_URL, convYTUrl)
        startActivity(playVideo)
    }
}