package com.hazrat.islam24.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.hazrat.islam24.notification.NotificationConstant.ASR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.ASR_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConstant.FAJR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.FAJR_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConstant.ISHA_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.ISHA_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_CHANNEL_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class NotificationChannels @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun createNotificationChannels() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val fajrChannel = NotificationChannel(
            FAJR_CHANNEL_ID,
            FAJR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val dhuhrChannel = NotificationChannel(
            DHUHR_CHANNEL_ID,
            DHUHR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val asrChannel = NotificationChannel(
            ASR_CHANNEL_ID,
            ASR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val maghribChannel = NotificationChannel(
            MAGHRIB_CHANNEL_ID,
            MAGHRIB_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableVibration(true)
        }

        val ishaChannel = NotificationChannel(
            ISHA_CHANNEL_ID,
            ISHA_CHANNEL_NAME,
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
