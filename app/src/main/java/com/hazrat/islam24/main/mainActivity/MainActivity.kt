package com.hazrat.islam24.main.mainActivity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.service.*
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.util.calculateQiblaDirection
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// MainActivity.kt

/**
 * MainActivity is the entry point of the application, responsible for setting up
 * the UI, managing permissions, and initializing services.
 */

/**
 * @author Hazrat Ummar Shaikh
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Dependency injected services
    @Inject
    lateinit var updateManager: UpdateManager

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var locationHandler: LocationHandler

    @Inject
    lateinit var compassSensorManager: CompassSensorManager

    // Permissions manager, initialized in onCreate
    private lateinit var permissionsManager: PermissionsManager

    // ViewModel for the activity
    private val viewModel by viewModels<MainViewModel>()

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Hide the action bar
        actionBar?.hide()

        // Set window decor to fit system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Install and configure splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }

        // Initialize PermissionsManager and handle location permissions
        permissionsManager = PermissionsManager(this)
        permissionsManager.onPermissionGranted = {
            locationManager.getLastKnownLocation()
        }
        permissionsManager.checkAndRequestLocationPermission()

        // Handle location updates
        locationManager.onLocationReceived = { location ->
            val qiblaDirection = calculateQiblaDirection(location.latitude, location.longitude).toFloat()
            Log.d("MainActivity Qibla", "New Qibla Direction: $qiblaDirection")
            viewModel.updateQiblaDirection(qiblaDirection)
        }

        // Handle compass direction changes
        compassSensorManager.onDirectionChanged = { direction ->
            Log.d("MainActivity Current", "New Current Direction: $direction")
            viewModel.updateCurrentDirection(direction)
        }

        // Set the content view with Jetpack Compose
        setContent {
            val qiblaState = viewModel.qiblaState.collectAsState()
            Islam24Theme {
                NavGraph(
                    startDestination = viewModel.startDestination.value,
                    qiblaDirection = qiblaState.value.qiblaDirection,
                    currentDirection = qiblaState.value.currentDirection
                )
            }
        }

        // Register compass sensor listeners
        compassSensorManager.registerListeners()

        // Check for app updates
        updateManager.checkForAppUpdates(this)

        // Show location permission dialog if needed
        locationHandler.showLocationPermissionDialog(this)
    }

    /**
     * Called when the activity will start interacting with the user.
     */
    override fun onResume() {
        super.onResume()
        updateManager.onResume(this)
    }

    /**
     * Perform any final cleanup before an activity is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        updateManager.onDestroy()
    }
}