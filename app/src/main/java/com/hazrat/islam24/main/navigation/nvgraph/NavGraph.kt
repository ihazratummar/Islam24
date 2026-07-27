package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.hazrat.islam24.main.navigation.AppNavigator

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    isHapticFeedback: Boolean = false
) {
    AppNavigator(
        isHapticFeedback = isHapticFeedback
    )
}