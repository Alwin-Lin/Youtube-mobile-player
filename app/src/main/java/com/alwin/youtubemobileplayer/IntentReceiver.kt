package com.alwin.youtubemobileplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alwin.youtubemobileplayer.addVideo.AddVideoActivity

const val YT_VIDEO_NAME = "com.alwin.youtubemobileplayer.VIDEO_NAME"
const val YT_VIDEO_URL = "com.alwin.youtubemobileplayer.VIDEO_URL"

class IntentReceiver : AppCompatActivity() {
    private val TAG : String = "com.alwin.youtubemobileplayer.IntentReceiver"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (intent.type != null && "text/plain" == intent.type) {
                    handleActionSendIntent(intent)
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
        // Get video Url
        var ytUrl: String = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
        Log.i(TAG, "Receive ACTION_SEND, url: $ytUrl")

        // Todo: 1.Convert Youtube Url
        var convYTName = "Todo: Name"
        var convYTUrl = "Todo: $ytUrl"
        Log.i(TAG, "Video name: $convYTName, url: $convYTUrl")

        // Todo: 2.Add Url to video List
        val processedYTIntent = Intent(this, MainActivity()::class.java)
        processedYTIntent.putExtra(YT_VIDEO_NAME, convYTName)
        processedYTIntent.putExtra(YT_VIDEO_URL, convYTUrl)
        Log.i(TAG, "Starting AddVideoActivity with $intent")
        startActivity(processedYTIntent)
        // Todo: 3.Play Youtube video
        Log.i(TAG, "Todo: Play video")
        finish()
    }
}
