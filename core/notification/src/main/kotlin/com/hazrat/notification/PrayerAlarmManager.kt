package com.hazrat.notification

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
import java.util.Calendar

class PrayerAlarmManager  (
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setFajrPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = NotificationConstant.FAJR_TITLE_KEY,
            titleContent = NotificationConstant.FAJR_TITLE_CONTENT,
            messageKey = NotificationConstant.FAJR_MESSAGE_KEY,
            messageContent = NotificationConstant.FAJR_MESSAGE_CONTENT,
            requestCode = 1
        )
    }

    fun setDhuhrPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = NotificationConstant.DHUHR_TITLE_KEY,
            titleContent = NotificationConstant.DHUHR_TITLE_CONTENT,
            messageKey = NotificationConstant.DHUHR_MESSAGE_KEY,
            messageContent = NotificationConstant.DHUHR_MESSAGE_CONTENT,
            requestCode = 2
        )
    }

    fun setAsrPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = NotificationConstant.ASR_TITLE_KEY,
            titleContent = NotificationConstant.ASR_TITLE_CONTENT,
            messageKey = NotificationConstant.ASR_MESSAGE_KEY,
            messageContent = NotificationConstant.ASR_MESSAGE_CONTENT,
            requestCode = 3
        )
    }

    fun setMaghribPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = NotificationConstant.MAGHRIB_TITLE_KEY,
            titleContent = NotificationConstant.MAGHRIB_TITLE_CONTENT,
            messageKey = NotificationConstant.MAGHRIB_MESSAGE_KEY,
            messageContent = NotificationConstant.MAGHRIB_MESSAGE_CONTENT,
            requestCode = 4
        )
    }

    fun setIshaPrayerAlarm(prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleKey = NotificationConstant.ISHA_TITLE_KEY,
            titleContent = NotificationConstant.ISHA_TITLE_CONTENT,
            messageKey = NotificationConstant.ISHA_MESSAGE_KEY,
            messageContent = NotificationConstant.ISHA_MESSAGE_CONTENT,
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
            putExtra("prayerName", titleContent)
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
