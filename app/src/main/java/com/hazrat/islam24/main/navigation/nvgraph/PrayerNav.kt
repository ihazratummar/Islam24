package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.hazrat.datastore.PrayerName
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.prayer.ui.prayertime.PrayerTimeScreen
import com.hazrat.prayer.ui.prayertime.PrayerTimeViewModel
import com.hazrat.prayer.ui.component.listOfAzan
import com.hazrat.prayer.ui.component.listOfFajrAzan
import com.hazrat.prayer.ui.notification.NotificationEvent
import com.hazrat.prayer.ui.notification.PrayerNotificationScreen
import com.hazrat.ui.R
import kotlinx.serialization.Serializable

/** @author Hazrat Ummar Shaikh */
@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.prayerNav(
    navController: NavController,
    prayerTimeViewModel: PrayerTimeViewModel
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
//            val prayerTimes by prayerTimeViewModel.prayerTimes.collectAsState()
            val prayerTimesUiState by prayerTimeViewModel.uiState.collectAsStateWithLifecycle()
            PrayerTimeScreen(
                event = prayerTimeViewModel::onEvent,
                onPrayerSettingClick = { navController.navigate(MainRoute.PrayerSetting) },
                prayerTimeUiState = prayerTimesUiState
            )
        }
        composable<FajrSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            PrayerNotificationScreen(
                prayerName = PrayerName.FAJR,
                titleRes = R.string.fajr_notification,
                isNotificationEnabled = notificationState.isFajrNotification,
                onToggleNotification = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.ToggleFajrNotification
                    )
                },
                onAzanClick = { url, fileName ->
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.OnFajrAzanClick(
                            azanUrl = url,
                            fileName = fileName
                        )
                    )
                },
                selectedAzan = notificationState.selectedFajrAzan,
                onAzanOptionSelected = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.SelectFajrAzanOption(it)
                    )
                },
                azanList = listOfFajrAzan,
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<DhuhrSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            PrayerNotificationScreen(
                prayerName = PrayerName.DHUHR,
                titleRes = R.string.dhuhr_notification,
                isNotificationEnabled = notificationState.isDhuhrNotification,
                onToggleNotification = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.ToggleDhuhrNotification
                    )
                },
                onAzanClick = { url, fileName ->
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.OnDhuhrAzanClick(
                            azanUrl = url,
                            fileName = fileName
                        )
                    )
                },
                selectedAzan = notificationState.selectedDhuhrAzan,
                onAzanOptionSelected = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.SelectDhuhrAzanOption(it)
                    )
                },
                azanList = listOfAzan,
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<AsrSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            PrayerNotificationScreen(
                prayerName = PrayerName.ASR,
                titleRes = R.string.asr_notification,
                isNotificationEnabled = notificationState.isAsrNotification,
                onToggleNotification = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.ToggleAsrNotification
                    )
                },
                onAzanClick = { url, fileName ->
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.OnAsrAzanClick(azanUrl = url, fileName = fileName)
                    )
                },
                selectedAzan = notificationState.selectedAsrAzan,
                onAzanOptionSelected = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.SelectAsrAzanOption(it)
                    )
                },
                azanList = listOfAzan,
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<MaghribSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            PrayerNotificationScreen(
                prayerName = PrayerName.MAGHRIB,
                titleRes = R.string.maghrib_notification,
                isNotificationEnabled = notificationState.isMaghribNotification,
                onToggleNotification = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.ToggleMaghribNotification
                    )
                },
                onAzanClick = { url, fileName ->
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.OnMaghribAzanClick(
                            azanUrl = url,
                            fileName = fileName
                        )
                    )
                },
                selectedAzan = notificationState.selectedMaghribAzan,
                onAzanOptionSelected = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.SelectMaghribAzanOption(it)
                    )
                },
                azanList = listOfAzan,
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<IshaSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            PrayerNotificationScreen(
                prayerName = PrayerName.ISHA,
                titleRes = R.string.isha_notification,
                isNotificationEnabled = notificationState.isIshaNotification,
                onToggleNotification = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.ToggleIshaNotification
                    )
                },
                onAzanClick = { url, fileName ->
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.OnIshaAzanClick(
                            azanUrl = url,
                            fileName = fileName
                        )
                    )
                },
                selectedAzan = notificationState.selectedIshaAzan,
                onAzanOptionSelected = {
                    prayerTimeViewModel.onNotificationEvent(
                        NotificationEvent.SelectIshaAzanOption(it)
                    )
                },
                azanList = listOfAzan,
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
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
