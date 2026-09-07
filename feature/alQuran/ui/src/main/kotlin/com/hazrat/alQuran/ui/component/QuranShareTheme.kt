package com.hazrat.alQuran.ui.component

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Color gradients and themes for Quran Ayah share cards.
 * @author hazratummar
 */
enum class QuranShareTheme(
    val title: String,
    val startColor: Color,
    val endColor: Color,
    val accentColor: Color,
    val cardBackground: Color
) {
    EMERALD(
        title = "Emerald",
        startColor = Color(0xFF0F4C3A),
        endColor = Color(0xFF061F17),
        accentColor = Color(0xFFE5B869),
        cardBackground = Color(0xFF0B3327)
    ),
    SAPPHIRE(
        title = "Sapphire",
        startColor = Color(0xFF0F3254),
        endColor = Color(0xFF061729),
        accentColor = Color(0xFF64B5F6),
        cardBackground = Color(0xFF0B2138)
    ),
    AMETHYST(
        title = "Amethyst",
        startColor = Color(0xFF451952),
        endColor = Color(0xFF1B0721),
        accentColor = Color(0xFFE1BEE7),
        cardBackground = Color(0xFF2E0F38)
    ),
    OBSIDIAN(
        title = "Obsidian",
        startColor = Color(0xFF2B2B2B),
        endColor = Color(0xFF141414),
        accentColor = Color(0xFFE0E0E0),
        cardBackground = Color(0xFF1E1E1E)
    ),
    GOLDEN(
        title = "Golden",
        startColor = Color(0xFF4A3008),
        endColor = Color(0xFF1F1403),
        accentColor = Color(0xFFFFD54F),
        cardBackground = Color(0xFF332005)
    );

    val outerGradient: Brush
        get() = Brush.verticalGradient(listOf(startColor, endColor))
}
