package com.hazrat.islam24.main.mainActivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.common.ChangelogDialog
import com.hazrat.islam24.main.navigation.NavigationCommandBus
import com.hazrat.islam24.main.navigation.NavigationTarget
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.notification.NotificationChannels
import com.hazrat.notification.PrayerRescheduleWorker
import com.hazrat.ui.common.LocaleManager
import com.hazrat.ui.common.rememberImageLoader
import com.hazrat.ui.theme.Islam24Theme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

// MainActivity.kt

/**
 * MainActivity is the entry point of the application, responsible for setting up
 * the UI, managing permissions, and initializing services.
 */

/**
 * Author: Hazrat Ummar Shaikh
 */
class MainActivity : AppCompatActivity() {

    private val updateManager: UpdateManager by inject()

    private val  notificationHelper: NotificationChannels by inject()

    private lateinit var mainViewModel: MainViewModel

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down, this contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display with explicit transparent status and navigation bars
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        mainViewModel = getViewModel()

        // Hold splash screen until isLoggedIn & isSubscribed state finish loading from DataStore
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoggedIn.value == null || mainViewModel.isSubscribed.value == null
        }

        notificationHelper.createNotificationChannels()

        // Enterprise-grade: Ensure alarms are correctly scheduled on every app launch
        // Handle notification deep link on cold start
        handleNavigationIntent(intent)

        setContent {
            val isDarkModeEnabled by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val isHapticFeedback by mainViewModel.isHapticFeedback.collectAsStateWithLifecycle()
            val showChangelog by mainViewModel.showChangelog.collectAsStateWithLifecycle()
            val isLoggedInState by mainViewModel.isLoggedIn.collectAsStateWithLifecycle()
            val isSubscribedState by mainViewModel.isSubscribed.collectAsStateWithLifecycle()
            val currentLanguageCode by mainViewModel.userLanguageCode.collectAsStateWithLifecycle()

            var isInitialLanguageSyncDone by remember { mutableStateOf(false) }

            LaunchedEffect(currentLanguageCode) {
                val systemLocaleCode = LocaleManager.getAppLocale()
                if (!isInitialLanguageSyncDone) {
                    isInitialLanguageSyncDone = true
                    if (systemLocaleCode != currentLanguageCode) {
                        mainViewModel.setAppLanguageCode(systemLocaleCode)
                    }
                } else {
                    if (systemLocaleCode != currentLanguageCode) {
                        LocaleManager.setAppLocale(currentLanguageCode)
                    }
                }
            }

            Islam24Theme(
                darkTheme = isDarkModeEnabled,
                isLoggedIn = isLoggedInState ?: false,
                isSubscribed = isSubscribedState ?: false
            ) {
                rememberImageLoader(this)
                NavGraph(
                    isHapticFeedback = isHapticFeedback,
                )

                showChangelog?.let { releaseNote ->
                    ChangelogDialog(
                        releaseNote = releaseNote,
                        onDismiss = { mainViewModel.onChangelogDismissed() }
                    )
                }
            }

        }
        // Check for app updates
        updateManager.checkForAppUpdates(this)

    }

    /**
     * Called when the activity will start interacting with the user.
     */
    override fun onResume() {
        super.onResume()
        updateManager.onResume(this)
    }

    override fun onStop() {
        super.onStop()
        try {
            sendBroadcast(Intent("com.hazrat.islam24.ACTION_REFRESH_PRAYER_WIDGET").setPackage(packageName))
        } catch (_: Exception) {}
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
        setIntent(intent)
        // Handle notification navigation when app is already running (singleTask)
        handleNavigationIntent(intent)
    }

    /**
     * Reads navigation extras from the intent and posts a command via
     * [NavigationCommandBus]. Handles both screen-level targets (prayer, zakat)
     * and Quran Ayah targets. Works for both cold start and warm relaunch.
     */
    private fun handleNavigationIntent(intent: Intent?) {
        intent ?: return

        // 1. Check for screen-level navigation target (prayer, zakat notifications)
        val navTarget = intent.getStringExtra(EXTRA_NAV_TARGET)
        if (navTarget != null) {
            when (navTarget) {
                NAV_TARGET_PRAYER_TIME -> NavigationCommandBus.navigateTo(NavigationTarget.PrayerTime)
                NAV_TARGET_ZAKAT -> NavigationCommandBus.navigateTo(NavigationTarget.Zakat)
                NAV_TARGET_CALENDAR -> NavigationCommandBus.navigateTo(NavigationTarget.Calendar)
            }
            intent.removeExtra(EXTRA_NAV_TARGET)
            return
        }

        // 2. Check for Quran Ayah navigation target (audio service notifications)
        val surahNumber = intent.getIntExtra(EXTRA_NAV_SURAH_NUMBER, -1)
        val ayahNumber = intent.getIntExtra(EXTRA_NAV_AYAH_NUMBER, -1)
        if (surahNumber > 0 && ayahNumber > 0) {
            NavigationCommandBus.navigateToAyah(surahNumber, ayahNumber)
            intent.removeExtra(EXTRA_NAV_SURAH_NUMBER)
            intent.removeExtra(EXTRA_NAV_AYAH_NUMBER)
        }
    }

    companion object {
        const val EXTRA_NAV_SURAH_NUMBER = "extra_nav_surah_number"
        const val EXTRA_NAV_AYAH_NUMBER = "extra_nav_ayah_number"
        /** Screen-level navigation target key used by notification receivers. */
        const val EXTRA_NAV_TARGET = "extra_nav_target"
        const val NAV_TARGET_PRAYER_TIME = "prayertime"
        const val NAV_TARGET_ZAKAT = "zakat"
        const val NAV_TARGET_CALENDAR = "calendar"
    }

}

