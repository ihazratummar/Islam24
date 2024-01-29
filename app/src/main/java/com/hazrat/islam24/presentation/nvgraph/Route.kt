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

}