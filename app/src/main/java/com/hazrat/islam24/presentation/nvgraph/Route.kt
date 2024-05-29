package com.hazrat.islam24.presentation.nvgraph

sealed class Route (
    val route: String
){
    object OnBoardingScreen: Route(route = "onBoardingScreen")

    object HomeScreen: Route(route = "homeScreen")

    object AppStartNavigation : Route(route = "appStartNavigation")

    object HomeNavigation: Route(route = "homeNavigation")

    object TasbihScreen:Route(route = "TasbihScreen")

    object DuasPageScreen:Route(route = "DuasPageScreen")

    object PrayerTimeScreen:Route(route = "PrayerTimeScreen")

    object QiblaDirectionScreen:Route(route = "QiblaDirectionScreen")

    object CalendarScreen:Route(route = "CalendarScreen")

    object ZakatScreen:Route(route = "ZakatScreen")

    object NamesOfAllah:Route(route = "NamesOfAllah")

    object UserSettings:Route(route = "UserSettingScreen")

    object AthkarScreen:Route(route = "AthkarScreen")

    object NoInternetScreen: Route(route = "NoInternetScreen")
}