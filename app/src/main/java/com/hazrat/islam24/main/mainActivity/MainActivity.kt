package com.hazrat.islam24.main.mainActivity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.auth.presentation.forgetPassword.ForgetPasswordViewModel
import com.hazrat.islam24.auth.presentation.login.LoginViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileViewModel
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileDetailsViewModel
import com.hazrat.islam24.auth.presentation.signup.SingupViewModel
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.presentation.al_quran.QuranViewModel
import com.hazrat.islam24.core.presentation.common.rememberImageLoader
import com.hazrat.islam24.core.presentation.home.HomeViewModel
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.core.presentation.qibla.QiblaViewModel
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.notification.MediaPlayerHelper
import com.hazrat.islam24.notification.NotificationChannels
import com.hazrat.islam24.notification.PrayerAlarmManager
import com.hazrat.islam24.service.LocationHandler
import com.hazrat.islam24.service.LocationManager
import com.hazrat.islam24.service.PermissionsManager
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.ui.theme.Islam24Theme
import com.hazrat.islam24.util.LocaleContextWrapper
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
    lateinit var notificationHelper: NotificationChannels

    @Inject
    lateinit var prayerAlarmManager: PrayerAlarmManager

    @Inject
    lateinit var networkRepository: NetworkRepository

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    // Permissions manager, initialized in onCreate
    private lateinit var permissionsManager: PermissionsManager

    // Inject ViewModels properly
    private val appSettingViewModel: AppSettingViewModel by viewModels()
    private val quranViewModel: QuranViewModel by viewModels()
    private val prayerTimeViewModel: PrayerTimeViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val qiblaViewModel: QiblaViewModel by viewModels()
    private val zakatViewModel: ZakatViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val singupViewModel: SingupViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()
    private val profileDetailsViewModel: ProfileDetailsViewModel by viewModels()

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
        notificationHelper.createNotificationChannels()
        setContent {
            val isDarkModeEnabled by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val isHapticFeedback by mainViewModel.isHapticFeedback.collectAsStateWithLifecycle()

            Islam24Theme(
                darkTheme = isDarkModeEnabled
            ) {
                rememberImageLoader(this)
                NavGraph(
                    zakatViewModel = zakatViewModel,
                    quranViewModel = quranViewModel,
                    prayerTimeViewModel = prayerTimeViewModel,
                    appSettingViewModel = appSettingViewModel,
                    isHapticFeedback = isHapticFeedback,
                    qiblaViewModel = qiblaViewModel,
                    mainViewModel = mainViewModel,
                    homeViewModel = homeViewModel,
                    profileViewModel = profileViewModel,
                    loginViewModel = loginViewModel,
                    signUpViewModel = singupViewModel,
                    profileDetailsViewModel = profileDetailsViewModel,
                    forgetPasswordViewModel = forgetPasswordViewModel
                )
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

    }

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale.getDefault()
        val wrappedContext = LocaleHelper.wrap(newBase, locale)
        super.attachBaseContext(wrappedContext)
    }

}
