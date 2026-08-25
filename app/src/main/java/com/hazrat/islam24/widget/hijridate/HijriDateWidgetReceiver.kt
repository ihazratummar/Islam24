package com.hazrat.islam24.widget.hijridate

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HijriDateWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = HijriDateWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            ACTION_REFRESH_WIDGET -> {
                CoroutineScope(Dispatchers.IO).launch {
                    HijriDateWidget().updateAll(context)
                }
            }
        }
    }

    companion object {
        const val ACTION_REFRESH_WIDGET = "com.hazrat.islam24.ACTION_REFRESH_HIJRI_WIDGET"
    }
}
