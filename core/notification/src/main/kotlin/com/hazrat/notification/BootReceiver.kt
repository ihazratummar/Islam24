package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.core.component.KoinComponent

/**
 * BroadcastReceiver triggered on system boot to restore and reschedule all alarms.
 *
 * @author Hazrat Ummar Shaikh
 */
class BootReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "BOOT_COMPLETED received. Enqueueing Prayer & Zakat reschedule workers.")
            PrayerRescheduleWorker.enqueue(context)
            ZakatRescheduleWorker.enqueue(context)
        }
    }
}
