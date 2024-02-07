package com.hazrat.islam24.presentation.nvgraph

import androidx.navigation.NamedNavArgument

sealed class Route (
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
){

    object OnBoardingScreen: Route(route = "onBoardingScreen")

    object HomeScreen: Route(route = "homeScreen")

    object AppStartNavigation : Route(route = "appStartNavigation")

    object HomeNavigation: Route(route = "homeNavigation")
    object HomeNavigatorScreen: Route(route = "homeNavigatorScreen")

    object TasbihScreen:Route(route = "TasbihScreen")

    object DuasPageScreen:Route(route = "DuasPageScreen")

    object PrayerTimeScreen:Route(route = "PrayerTimeScreen")

    object QuranPageScreen:Route(route = "QuranPageScreen")

    object ProfileScreen:Route(route = "ProfileScreen")

    object QiblaDirectionScreen:Route(route = "QiblaDirectionScreen")

    object CalendarScreen:Route(route = "CalendarScreen")

    object ZakatScreen:Route(route = "ZakatScreen")

    object NamesOfAllah:Route(route = "NamesOfAllah")

}