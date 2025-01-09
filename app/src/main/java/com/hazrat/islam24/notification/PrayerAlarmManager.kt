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
import com.hazrat.islam24.notification.NotificationConts.ASR_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.ASR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.ASR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.ASR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.DHUHR_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.DHUHR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.DHUHR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.DHUHR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.FAJR_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.FAJR_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.FAJR_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.FAJR_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.ISHA_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.ISHA_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.ISHA_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.ISHA_TITLE_KEY
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_MESSAGE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_MESSAGE_KEY
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_TITLE_CONTENT
import com.hazrat.islam24.notification.NotificationConts.MAGHRIB_TITLE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class PrayerAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
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
        Log.d("PrayerAlarmManager", "Alarm set for: $titleContent  ${calendar.time}")

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

    fun cancelFajrAlarm(){
        cancelAlarm(1)
    }
    fun cancelDhuhrAlarm(){
        cancelAlarm(2)
    }
    fun cancelAsrAlarm(){
        cancelAlarm(3)
    }
    fun cancelMaghribAlarm(){
        cancelAlarm(4)
    }
    fun cancelIshaAlarm(){
        cancelAlarm(5)
    }

    private fun cancelAlarm(
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
            Log.d("PrayerAlarmManager", "Existing prayer alarm cancelled $requestCode")
        }
    }

}


object NotificationConts {

    /*
    Intent Extras
     */

    //Fajr
    const val FAJR_TITLE_KEY = "fajr_title"
    const val FAJR_TITLE_CONTENT = "Fajr"
    const val FAJR_MESSAGE_KEY = "fajr_message"
    const val FAJR_MESSAGE_CONTENT = "It's time for Fajr prayer!"


    //Dhuhr
    const val DHUHR_TITLE_KEY = "dhuhr_title"
    const val DHUHR_TITLE_CONTENT = "Dhuhr"
    const val DHUHR_MESSAGE_KEY = "Dhuhr_message"
    const val DHUHR_MESSAGE_CONTENT = "It's time for Dhuhr prayer!"

    //Asr
    const val ASR_TITLE_KEY = "asr_title"
    const val ASR_TITLE_CONTENT = "Asr"
    const val ASR_MESSAGE_KEY = "asr_message"
    const val ASR_MESSAGE_CONTENT = "It's time for Asr prayer!"

    //Asr
    const val MAGHRIB_TITLE_KEY = "maghrib_title"
    const val MAGHRIB_TITLE_CONTENT = "Maghrib"
    const val MAGHRIB_MESSAGE_KEY = "maghrib_message"
    const val MAGHRIB_MESSAGE_CONTENT = "It's time for Maghrib prayer!"

    //iSHA
    const val ISHA_TITLE_KEY = "isha_title"
    const val ISHA_TITLE_CONTENT = "Isha"
    const val ISHA_MESSAGE_KEY = "isha_message"
    const val ISHA_MESSAGE_CONTENT = "It's time for Isha prayer!"
    /*
    Notificaion channels
     */
    //Fajr
    const val FAJR_CHANNEL_ID = "fajr_channel"
    const val FAJR_CHANNEL_NAME = "Fajr"
    //Dhuhr
    const val DHUHR_CHANNEL_ID = "dhuhr_channel"
    const val DHUHR_CHANNEL_NAME = "Dhuhr"

    //Asr
    const val ASR_CHANNEL_ID = "asr_channel"
    const val ASR_CHANNEL_NAME = "Asr"

    //MAGHRIB
    const val MAGHRIB_CHANNEL_ID = "maghrib_channel"
    const val MAGHRIB_CHANNEL_NAME = "Maghrib"

    //ISHA
    const val ISHA_CHANNEL_ID = "ish_channel"
    const val ISHA_CHANNEL_NAME = "Isha"

}