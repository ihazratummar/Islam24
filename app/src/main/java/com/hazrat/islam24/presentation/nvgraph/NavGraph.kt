package com.hazrat.islam24.presentation.nvgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.presentation.navigator.AppNavigator
import com.hazrat.islam24.presentation.onboarding.OnBoardingScreen
import com.hazrat.islam24.presentation.onboarding.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination){
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ){
            composable(route = Route.OnBoardingScreen.route){
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(onEvent = viewModel::onEvent)
            }
        }
        navigation(
            route = Route.HomeNavigation.route,
            startDestination = Route.HomeNavigatorScreen.route
        ){
            composable(
                route = Route.HomeNavigatorScreen.route
            ){
                AppNavigator()
            }
        }
    }

}