package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.core.component.KoinComponent

/**
 * Receiver responsible for rescheduling all alarms when system state changes
 * (Timezone change, Time set, App update, exact alarm permission changed, etc.)
 */
class RescheduleReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        Log.d("RescheduleReceiver", "Received action: $action. Enqueueing Prayer & Zakat reschedule workers.")

        PrayerRescheduleWorker.enqueue(context)
        ZakatRescheduleWorker.enqueue(context)
    }
}
