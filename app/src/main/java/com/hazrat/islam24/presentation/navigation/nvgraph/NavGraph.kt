package com.hazrat.islam24.presentation.navigation.nvgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.presentation.navigation.AppNavigator

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination){
        navigation(
            route = Route.HomeNavigation.route,
            startDestination = Route.HomeScreen.route
        ){
            composable(
                route = Route.HomeScreen.route
            ){
                AppNavigator()
            }
        }
    }

}