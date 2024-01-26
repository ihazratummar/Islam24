package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hazrat.islam24.domain.repository.LocationRepository


class MainActivity: ComponentActivity() {
    private val locationRepository: LocationRepository by lazy { LocationRepository(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            locationRepository.getCurrentLocation()
        }
    }

}