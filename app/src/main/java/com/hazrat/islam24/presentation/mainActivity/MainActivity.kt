package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.hazrat.islam24.domain.repository.LocationRepository


class MainActivity: ComponentActivity() {
    private val locationRepository: LocationRepository by lazy { LocationRepository(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            locationRepository.getCurrentLocation()
        }
    }

}