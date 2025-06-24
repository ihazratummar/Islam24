package com.hazrat.ui.theme

import android.content.ContextWrapper
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
    CompositionLocalProvider(
        LocalAppDimens provides dimens
    ) {
        content()
    }

}


val LocalWrappedContext = compositionLocalOf<ContextWrapper> {
    error("No wrapped context provided")
}

val LocalAppDimens = compositionLocalOf {
    CompactDimens
}