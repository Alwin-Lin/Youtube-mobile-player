package com.alwin.youtubemobileplayer

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.runner.AndroidJUnit4
import com.alwin.youtubemobileplayer.videoRecordUI.AddVideoActivity
import com.alwin.youtubemobileplayer.videoRecordUI.VIDEO_NAME
import com.alwin.youtubemobileplayer.videoRecordUI.VIDEO_URL
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddYTIntentTest {
    private val TESTING_VIDEO_NAME = "Testing Video Name"
    private val TESTING_VIDEO_URL = "www.testing.Video.url"
    private val YT_TESTING_VIDEO_NAME = "Youtube Testing Video Name"
    private val YT_TESTING_VIDEO_URL = "www.youtube.testing.Video.url"

    @get:Rule
    val intentsTestRule = IntentsTestRule(AddVideoActivity::class.java)

    @Test
    fun manualAddVideoTest(){
        val resultData = Intent()
        resultData.putExtra(VIDEO_NAME, TESTING_VIDEO_NAME)
        resultData.putExtra(VIDEO_URL, TESTING_VIDEO_URL)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(toPackage("com.alwin.youtubemobileplayer.videoListUI")).respondWith(result)
    }

    @Test
    fun ytShareVideoTest(){
        val ytShareIntent = Intent()
        ytShareIntent.putExtra(VIDEO_NAME, YT_TESTING_VIDEO_NAME)
        ytShareIntent.putExtra(VIDEO_URL, YT_TESTING_VIDEO_URL)
    }
}