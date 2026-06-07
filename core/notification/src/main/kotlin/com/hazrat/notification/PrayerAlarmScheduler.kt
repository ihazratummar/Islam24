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
import com.hazrat.model.Prayer
import java.util.Calendar

class PrayerAlarmScheduler(
    private val context: Context
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

    fun setPrayerAlarm(prayerName: Prayer, prayerTime: Long) {
        setAlarm(
            prayerTime = prayerTime,
            titleContent = prayerName.notificationTitle,
            requestCode = prayerName.notificationCode,
            prayer = prayerName
        )
    }

    /**
     * Reschedules all prayer alarms for the next 24 hours.
     * It intelligently picks today's or tomorrow's time based on the current time.
     */
    fun rescheduleAll(
        today: com.hazrat.model.MinimalPrayerData,
        tomorrow: com.hazrat.model.MinimalPrayerData,
        enabledPrayers: Set<Prayer>
    ) {
        val now = System.currentTimeMillis()
        Prayer.entries.forEach { prayer ->
            if (enabledPrayers.contains(prayer)) {
                val todayTime = today.getPrayerTime(prayer)
                val tomorrowTime = tomorrow.getPrayerTime(prayer)

                val targetTime = if (todayTime > now + 1000) todayTime else tomorrowTime
                setPrayerAlarm(prayer, targetTime)
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
