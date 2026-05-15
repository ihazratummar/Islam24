package com.hazrat.ui.theme

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Islam24Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity? = LocalActivity.current as? Activity,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current

    val window = if (activity != null){
        calculateWindowSizeClass(activity = activity)
    }else{
        null
    }
    val config = LocalConfiguration.current

    val typography: Typography
    val appDimens: Dimens

    if (!view.isInEditMode){
        SideEffect {
            activity?.window?.colorMode = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(activity?.window!!, view).isAppearanceLightStatusBars =
                !darkTheme
        }
    }
    when (window?.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360) {
                appDimens = CompactSmallDimens
                typography = CompactSmallTypography
            } else {
                appDimens = CompactDimens
                typography = CompactTypography
            }
        }

        WindowWidthSizeClass.Medium -> {
            appDimens = MediumDimens
            typography = MediumTypography
        }

        else -> {
            appDimens = ExpandedDimens
            typography = ExpandedTypography
        }
    }
    AppUtils(
        appDimens = appDimens
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