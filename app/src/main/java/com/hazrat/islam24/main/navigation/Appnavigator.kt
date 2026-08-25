package com.hazrat.islam24.main.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.hazrat.athkar.ui.dua.category.DuaBookmarksScreen
import com.hazrat.athkar.ui.dua.category.DuaCategoryDetailScreen
import com.hazrat.athkar.ui.dua.category.DuaRecentsScreen
import com.hazrat.athkar.ui.dua.category.DuaScreen
import com.hazrat.athkar.ui.dua.category.DuaViewModel
import com.hazrat.athkar.ui.dua.category.HisnulMuslimCategoryGridScreen
import com.hazrat.athkar.ui.dua.dua_details.DuaItemScreen
import com.hazrat.athkar.ui.dua.dua_details.DuaItemViewModel
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.calendar.CalendarScreen
import com.hazrat.home.ui.HomeScreen
import com.hazrat.home.ui.HomeViewModel
import com.hazrat.home.ui.component.HomeRoutes
import com.hazrat.home.ui.widgets.HomeScreenWidgetsScreen
import com.hazrat.islam24.widget.nextprayer.NextPrayerWidgetReceiver
import com.hazrat.islam24.main.navigation.nvgraph.PrayerTimeScreenRoute
import com.hazrat.islam24.main.navigation.nvgraph.prayerNav
import com.hazrat.islam24.main.navigation.nvgraph.zakatNavGraph
import com.hazrat.prayer.ui.setting.PrayerSetting
import com.hazrat.prayer.ui.setting.PrayerSettingViewModel
import com.hazrat.qibla.ui.QiblaScreen
import com.hazrat.qibla.ui.QiblaViewModel
import com.hazrat.tasbih.ui.TasbihViewModel
import com.hazrat.ui.R
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

    // Observe notification navigation commands and navigate programmatically.
    // This is the single entry point for all notification-driven navigation,
    // avoiding synthetic back stacks created by URI deep links.
    val pendingNav by NavigationCommandBus.pendingNavigation.collectAsStateWithLifecycle()
    LaunchedEffect(pendingNav) {
        val target = pendingNav ?: return@LaunchedEffect
        when (target) {
            is NavigationTarget.Ayah -> {
                val surahName = com.hazrat.ui.common.SurahNameProvider.getSurahName(target.surahNumber)
                navController.navigate(
                    MainRoute.AyahScreenRoute(
                        SurahData(
                            name = surahName,
                            totalAyah = 286,
                            meaning = "",
                            surahNumber = target.surahNumber,
                            targetAyahNumber = target.ayahNumber,
                            isRecordRecentRead = false
                        )
                    )
                ) {
                    launchSingleTop = true
                }
            }

            is NavigationTarget.PrayerTime -> {
                navController.navigate(PrayerTimeScreenRoute) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            is NavigationTarget.Zakat -> {
                navController.navigate(HomeRoutes.Zakat) {
                    launchSingleTop = true
                }
            }

            is NavigationTarget.Calendar -> {
                navController.navigate(HomeRoutes.Calendar) {
                    launchSingleTop = true
                }
            }
        }
        NavigationCommandBus.consumePendingNavigation()
    }
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
            composable<MainRoute.HomeScreen> {

                val homeViewModel = koinViewModel<HomeViewModel>()
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
                    onWidgetClick = { homeWidgetNav ->
                        navController.navigate(homeWidgetNav.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onRecentReadClick = { recent ->
                        val surahInfo = homeState.surahs.find { it.surahNumber == recent.surahNumber }
                        navController.navigate(
                            MainRoute.AyahScreenRoute(
                                surahData = SurahData(
                                    name = recent.surahName,
                                    totalAyah = surahInfo?.totalAyahs ?: recent.ayahNumber,
                                    meaning = surahInfo?.nameEnglish ?: "",
                                    surahNumber = recent.surahNumber,
                                    targetAyahNumber = recent.ayahNumber
                                )
                            )
                        )
                    },
                    homeState = homeState,
                    refreshLocation = homeViewModel::refreshLocation,
                    dailyPrayerStatus = dailyStatus,
                    onSupportClick = { navController.navigate(SupportIslam24Route) },
                    onDailyVerseClick = { dailyVerse ->
                        val surahInfo = homeState.surahs.find { it.surahNumber == dailyVerse.surahNumber }
                        navController.navigate(
                            MainRoute.AyahScreenRoute(
                                surahData = SurahData(
                                    name = dailyVerse.surahName,
                                    totalAyah = surahInfo?.totalAyahs ?: 69,
                                    meaning = surahInfo?.nameEnglish ?: "The Spider",
                                    surahNumber = dailyVerse.surahNumber,
                                    targetAyahNumber = dailyVerse.verseNumber,
                                    isRecordRecentRead = false
                                )
                            )
                        )
                    },
                    onHomeScreenWidgetsClick = {
                        navController.navigate(HomeRoutes.HomeScreenWidgetsRoute)
                    }
                )
            }

            composable<MainRoute.QuranScreenRoute>(
                deepLinks = listOf(navDeepLink {
                    uriPattern = "https://islam24.app/quran-screen"
                })
            ) {

                val surahViewModel = koinViewModel<SurahViewModel>()
                val quranState by surahViewModel.surahState.collectAsStateWithLifecycle()

                DisposableEffect(Unit) {
                    surahViewModel.loadRecentReads()
                    onDispose {}
                }

                QuranScreen(
                    surahState = quranState,
                    onSurahClick = { surahData ->
                        navController.navigate(
                            MainRoute.AyahScreenRoute(
                                SurahData(
                                    name = surahData.name,
                                    totalAyah = surahData.totalAyah,
                                    meaning = surahData.meaning,
                                    surahNumber = surahData.number,
                                    targetAyahNumber = surahData.targetAyahNumber,
                                    isRecordRecentRead = !surahData.isFromBookmark && !surahData.isFromKhatam,
                                    isFromBookmark = surahData.isFromBookmark,
                                    isFromKhatam = surahData.isFromKhatam
                                )
                            )
                        )
                    },
                    onSearchQueryChanged = surahViewModel::onSearchQueryChanged,
                    onSearchActiveChanged = surahViewModel::onSearchActiveChanged,
                    onTabSelected = surahViewModel::onTabSelected,
                    onViewModeChanged = surahViewModel::onViewModeChanged,
                    onStartNewKhatamClick = surahViewModel::onOpenTargetDatePicker,
                    onTargetDateSelected = surahViewModel::onTargetDateSelected,
                    onOpenEditPlanSheet = surahViewModel::onOpenEditPlanSheet,
                    onResetPlanClicked = surahViewModel::onResetPlanClicked,
                    onEndPlanClicked = surahViewModel::onEndPlanClicked,
                    onDismissSheets = surahViewModel::dismissSheets
                )
            }

            composable<MainRoute.AyahScreenRoute>(
                typeMap = mapOf(typeOf<SurahData>() to SurahDataType)
            ) { navBackStackEntry ->
                val surahData = navBackStackEntry.toRoute<MainRoute.AyahScreenRoute>().surahData
                val ayahViewModel = koinViewModel<AyahViewModel>(
                    parameters = {
                        parametersOf(surahData.surahNumber, surahData.targetAyahNumber, surahData.isFromBookmark, surahData.isFromKhatam)
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
                        number = surahData.surahNumber,
                        targetAyahNumber = surahData.targetAyahNumber,
                        isFromBookmark = surahData.isFromBookmark,
                        isFromKhatam = surahData.isFromKhatam
                    ),
                    onAyahScrolled = { ayahNum ->
                        ayahViewModel.saveLastReadAyah(ayahNumber = ayahNum)
                    },
                    onSurahCompleted = {
                        ayahViewModel.onSurahCompleted()
                    },
                    onEvent = ayahViewModel::onEvent
                )
            }

            prayerNav(navController)
            composable<HomeRoutes.Qibla> {
                val viewModel: QiblaViewModel = koinViewModel()
                val state by viewModel.qiblaState.collectAsStateWithLifecycle()
                val qiblaEvent = viewModel::onEvent
                QiblaScreen(
                    state = state,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    isHapticFeedback = isHapticFeedback,
                    qiblaEvent = qiblaEvent,
                    navigateToTasbih = {
                        navController.navigate(HomeRoutes.TasbihRoute) {
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
                    nameEntity = names,
                    onSupportClick = { navController.navigate(SupportIslam24Route) }
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
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                AthkarScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onCountClick = viewModel::incrementCount,
                    onResetItemClick = viewModel::resetItemCount,
                    onResetAllClick = viewModel::resetCounts
                )
            }

            composable<HomeRoutes.TasbihRoute> {
                val viewModel: TasbihViewModel = koinViewModel()
                com.hazrat.tasbih.ui.TasbihScreen(
                    viewModel = viewModel,
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
                    onDuaClick = { chapterId ->
                        navController.navigate(HomeRoutes.DuaItemRoute(categoryId = chapterId))
                    },
                    onCategoryClick = { category ->
                        navController.navigate(HomeRoutes.DuaCategoryDetailRoute(categoryId = category.id))
                    },
                    onHisnulMuslimClick = {
                        navController.navigate(HomeRoutes.HisnulMuslimGridRoute)
                    },
                    onBookmarksClick = {
                        navController.navigate(HomeRoutes.DuaBookmarksRoute)
                    },
                    onRecentsClick = {
                        navController.navigate(HomeRoutes.DuaRecentsRoute)
                    },
                    onTasbihClick = {
                        navController.navigate(HomeRoutes.TasbihRoute)
                    },
                    event = viewModel::event
                )
            }

            composable<HomeRoutes.HisnulMuslimGridRoute> {
                HisnulMuslimCategoryGridScreen(
                    onCategoryClick = { category ->
                        navController.navigate(HomeRoutes.DuaCategoryDetailRoute(categoryId = category.id))
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<HomeRoutes.DuaCategoryDetailRoute> { navBackStack ->
                val categoryId = navBackStack.toRoute<HomeRoutes.DuaCategoryDetailRoute>().categoryId
                val category = HisnulMuslimCategory.fromId(categoryId)
                val viewModel: DuaViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                DuaCategoryDetailScreen(
                    category = category,
                    duaCategoryState = state,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onChapterClick = { chapterId ->
                        navController.navigate(HomeRoutes.DuaItemRoute(categoryId = chapterId))
                    },
                    onLoadCategory = { cat ->
                        viewModel.loadChaptersForCategory(cat)
                    }
                )
            }

            composable<HomeRoutes.DuaBookmarksRoute> {
                val viewModel: DuaViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                DuaBookmarksScreen(
                    duaCategoryState = state,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onDuaClick = { chapterId ->
                        navController.navigate(HomeRoutes.DuaItemRoute(categoryId = chapterId))
                    }
                )
            }

            composable<HomeRoutes.DuaRecentsRoute> {
                val viewModel: DuaViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                DuaRecentsScreen(
                    duaCategoryState = state,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onDuaClick = { chapterId ->
                        navController.navigate(HomeRoutes.DuaItemRoute(categoryId = chapterId))
                    },
                    onDeleteRecent = { chapterId ->
                        viewModel.event(com.hazrat.athkar.ui.dua.category.DuaCategoryEvent.DeleteRecent(chapterId))
                    }
                )
            }

            composable<HomeRoutes.DuaItemRoute> { navBackStack ->

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
                    },
                    event = viewModel::event
                )

            }

            composable<HomeRoutes.HomeScreenWidgetsRoute> {
                val context = androidx.compose.ui.platform.LocalContext.current
                HomeScreenWidgetsScreen(
                    onBackClick = { navController.popBackStack() },
                    onPinWidget = { widgetId ->
                        val receiverClass = when (widgetId) {
                            "next_prayer" -> NextPrayerWidgetReceiver::class.java
                            "hijri_date" -> com.hazrat.islam24.widget.hijridate.HijriDateWidgetReceiver::class.java
                            "hijri_calendar" -> com.hazrat.islam24.widget.calendar.HijriCalendarWidgetReceiver::class.java
                            else -> null
                        }
                        if (receiverClass != null) {
                            val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(context)
                            val provider = android.content.ComponentName(context, receiverClass)
                            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                                appWidgetManager.requestPinAppWidget(provider, null, null)
                            } else {
                                Toast.makeText(context, "Long-press your home screen to add widgets", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "This widget will be available in the next step!", Toast.LENGTH_SHORT).show()
                        }
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
            ContentDestination.Qibla,
            ContentDestination.Profile
        )
    }
    val backStackState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination
    val isBottomBarVisible =
        bottomNavigationItem.any { it.route::class.qualifiedName == currentDestination?.route }
    if (isBottomBarVisible) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
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
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
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
    val surahNumber: Int,
    val targetAyahNumber: Int = 1,
    val isRecordRecentRead: Boolean = true,
    val isFromBookmark: Boolean = false,
    val isFromKhatam: Boolean = false
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
            "Prayers",
            R.drawable.pray,
            PrayerTimeScreenRoute
        )

    @Serializable
    data object Quran : ContentDestination<MainRoute.QuranScreenRoute>(
        "Quran",
        R.drawable.quran,
        MainRoute.QuranScreenRoute
    )

    @Serializable
    data object Qibla : ContentDestination<HomeRoutes.Qibla>(
        "Qibla",
        R.drawable.qibla_compass,
        HomeRoutes.Qibla
    )

    @Serializable
    data object Profile :
        ContentDestination<MainRoute.ProfileScreen>(
            "Profile",
            R.drawable.profile,
            MainRoute.ProfileScreen
        )
}
