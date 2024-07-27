package com.hazrat.islam24.main.navigation.nvgraph

sealed class Route(
    val route: String
) {
    /*
    Root nav
     */
    data object RootNav : Route(route = "root_nav")

    /*
    Home screens and other screens
     */
    data object HomeScreen : Route(route = "homeScreen")

    data object PrayerTimeScreen : Route(route = "PrayerTimeScreen")

    data object TasbihScreen : Route(route = "TasbihScreen")

    data object DuasPageScreen : Route(route = "DuasPageScreen")

    data object QiblaDirectionScreen : Route(route = "QiblaDirectionScreen")

    data object CalendarScreen : Route(route = "CalendarScreen")

    data object ZakatScreen : Route(route = "ZakatScreen")

    data object PrayerSetting : Route(route = "PrayerSetting")

    data object AthkarScreen : Route(route = "AthkarScreen")

    /*
    Auth login signup screens
     */
    data object Auth : Route("auth")

    data object ProfileScreen : Route(route = "profileScreen")

    data object NamesOfAllah : Route(route = "NamesOfAllah")

    data object ProfileSettingScreen : Route(route = "profileSettingScreen")

    data object LoginScreen : Route(route = "loginScreen")

    data object SignupScreen : Route(route = "signupScreen")
}