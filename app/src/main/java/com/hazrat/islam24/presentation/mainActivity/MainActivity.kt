package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hazrat.islam24.presentation.navigation.nvgraph.NavGraph
import com.hazrat.islam24.service.CompassSensorManager
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.service.LocationHandler
import com.hazrat.islam24.service.LocationManager
import com.hazrat.islam24.service.PermissionsManager
import com.hazrat.islam24.util.calculateQiblaDirection
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//MainActivity.kt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var updateManager: UpdateManager

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var locationHandler: LocationHandler

    @Inject
    lateinit var compassSensorManager: CompassSensorManager

    private lateinit var permissionsManager: PermissionsManager

    private var qiblaDirection by mutableStateOf(0f)
    private var currentDirection by mutableStateOf(0f)

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }
        permissionsManager = PermissionsManager(this)
        permissionsManager.onPermissionGranted = {
            locationManager.getLastKnownLocation()
        }
        permissionsManager.checkAndRequestLocationPermission()

        locationManager.onLocationReceived = { location ->
            qiblaDirection = calculateQiblaDirection(location.latitude, location.longitude).toFloat()
        }

        compassSensorManager.onDirectionChanged = { direction ->
            currentDirection = direction
        }
        setContent {
            Islam24Theme {
                NavGraph(startDestination = viewModel.startDestination.value)
            }
        }
        updateManager.checkForAppUpdates(this)
        locationHandler.showLocationPermissionDialog(this)
    }



    override fun onResume() {
        super.onResume()
        updateManager.onResume(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        updateManager.onDestroy()
    }
}