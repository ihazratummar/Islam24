package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.navigation.authNavGraph
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingEvent
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingState
import com.hazrat.islam24.core.presentation.athkar.AthkarScreen
import com.hazrat.islam24.core.presentation.calendar.CalendarScreen
import com.hazrat.islam24.core.presentation.home.HomeScreen
import com.hazrat.islam24.core.presentation.namesofallah.NamesOfAllahScreen
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeScreen
import com.hazrat.islam24.core.presentation.prayertime.setting.PrayerSetting
import com.hazrat.islam24.core.presentation.qibla.QiblaScreen
import com.hazrat.islam24.core.presentation.qibla.QiblaViewModel
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigator(
    appSettingState: AppSettingState,
    appSettingEvent: (AppSettingEvent) -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
            modifier = Modifier.padding(bottom = bottomPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<HomeScreen> {
                HomeScreen(navController, navigateToPrayerTime = {
                    navController.navigate(PrayerTimeScreen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable<PrayerTimeScreen> {
                val viewModel: MainViewModel = hiltViewModel()
                PrayerTimeScreen(viewModel, navController)
            }
            composable<QiblaDirectionScreen> {
                val viewModel: QiblaViewModel = hiltViewModel()
                val mainViewModel: MainViewModel = hiltViewModel()
                val locationName by mainViewModel.locationName.collectAsState()
                val state by viewModel.qiblaState.collectAsState()
                val qiblaDirection = state.qiblaDirection
                val currentDirection = state.currentDirection
                QiblaScreen(
                    navController = navController,
                    currentDirection = currentDirection,
                    qiblaDirection = qiblaDirection,
                    locationName = locationName
                )

            }
            composable<NamesOfAllahScreen> {
                val viewModel: MainViewModel = hiltViewModel()
                NamesOfAllahScreen(viewModel, navController)
            }
            composable<PrayerSetting> {
                PrayerSetting(navController = navController)
            }
            composable<CalendarScreen> {
                CalendarScreen(navController)
            }
            composable<AthkarScreen> {
                AthkarScreen(navController)
            }
            authNavGraph(
                navController = navController,
                appSettingState = appSettingState,
                appSettingEvent = appSettingEvent
            )
        }
    }

}

@Composable
private fun BottomBar(navController: NavHostController) {
    val bottomNavigationItem = remember {
        listOf(
            ContentDestination.Home,
            ContentDestination.PrayerTime,
            ContentDestination.Profile
        )
    }
    val backStackState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination
    val isBottomBarVisible =
        bottomNavigationItem.any { it.route::class.qualifiedName == currentDestination?.route }
    if (isBottomBarVisible) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = dimens.size10
        ) {
            bottomNavigationItem.forEach { screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route::class.qualifiedName } == true
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.name,
                            modifier = Modifier.size(dimens.size35)
                        )
                    },
                    label = { Text(text = screen.name) },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        indicatorColor = Color.Transparent
                    ),
                )
            }
        }
    }
}


@Serializable
data object HomeScreen

@Serializable
data object PrayerTimeScreen

@Serializable
data object ProfileScreen

@Serializable
data object QiblaDirectionScreen

@Serializable
data object PrayerSetting

@Serializable
data object CalendarScreen

@Serializable
data object NamesOfAllahScreen

@Serializable
data object AthkarScreen

@Serializable
sealed class ContentDestination<T>(val name: String, @DrawableRes val icon: Int, val route: T) {

    @Serializable
    data object Home :
        ContentDestination<HomeScreen>("Home", R.drawable.naviconhome, HomeScreen)

    @Serializable
    data object PrayerTime :
        ContentDestination<PrayerTimeScreen>("PrayerTime", R.drawable.pray, PrayerTimeScreen)

    @Serializable
    data object Profile :
        ContentDestination<ProfileScreen>("Profile", R.drawable.profile, ProfileScreen)
}
