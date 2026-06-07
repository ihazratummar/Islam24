package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.prayer.ui.prayertime.PrayerTimeScreen
import com.hazrat.prayer.ui.prayertime.PrayerTimeViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

/** @author Hazrat Ummar Shaikh */
@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.prayerNav(
    navController: NavController
) {
    navigation<PrayerTime>(PrayerTimeScreenRoute) {
        composable<PrayerTimeScreenRoute>(
            deepLinks =
                listOf(
                    navDeepLink {
                        uriPattern = "https://islam24.hazratdev.top/prayertime"
                    }
                )
        ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<PrayerTime>() // scope to PrayerTime nav graph
            }
            val prayerTimeViewModel = koinViewModel<PrayerTimeViewModel>(
                viewModelStoreOwner = parentEntry
            )
            val prayerTimesUiState by prayerTimeViewModel.uiState.collectAsStateWithLifecycle()
            val dailyStatus by prayerTimeViewModel.dailyStatus.collectAsStateWithLifecycle()
            val notificationState by prayerTimeViewModel.notificationState.collectAsStateWithLifecycle()
            PrayerTimeScreen(
                event = prayerTimeViewModel::onEvent,
                onPrayerSettingClick = { navController.navigate(MainRoute.PrayerSetting) },
                prayerTimeUiState = prayerTimesUiState,
                dailyPrayerStatus = dailyStatus,
                notificationState = notificationState
            )
        }
    }
}

@Serializable
data object PrayerTime

@Serializable
data object PrayerTimeScreenRoute

@Serializable
data object FajrSetting

@Serializable
data object DhuhrSetting

@Serializable
data object AsrSetting

@Serializable
data object MaghribSetting

@Serializable
data object IshaSetting
