package com.hazrat.notification

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import java.io.File

/**
 * Native Android MediaPlayer wrapper supporting exact millisecond/byte pause-and-resume,
 * playback speed adjustment, and completion callbacks.
 *
 * @author hazratummar
 */
class QuranAudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private var checkCompletionRunnable: Runnable? = null

    var currentFilePath: String? = null
        private set
    var currentSpeed: Float = 1.0f
        private set

    // Trim studio silence padding (Optimal user tested balance limit):
    // Leading ~70ms (preserves word start crystal clear)
    // Trailing ~450ms (trims studio tail for continuous audio flow)
    private val leadingSilenceMs = 70
    private val trailingSilenceMs = 450

    fun playFile(
        file: File,
        speed: Float = currentSpeed,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) {
        currentSpeed = speed

        // If the SAME file is already loaded, simply RESUME
        if (currentFilePath == file.absolutePath && mediaPlayer != null) {
            resume()
            setSpeedInternal(speed)
            return
        }

        stopEarlyCompletionCheck()
        currentFilePath = file.absolutePath

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            } else {
                mediaPlayer?.reset()
            }

            mediaPlayer?.apply {
                setDataSource(context, Uri.fromFile(file))
                prepare()
                setSpeedInternal(speed)

                // Skip leading studio silence padding if track is long enough
                if (duration > 600) {
                    seekTo(leadingSilenceMs)
                }

                var hasCompleted = false

                val triggerCompletion = {
                    if (!hasCompleted) {
                        hasCompleted = true
                        stopEarlyCompletionCheck()
                        currentFilePath = null
                        onCompletion()
                    }
                }

                setOnCompletionListener {
                    triggerCompletion()
                }

                setOnErrorListener { _, what, extra ->
                    stopEarlyCompletionCheck()
                    currentFilePath = null
                    onError("MediaPlayer error ($what, $extra)")
                    true
                }

                start()

                // Monitor trailing silence and trigger seamless transition early
                if (duration > 800) {
                    scheduleEarlyCompletionCheck(duration, triggerCompletion)
                }
            }
        } catch (e: Exception) {
            stopEarlyCompletionCheck()
            currentFilePath = null
            onError(e.localizedMessage ?: "Failed to play audio")
        }
    }

    private fun scheduleEarlyCompletionCheck(durationMs: Int, onComplete: () -> Unit) {
        stopEarlyCompletionCheck()
        val targetPos = durationMs - trailingSilenceMs
        checkCompletionRunnable = object : Runnable {
            override fun run() {
                try {
                    val player = mediaPlayer
                    if (player != null && player.isPlaying) {
                        if (player.currentPosition >= targetPos) {
                            onComplete()
                            return
                        }
                    }
                    handler.postDelayed(this, 30)
                } catch (_: Exception) {}
            }
        }
        handler.post(checkCompletionRunnable!!)
    }

    private fun stopEarlyCompletionCheck() {
        checkCompletionRunnable?.let { handler.removeCallbacks(it) }
        checkCompletionRunnable = null
    }

    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (_: Exception) {}
    }

    fun resume() {
        try {
            if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
                mediaPlayer?.start()
            }
        } catch (_: Exception) {}
    }

    fun stop() {
        stopEarlyCompletionCheck()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        currentFilePath = null
    }

    fun setSpeed(speed: Float) {
        currentSpeed = speed
        setSpeedInternal(speed)
    }

    private fun setSpeedInternal(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mediaPlayer != null) {
            try {
                val params = mediaPlayer?.playbackParams ?: PlaybackParams()
                params.speed = speed
                mediaPlayer?.playbackParams = params
            } catch (_: Exception) {}
        }
    }

    fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying == true
        } catch (_: Exception) {
            false
        }
    }
}
