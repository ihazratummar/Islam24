package com.hazrat.islam24.main.mainActivity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.service.LocationHandler
import com.hazrat.islam24.service.LocationManager
import com.hazrat.islam24.service.PermissionsManager
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.util.DataStorePreference
import com.hazrat.islam24.util.DataStorePreference.getThemeMode
import com.hazrat.islam24.util.changeLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.Locale
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

    // Permissions manager, initialized in onCreate
    private lateinit var permissionsManager: PermissionsManager

    // ViewModel for the activity
    private val viewModel by viewModels<MainViewModel>()
    private val appSettingViewModel by viewModels<AppSettingViewModel>()

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

        // Initialize PermissionsManager and handle location permissions
        permissionsManager = PermissionsManager(this)
        permissionsManager.onPermissionGranted = {
            locationManager.getLastKnownLocation()
        }
        permissionsManager.checkAndRequestLocationPermission()

        setContent {
            val appSettingState by appSettingViewModel.appSettingState.collectAsState()
            Islam24Theme(
                darkTheme = appSettingState.isDarkMode
            ) {
                NavGraph(
                    appSettingState = appSettingState,
                    appSettingEvent = appSettingViewModel::onAppSettingEvent

                )
            }
        }
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
