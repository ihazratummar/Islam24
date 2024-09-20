package com.hazrat.islam24.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.hazrat.islam24.notification.NotificationConts.ASR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.ASR_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConts.DHUHR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.DHUHR_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConts.FAJR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.FAJR_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConts.ISHA_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.ISHA_CHANNEL_NAME
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_CHANNEL_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun notificationChannel() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val fajrChannel = NotificationChannel(
            FAJR_CHANNEL_ID,
            FAJR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val dhuhrChannel = NotificationChannel(
            DHUHR_CHANNEL_ID,
            DHUHR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val asrChannel = NotificationChannel(
            ASR_CHANNEL_ID,
            ASR_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val maghribChannel = NotificationChannel(
            MAGHRIB_CHANNEL_ID,
            MAGHRIB_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val ishaChannel = NotificationChannel(
            ISHA_CHANNEL_ID,
            ISHA_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(fajrChannel)
            manager.createNotificationChannel(dhuhrChannel)
            manager.createNotificationChannel(asrChannel)
            manager.createNotificationChannel(maghribChannel)
            manager.createNotificationChannel(ishaChannel)
        } else {
            manager.createNotificationChannel(fajrChannel)
            manager.createNotificationChannel(dhuhrChannel)
            manager.createNotificationChannel(asrChannel)
            manager.createNotificationChannel(maghribChannel)
            manager.createNotificationChannel(ishaChannel)
        }
    }
}
