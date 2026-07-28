package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver triggered on system boot to restore and reschedule all alarms.
 * NOTE: Does NOT implement KoinComponent to avoid triggering Firebase/datatransport
 * initialization from BOOT_COMPLETED, which would cause restricted foreground service warnings.
 *
 * @author Hazrat Ummar Shaikh
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            PrayerRescheduleWorker.enqueue(context)
            ZakatRescheduleWorker.enqueue(context)
        }
    }
}
