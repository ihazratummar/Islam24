package com.hazrat.islam24.Receiver

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.database.PrayerDatabase
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.notification.MediaPlayerHelper
import com.hazrat.islam24.notification.NotificationConstant.ASR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.ASR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.ASR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.ASR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.FAJR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.FAJR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.FAJR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.FAJR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.ISHA_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.ISHA_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.ISHA_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.ISHA_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_TITLE_KEY
import com.hazrat.islam24.notification.PrayerAlarmManager
import com.hazrat.islam24.util.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.islam24.util.Constants.SELECTED_ATHANS_SUB_FOLDER_NAME
import com.hazrat.islam24.util.datastore.LocalDataStore
import com.hazrat.islam24.util.datastore.NotificationType
import com.hazrat.islam24.util.datastore.PrayerName
import com.hazrat.islam24.util.fetchPrayerTimeForNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PrayerTimeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var prayerAlarmManager: PrayerAlarmManager

    @Inject
    lateinit var prayerTimeDatabase: PrayerDatabase

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    @Inject
    lateinit var dataStore: LocalDataStore

    private val prayerInfo = mapOf(
        PrayerName.FAJR to Triple(FAJR_CHANNEL_ID, FAJR_TITLE_KEY, FAJR_MESSAGE_KEY),
        PrayerName.DHUHR to Triple(DHUHR_CHANNEL_ID, DHUHR_TITLE_KEY, DHUHR_MESSAGE_KEY),
        PrayerName.ASR to Triple(ASR_CHANNEL_ID, ASR_TITLE_KEY, ASR_MESSAGE_KEY),
        PrayerName.MAGHRIB to Triple(MAGHRIB_CHANNEL_ID, MAGHRIB_TITLE_KEY, MAGHRIB_MESSAGE_KEY),
        PrayerName.ISHA to Triple(ISHA_CHANNEL_ID, ISHA_TITLE_KEY, ISHA_MESSAGE_KEY)
    )

    override fun onReceive(context: Context, intent: Intent?) {
        val prayerNameKey = intent?.getStringExtra("prayerName") ?: return
        val prayerName = when (prayerNameKey) {
            FAJR_TITLE_CONTENT -> PrayerName.FAJR
            DHUHR_TITLE_CONTENT -> PrayerName.DHUHR
            ASR_TITLE_CONTENT -> PrayerName.ASR
            MAGHRIB_TITLE_CONTENT -> PrayerName.MAGHRIB
            ISHA_TITLE_CONTENT -> PrayerName.ISHA
            else -> return
        }

        val (channelId, titleKey, messageKey) = prayerInfo[prayerName] ?: return

        fetchPrayerTimeForNotification(prayerName = prayerName, prayerDatabase = prayerTimeDatabase) { prayerTime ->
            prayerAlarmManager.setPrayerAlarm(prayerName, prayerTime)
            Log.d("PrayerTimeReceiver", "Scheduled alarm for $prayerName at $prayerTime")
        }

        val title = intent.getStringExtra(titleKey) ?: prayerName.name
        val message = intent.getStringExtra(messageKey) ?: "It's time for ${prayerName.name} prayer!"
        val notificationId = prayerName.ordinal + 1000
        createAndShowNotification(context, channelId, title, message, prayerName, notificationId)

        val azanSound = getAzanSound(context, prayerName)
        playNotificationSound(prayerName, azanSound)
    }

    private fun createAndShowNotification(
        context: Context,
        channelId: String,
        title: String,
        message: String,
        prayerName: PrayerName,
        notificationId: Int
    ) {
        val notification = createNotification(context, channelId, title, message, prayerName)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(notificationId, notification.build())
        }
    }

    private fun createNotification(
        context: Context,
        channelId: String,
        title: String,
        message: String,
        prayerName: PrayerName
    ): NotificationCompat.Builder {
        val notificationClickIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationClickIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val muteIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, MuteReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val pattern = longArrayOf(0, 500, 1000)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.naviconhome)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(muteIntent)
            .setVibrate(pattern)

        val notificationType = runBlocking {
            dataStore.getPrayerNotificationType(prayerName).firstOrNull() ?: NotificationType.DEFAULT
        }
        Log.d("NotificationTypeCheck", "Notification type for $prayerName: $notificationType")

        // Add mute action if the notification type is AZAN
        if (notificationType == NotificationType.AZAN) {
            builder.addAction(
                R.drawable.volume,
                "Mute",
                muteIntent
            )
        }

        if (notificationType != NotificationType.SILENT) {
            val pattern = longArrayOf(0, 500, 1000)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            builder.setSound(defaultSoundUri)
            builder.setVibrate(pattern)
        } else {
            Log.d("NotificationTypeCheck", "Notification is SILENT for $prayerName")
        }
        return builder
    }

    private fun getAzanSound(context: Context, prayerName: PrayerName): String {
        return when (prayerName) {
            PrayerName.FAJR -> "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$SELECTED_ATHANS_SUB_FOLDER_NAME/fajrAzan.mp3"
            PrayerName.DHUHR -> "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$SELECTED_ATHANS_SUB_FOLDER_NAME/dhurAzan.mp3"
            PrayerName.ASR -> "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$SELECTED_ATHANS_SUB_FOLDER_NAME/asrAzan.mp3"
            PrayerName.MAGHRIB -> "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$SELECTED_ATHANS_SUB_FOLDER_NAME/maghribAzan.mp3"
            PrayerName.ISHA -> "${context.filesDir}/$PARENT_FOLDER_NAME_DOWNLOAD/$SELECTED_ATHANS_SUB_FOLDER_NAME/ishaAzan.mp3"
        }
    }

    private fun playNotificationSound(prayerName: PrayerName, azanSound: String) {
        val notificationType = runBlocking {
            dataStore.getPrayerNotificationType(prayerName).firstOrNull() ?: NotificationType.DEFAULT
        }
        Log.d("NotificationTypeCheck", "Notification type for $prayerName: $notificationType")

        when (notificationType) {
            NotificationType.DEFAULT -> {
                mediaPlayerHelper.prepareDefault()
                mediaPlayerHelper.start()
            }

            NotificationType.AZAN -> {
                mediaPlayerHelper.prepareAzanNotification(azanSound)
                mediaPlayerHelper.startAzan()
            }

            NotificationType.SILENT -> {
                Log.d("PrayerAlarmStart", "$prayerName is set to Silent - No sound played")
            }
        }
    }
}
