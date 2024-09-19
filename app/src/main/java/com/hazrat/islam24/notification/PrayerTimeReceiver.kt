package com.hazrat.islam24.notification

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.database.PrayerDatabase
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.notification.NotificationConts.ASR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.ASR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.ASR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.ASR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.DHUHR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.DHUHR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.DHUHR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.DHUHR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.FAJR_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.FAJR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.FAJR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.FAJR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.ISHA_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.ISHA_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.ISHA_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.ISHA_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_CHANNEL_ID
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_TITLE_KEY
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @author Hazrat Ummar Shaikh
 */

@AndroidEntryPoint
class PrayerTimeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var prayerAlarmManager: PrayerAlarmManager

    @Inject
    lateinit var prayerTimeDatabase: PrayerDatabase

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("PrayerTimeReceiver", "onReceive called with action: ${intent?.action}")
        val prayerName = intent?.getStringExtra("prayerName")
        when (prayerName) {
            FAJR_TITLE_CONTENT -> {
                fetchFajrPrayerTime { fajrTimeFromDatabase ->
                    prayerAlarmManager.setFajrPrayerAlarm(fajrTimeFromDatabase)
                    Log.d("PrayerTimeReceiver", "Scheduled alarm for Fajr $fajrTimeFromDatabase")
                }
                //fajr
                val fajrTitle = intent.getStringExtra(FAJR_TITLE_KEY) ?: "Fajr"
                val fajrMessage =
                    intent.getStringExtra(FAJR_MESSAGE_KEY) ?: "It's time for Fajr prayer!"
                val fajrNotification =
                    createNotification(context, FAJR_CHANNEL_ID, fajrTitle, fajrMessage)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(1001, fajrNotification.build())
                }
            }

            DHUHR_TITLE_CONTENT -> {
                fetchDhuhrPrayerTime {
                    fetchDhuhrPrayerTime { dhuhrTime ->
                        prayerAlarmManager.setDhuhrPrayerAlarm(dhuhrTime)
                        Log.d("PrayerTimeReceiver", "Scheduled alarm for Dhuhr $dhuhrTime")
                    }
                }
                //Dhuhr
                val dhuhrTitle = intent.getStringExtra(DHUHR_TITLE_KEY) ?: "Dhuhr"
                val dhuhrMessage =
                    intent.getStringExtra(DHUHR_MESSAGE_KEY) ?: "It's time for Dhuhr prayer!"
                val dhuhrNotification =
                    createNotification(context, DHUHR_CHANNEL_ID, dhuhrTitle, dhuhrMessage)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(1002, dhuhrNotification.build())
                }
            }

            ASR_TITLE_CONTENT -> {
                fetchAsrPrayerTime { asrTime ->
                    prayerAlarmManager.setAsrPrayerAlarm(asrTime)
                    Log.d("PrayerTimeReceiver", "Scheduled alarm for asr $asrTime")
                }
                //Asr
                val asrTitle = intent.getStringExtra(ASR_TITLE_KEY) ?: "Asr"
                val asrMessage =
                    intent.getStringExtra(ASR_MESSAGE_KEY) ?: "It's time for Asr prayer!"
                val asrNotification =
                    createNotification(context, ASR_CHANNEL_ID, asrTitle, asrMessage)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(1003, asrNotification.build())
                }
            }

            MAGHRIB_TITLE_CONTENT -> {
                fetchMaghribPrayerTime { maghribTime ->
                    prayerAlarmManager.setMaghribPrayerAlarm(maghribTime)
                    Log.d("PrayerTimeReceiver", "Scheduled alarm for Maghrib $maghribTime")
                }
                //Maghrib
                val maghribTitle = intent.getStringExtra(MAGHRIB_TITLE_KEY) ?: "Maghrib"
                val maghribMessage =
                    intent.getStringExtra(MAGHRIB_MESSAGE_KEY) ?: "It's time for Maghrib prayer!"
                val maghribNotification = createNotification(
                    context, MAGHRIB_CHANNEL_ID, maghribTitle, maghribMessage
                )
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(1004, maghribNotification.build())
                }
            }

            ISHA_TITLE_CONTENT -> {
                fetchIshaPrayerTime { ishaTime ->
                    prayerAlarmManager.setIshaPrayerAlarm(ishaTime)
                    Log.d("PrayerTimeReceiver", "Scheduled alarm for Isha $ishaTime")
                }
                //Isha
                val ishaTitle = intent.getStringExtra(ISHA_TITLE_KEY) ?: "Isha"
                val ishaMessage =
                    intent.getStringExtra(ISHA_MESSAGE_KEY) ?: "It's time for Isha prayer!"
                val ishaNotification = createNotification(
                    context, ISHA_CHANNEL_ID, ishaTitle, ishaMessage
                )
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(1005, ishaNotification.build())
                }
            }
        }


    }

    private fun createNotification(
        context: Context,
        channelId: String,
        title: String,
        message: String
    ): NotificationCompat.Builder {
        val notificationClickIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationClickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_splash)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }


    private fun fetchFajrPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime =
                prayerTimeDatabase.prayerTimeDao().getFajrTimeForTheDay(getCurrentDay())
            callback(prayerTime)
        }
    }

    private fun fetchDhuhrPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime =
                prayerTimeDatabase.prayerTimeDao().getDhuhrTimeForTheDay(getCurrentDay())
            callback(prayerTime)
        }
    }

    private fun fetchAsrPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime = prayerTimeDatabase.prayerTimeDao().getAsrTimeForTheDay(getCurrentDay())
            callback(prayerTime)
        }
    }

    private fun fetchMaghribPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime = prayerTimeDatabase.prayerTimeDao().getMaghribTimeForTheDay(
                getCurrentDay()
            )
            callback(prayerTime)
        }
    }

    private fun fetchIshaPrayerTime(callback: (Long) -> Unit) {
        thread {
            val prayerTime =
                prayerTimeDatabase.prayerTimeDao().getIshaTimeForTheDay(getCurrentDay())
            callback(prayerTime)
        }
    }
}