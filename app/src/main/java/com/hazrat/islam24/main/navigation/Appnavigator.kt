package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.navigation.authNavGraph
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.core.presentation.al_quran.QuranScreen
import com.hazrat.islam24.core.presentation.al_quran.QuranViewModel
import com.hazrat.islam24.core.presentation.al_quran.SurahScreen
import com.hazrat.islam24.core.presentation.athkar.AthkarScreen
import com.hazrat.islam24.core.presentation.athkar.AthkarViewModel
import com.hazrat.islam24.core.presentation.calendar.CalendarViewModel
import com.hazrat.islam24.core.presentation.calendar.GregorianCalendarScreen
import com.hazrat.islam24.core.presentation.home.HomeScreen
import com.hazrat.islam24.core.presentation.home.HomeViewModel
import com.hazrat.islam24.core.presentation.home.component.BenefitsOfRecitingScreen
import com.hazrat.islam24.core.presentation.namesofallah.NamesOfAllahScreen
import com.hazrat.islam24.core.presentation.namesofallah.NamesViewmodel
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.core.presentation.prayertime.setting.PrayerSetting
import com.hazrat.islam24.core.presentation.prayertime.setting.PrayerSettingViewModel
import com.hazrat.islam24.core.presentation.qibla.QiblaScreen
import com.hazrat.islam24.core.presentation.qibla.QiblaViewModel
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.main.navigation.MainRoute.BenifitsOfRecitingRoute
import com.hazrat.islam24.main.navigation.nvgraph.PrayerTimeScreen
import com.hazrat.islam24.main.navigation.nvgraph.prayerNav
import com.hazrat.islam24.main.navigation.nvgraph.zakatNavGraph
import com.hazrat.islam24.ui.theme.dimens
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigator(
    zakatViewModel: ZakatViewModel,
    quranViewModel: QuranViewModel,
    prayerTimeViewModel: PrayerTimeViewModel,
    appSettingViewModel: AppSettingViewModel,
    isHapticFeedback: Boolean = false
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
            startDestination = MainRoute.HomeScreen,
            modifier = Modifier.padding(bottom = bottomPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<MainRoute.HomeScreen> {
                val mainViewModel : MainViewModel = hiltViewModel()
                val prayerTimes by prayerTimeViewModel.prayerTimes.collectAsState()
                val quranViewModel: QuranViewModel = hiltViewModel()
                val quranState by quranViewModel.quranState.collectAsStateWithLifecycle()
                val locationName by mainViewModel.locationName.collectAsState()
                val homeViewModel: HomeViewModel = hiltViewModel()
                val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
                HomeScreen(
                    navigateToPrayerTime = {
                        navController.navigate(PrayerTimeScreen) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, prayerTimes = prayerTimes,
                    locationName = locationName,
                    onWidgetClick = { homeWidgetNav ->
                        navController.navigate(homeWidgetNav.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onBenefitsWidgetClick = {
                        navController.navigate(BenifitsOfRecitingRoute)
                    },
                    quranState =  quranState,
                    homeState = homeState,
                    onDailyQuranClick = {surah, ayah ->
                        navController.navigate(MainRoute.SurahScreenRoute(surahNumber = surah, ayahNumber = ayah, isTracking = false))
                    }
                )
            }

            composable<BenifitsOfRecitingRoute> {
                BenefitsOfRecitingScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<MainRoute.QuranScreenRoute> {
                val quranState by quranViewModel.quranState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    quranViewModel.refreshQuran()
                }

                QuranScreen(
                    onSurahClick = { surahNumber, ayahNumber, isTracking ->
                        navController.navigate(MainRoute.SurahScreenRoute(surahNumber,
                            ayahNumber?.minus(1), isTracking)){
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    quranState = quranState,
                    refresh = quranViewModel::refreshLastRead,

                )
            }

            composable<MainRoute.SurahScreenRoute> { navBackStackEntry ->
                val quranState by quranViewModel.quranState.collectAsStateWithLifecycle()
                val surahNumber =  navBackStackEntry.toRoute<MainRoute.SurahScreenRoute>().surahNumber
                val ayahNumber =  navBackStackEntry.toRoute<MainRoute.SurahScreenRoute>().ayahNumber
                val isTracking =  navBackStackEntry.toRoute<MainRoute.SurahScreenRoute>().isTracking
                SurahScreen(
                    surahNumber = surahNumber,
                    ayatNumber = ayahNumber,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    event = quranViewModel::onEvent,
                    quranState = quranState,
                    isTracking = isTracking
                )
            }

            prayerNav(navController,prayerTimeViewModel)
            composable<MainRoute.QiblaDirectionScreen> {
                val viewModel: QiblaViewModel = hiltViewModel()
                val locationName by viewModel.locationName.collectAsState()
                val state by viewModel.qiblaState.collectAsStateWithLifecycle()
                QiblaScreen(
                    locationName = locationName,
                    state = state,
                    isFacingQibla = viewModel.isFacingQibla(),
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            }
            composable<MainRoute.NamesOfAllahScreen> {
                val viewModel: NamesViewmodel = hiltViewModel()
                val names by viewModel.names.collectAsState()
                NamesOfAllahScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    nameEntity = names
                )
            }
            composable<MainRoute.PrayerSetting> {
                val prayerTimeSettingViewmodel: PrayerSettingViewModel = hiltViewModel()
                val prayerTimeEntity by prayerTimeSettingViewmodel.prayerTime.collectAsState()
                val prayerSettingState by prayerTimeSettingViewmodel.state.collectAsState()
                PrayerSetting(
                    prayerTimeEntity = prayerTimeEntity,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    state = prayerSettingState,
                    event = prayerTimeSettingViewmodel::onEvent
                )
            }
            composable<MainRoute.CalendarScreen> {
                val viewModel: CalendarViewModel = hiltViewModel()
                val gregorianToHijriEntity by viewModel.gregorianToHijriEntity.collectAsState()
                GregorianCalendarScreen(
                    onBackClick = { navController.popBackStack() },
                    gregorianToHijriEntity = gregorianToHijriEntity
                )
            }
            composable<MainRoute.AthkarScreen> {
                val viewModel: AthkarViewModel = hiltViewModel()
                val athkarEntity by viewModel.athkarList.collectAsState()
                AthkarScreen(
                    athkar = athkarEntity,
                    onBackClick = { navController.popBackStack() }
                )
            }
            authNavGraph(
                navController = navController,
                appSettingViewModel = appSettingViewModel,
                isHapticFeedback = isHapticFeedback
            )
            zakatNavGraph(
                navController = navController,
                zakatViewModel = zakatViewModel,
            )
        }
    }

}


@Composable
private fun BottomBar(navController: NavHostController) {
    val bottomNavigationItem = remember {
        listOf(
            ContentDestination.Home,
            ContentDestination.Quran,
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
            tonalElevation = dimens.size5
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
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = Color.Transparent
                    ),
                    interactionSource = remember { MutableInteractionSource() },
                )
            }
        }
    }
}

@Serializable
sealed class MainRoute {


    @Serializable
    data object HomeScreen : MainRoute()

    @Serializable
    data object ProfileScreen : MainRoute()

    @Serializable
    data object QuranScreenRoute : MainRoute()

    @Serializable
    data class SurahScreenRoute(val surahNumber: Int, val ayahNumber: Int? = 1, val isTracking: Boolean = true) : MainRoute()

    @Serializable
    data object QiblaDirectionScreen : MainRoute()

    @Serializable
    data object PrayerSetting : MainRoute()

    @Serializable
    data object CalendarScreen : MainRoute()

    @Serializable
    data object NamesOfAllahScreen : MainRoute()

    @Serializable
    data object AthkarScreen : MainRoute()

    @Serializable
    data object ZakatScreen : MainRoute()

    @Serializable
    data object BenifitsOfRecitingRoute : MainRoute()
}


@Serializable
sealed class ContentDestination<T>(val name: String, @DrawableRes val icon: Int, val route: T) {

    @Serializable
    data object Home :
        ContentDestination<MainRoute.HomeScreen>(
            "Home",
            R.drawable.naviconhome,
            MainRoute.HomeScreen
        )

    @Serializable
    data object PrayerTime :
        ContentDestination<PrayerTimeScreen>("PrayerTime", R.drawable.pray, PrayerTimeScreen)

    @Serializable
    data object Profile :
        ContentDestination<MainRoute.ProfileScreen>(
            "Profile",
            R.drawable.profile,
            MainRoute.ProfileScreen
        )

    @Serializable
    data object Quran : ContentDestination<MainRoute.QuranScreenRoute>(
        "Quran",
        R.drawable.quran,
        MainRoute.QuranScreenRoute
    )
}
