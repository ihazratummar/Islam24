package com.hazrat.islam24.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-01-2025
 */


class WuzuReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WuzuWidget()
}
