package com.hazrat.islam24.presentation.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.R
import com.hazrat.islam24.presentation.athkar.AthkarScreen
import com.hazrat.islam24.presentation.calendar.CalendarScreen
import com.hazrat.islam24.presentation.dua.DuaScreen
import com.hazrat.islam24.presentation.tasbih.TasbihScreen
import com.hazrat.islam24.presentation.home.HomeScreen
import com.hazrat.islam24.presentation.navigator.component.AppBottomNavigation
import com.hazrat.islam24.presentation.navigator.component.BottomNavigationItem
import com.hazrat.islam24.presentation.nvgraph.Route
import com.hazrat.islam24.presentation.prayertime.PrayerTimeScreen
import com.hazrat.islam24.presentation.qiblapage.QiblaScreen
import com.hazrat.islam24.presentation.zakatscreen.ZakatScreen
import com.hazrat.islam24.presentation.namesofallah.NamesOfAllahScreen
import com.hazrat.islam24.presentation.namesofallah.NamesViewModel
import com.hazrat.islam24.presentation.prayertime.setting.UserSetting
import com.hazrat.islam24.presentation.prayertime.setting.UserSettingViewModel
import com.hazrat.islam24.presentation.prayertime.PrayerTimeViewModel

@Composable
fun AppNavigator() {
    val bottomNavigationItem = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.naviconhome, text = "Home"),
            BottomNavigationItem(icon = R.drawable.prayericon, text = "Time"),
            BottomNavigationItem(icon = R.drawable.zakaticon, text = "Zakat"),
            BottomNavigationItem(icon = R.drawable.qiblaicon, text = "Qibla")
        )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.PrayerTimeScreen.route -> 1
        Route.ZakatScreen.route -> 2
        Route.QiblaDirectionScreen.route -> 3
        else -> 0
    }

    //Hide the bottom navigation when the user is in the details screen
    val isBottomBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.PrayerTimeScreen.route ||
                backStackState?.destination?.route == Route.ZakatScreen.route ||
                backStackState?.destination?.route == Route.QiblaDirectionScreen.route
    }

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

                            2 -> navigateToTab(
                                navController = navController,
                                route = Route.ZakatScreen.route
                            )

                            3 -> navigateToTab(
                                navController = navController,
                                route = Route.QiblaDirectionScreen.route
                            )
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
                val prayerTimeViewModel: PrayerTimeViewModel = hiltViewModel()
                HomeScreen(navController, prayerTimeViewModel, navigateToPrayerTime = {
                    navigateToTab(
                        navController = navController,
                        route = Route.PrayerTimeScreen.route
                    )
                })
            }
            composable(route = Route.PrayerTimeScreen.route) {
                val viewModel: PrayerTimeViewModel = hiltViewModel()
                PrayerTimeScreen(viewModel, navController)
            }
            composable(route = Route.QiblaDirectionScreen.route) {
                QiblaScreen(navController)
            }
            composable(route = Route.ZakatScreen.route) {
                ZakatScreen(navController)
            }
            composable(route = Route.NamesOfAllah.route) {
                val viewModel: NamesViewModel = hiltViewModel()
                NamesOfAllahScreen(viewModel, navController)
            }
            composable(route = Route.TasbihScreen.route) {
                TasbihScreen(navController)
            }
            composable(route = Route.UserSettings.route) {
                val viewModel: UserSettingViewModel = hiltViewModel()
                UserSetting(navController = navController, viewModel)
            }
            composable(route = Route.CalendarScreen.route){
                CalendarScreen(navController)
            }
            composable(route = Route.DuasPageScreen.route){
                DuaScreen()
            }
            composable(route = Route.AthkarScreen.route){
                AthkarScreen(navController)
            }
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