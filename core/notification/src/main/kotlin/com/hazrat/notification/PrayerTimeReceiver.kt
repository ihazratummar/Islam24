package com.hazrat.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.hazrat.database.database.PrayerDatabase
import com.hazrat.datastore.NotificationType
import com.hazrat.datastore.UserDataStore
import com.hazrat.model.Prayer
import com.hazrat.ui.R
import com.hazrat.utils.Constants.PARENT_FOLDER_NAME_DOWNLOAD
import com.hazrat.utils.Constants.SELECTED_ATHANS_SUB_FOLDER_NAME
import com.hazrat.utils.DateUtil.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrayerTimeReceiver : BroadcastReceiver(), KoinComponent {


    private val notificationManager: NotificationManagerCompat by inject()
    private val prayerAlarmManager: PrayerAlarmScheduler by inject()
    private val prayerTimeDatabase: PrayerDatabase by inject()
    private val mediaPlayerHelper: MediaPlayerHelper by inject()
    private val dataStore: UserDataStore by inject()



    override fun onReceive(context: Context, intent: Intent?) {
        val prayerNameKey = intent?.getStringExtra("prayer_key") ?: return
        val prayer = Prayer.fromKey(key = prayerNameKey) ?: return

        val pendingResult = goAsync()
        runBlocking(Dispatchers.IO) {
            try {
                fetchPrayerTimeForNotification(
                    prayerName = prayer,
                    prayerDatabase = prayerTimeDatabase
                ) { prayerTime ->
                    prayerAlarmManager.setPrayerAlarm(prayer, prayerTime)
                    Log.d("PrayerTimeReceiver", "Scheduled next alarm for $prayer at $prayerTime")
                }

                val notificationId = prayer.ordinal + 1000
                createAndShowNotification(
                    context,
                    channelId = prayer.notificationChannelId,
                    title = prayer.notificationTitle,
                    message = prayer.notificationMessage,
                    prayerName = prayer,
                    notificationId = notificationId
                )

                handleAudioPlayback(context, prayer)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun handleAudioPlayback(context: Context, prayer: Prayer) {
        val notificationType = runBlocking {
            dataStore.getPrayerNotificationType(prayer).firstOrNull() ?: NotificationType.DEFAULT
        }
        Log.d("PrayerTimeReceiver", "Playback type for $prayer: $notificationType")

        when (notificationType) {
            NotificationType.DEFAULT -> {
                mediaPlayerHelper.prepareDefault()
                mediaPlayerHelper.start()
            }

            NotificationType.AZAN -> {
                AzanPlaybackService.start(context, prayer)
            }

            NotificationType.SILENT -> {
                Log.d("PrayerTimeReceiver", "$prayer is set to Silent")
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun createAndShowNotification(
        context: Context,
        channelId: String,
        title: String,
        message: String,
        prayerName: Prayer,
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
        prayerName: Prayer
    ): NotificationCompat.Builder {

        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "https://islam24.hazratdev.top/prayertime".toUri()
        )

        val muteIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, MuteReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val clickPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val pattern = longArrayOf(0, 500, 1000)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.naviconhome)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(clickPendingIntent)
            .setDeleteIntent(muteIntent)
            .setVibrate(pattern)

        val notificationType = runBlocking(Dispatchers.IO) {
            dataStore.getPrayerNotificationType(prayerName).firstOrNull()
                ?: NotificationType.DEFAULT
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
}


fun fetchPrayerTimeForNotification(
    prayerName: Prayer,
    prayerDatabase: PrayerDatabase,
    callback: (Long) -> Unit
): Unit = runBlocking(Dispatchers.IO) {
    val prayerTime = when (prayerName) {
        Prayer.FAJR -> prayerDatabase.prayerTimeDao().getFajrTimeForTheDay(getCurrentDate())
        Prayer.DHUHR -> prayerDatabase.prayerTimeDao()
            .getDhuhrTimeForTheDay(getCurrentDate())

        Prayer.ASR -> prayerDatabase.prayerTimeDao().getAsrTimeForTheDay(getCurrentDate())
        Prayer.MAGHRIB -> prayerDatabase.prayerTimeDao()
            .getMaghribTimeForTheDay(getCurrentDate())

        Prayer.ISHA -> prayerDatabase.prayerTimeDao().getIshaTimeForTheDay(getCurrentDate())
    }
    callback(prayerTime)
}