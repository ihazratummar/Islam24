package com.hazrat.notification

/**
 * @author Hazrat Ummar Shaikh
 */


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.hazrat.database.dao.prayer.PrayerSettingDao
import com.hazrat.model.Prayer
import java.util.Calendar

class PrayerAlarmScheduler(
    private val context: Context,
    private val prayerSettingDao: PrayerSettingDao
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(
        prayer: Prayer,
        prayerTime: Long,
        titleContent: String,
        requestCode: Int
    ) {
        cancelAlarm(requestCode)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = prayerTime
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        Log.d("PrayerAlarmScheduler", "Setting alarm for: $titleContent at ${calendar.time}")

        val intent = Intent(context, PrayerTimeReceiver::class.java).apply {
            putExtra("prayer_key", prayer.key)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                Log.w("PrayerAlarmScheduler", "Exact alarms not allowed, falling back to inexact.")
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            Log.e("PrayerAlarmScheduler", "SecurityException while scheduling alarm", e)
            // Final fallback
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
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

    suspend fun setPrayerAlarm(prayerName: Prayer, prayerTime: Long, preAlertMinutes: Int? = null) {
        val prayerSetting =  prayerSettingDao.getSettings()
        val offset = preAlertMinutes ?: when(prayerName){
            Prayer.FAJR -> prayerSetting?.notificationSettingsJson?.fajr?.offsetMinutes ?: 0
            Prayer.DHUHR -> prayerSetting?.notificationSettingsJson?.dhuhr?.offsetMinutes ?: 0
            Prayer.ASR -> prayerSetting?.notificationSettingsJson?.asr?.offsetMinutes ?: 0
            Prayer.MAGHRIB -> prayerSetting?.notificationSettingsJson?.maghrib?.offsetMinutes ?: 0
            Prayer.ISHA -> prayerSetting?.notificationSettingsJson?.isha?.offsetMinutes ?: 0
        }
        val adjustedTime = prayerTime - (offset * 60 * 1000L)
        setAlarm(
            prayerTime = adjustedTime,
            titleContent = prayerName.notificationTitle,
            requestCode = prayerName.notificationCode,
            prayer = prayerName
        )
    }

    /**
     * Reschedules all prayer alarms for the next 24 hours.
     * It intelligently picks today's or tomorrow's time based on the current time
     * and accounts for pre-alert offsets (e.g. 5 min before).
     */
    fun rescheduleAll(
        today: com.hazrat.model.MinimalPrayerData,
        tomorrow: com.hazrat.model.MinimalPrayerData,
        enabledPrayers: Set<Prayer>,
        preAlertOffsets: Map<Prayer, Int> = emptyMap()
    ) {
        val now = System.currentTimeMillis()
        Prayer.entries.forEach { prayer ->
            if (enabledPrayers.contains(prayer)) {
                val offset = preAlertOffsets[prayer] ?: 0
                val offsetMillis = offset * 60 * 1000L

                val todayAdjustedTime = today.getPrayerTime(prayer) - offsetMillis
                val tomorrowAdjustedTime = tomorrow.getPrayerTime(prayer) - offsetMillis

                val targetAdjustedTime = if (todayAdjustedTime > now + 1000) todayAdjustedTime else tomorrowAdjustedTime
                setAlarm(
                    prayerTime = targetAdjustedTime,
                    titleContent = prayer.notificationTitle,
                    requestCode = prayer.notificationCode,
                    prayer = prayer
                )
            } else {
                cancelAlarm(prayer.notificationCode)
            }
        }
    }


    private fun canScheduleExactAlarms(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}
