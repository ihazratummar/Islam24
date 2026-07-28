package com.hazrat.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receiver responsible for rescheduling all alarms when system state changes
 * (Timezone change, Time set, App update, exact alarm permission changed, etc.)
 * NOTE: Does NOT implement KoinComponent to avoid triggering Firebase/datatransport
 * initialization from broadcast receivers.
 */
class RescheduleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        PrayerRescheduleWorker.enqueue(context)
        ZakatRescheduleWorker.enqueue(context)
    }
}
