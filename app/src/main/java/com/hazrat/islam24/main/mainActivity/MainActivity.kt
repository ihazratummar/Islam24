package com.hazrat.islam24.main.mainActivity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.signup.SignUpViewModel
import com.hazrat.auth.ui.profiledetails.ProfileDetailsViewModel
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.notification.NotificationChannels
import com.hazrat.prayer.ui.prayertime.PrayerTimeViewModel
import com.hazrat.ui.common.rememberImageLoader
import com.hazrat.ui.theme.Islam24Theme
import com.hazrat.utils.LocaleHelper
import com.hazrat.zakat.screen.zakat.ZakatViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.Locale

// MainActivity.kt

/**
 * MainActivity is the entry point of the application, responsible for setting up
 * the UI, managing permissions, and initializing services.
 */

/**
 * Author: Hazrat Ummar Shaikh
 */
class MainActivity : ComponentActivity() {

    private val updateManager: UpdateManager by inject()

    private val  notificationHelper: NotificationChannels by inject()

    private lateinit var mainViewModel: MainViewModel

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

        mainViewModel = getViewModel()
        // Set window decor to fit system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)
        notificationHelper.createNotificationChannels()
        setContent {
            val isDarkModeEnabled by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val isHapticFeedback by mainViewModel.isHapticFeedback.collectAsStateWithLifecycle()

            Islam24Theme(
                darkTheme = isDarkModeEnabled
            ) {
                rememberImageLoader(this)
                NavGraph(
                    isHapticFeedback = isHapticFeedback,
                )
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

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale.getDefault()
        val wrappedContext = LocaleHelper.wrap(newBase, locale)
        super.attachBaseContext(wrappedContext)
    }

}
