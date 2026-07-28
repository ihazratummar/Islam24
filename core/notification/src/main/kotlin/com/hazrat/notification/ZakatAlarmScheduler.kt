package com.hazrat.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 * Enterprise-grade AlarmScheduler for scheduling exact/background Zakat Hawl reminders.
 * Supports background execution and survives app restart / system reboot.
 *
 * @author Hazrat Ummar Shaikh
 */
class ZakatAlarmScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleZakatReminder(
        zakatId: String,
        triggerTimeMillis: Long,
        zakatAmount: Double
    ) {
        if (triggerTimeMillis <= System.currentTimeMillis()) {
            Log.w("ZakatAlarmScheduler", "Trigger time is in the past ($triggerTimeMillis). Skipping schedule.")
            return
        }

        val requestCode = zakatId.hashCode()
        cancelZakatReminder(zakatId)

        Log.d("ZakatAlarmScheduler", "Scheduling Zakat reminder for ID: $zakatId at $triggerTimeMillis (Amount: $zakatAmount)")

        val intent = Intent(context, ZakatNotificationReceiver::class.java).apply {
            putExtra(EXTRA_ZAKAT_ID, zakatId)
            putExtra(EXTRA_ZAKAT_AMOUNT, zakatAmount)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= 35) {
                // Android 15+: Use inexact alarm with a 15-minute window to avoid
                // restricted foreground service type restrictions with BOOT_COMPLETED
                val windowLengthMs = 15 * 60 * 1000L // 15 minutes
                alarmManager.setWindow(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    windowLengthMs,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            Log.e("ZakatAlarmScheduler", "SecurityException while scheduling Zakat alarm", e)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    }

    fun cancelZakatReminder(zakatId: String) {
        val requestCode = zakatId.hashCode()
        val intent = Intent(context, ZakatNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d("ZakatAlarmScheduler", "Cancelled existing Zakat reminder for ID: $zakatId")
        }
    }

    companion object {
        const val EXTRA_ZAKAT_ID = "extra_zakat_id"
        const val EXTRA_ZAKAT_AMOUNT = "extra_zakat_amount"
    }
}
