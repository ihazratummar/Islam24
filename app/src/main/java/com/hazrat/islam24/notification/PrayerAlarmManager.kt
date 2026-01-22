package com.hazrat.islam24.notification

/**
 * @author Hazrat Ummar Shaikh
 */


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hazrat.datastore.PrayerName
import com.hazrat.islam24.notification.NotificationConstant.ASR_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.ASR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.ASR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.ASR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.DHUHR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.FAJR_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.FAJR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.FAJR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.FAJR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.ISHA_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.ISHA_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.ISHA_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.ISHA_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConstant.MAGHRIB_TITLE_KEY
import com.hazrat.islam24.receiver.PrayerTimeReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class PrayerAlarmManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setFajrPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = FAJR_TITLE_KEY,
            titleContent = FAJR_TITLE_CONTENT,
            messageKey = FAJR_MESSAGE_KEY,
            messageContent = FAJR_MESSAGE_CONTENT,
            requestCode = 1
        )
    }

    fun setDhuhrPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = DHUHR_TITLE_KEY,
            titleContent = DHUHR_TITLE_CONTENT,
            messageKey = DHUHR_MESSAGE_KEY,
            messageContent = DHUHR_MESSAGE_CONTENT,
            requestCode = 2
        )
    }

    fun setAsrPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = ASR_TITLE_KEY,
            titleContent = ASR_TITLE_CONTENT,
            messageKey = ASR_MESSAGE_KEY,
            messageContent = ASR_MESSAGE_CONTENT,
            requestCode = 3
        )
    }

    fun setMaghribPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = MAGHRIB_TITLE_KEY,
            titleContent = MAGHRIB_TITLE_CONTENT,
            messageKey = MAGHRIB_MESSAGE_KEY,
            messageContent = MAGHRIB_MESSAGE_CONTENT,
            requestCode = 4
        )
    }

    fun setIshaPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = ISHA_TITLE_KEY,
            titleContent = ISHA_TITLE_CONTENT,
            messageKey = ISHA_MESSAGE_KEY,
            messageContent = ISHA_MESSAGE_CONTENT,
            requestCode = 5
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(
        prayerTime: Long,
        titleKey: String,
        titleContent: String,
        messageKey: String,
        messageContent: String,
        requestCode: Int
    ) {
        cancelAlarm(requestCode)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = prayerTime
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < System.currentTimeMillis() ) {
                cancelAlarm(requestCode)
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        Log.d("PrayerAlarmStart", "Alarm set for: $titleContent  ${calendar.time}")

        val intent = Intent(context, PrayerTimeReceiver::class.java).apply {
            putExtra(titleKey, titleContent)
            putExtra(messageKey, messageContent)
            putExtra("prayerName",titleContent )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(
        requestCode: Int
    ) {
        val intent = Intent(context, PrayerTimeReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d("PrayerAlarmStart", "Existing prayer alarm cancelled $requestCode")
        }
    }

    fun setPrayerAlarm(prayerName: PrayerName, prayerTime: Long) {
        when (prayerName) {
            PrayerName.FAJR -> setFajrPrayerAlarm(prayerTime)
            PrayerName.DHUHR -> setDhuhrPrayerAlarm(prayerTime)
            PrayerName.ASR -> setAsrPrayerAlarm(prayerTime)
            PrayerName.MAGHRIB -> setMaghribPrayerAlarm(prayerTime)
            PrayerName.ISHA -> setIshaPrayerAlarm(prayerTime)
        }
    }

}
