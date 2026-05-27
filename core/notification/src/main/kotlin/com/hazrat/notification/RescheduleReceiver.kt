package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.core.component.KoinComponent

/**
 * Receiver responsible for rescheduling all prayer alarms when system state changes
 * (Timezone change, Time set, App update, etc.)
 */
class RescheduleReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        Log.d("RescheduleReceiver", "Received action: $action")

        // Enterprise-grade: Just trigger the worker, don't do logic here.
        PrayerRescheduleWorker.enqueue(context)
    }
}
