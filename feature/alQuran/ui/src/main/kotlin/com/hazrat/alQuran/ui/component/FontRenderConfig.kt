package com.hazrat.alQuran.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.hazrat.ui.theme.IndoPakFontFamily
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.UthmanicHafs
import com.hazrat.ui.theme.UthmanicNew

/**
 * Dedicated per-font rendering configuration.
 * Encapsulates font family, typography feature flags, spacing metrics, and Tajweed color toggles.
 *
 * @author hazratummar
 */
data class FontRenderConfig(
    val fontFamily: FontFamily,
    val fontFeatureSettings: String,
    val lineMultiplier: Float,
    val letterSpacing: TextUnit,
    val enableTajweedColors: Boolean
)

@Composable
fun rememberFontRenderConfig(selectedFont: String): FontRenderConfig {
    return remember(selectedFont) {
        when (selectedFont) {
            "INDOPAK" -> FontRenderConfig(
                fontFamily = IndoPakFontFamily,
                fontFeatureSettings = "",
                lineMultiplier = 2.5f,
                letterSpacing = 1.0.sp, // Expanded letter spacing to eliminate squeeze
                enableTajweedColors = false // Plain uncolored text for IndoPak
            )
            "UTHMANIC" -> FontRenderConfig(
                fontFamily = UthmanicNew, // Modern OpenType Uthmanic script (uthamnic_new.otf) without baked-in dotted circles!
                fontFeatureSettings = "",
                lineMultiplier = 2.2f,
                letterSpacing = 0.4.sp, // Comfortable spacing for Medina Uthmanic script
                enableTajweedColors = false // Plain uncolored text for Uthmanic font as requested!
            )
            else -> FontRenderConfig(
                fontFamily = ScheherazadeFontFamily,
                fontFeatureSettings = "cv62",
                lineMultiplier = 2.0f,
                letterSpacing = 0.sp, // SCHEHERAZADE baseline (100% UNTOUCHED)
                enableTajweedColors = true // SCHEHERAZADE baseline (100% UNTOUCHED)
            )
        }
    }
}
