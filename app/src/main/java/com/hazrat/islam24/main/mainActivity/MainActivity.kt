package com.hazrat.islam24.main.mainActivity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.common.ChangelogDialog
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.model.Languages
import com.hazrat.notification.NotificationChannels
import com.hazrat.notification.PrayerRescheduleWorker
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
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Hide the action bar is not needed in Compose and causes AppCompat theme crashes

        mainViewModel = getViewModel()
        // Set window decor to fit system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)
        notificationHelper.createNotificationChannels()

        // Enterprise-grade: Ensure alarms are correctly scheduled on every app launch
       PrayerRescheduleWorker.enqueue(this)

        val pref = getSharedPreferences("app_setting", Context.MODE_PRIVATE)
        val language = pref.getString("language", Languages.ENGLISH.name) ?: Languages.ENGLISH.name
        val langCode = try {
            Languages.valueOf(language).code
        } catch (e: Exception) {
            "en"
        }
       AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(langCode))

        setContent {
            val isDarkModeEnabled by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val isHapticFeedback by mainViewModel.isHapticFeedback.collectAsStateWithLifecycle()
            val showChangelog by mainViewModel.showChangelog.collectAsStateWithLifecycle()

            Islam24Theme(
                darkTheme = isDarkModeEnabled
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

}
