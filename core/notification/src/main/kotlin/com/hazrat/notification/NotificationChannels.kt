package com.hazrat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

/**
 * @author Hazrat Ummar Shaikh
 */

class NotificationChannels (
    private val context: Context
) {

    fun createNotificationChannels() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val fajrChannel = NotificationChannel(
            NotificationConstant.FAJR_CHANNEL_ID,
            NotificationConstant.FAJR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val dhuhrChannel = NotificationChannel(
            NotificationConstant.DHUHR_CHANNEL_ID,
            NotificationConstant.DHUHR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val asrChannel = NotificationChannel(
            NotificationConstant.ASR_CHANNEL_ID,
            NotificationConstant.ASR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val maghribChannel = NotificationChannel(
            NotificationConstant.MAGHRIB_CHANNEL_ID,
            NotificationConstant.MAGHRIB_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val ishaChannel = NotificationChannel(
            NotificationConstant.ISHA_CHANNEL_ID,
            NotificationConstant.ISHA_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }


        manager.createNotificationChannel(fajrChannel)
        manager.createNotificationChannel(dhuhrChannel)
        manager.createNotificationChannel(asrChannel)
        manager.createNotificationChannel(maghribChannel)
        manager.createNotificationChannel(ishaChannel)
    }
}
