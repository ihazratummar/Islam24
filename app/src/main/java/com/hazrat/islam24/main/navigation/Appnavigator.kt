package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigator(
    qiblaDirection: Float,
    currentDirection: Float
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val bottomNavigationItem = remember {
                listOf(
                    ContentDestination.Home,
                    ContentDestination.PrayerTime,
                    ContentDestination.Profile
                )
            }

            NavigationBar {
                val backStackState by navController.currentBackStackEntryAsState()
                val currentDestination = backStackState?.destination
                val isBottomBarVisible =
                    bottomNavigationItem.any { it.route::class.qualifiedName == currentDestination?.route }

                bottomNavigationItem.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
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
                                contentDescription = screen.name
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
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable<HomeScreen> {
                HomeScreen(navController, navigateToPrayerTime = {

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
            authNavGraph(navController)
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
sealed class ContentDestination<T>(val name: String, @DrawableRes val icon: Int, val route: T) {

    @Serializable
    data object Home : ContentDestination<HomeScreen>("Home", R.drawable.naviconhome, HomeScreen)

    @Serializable
    data object PrayerTime :
        ContentDestination<PrayerTimeScreen>("PrayerTime", R.drawable.pray, PrayerTimeScreen)

    @Serializable
    data object Profile :
        ContentDestination<ProfileScreen>("Profile", R.drawable.profile, ProfileScreen)
}
