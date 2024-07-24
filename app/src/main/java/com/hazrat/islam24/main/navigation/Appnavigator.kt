package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.navigation.authNavGraph
import com.hazrat.islam24.core.presentation.athkar.AthkarScreen
import com.hazrat.islam24.core.presentation.calendar.CalendarScreen
import com.hazrat.islam24.core.presentation.home.HomeScreen
import com.hazrat.islam24.core.presentation.namesofallah.NamesOfAllahScreen
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeScreen
import com.hazrat.islam24.core.presentation.prayertime.setting.PrayerSetting
import com.hazrat.islam24.core.presentation.tasbih.TasbihScreen
import com.hazrat.islam24.main.navigation.component.AppBottomNavigation
import com.hazrat.islam24.main.navigation.component.BottomNavigationItem
import com.hazrat.islam24.main.navigation.nvgraph.Route
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.core.presentation.qibla.QiblaScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigator(
    mainViewModel: MainViewModel = hiltViewModel(),
    qiblaDirection: Float,
    currentDirection: Float
) {
    val bottomNavigationItem = remember {
        listOf(
            BottomNavigationItem(
                route = Route.HomeScreen.route,
                icon = R.drawable.naviconhome, text = "Home"
            ),
            BottomNavigationItem(
                route = Route.PrayerTimeScreen.route,
                icon = R.drawable.pray, text = "Time"
            ),
//            BottomNavigationItem(
//                route = Route.ProfileScreen.route,
//                icon = R.drawable.profile, text = "Profile"
//            ),
        )
    }

    val navController = rememberNavController()
    val backStackState by navController.currentBackStackEntryAsState()
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.PrayerTimeScreen.route -> 1
//        Route.ProfileScreen.route -> 2
        else -> 0
    }

    //Hide the bottom navigation when the user is in the details screen
    val isBottomBarVisible = bottomNavigationItem.any { it.route == backStackState?.destination?.route }

    TotalContent(
        isBottomBarVisible,
        bottomNavigationItem,
        selectedItem,
        navController,
        qiblaDirection,
        currentDirection
    )

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun TotalContent(
    isBottomBarVisible: Boolean,
    bottomNavigationItem: List<BottomNavigationItem>,
    selectedItem: Int,
    navController: NavHostController,
    qiblaDirection: Float,
    currentDirection: Float
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                AppBottomNavigation(items = bottomNavigationItem,
                    selectedItem = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(
                                navController = navController,
                                route = Route.HomeScreen.route
                            )

                            1 -> navigateToTab(
                                navController = navController,
                                route = Route.PrayerTimeScreen.route
                            )

//                            2 -> navigateToTab(
//                                navController = navController,
//                                route = Route.ProfileScreen.route
//                            )
                        }
                    }
                )
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = Route.HomeScreen.route) {
                HomeScreen(navController, navigateToPrayerTime = {
                    navigateToTab(
                        navController = navController,
                        route = Route.PrayerTimeScreen.route
                    )
                })
            }
            composable(route = Route.PrayerTimeScreen.route) {
                val viewModel: MainViewModel = hiltViewModel()
                PrayerTimeScreen(viewModel, navController)
            }
            composable(route = Route.QiblaDirectionScreen.route) {
                QiblaScreen(
                    navController = navController,
                    currentDirection = currentDirection,
                    qiblaDirection = qiblaDirection
                )
            }
            composable(route = Route.NamesOfAllah.route) {
                val viewModel: MainViewModel = hiltViewModel()
                NamesOfAllahScreen(viewModel, navController)
            }
            composable(route = Route.TasbihScreen.route) {
                TasbihScreen(navController)
            }
            composable(route = Route.PrayerSetting.route) {
                PrayerSetting(navController = navController)
            }
            composable(route = Route.CalendarScreen.route) {
                CalendarScreen(navController)
            }
            composable(route = Route.AthkarScreen.route) {
                AthkarScreen(navController)
            }
//            authNavGraph(navController)
        }
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}