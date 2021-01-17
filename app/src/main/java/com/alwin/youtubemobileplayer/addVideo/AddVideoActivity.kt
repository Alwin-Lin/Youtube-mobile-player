package com.alwin.youtubemobileplayer.addVideo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alwin.youtubemobileplayer.R
import com.alwin.youtubemobileplayer.YT_VIDEO_NAME
import com.alwin.youtubemobileplayer.YT_VIDEO_URL
import com.google.android.material.textfield.TextInputEditText

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


    /* The onClick action for the done button. Closes the activity and returns the new video name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. */

    private fun addVideo() {
        val resultIntent = Intent()
        if (addVideoName.text.isNullOrEmpty() || addVideoUrl.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = addVideoName.text.toString()
            val url = addVideoUrl.text.toString()
            resultIntent.putExtra(VIDEO_NAME, name)
            resultIntent.putExtra(VIDEO_URL, url)
            setResult(Activity.RESULT_OK, resultIntent)
            Log.i(TAG,"Adding video, name: $name, url: $url, Activity result: ${Activity.RESULT_OK}, intent: $resultIntent")
        }
        finish()
    }
}