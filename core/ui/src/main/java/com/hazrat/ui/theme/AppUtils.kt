package com.hazrat.ui.theme

import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun AppUtils(
    appDimens: Dimens,
    content: @Composable () -> Unit
) {
    val dimens = remember {
        appDimens
    }

    val gradients = if (isSystemInDarkTheme()) DarkCustomColors else LightCustomColors

    CompositionLocalProvider(
        LocalAppDimens provides dimens,
        LocalCustomColors provides gradients
    ) {
        content()
    }

}


val customColors
    @Composable
    get() = LocalCustomColors.current

val LocalWrappedContext = compositionLocalOf<ContextWrapper> {
    error("No wrapped context provided")
}

val LocalAppDimens = compositionLocalOf {
    CompactDimens
}