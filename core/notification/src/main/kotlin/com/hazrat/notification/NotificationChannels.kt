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

        Prayer.entries.forEach {prayer ->
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
