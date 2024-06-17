package com.hazrat.islam24.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.hazrat.islam24.presentation.CompactDimens
import com.hazrat.islam24.presentation.CompactMediumDimens
import com.hazrat.islam24.presentation.CompactSmallDimens
import com.hazrat.islam24.presentation.ExpandedDimens
import com.hazrat.islam24.presentation.MediumDimens
import com.hazrat.islam24.presentation.mainActivity.MainActivity

private val DarkColorScheme = darkColorScheme(
    background = DarkGreen,
    primary = Green,
    error = DarkRed,
    surface = DarkGreen
    
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    background = LightGreen,
    error = LightRed,
    surface = DarkGreen,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Islam24Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    activity: Activity = LocalContext.current as MainActivity,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val window = calculateWindowSizeClass(activity = activity)
    val config  = LocalConfiguration.current

    var typography = CompactTypography
    var appDimens = CompactDimens

    when(window.widthSizeClass){
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360 ){
                appDimens = CompactSmallDimens
                typography = CompactSmallTypography
            }
            else if (config.screenWidthDp < 599 ){
                appDimens = CompactMediumDimens
                typography = CompactMediumTypography
            }else{
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
    AppUtils(appDimens =appDimens ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

val MaterialTheme.dimens
    @Composable
    get() = LocalAppDimens.current