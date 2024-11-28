package com.hazrat.islam24.main.mainActivity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.notification.NotificationHelper
import com.hazrat.islam24.notification.PrayerAlarmManager
import com.hazrat.islam24.service.LocationHandler
import com.hazrat.islam24.service.LocationManager
import com.hazrat.islam24.service.PermissionsManager
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

// MainActivity.kt

/**
 * MainActivity is the entry point of the application, responsible for setting up
 * the UI, managing permissions, and initializing services.
 */

/**
 * Author: Hazrat Ummar Shaikh
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
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var prayerAlarmManager: PrayerAlarmManager

    @Inject
    lateinit var networkRepository: NetworkRepository

    // Permissions manager, initialized in onCreate
    private lateinit var permissionsManager: PermissionsManager

    private val appSettingViewModel by viewModels<AppSettingViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

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
        permissionsManager.requestPermission()
        permissionsManager.requestExactAlarmPermission()
        notificationHelper.notificationChannel()
        setContent {
            val appSettingState by appSettingViewModel.appSettingState.collectAsState()
            val networkStatus by networkRepository.networkStatus.collectAsState()
            Islam24Theme(
                darkTheme = appSettingState.isDarkMode
            ) {
                val zakatViewModel by viewModels<ZakatViewModel>()
                NavGraph(
                    appSettingState = appSettingState,
                    appSettingEvent = appSettingViewModel::onAppSettingEvent,
                    zakatViewModel = zakatViewModel
                )
                Log.d("MainActivityNetworkStatus", "$networkStatus")
            }
        }
        // Check for app updates
        updateManager.checkForAppUpdates(this)

        // Show location permission dialog if needed
        locationHandler.showLocationPermissionDialog(this)
        networkRepository.observeNetworkStatus()
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

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale.getDefault()
        val wrappedContext = LocaleHelper.wrap(newBase, locale)
        super.attachBaseContext(wrappedContext)
    }
}
