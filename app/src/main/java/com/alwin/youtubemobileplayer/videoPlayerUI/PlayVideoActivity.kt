package com.alwin.youtubemobileplayer.videoPlayerUI

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.net.toUri
import com.alwin.youtubemobileplayer.R
import com.alwin.youtubemobileplayer.YT_VIDEO_URL
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.*
import kotlinx.android.synthetic.main.activity_play_video.*

/**
 * Takes in intent(URL), sets up ExoPlayer and it's required components
 * Hands control over to PlayerOverlayProcessor after player is running
 */

class PlayVideoActivity: Activity(), View.OnTouchListener {
    private val TAG = "com.alwin.youtubemobileplayer.videoPlayerUI.PlayVideoActivity"
    private var mPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0;
    private var playbackUrl = ""
    private val DRM_SCHEME_EXTRA = "drm_scheme"
    private val DRM_LICENSE_URL_EXTRA = "drm_license_url"
    private val EXTENSION_EXTRA = "extension"
    private lateinit var playerOverlayProcessor: PlayerOverlayProcessor
    private lateinit var videoProcessingGLSurfaceView: VideoProcessingGLSurfaceView
    private val SELECTED_POSITION = "POSITION"
    private var position = C.TIME_UNSET


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        playerView.setOnTouchListener(this)

        if (intent.hasExtra(Intent.EXTRA_TEXT)){
            playbackUrl= intent.getStringExtra(Intent.EXTRA_TEXT).toString()
            Log.i(TAG, "Received intent with url: $playbackUrl")
        } else {
            playbackUrl = intent.getStringExtra(YT_VIDEO_URL).toString()
            Log.i(TAG, "Received and playing $playbackUrl")
        }

        val requestSecureSurface = intent.hasExtra(DRM_SCHEME_EXTRA)
        if (requestSecureSurface && !GlUtil.isProtectedContentExtensionSupported(applicationContext)) {
            Toast.makeText(
                    applicationContext, "The GL protected content extension is not supported", Toast.LENGTH_LONG)
                    .show()
            Log.i(TAG, "The GL protected content extension is not supported")
        }

        playerOverlayProcessor = PlayerOverlayProcessor(applicationContext)
        val videoProcessingGLSurfaceView = VideoProcessingGLSurfaceView(
                applicationContext, requestSecureSurface, playerOverlayProcessor)
        val contentFrame = findViewById<FrameLayout>(R.id.player_frame)
        contentFrame.addView(videoProcessingGLSurfaceView)
        this.videoProcessingGLSurfaceView = videoProcessingGLSurfaceView

        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
        }
    }

    private fun initPlayer() {
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Bind the player to the view.
        playerView.player = mPlayer
        mPlayer!!.playWhenReady = true
        mPlayer!!.seekTo(playbackPosition)
        val videoProcessingGLSurfaceView = Assertions.checkNotNull(videoProcessingGLSurfaceView)
        videoProcessingGLSurfaceView.setVideoComponent(
                Assertions.checkNotNull(mPlayer!!.getVideoComponent()))
        if (position != C.TIME_UNSET) mPlayer!!.seekTo(position)
        mPlayer!!.prepare(buildMediaSource(), false, false)
        playerOverlayProcessor.setPlayer(mPlayer)
        Log.i(TAG, "MediaSource built, starting player")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SELECTED_POSITION, position);
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || mPlayer == null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mPlayer != null){
            position = mPlayer!!.currentPosition
        }
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        playWhenReady = mPlayer!!.playWhenReady
        playbackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentWindowIndex
        mPlayer!!.release()
        mPlayer = null
    }

    private fun buildMediaSource(): MediaSource {
        // Create dataSourceFactory
        val userAgent =
                Util.getUserAgent(playerView.context, playerView.context.getString(R.string.app_name))
        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)

        // Building drmSessionManager for mediaSourceFactory
        val drmSessionManager: DrmSessionManager = if (Util.SDK_INT >= 18 && intent.hasExtra(DRM_SCHEME_EXTRA)) {
            val drmScheme = Assertions.checkNotNull(intent.getStringExtra(DRM_SCHEME_EXTRA))
            val drmLicenseUrl = Assertions.checkNotNull(intent.getStringExtra(DRM_LICENSE_URL_EXTRA))
            val drmSchemeUuid = Assertions.checkNotNull(Util.getDrmUuid(drmScheme))
            val licenseDataSourceFactory: HttpDataSource.Factory = DefaultHttpDataSourceFactory()
            val drmCallback = HttpMediaDrmCallback(drmLicenseUrl, licenseDataSourceFactory)
            DefaultDrmSessionManager.Builder()
                    .setUuidAndExoMediaDrmProvider(drmSchemeUuid, FrameworkMediaDrm.DEFAULT_PROVIDER)
                    .build(drmCallback)
        } else {
            DrmSessionManager.getDummyDrmSessionManager()
        }

        // Build mediaItem
        val mediaSource: MediaSource
        val mediaItem: MediaItem = MediaItem.Builder()
                .setUri(Uri.parse(playbackUrl))
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()

        // Use
        @C.ContentType
        val type = Util.inferContentType(playbackUrl.toUri(), intent.getStringExtra(EXTENSION_EXTRA))
        if (type == C.TYPE_DASH) {
            mediaSource = DashMediaSource.Factory(dataSourceFactory)
                    .setDrmSessionManager(drmSessionManager)
                    .createMediaSource(mediaItem)
            Log.i(TAG, "mediaSource created from $playbackUrl, type :C.TYPE_DASH")
        } else if (type == C.TYPE_OTHER) {
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .setDrmSessionManager(drmSessionManager)
                    .createMediaSource(mediaItem)
            Log.i(TAG, "mediaSource created from $playbackUrl, type :C.TYPE_OTHER")
        } else {
            throw IllegalStateException()
            Log.i(TAG, "Unable to create mediaSource from $playbackUrl")
        }
        return mediaSource
    }

    override fun onTouch(p0: View?, touchEvent: MotionEvent?): Boolean {
        val x = touchEvent?.getX()?.toInt()
        val y = touchEvent?.getY()?.toInt()
        if (x != null && y != null){
            playerOverlayProcessor.setOffset(x, y)
        }
        return false
    }
}
