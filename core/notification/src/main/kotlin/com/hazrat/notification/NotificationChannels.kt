package com.hazrat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.hazrat.model.Prayer

/**
 * @author Hazrat Ummar Shaikh
 */

class NotificationChannels (
    private val context: Context
) {

    fun createNotificationChannels() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Azan Playback Channel (Foreground Service)
        val azanPlaybackChannel = NotificationChannel(
            AzanPlaybackService.CHANNEL_ID,
            "Azan Playback",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notification for active Azan playback"
            setSound(null, null)
            enableVibration(false)
        }
        manager.createNotificationChannel(azanPlaybackChannel)

        Prayer.entries.forEach { prayer ->
            val notificationChannel = NotificationChannel(
                prayer.notificationChannelId,
                prayer.toString(),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(null, null)
                enableVibration(true)
            }

            manager.createNotificationChannel(notificationChannel)
        }

    }
}
