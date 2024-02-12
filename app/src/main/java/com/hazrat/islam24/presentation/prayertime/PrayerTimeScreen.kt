package com.hazrat.islam24.presentation.prayertime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun PrayerTimeScreen(viewModel: PrayerTimeViewModel, navController:NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PrayerTimer(viewModel,navController)
    }
}