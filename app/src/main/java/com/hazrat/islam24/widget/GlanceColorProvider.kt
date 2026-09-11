package com.hazrat.islam24.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.glance.unit.ColorProvider

/**
 * Public, lint-safe implementation of [ColorProvider] that directly implements
 * [ColorProvider] without invoking the restricted [androidx.glance.unit.ColorProviderKt.ColorProvider]
 * factory methods.
 *
 * @author hazratummar
 */
class GlanceColor(private val color: Color) : ColorProvider {
    override fun getColor(context: Context): Color = color
}

/**
 * Creates a [ColorProvider] for Glance widgets from a Jetpack Compose [Color].
 */
fun glanceColor(color: Color): ColorProvider = GlanceColor(color)
