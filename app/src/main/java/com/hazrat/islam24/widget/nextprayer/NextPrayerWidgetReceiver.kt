package com.hazrat.islam24.widget.nextprayer

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NextPrayerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = NextPrayerWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // First widget instance placed → start 1-minute alarm refresh
        NextPrayerWidget.scheduleMinuteRefresh(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Last widget instance removed → stop the alarm
        NextPrayerWidget.cancelMinuteRefresh(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            ACTION_REFRESH_WIDGET -> {
                val pendingResult = try { goAsync() } catch (_: Exception) { null }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        NextPrayerWidget().updateAll(context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult?.finish()
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_REFRESH_WIDGET = "com.hazrat.islam24.ACTION_REFRESH_PRAYER_WIDGET"
    }
}
