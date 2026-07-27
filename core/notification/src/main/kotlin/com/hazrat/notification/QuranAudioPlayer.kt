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
    var currentFilePath: String? = null
        private set
    var currentSpeed: Float = 1.0f
        private set

    fun playFile(
        file: File,
        speed: Float = currentSpeed,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) {
        currentSpeed = speed

        // If the SAME file is already loaded in mediaPlayer, simply RESUME from exact byte/millisecond position!
        if (currentFilePath == file.absolutePath && mediaPlayer != null) {
            resume()
            setSpeedInternal(speed)
            return
        }

        // New Ayah file: stop old, prepare new
        stop()
        currentFilePath = file.absolutePath

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.fromFile(file))
                prepare()
                setSpeedInternal(speed)
                setOnCompletionListener {
                    currentFilePath = null
                    onCompletion()
                }
                setOnErrorListener { _, what, extra ->
                    currentFilePath = null
                    onError("MediaPlayer error ($what, $extra)")
                    true
                }
                start()
            }
        } catch (e: Exception) {
            currentFilePath = null
            onError(e.localizedMessage ?: "Failed to play audio")
        }
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
