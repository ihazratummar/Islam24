package com.hazrat.islam24.main.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.hazrat.alQuran.ui.ayah.AyahScreen
import com.hazrat.alQuran.ui.ayah.AyahViewModel
import com.hazrat.alQuran.ui.surah.QuranScreen
import com.hazrat.alQuran.ui.surah.SurahScreenData
import com.hazrat.alQuran.ui.surah.SurahViewModel
import com.hazrat.allahNames.ui.namesofallah.NamesOfAllahScreen
import com.hazrat.allahNames.ui.namesofallah.NamesViewmodel
import com.hazrat.athkar.ui.azkar.AthkarScreen
import com.hazrat.athkar.ui.azkar.AthkarViewModel
import com.hazrat.athkar.ui.dua.category.DuaScreen
import com.hazrat.athkar.ui.dua.category.DuaViewModel
import com.hazrat.athkar.ui.dua.dua_details.DuaItemScreen
import com.hazrat.athkar.ui.dua.dua_details.DuaItemViewModel
import com.hazrat.calendar.CalendarScreen
import com.hazrat.home.ui.HomeScreen
import com.hazrat.home.ui.HomeViewModel
import com.hazrat.home.ui.component.HomeRoutes
import com.hazrat.islam24.main.navigation.nvgraph.PrayerTimeScreenRoute
import com.hazrat.islam24.main.navigation.nvgraph.prayerNav
import com.hazrat.islam24.main.navigation.nvgraph.zakatNavGraph
import com.hazrat.model.AuthState
import com.hazrat.prayer.ui.setting.PrayerSetting
import com.hazrat.prayer.ui.setting.PrayerSettingViewModel
import com.hazrat.qibla.ui.QiblaScreen
import com.hazrat.qibla.ui.QiblaViewModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigator(
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
            // ✅ Remove None transitions — let each composable define its own
            enterTransition = {
                fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable<MainRoute.HomeScreen>(
                deepLinks = listOf(navDeepLink { uriPattern = "https://islam24.hazratdev.top" })
            ) {

                val homeViewModel = koinViewModel<HomeViewModel>()
                val locationName by homeViewModel.locationName.collectAsState()
                val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
                val dailyStatus by homeViewModel.dailyStatus.collectAsStateWithLifecycle()

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
                    homeState = homeState,
                    refreshLocation = homeViewModel::refreshLocation,
                    dailyPrayerStatus = dailyStatus
                )
            }

            composable<MainRoute.QuranScreenRoute>(
                deepLinks = listOf(navDeepLink {
                    uriPattern = "https://islam24.hazratdev.top/quran-screen"
                })
            ) {

                val surahViewModel = koinViewModel<SurahViewModel>()
                val quranState by surahViewModel.surahState.collectAsStateWithLifecycle()


                QuranScreen(
                    surahState = quranState,
                    onSurahClick = {surahData ->
                        navController.navigate(
                            MainRoute.AyahScreenRoute(
                                SurahData(
                                    name = surahData.name,
                                    totalAyah = surahData.totalAyah,
                                    meaning = surahData.meaning,
                                    surahNumber = surahData.number
                                )
                            )
                        )
                    }
                )
            }

            composable<MainRoute.AyahScreenRoute>(
                typeMap = mapOf(typeOf<SurahData>() to SurahDataType)
            ) { navBackStackEntry ->
                val surahData = navBackStackEntry.toRoute<MainRoute.AyahScreenRoute>().surahData
                val ayahViewModel = koinViewModel<AyahViewModel>(
                    parameters = {
                        parametersOf(surahData.surahNumber)
                    }
                )
                val state by ayahViewModel.state.collectAsStateWithLifecycle()
                AyahScreen(
                    ayahState = state,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    surahScreenData = SurahScreenData(
                        name = surahData.name,
                        totalAyah = surahData.totalAyah,
                        meaning = surahData.meaning,
                        number = surahData.surahNumber
                    )
                )
            }

            prayerNav(navController)
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
                        navController.navigate(Login) {
                            launchSingleTop = true
                        }
                    }
                )

            }
            composable<HomeRoutes.NamesOfAllah> {
                val viewModel: NamesViewmodel = koinViewModel()
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

            composable<HomeRoutes.DuaRoute> {
                val viewModel: DuaViewModel = koinViewModel()
                val duaCategoryModel by viewModel.state.collectAsStateWithLifecycle()
                DuaScreen(
                    duaCategoryState = duaCategoryModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onDuaClick = {categoryId ->
                        navController.navigate(HomeRoutes.DuaItemRoute(categoryId = categoryId))
                    },
                    event = viewModel::event
                )
            }

            composable<HomeRoutes.DuaItemRoute> {navBackStack ->

                val categoryId = navBackStack.toRoute<HomeRoutes.DuaItemRoute>().categoryId
                val viewModel = koinViewModel<DuaItemViewModel>(
                    parameters = {
                        parametersOf(categoryId)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                DuaItemScreen(
                    state = state,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            }


            authNavGraph(
                navController = navController,
                isHapticFeedback = isHapticFeedback
            )
            zakatNavGraph(
                navController = navController,
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
            ContentDestination.Quran,
            ContentDestination.Profile
        )
    }
    val backStackState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination
    val isBottomBarVisible =
        bottomNavigationItem.any { it.route::class.qualifiedName == currentDestination?.route }
    if (isBottomBarVisible) {
        NavigationBar(
            containerColor = customColors.navBarColor,
            tonalElevation = dimens.space4,
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
                            modifier = Modifier.size(dimens.iconMd)
                        )
                    },
                    label = { Text(text = screen.name) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
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
    data class AyahScreenRoute(
        val surahData: SurahData,
        val isTracking: Boolean = true
    ) : MainRoute()


    @Serializable
    data object PrayerSetting : MainRoute()


}


@Serializable
data class SurahData(
    val name: String,
    val totalAyah: Int,
    val meaning: String,
    val surahNumber: Int
)

val SurahDataType = object : NavType<SurahData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): SurahData? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): SurahData {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: SurahData): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: SurahData) {
        bundle.putString(key, Json.encodeToString(value))
    }
}

@Serializable
sealed class ContentDestination<T>(
    val name: String,
    @param:DrawableRes val icon: Int,
    val route: T
) {

    @Serializable
    data object Home :
        ContentDestination<MainRoute.HomeScreen>(
            "Home",
            R.drawable.naviconhome,
            MainRoute.HomeScreen
        )

    @Serializable
    data object PrayerTime :
        ContentDestination<PrayerTimeScreenRoute>(
            "PrayerTime",
            R.drawable.pray,
            PrayerTimeScreenRoute
        )

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
