package com.hazrat.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.hazrat.model.Prayer
import com.hazrat.ui.R
import com.hazrat.utils.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.utils.Constants.SELECTED_ATHANS_SUB_FOLDER_NAME
import org.koin.android.ext.android.inject

class AzanPlaybackService : Service() {

    private val mediaPlayerHelper: MediaPlayerHelper by inject()
    private var wakeLock: PowerManager.WakeLock? = null

    companion object {
        const val CHANNEL_ID = "azan_playback_channel"
        const val NOTIFICATION_ID = 9999
        const val ACTION_STOP_AZAN = "ACTION_STOP_AZAN"
        const val EXTRA_PRAYER_KEY = "prayer_key"

        fun start(context: Context, prayer: Prayer) {
            val intent = Intent(context, AzanPlaybackService::class.java).apply {
                putExtra(EXTRA_PRAYER_KEY, prayer.key)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        acquireWakeLock()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_AZAN) {
            stopAzan()
            return START_NOT_STICKY
        }

        val prayerKey = intent?.getStringExtra(EXTRA_PRAYER_KEY)
        val prayer = prayerKey?.let { Prayer.fromKey(it) }

        if (prayer != null) {
            val azanPath = "${filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$SELECTED_ATHANS_SUB_FOLDER_NAME/${prayer.azanFileName}.mp3"
            startForeground(NOTIFICATION_ID, createNotification(prayer), 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0)
            
            mediaPlayerHelper.playAzan(azanPath)
        } else {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun createNotification(prayer: Prayer): Notification {
        val stopIntent = Intent(this, AzanPlaybackService::class.java).apply {
            action = ACTION_STOP_AZAN
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Playing Azan")
            .setContentText("Now playing Azan for ${prayer.notificationTitle}")
            .setSmallIcon(R.drawable.naviconhome)
            .setOngoing(true)
            .addAction(R.drawable.volume, "Stop", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Azan Playback",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notification for active Azan playback"
                setSound(null, null) // Sound is handled by MediaPlayer
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun stopAzan() {
        mediaPlayerHelper.stopAzan()
        stopForeground(true)
        stopSelf()
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Islam24:AzanWakeLock").apply {
            acquire(10 * 60 * 1000L /*10 minutes*/)
        }
    }

    override fun onDestroy() {
        mediaPlayerHelper.stopAzan()
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
        super.onDestroy()
    }
}
