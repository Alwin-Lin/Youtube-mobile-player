package com.alwin.youtubemobileplayer.videoPlayerUI

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.GlUtil
import com.google.android.exoplayer2.util.GlUtil.Uniform
import com.google.android.exoplayer2.util.Util
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.opengles.GL10

/**
 * The VideoProcessingGLSurfaceView.VideoProcessor implemented here is an interface.
 * This class scales incoming video and divides the screen accordingly
 * Also handles the moving of close-up view when user taps on screen
 */

class PlayerOverlayProcessor : VideoProcessingGLSurfaceView.VideoProcessor {

    private val TAG = "com.alwin.youtubemobileplayer.videoPlayerUI.PlayerOverlayProcessor"

    private val OVERLAY_WIDTH = 512
    private val OVERLAY_HEIGHT = 256
    private val SEPARATE = 6

    private var context: Context? = null
    private var paint: Paint? = null
    private var textures: IntArray
    private var overlayBitmap: Bitmap? = null
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var offsetX = 0
    private var offsetY = 0
    private var surfaceWidth = 0
    private var surfaceHeight = 0
    private var fullVideoWidth = 0
    private var fullVideoHeight = 0
    private var fovVideoHeight = 0
    private var fovVideoWidth = 0
    private var minOffsetX = 0
    private var maxOffsetX = 0
    private var minOffsetY = 0
    private var maxOffsetY = 0

    private var program = 0
    private var attributes: Array<GlUtil.Attribute>? = null
    private var uniforms: Array<Uniform>? = null

    private var bitmapScaleX = 0f
    private var bitmapScaleY = 0f

    constructor(applicationContext: Context) {
        this.context = applicationContext.applicationContext
        paint = Paint()
        paint!!.setTextSize(64f)
        paint!!.setAntiAlias(true)
        paint!!.setARGB(0xFF, 0xFF, 0xFF, 0xFF)
        textures = IntArray(1)
        overlayBitmap = Bitmap.createBitmap(OVERLAY_WIDTH, OVERLAY_HEIGHT, Bitmap.Config.ARGB_8888)
    }

    // Initializes the processor
    override fun initialize() {
        val vertexShaderCode = loadAssetAsString(context, "bitmap_overlay_video_processor_vertex.glsl")
        val fragmentShaderCode = loadAssetAsString(context, "bitmap_overlay_video_processor_fragment.glsl")
        program = GlUtil.compileProgram(vertexShaderCode, fragmentShaderCode)
        val attributes = GlUtil.getAttributes(program)
        val uniforms = GlUtil.getUniforms(program)
        for (attribute in attributes) {
            if (attribute.name == "a_position") {
                attribute.setBuffer(floatArrayOf(
                        -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f),
                        4)
            } else if (attribute.name == "a_texcoord") {
                attribute.setBuffer(floatArrayOf(
                        0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f),
                        3)
            }
        }
        this.attributes = attributes
        this.uniforms = uniforms
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, textures[0])
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat())
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT.toFloat())
        GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT.toFloat())
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D,  /* level= */0, overlayBitmap,  /* border= */0)
    }

    /**
     * Calculates scaling of video and set boundaries.
     * @param width screen's width in pixels
     * @param height screen's height in pixels
     */
    override fun setSurfaceSize(width: Int, height: Int) {
        bitmapScaleX = width.toFloat() / OVERLAY_WIDTH
        bitmapScaleY = height.toFloat() / OVERLAY_HEIGHT
        surfaceHeight = height
        surfaceWidth = width
        if (surfaceWidth > surfaceHeight) {
            // Landscape
            fullVideoHeight = surfaceHeight
            fullVideoWidth = (fullVideoHeight * getVideoRatio()).toInt()
            fovVideoWidth = surfaceWidth - fullVideoWidth
            fovVideoHeight = (fovVideoWidth / getVideoRatio()).toInt()
            Log.i(TAG, String.format("Landscape phone, " +
                    " fullVideoHeight: %d, fullVideoWidth: %d, fovVideoWidth: %d, fovVideoHeight: %d, video ratio: %f", fullVideoHeight, fullVideoWidth, fovVideoWidth, fovVideoHeight, getVideoRatio()))
            maxOffsetY = (fullVideoWidth * getVideoRatio()).toInt() * 2
            minOffsetY = surfaceHeight - maxOffsetY
        } else {
            // Portrait
            fullVideoWidth = surfaceWidth
            fullVideoHeight = (fullVideoWidth * getVideoRatio()).toInt()
            fovVideoHeight = surfaceHeight - fullVideoHeight
            fovVideoWidth = (fovVideoHeight / getVideoRatio()).toInt()
            Log.i(TAG, String.format("Portrait phone, " +
                    " fullVideoHeight: %d, fullVideoWidth: %d, fovVideoWidth: %d, fovVideoHeight: %d, video ratio: %f", fullVideoHeight, fullVideoWidth, fovVideoWidth, fovVideoHeight, getVideoRatio()))
            minOffsetX = (fullVideoHeight * getVideoRatio()).toInt() / 2
            maxOffsetX = surfaceWidth - minOffsetX
        }
    }

    private fun drawFrame(frameTexture: Int) {
        val uniforms = Assertions.checkNotNull(uniforms)
        val attributes = Assertions.checkNotNull(attributes)
        GLES20.glUseProgram(program)
        for (uniform in uniforms) {
            when (uniform.name) {
                "tex_sampler_0" -> uniform.setSamplerTexId(frameTexture,  /* unit= */0)
                "tex_sampler_1" -> uniform.setSamplerTexId(textures[0],  /* unit= */1)
                "scaleX" -> uniform.setFloat(bitmapScaleX)
                "scaleY" -> uniform.setFloat(bitmapScaleY)
            }
        }
        for (copyExternalAttribute in attributes) {
            copyExternalAttribute.bind()
        }
        for (copyExternalUniform in uniforms) {
            copyExternalUniform.bind()
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,  /* first= */0,  /* count= */4)
        GlUtil.checkGlError()
    }

    /**
     * setOffset adjusts focus of the main player when split and sets boundaries to prevent video sliding out.
     *
     * @param x The x value of where user touched on screen
     * @param y The y value of where user touched on screen
     */
    fun setOffset(x: Int, y: Int) {
        var inFullVideo = false
        if (surfaceHeight > surfaceWidth) {
            // Portrait
            inFullVideo = y > surfaceWidth / getVideoRatio()
            if (inFullVideo) {
                // touched within fov
                // ignore y
                if (x < minOffsetX) {
                    offsetX = (fovVideoWidth - surfaceWidth) / 2
                    Log.i(TAG, String.format("Touch(%d, %d), offset(%d, %d), reached portrait min offset", x, y, offsetX, offsetY))
                } else if (x > maxOffsetX) {
                    offsetX = (surfaceWidth - fovVideoWidth) / 4
                    Log.i(TAG, String.format("Touch(%d, %d), offset(%d, %d), reached portrait max offset", x, y, offsetX, offsetY))
                } else {
                    offsetX = ((surfaceWidth / 2 - x) / 4 * getVideoRatio()).toInt()
                }
            } else {
                return
            }
        } else {
            //landscape
            inFullVideo = x < surfaceHeight * getVideoRatio()
            if (inFullVideo) {
                // touched within fov
                // ignore x
                if (y < minOffsetY) {
                    offsetY = (surfaceHeight - fovVideoHeight) / 4
                    Log.i(TAG, String.format("Touch(%d, %d), offset(%d, %d), reached landscape min offset", x, y, offsetX, offsetY))
                } else if (y > maxOffsetY) {
                    offsetY = (fovVideoHeight - surfaceHeight) / 2
                    Log.i(TAG, String.format("Touch(%d, %d), offset(%d, %d), reached landscape max offset", x, y, offsetX, offsetY))
                } else {
                    offsetY = ((y - surfaceHeight / 2) * 4 * getVideoRatio()).toInt()
                }
            } else {
                return
            }
        }
        Log.i(TAG, String.format("Touch(%d, %d), offset(%d, %d)", x, y, offsetX, offsetY))
    }

    private fun landscapeSplit(frameTexture: Int) {
        // Draw full content frame in landscape mode
        GLES20.glViewport(0, 0, fullVideoWidth, fullVideoHeight)
        drawFrame(frameTexture)

        // Draw field of view content to fill the rest.
        val xOrigin = fullVideoWidth + SEPARATE
        val yOrigin = (fullVideoHeight - fovVideoHeight) / 2
        GLES20.glViewport(xOrigin, yOrigin + offsetY, fovVideoWidth, fovVideoHeight + offsetY)
        drawFrame(frameTexture)
    }

    private fun portraitSplit(frameTexture: Int) {
        GLES20.glViewport(0, 0, fullVideoWidth, fullVideoHeight)
        drawFrame(frameTexture)

        // Draw field of view content to fill the rest.
        val xOrigin = (fullVideoWidth - fovVideoWidth) / 2
        val yOrigin = fullVideoHeight + SEPARATE
        GLES20.glViewport(xOrigin + offsetX, yOrigin, fovVideoWidth + offsetX, fovVideoHeight)
        drawFrame(frameTexture)
    }

    private fun fullScreen(frameTexture: Int) {
        // ToDo: Fix video scale.
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight)
        drawFrame(frameTexture)
    }

    private fun getVideoWidth(): Int {
        if (simpleExoPlayer == null) {
            return 720
        }
        val selectedVideoFormat = simpleExoPlayer!!.videoFormat ?: return 720
        return selectedVideoFormat.width
    }

    private fun getVideoHeight(): Int {
        if (simpleExoPlayer == null) {
            return 1280
        }
        val selectedVideoFormat = simpleExoPlayer!!.videoFormat ?: return 1280
        return selectedVideoFormat.height
    }

    private fun getVideoRatio(): Float {
        return getVideoWidth().toFloat() / getVideoHeight()
    }

    fun setPlayer(player: SimpleExoPlayer?) {
        simpleExoPlayer = player
    }

    /**
     * Checks the orientation of phone and video, then splits screen if needed
     */
    override fun draw(frameTexture: Int, frameTimestampUs: Long) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        if (getVideoWidth() > getVideoHeight()) {
            if (surfaceWidth > surfaceHeight) {
                fullScreen(frameTexture)
            } else {
                portraitSplit(frameTexture)
            }
        } else {
            if (surfaceWidth > surfaceHeight) {
                landscapeSplit(frameTexture)
            } else {
                fullScreen(frameTexture)
            }
        }
    }

    private fun loadAssetAsString(context: Context?, assetFileName: String): String {
        var inputStream: InputStream? = null
        return try {
            inputStream = context!!.assets.open(assetFileName)
            Util.fromUtf8Bytes(Util.toByteArray(inputStream))
        } catch (e: IOException) {
            throw IllegalStateException(e)
        } finally {
            Util.closeQuietly(inputStream)
        }
    }
}