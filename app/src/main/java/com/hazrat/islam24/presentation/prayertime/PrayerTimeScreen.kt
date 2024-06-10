package com.hazrat.islam24.presentation.prayertime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.hazrat.islam24.presentation.mainActivity.MainViewModel


@Composable
fun PrayerTimeScreen(viewModel: MainViewModel, navController:NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PrayerTimer(viewModel,navController)
    }
}