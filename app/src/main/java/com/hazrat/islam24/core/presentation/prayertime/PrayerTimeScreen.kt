package com.hazrat.islam24.core.presentation.prayertime

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimeScreen(viewModel: MainViewModel, navController:NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        PrayerTimer(viewModel,navController)
    }
}