package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingEvent
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingState
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.main.navigation.AppNavigator
import com.hazrat.islam24.main.navigation.HomeScreen
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    appSettingState: AppSettingState,
    appSettingEvent : (AppSettingEvent) -> Unit,
    zakatViewModel: ZakatViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RootNav,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation<RootNav>(startDestination = HomeScreen) {
            composable<HomeScreen> {
                AppNavigator(
                    appSettingState = appSettingState,
                    appSettingEvent = appSettingEvent,
                    zakatViewModel = zakatViewModel
                )
            }
        }
    }
}

@Serializable
data object RootNav