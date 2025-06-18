package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeScreen
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.core.presentation.prayertime.notification.screen.AsrNotification
import com.hazrat.islam24.core.presentation.prayertime.notification.screen.DhuhrNotification
import com.hazrat.islam24.core.presentation.prayertime.notification.screen.FajrNotification
import com.hazrat.islam24.core.presentation.prayertime.notification.screen.IshaNotification
import com.hazrat.islam24.core.presentation.prayertime.notification.screen.MaghribNotification
import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 */

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.prayerNav(
    navController: NavController,
    prayerTimeViewModel: PrayerTimeViewModel
){
    navigation<PrayerTime>(PrayerTimeScreenRoute){
        composable<PrayerTimeScreenRoute>(
            deepLinks = listOf(navDeepLink { uriPattern = "https://islam24.hazratdev.top/prayertime" })
        ) {
            val prayerTimes by prayerTimeViewModel.prayerTimes.collectAsState()
            val isRefreshing by prayerTimeViewModel.isPrayerTimeRefreshing.collectAsState()
            PrayerTimeScreen(
                navController = navController,
                event = prayerTimeViewModel::onEvent,
                prayerTimes = prayerTimes,
                isRefreshing = isRefreshing
            )
        }
        composable<FajrSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            FajrNotification(
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = {
                    navController.popBackStack()
                },
                notificationState = notificationState,

            )
        }
        composable<DhuhrSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            DhuhrNotification(
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<AsrSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            AsrNotification(
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<MaghribSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            MaghribNotification(
                notificationEvent = prayerTimeViewModel::onNotificationEvent,
                onBackClick = { navController.popBackStack() },
                notificationState = notificationState
            )
        }
        composable<IshaSetting> {
            val notificationState by prayerTimeViewModel.notificationState.collectAsState()
            IshaNotification(
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