package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hazrat.islam24.data.LocationDataHolder
import com.hazrat.islam24.util.LocationHandler
import com.hazrat.islam24.presentation.nvgraph.NavGraph
import com.hazrat.islam24.ui.theme.DarkGreen
import com.hazrat.islam24.ui.theme.Islam24Theme
import dagger.hilt.android.AndroidEntryPoint

//MainActivity.kt

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private lateinit var locationHandler: LocationHandler

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }
        locationHandler = LocationHandler(this)
        getCurrentLocation()

        setContent {
            Islam24Theme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkGreen)
                ) {
                    NavGraph(startDestination = viewModel.startDestination.value)
                }
            }
        }
    }

    private fun getCurrentLocation() {
        locationHandler.getCurrentLocation(
            onLocationReceived = { location ->
                // Save the location
                LocationDataHolder.saveLocation(location.latitude, location.longitude)

                // Do something with the location
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("MainActivity", "Location received: $latitude, $longitude")
            },
            onLocationError = {
                // Handle location error
                Log.e("MainActivity", "Error getting location")
            }
        )
    }
}