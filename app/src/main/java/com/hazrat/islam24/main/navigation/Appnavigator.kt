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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.hazrat.alQuran.ui.QuranScreen
import com.hazrat.alQuran.ui.QuranViewModel
import com.hazrat.alQuran.ui.SurahScreen
import com.hazrat.allahNames.ui.namesofallah.NamesOfAllahScreen
import com.hazrat.allahNames.ui.namesofallah.NamesViewmodel
import com.hazrat.athkar.ui.AthkarScreen
import com.hazrat.athkar.ui.AthkarViewModel
import com.hazrat.auth.ui.appSetting.AppSettingViewModel
import com.hazrat.auth.ui.forgetPassword.ForgetPasswordViewModel
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.profiledetails.ProfileDetailsViewModel
import com.hazrat.auth.ui.signup.SignUpViewModel
import com.hazrat.calendar.CalendarScreen
import com.hazrat.home.ui.HomeScreen
import com.hazrat.home.ui.HomeViewModel
import com.hazrat.home.ui.component.HomeRoutes
import com.hazrat.islam24.main.navigation.nvgraph.PrayerTimeScreenRoute
import com.hazrat.islam24.main.navigation.nvgraph.prayerNav
import com.hazrat.islam24.main.navigation.nvgraph.zakatNavGraph
import com.hazrat.model.AuthState
import com.hazrat.prayer.ui.prayertime.PrayerTimeViewModel
import com.hazrat.prayer.ui.setting.PrayerSetting
import com.hazrat.prayer.ui.setting.PrayerSettingViewModel
import com.hazrat.qibla.ui.QiblaScreen
import com.hazrat.qibla.ui.QiblaViewModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.hazrat.zakat.screen.zakat.ZakatViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigator(
    zakatViewModel: ZakatViewModel,
    quranViewModel: QuranViewModel,
    prayerTimeViewModel: PrayerTimeViewModel,
    appSettingViewModel: AppSettingViewModel,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    profileDetailsViewModel: ProfileDetailsViewModel,
    forgetPasswordViewModel: ForgetPasswordViewModel,
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
            composable<MainRoute.HomeScreen>(
                deepLinks = listOf(navDeepLink { uriPattern = "https://islam24.hazratdev.top" })
            ) {navBackStackEntry ->

                val homeViewModel = koinViewModel<HomeViewModel>()
                val locationName by homeViewModel.locationName.collectAsState()
                val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

                val lifecycle = navBackStackEntry.lifecycle
                DisposableEffect(lifecycle) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START){
                            homeViewModel.refreshLocation()
                        }
                    }
                    lifecycle.addObserver(observer)
                    onDispose { lifecycle.removeObserver(observer) }
                }

                HomeScreen(
                    navigateToPrayerTime = {
                        navController.navigate(PrayerTimeScreenRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    locationName = locationName,
                    onWidgetClick = { homeWidgetNav ->
                        navController.navigate(homeWidgetNav.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    homeState = homeState
                )
            }

            composable<MainRoute.QuranScreenRoute>(
                deepLinks = listOf(navDeepLink { uriPattern = "https://islam24.hazratdev.top/quran-screen" })
            ) {
                val quranState by quranViewModel.quranState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    quranViewModel.refreshQuran()
                }

                QuranScreen(
                    onSurahClick = { surahNumber, ayahNumber, isTracking ->
                        navController.navigate(
                            MainRoute.SurahScreenRoute(
                                surahNumber,
                                ayahNumber?.minus(1), isTracking
                            )
                        ) {
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
                val surahNumber =
                    navBackStackEntry.toRoute<MainRoute.SurahScreenRoute>().surahNumber
                val ayahNumber = navBackStackEntry.toRoute<MainRoute.SurahScreenRoute>().ayahNumber
                val isTracking = navBackStackEntry.toRoute<MainRoute.SurahScreenRoute>().isTracking
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

            prayerNav(navController, prayerTimeViewModel)
            composable<HomeRoutes.Qibla> {
                val viewModel: QiblaViewModel = koinViewModel()
                val state by viewModel.qiblaState.collectAsStateWithLifecycle()
                val qiblaEvent = viewModel::onEvent
                val authState by viewModel.authState.observeAsState(initial = AuthState.Unauthenticated)
                QiblaScreen(
                    state = state,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    isHapticFeedback = isHapticFeedback,
                    authState = authState,
                    qiblaEvent = qiblaEvent,
                    navigateToLogin = {
                        navController.navigate(Login){
                            launchSingleTop = true
                        }
                    }
                )

            }
            composable<HomeRoutes.NamesOfAllah> {
                val viewModel: NamesViewmodel =koinViewModel()
                val names by viewModel.names.collectAsStateWithLifecycle()
                NamesOfAllahScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    nameEntity = names
                )
            }
            composable<MainRoute.PrayerSetting> {
                val prayerTimeSettingViewmodel: PrayerSettingViewModel = koinViewModel()
                val prayerSettingState by prayerTimeSettingViewmodel.state.collectAsStateWithLifecycle()
                PrayerSetting(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    state = prayerSettingState,
                    event = prayerTimeSettingViewmodel::onEvent
                )
            }
            composable<HomeRoutes.Calendar> {
                CalendarScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<HomeRoutes.Athkar> {
                val viewModel: AthkarViewModel = koinViewModel()
                val athkarEntity by viewModel.athkarList.collectAsState()
                AthkarScreen(
                    athkar = athkarEntity,
                    onBackClick = { navController.popBackStack() }
                )
            }

            authNavGraph(
                navController = navController,
                appSettingViewModel = appSettingViewModel,
                isHapticFeedback = isHapticFeedback,
                loginViewModel = loginViewModel,
                signUpViewModel = signUpViewModel,
                profileDetailsViewModel = profileDetailsViewModel,
                forgetPasswordViewModel = forgetPasswordViewModel
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
            tonalElevation = dimens.space4
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
                            modifier = Modifier.size(dimens.space32)
                        )
                    },
                    label = { Text(text = screen.name) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
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
    data class SurahScreenRoute(
        val surahNumber: Int,
        val ayahNumber: Int? = 1,
        val isTracking: Boolean = true
    ) : MainRoute()


    @Serializable
    data object PrayerSetting : MainRoute()


}


@Serializable
sealed class ContentDestination<T>(val name: String, @param:DrawableRes val icon: Int, val route: T) {

    @Serializable
    data object Home :
        ContentDestination<MainRoute.HomeScreen>(
            "Home",
            R.drawable.naviconhome,
            MainRoute.HomeScreen
        )

    @Serializable
    data object PrayerTime :
        ContentDestination<PrayerTimeScreenRoute>("PrayerTime", R.drawable.pray, PrayerTimeScreenRoute)

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
