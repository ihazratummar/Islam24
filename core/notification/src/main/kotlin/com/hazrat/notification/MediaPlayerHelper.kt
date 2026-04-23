package com.hazrat.notification

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import com.hazrat.utils.isMp3FileValid
import java.io.File
import java.io.IOException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 23-12-2024
 */

class MediaPlayerHelper (
    val context: Context
) {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    private val pattern = longArrayOf(0, 500, 1000)

    private var isReceiverRegistered = false

    private val volumeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "android.media.VOLUME_CHANGED_ACTION"){
                stopAzan()
            }
        }
    }

    fun registerVolumeReceiver() {
        if (!isReceiverRegistered) { // Only register if not already registered
            context.registerReceiver(volumeReceiver, IntentFilter("android.media.VOLUME_CHANGED_ACTION"))
            isReceiverRegistered = true
        }
    }

    fun unregisterVolumeReceiver() {
        if (isReceiverRegistered) { // Only unregister if registered
            context.unregisterReceiver(volumeReceiver)
            isReceiverRegistered = false
        }
    }


    fun prepareAzanNotification(filePath: String){
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            isLooping = false
            setDataSource(filePath)
            prepare()
        }

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun prepareDefault(){
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            isLooping = false
            setDataSource(context, Settings.System.DEFAULT_NOTIFICATION_URI)
            prepare()
        }
    }

    fun playAzan(filePath: String) {
        Log.d("MediaPlayerHelper", "Trying to play MP3: $filePath")

        if (!isMp3FileValid(filePath)) {
            Log.e("MediaPlayerHelper", "Invalid MP3 file: $filePath")
            return
        }

        stopAzan()

        val file = File(filePath)
        if (!file.exists()) {
            Log.e("MediaPlayerHelper", "File not found: $filePath")
            return
        }

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) // Use MUSIC instead of SONIFICATION
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION) // Use MEDIA instead of NOTIFICATION
                    .build()
            )
            isLooping = false

            try {
                setDataSource(file.absolutePath) // Use absolute path
                prepare()
                start()
                registerVolumeReceiver()
                Log.d("MediaPlayerHelper", "Azan is playing...")
            } catch (e: IOException) {
                Log.e("MediaPlayerHelper", "Error preparing Azan: ${e.message}", e)
            }
        }
    }


    fun startAzan(){
        mediaPlayer?.start()
    }
    fun stopAzan() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
            }
            unregisterVolumeReceiver()
            mediaPlayer = null
            vibrator = null
        } catch (e: IllegalStateException) {
            // Handle the error, e.g., log it or take appropriate action
            Log.e("MediaPlayerHelper", "Error stopping Azan: ${e.message}")
        }
    }

    fun releaseAzan() {
        mediaPlayer?.release()
        mediaPlayer = null // Clear the reference to the MediaPlayer
    }

    @SuppressLint("ObsoleteSdkInt")
    fun start(){
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator = null
    }

}