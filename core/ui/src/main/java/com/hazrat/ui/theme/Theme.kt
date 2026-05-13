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
    primary = Primary60,
    onPrimary = Neutral99,
    primaryContainer = Primary90,
    onPrimaryContainer = Primary10,
    inversePrimary = Primary80,
    secondary = Secondary50,
    onSecondary = Secondary10,
    secondaryContainer = Secondary90,
    onSecondaryContainer = Secondary10,
    tertiary = Tertiary50,
    onTertiary = Tertiary99,
    tertiaryContainer = Tertiary90,
    onTertiaryContainer = Tertiary10,
    error = Error40,
    onError = Neutral99,
    errorContainer = Error90,
    onErrorContainer = Error10,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral96,
    onSurfaceVariant = NeutralVariant30,
    surfaceTint = Primary60,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,
    outline = NeutralVariant80,
    outlineVariant = Neutral90,
    scrim = Neutral0,
    surfaceBright = Neutral98,
    surfaceDim = Neutral94,
    surfaceContainer = Neutral96,
    surfaceContainerHigh = Neutral92,
    surfaceContainerHighest = Neutral90,
    surfaceContainerLow = Neutral95,
    surfaceContainerLowest = Neutral99
)
private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary20,
    primaryContainer = Primary30,
    onPrimaryContainer = Primary90,
    inversePrimary = Primary40,
    secondary = Secondary80,
    onSecondary = Secondary20,
    secondaryContainer = Secondary30,
    onSecondaryContainer = Secondary90,
    tertiary = Tertiary80,
    onTertiary = Tertiary20,
    tertiaryContainer = Tertiary30,
    onTertiaryContainer = Tertiary90,
    error = Error80,
    onError = Error20,
    errorContainer = Error30,
    onErrorContainer = Error90,
    background = Neutral6,
    onBackground = Neutral90,
    surface = Neutral6,
    onSurface = Neutral90,
    surfaceVariant = Neutral12,
    onSurfaceVariant = NeutralVariant80,
    surfaceTint = Primary80,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral10,
    outline = NeutralVariant50,
    outlineVariant = Neutral30,
    scrim = Neutral0,
    surfaceBright = Neutral12,
    surfaceDim = Neutral6,
    surfaceContainer = Neutral12,
    surfaceContainerHigh = Neutral17,
    surfaceContainerHighest = Neutral20,
    surfaceContainerLow = Neutral10,
    surfaceContainerLowest = Neutral4
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Islam24Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity = LocalActivity.current as Activity,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val window = calculateWindowSizeClass(activity = activity)
    val config = LocalConfiguration.current

    val typography: Typography
    val appDimens: Dimens
    val view = LocalView.current
    SideEffect {
        activity.window?.colorMode = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(activity.window!!, view).isAppearanceLightStatusBars =
            !darkTheme
    }
    when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360) {
                appDimens = CompactSmallDimens
                typography = CompactSmallTypography
            } else if (config.screenWidthDp < 599) {
                appDimens = CompactMediumDimens
                typography = CompactMediumTypography
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