package com.hazrat.islam24.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.AuthViewModel
import com.hazrat.islam24.auth.presentation.login.AuthLoginScreen
import com.hazrat.islam24.auth.presentation.login.LoginViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileScreen
import com.hazrat.islam24.auth.presentation.profileSetting.ProfileSettingScreen
import com.hazrat.islam24.auth.presentation.signup.AuthSignupScreen
import com.hazrat.islam24.auth.presentation.signup.SingupViewModel
import com.hazrat.islam24.main.navigation.nvgraph.Route

/**
 * @author Hazrat Ummar Shaikh
 */

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = Route.Auth.route,
        startDestination = Route.ProfileScreen.route
    ) {
        composable(route = Route.LoginScreen.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val loginState = loginViewModel.loginState.collectAsState()
            val loginEvent = loginViewModel::onEvent
            val authState = loginViewModel.authState.observeAsState(initial = AuthState.Loading)
            AuthLoginScreen(
                navController = navController,
                state = loginState.value,
                loginEvent = loginEvent,
                authState = authState.value
            )
        }
        composable(route = Route.SignupScreen.route) {
            val singUpViewModel = hiltViewModel<SingupViewModel>()
            val signUpState = singUpViewModel.state.collectAsState()
            val signUpEvent = singUpViewModel::onEvent
            val authState = singUpViewModel.authState.observeAsState(initial = AuthState.Loading)
            AuthSignupScreen(
                navController = navController,
                signUpState = signUpState.value,
                onEvent = signUpEvent,
                authState = authState.value
            )
        }
        composable(route = Route.ProfileScreen.route) {
            val authViewModel = hiltViewModel<AuthViewModel>()
            val authState = authViewModel.authState.observeAsState(AuthState.Loading)
            val authEvent = authViewModel::onEvent
            ProfileScreen(
                navController = navController,
                state = authState.value,
                authEvent = authEvent
            )
        }
        composable(route = Route.ProfileSettingScreen.route) {
            val authViewModel = hiltViewModel<AuthViewModel>()
            val authState = authViewModel.authState.observeAsState(AuthState.Loading)
            val authEvent = authViewModel::onEvent
            ProfileSettingScreen(
                navController = navController,
                state = authState.value,
                authEvent = authEvent
            )
        }
    }
}