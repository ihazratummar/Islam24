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
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.core.presentation.al_quran.QuranViewModel
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.main.navigation.AppNavigator
import com.hazrat.islam24.main.navigation.MainRoute
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    zakatViewModel: ZakatViewModel,
    quranViewModel: QuranViewModel,
    prayerTimeViewModel : PrayerTimeViewModel,
    appSettingViewModel : AppSettingViewModel,
    isHapticFeedback : Boolean = false
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RootNav,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation<RootNav>(startDestination = MainRoute.HomeScreen) {
            composable<MainRoute.HomeScreen> {
                AppNavigator(
                    zakatViewModel = zakatViewModel,
                    quranViewModel = quranViewModel,
                    prayerTimeViewModel = prayerTimeViewModel,
                    appSettingViewModel = appSettingViewModel,
                    isHapticFeedback = isHapticFeedback
                )
            }
        }
    }
}

@Serializable
data object RootNav