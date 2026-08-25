package com.hazrat.islam24.widget.calendar

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HijriCalendarWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = HijriCalendarWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            ACTION_REFRESH_CALENDAR_WIDGET -> {
                CoroutineScope(Dispatchers.Default).launch {
                    HijriCalendarWidget().updateAll(context)
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        CoroutineScope(Dispatchers.Default).launch {
            HijriCalendarWidget().updateAll(context)
        }
    }

    companion object {
        const val ACTION_REFRESH_CALENDAR_WIDGET = "com.hazrat.islam24.widget.ACTION_REFRESH_CALENDAR_WIDGET"
    }
}
