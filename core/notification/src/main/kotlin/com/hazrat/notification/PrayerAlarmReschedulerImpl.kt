package com.hazrat.notification

import android.content.Context
import com.hazrat.domain.repository.PrayerAlarmRescheduler

class PrayerAlarmReschedulerImpl(
    private val context: Context
) : PrayerAlarmRescheduler {
    override fun rescheduleAlarms() {
        PrayerRescheduleWorker.enqueue(context)
    }
}
