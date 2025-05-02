package com.hazrat.islam24.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.hazrat.islam24.util.CompactDimens
import com.hazrat.islam24.util.Dimens

@Composable
fun AppUtils(
    appDimens: Dimens,
    content:@Composable () -> Unit
) {
    val dimens = remember {
        appDimens
    }
    CompositionLocalProvider(LocalAppDimens provides dimens) {
        content()
    }

}


val LocalAppDimens = compositionLocalOf {
    CompactDimens
}