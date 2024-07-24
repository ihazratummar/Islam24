package com.hazrat.islam24.core.presentation.prayertime

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.main.mainActivity.MainViewModel


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PrayerTimer(viewModel, navController)
    }
}