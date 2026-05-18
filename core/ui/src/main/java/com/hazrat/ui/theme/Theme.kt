package com.hazrat.ui.theme

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(

    // Brand
    primary = Primary60,
    onPrimary = Neutral99,

    primaryContainer = Primary95,
    onPrimaryContainer = Primary20,

    inversePrimary = Primary80,

    // Gold Accent
    secondary = Secondary50,
    onSecondary = Secondary10,

    secondaryContainer = Secondary95,
    onSecondaryContainer = Secondary30,

    // Warm Accent
    tertiary = Tertiary50,
    onTertiary = Neutral99,

    tertiaryContainer = Tertiary95,
    onTertiaryContainer = Tertiary30,

    // Error
    error = Error40,
    onError = Neutral99,

    errorContainer = Error95,
    onErrorContainer = Error20,

    // Main Background
    background = Neutral98,
    onBackground = Neutral10,

    // Surfaces
    surface = Neutral99,
    onSurface = Neutral10,

    surfaceVariant = Neutral95,
    onSurfaceVariant = NeutralVariant50,

    // Elevated Surfaces
    surfaceBright = Neutral99,
    surfaceDim = Neutral94,

    surfaceContainer = Neutral96,
    surfaceContainerHigh = Neutral95,
    surfaceContainerHighest = Neutral92,
    surfaceContainerLow = Neutral98,
    surfaceContainerLowest = Neutral99,

    // Misc
    surfaceTint = Primary60,

    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,

    outline = NeutralVariant80,
    outlineVariant = Neutral90,

    scrim = Neutral0
)
private val DarkColorScheme = darkColorScheme(

    // Brand
    primary = Primary80,
    onPrimary = Primary20,

    primaryContainer = Primary30,
    onPrimaryContainer = Primary95,

    inversePrimary = Primary40,

    // Gold Accent
    secondary = Secondary70,
    onSecondary = Secondary20,

    secondaryContainer = Secondary30,
    onSecondaryContainer = Secondary90,

    // Warm Accent
    tertiary = Tertiary70,
    onTertiary = Tertiary20,

    tertiaryContainer = Tertiary30,
    onTertiaryContainer = Tertiary95,

    // Error
    error = Error80,
    onError = Error20,

    errorContainer = Error30,
    onErrorContainer = Error95,

    // Main Background
    background = Neutral4,
    onBackground = Neutral90,

    // Surfaces
    surface = Neutral6,
    onSurface = Neutral90,

    surfaceVariant = Neutral12,
    onSurfaceVariant = NeutralVariant80,

    // Elevated surfaces
    surfaceBright = Neutral17,
    surfaceDim = Neutral4,

    surfaceContainer = Neutral10,
    surfaceContainerHigh = Neutral12,
    surfaceContainerHighest = Neutral17,
    surfaceContainerLow = Neutral6,
    surfaceContainerLowest = Neutral4,

    // Misc
    surfaceTint = Primary80,

    inverseSurface = Neutral90,
    inverseOnSurface = Neutral10,

    outline = NeutralVariant50,
    outlineVariant = Neutral30,

    scrim = Neutral0
)

/**
 * CompositionLocal used to provide [Dimens] throughout the hierarchy.
 */
val LocalAppDimens = compositionLocalOf {
    CompactDimens
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Islam24Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity? = LocalActivity.current,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    val windowSizeClass = activity?.let {
        calculateWindowSizeClass(it)
    }

    // Industry-standard approach: Derive tokens from window size class
    val (appDimens, typography) = when (windowSizeClass?.widthSizeClass) {
        WindowWidthSizeClass.Compact -> CompactDimens to CompactTypography
        WindowWidthSizeClass.Medium -> MediumDimens to MediumTypography
        WindowWidthSizeClass.Expanded -> ExpandedDimens to ExpandedTypography
        else -> CompactDimens to CompactTypography
    }

    // Custom design tokens (Gradients, specialized colors)
    val customColors = remember(darkTheme) {
        if (darkTheme) DarkCustomColors else LightCustomColors
    }

    // System bars management
    if (!view.isInEditMode && activity != null) {
        SideEffect {
            val window = activity.window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalAppDimens provides appDimens,
        LocalCustomColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
val dimens
    @Composable
    get() = LocalAppDimens.current

val customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current
