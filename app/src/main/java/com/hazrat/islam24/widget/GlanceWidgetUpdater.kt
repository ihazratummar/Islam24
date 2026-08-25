package com.hazrat.islam24.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.hazrat.domain.repository.WidgetUpdater
import com.hazrat.islam24.widget.nextprayer.NextPrayerWidget
import com.hazrat.islam24.widget.nextprayer.NextPrayerWidgetReceiver

/**
 * Glance-based implementation of [WidgetUpdater].
 * Updates each valid widget instance individually, skipping stale/orphaned IDs
 * that would cause "No app widget info for X" crashes with [GlanceAppWidget.updateAll].
 */
class GlanceWidgetUpdater(private val context: Context) : WidgetUpdater {

    override suspend fun updateAllPrayerWidgets() {
        safeUpdateWidget(NextPrayerWidget(), NextPrayerWidgetReceiver::class.java)
    }

    private suspend fun safeUpdateWidget(
        widget: GlanceAppWidget,
        receiverClass: Class<*>
    ) {
        try {
            // Only update widget IDs that the system still considers valid
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, receiverClass)
            val systemIds = appWidgetManager.getAppWidgetIds(component)

            if (systemIds == null || systemIds.isEmpty()) return

            val glanceManager = GlanceAppWidgetManager(context)
            for (systemId in systemIds) {
                try {
                    val glanceId = glanceManager.getGlanceIdBy(systemId)
                    widget.update(context, glanceId)
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // Fallback: silent failure rather than crash
        }
    }
}
