package com.alwin.youtubemobileplayer.videoRecordUI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alwin.youtubemobileplayer.R
import com.google.android.material.textfield.TextInputEditText

/**
 * The activity used for adding videos
 * Sends out intent once user fills in the name and url of video
 */

const val VIDEO_NAME = "name"
const val VIDEO_URL = "URL"

class AddVideoActivity : AppCompatActivity() {
    private lateinit var addVideoName: TextInputEditText
    private lateinit var addVideoUrl: TextInputEditText
    private val TAG = "com.alwin.youtubemobileplayer.addVideoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_video_layout)
        addVideoName = findViewById(R.id.add_video_name)
        addVideoUrl = findViewById(R.id.add_video_url)
            findViewById<Button>(R.id.done_button).setOnClickListener {
                addVideo()
        }
    }

    /*
     If nothing is entered, close the activity
     If the name or url is not empty, save it as a new video
    */

    private fun addVideo() {
        val resultIntent = Intent()
        if (addVideoName.text.isNullOrEmpty() || addVideoUrl.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
            Log.i(TAG, "Video name or url is empty, activity canceled")
        } else {
            val name = addVideoName.text.toString()
            val url = addVideoUrl.text.toString()
            resultIntent.putExtra(VIDEO_NAME, name)
            resultIntent.putExtra(VIDEO_URL, url)
            setResult(Activity.RESULT_OK, resultIntent)
            Log.i(TAG,"Adding video, name: $name, url: $url, Activity result: ${Activity.RESULT_OK}")
        }
        finish()
    }
}