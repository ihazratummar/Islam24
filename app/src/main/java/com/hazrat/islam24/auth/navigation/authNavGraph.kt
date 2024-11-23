package com.hazrat.islam24.auth.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingEvent
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingScreen
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingState
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.auth.presentation.login.AuthLoginScreen
import com.hazrat.islam24.auth.presentation.login.LoginViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileScreen
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileViewModel
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileAction
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileDetailsScreen
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileDetailsViewModel
import com.hazrat.islam24.auth.presentation.signup.AuthSignupScreen
import com.hazrat.islam24.auth.presentation.signup.SingupViewModel
import com.hazrat.islam24.main.navigation.MainRoute
import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 */

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    appSettingState: AppSettingState,
    appSettingEvent: (AppSettingEvent) -> Unit
) {
    navigation<Auth>(startDestination = MainRoute.ProfileScreen) {
        composable<Login> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val loginState by loginViewModel.loginState.collectAsState()
            val loginEvent = loginViewModel::onEvent
            val authState by loginViewModel.authState.observeAsState(initial = AuthState.Loading)
            AuthLoginScreen(
                state = loginState,
                loginEvent = loginEvent,
                authState = authState,
                navigateToSignup = {
                    navController.navigate(SignUp)
                },
                navigateToProfile = {
                    navController.navigate(MainRoute.ProfileScreen) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<SignUp> {
            val singUpViewModel = hiltViewModel<SingupViewModel>()
            val signUpState by singUpViewModel.state.collectAsState()
            val signUpEvent = singUpViewModel::onEvent
            val authState by singUpViewModel.authState.observeAsState(initial = AuthState.Loading)
            AuthSignupScreen(
                signUpState = signUpState,
                onEvent = signUpEvent,
                authState = authState,
                onBackClick = {
                    navController.popBackStack()
                },
                navigateToLogin = {
                    navController.navigate(Login){
                        popUpTo(SignUp){
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToProfile = {
                    navController.navigate(MainRoute.ProfileScreen) {
                        popUpTo(SignUp) { inclusive = true }
                    }
                }
            )
        }
        composable<MainRoute.ProfileScreen> {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            val authState by profileViewModel.authState.observeAsState(AuthState.Loading)
            val profileState by profileViewModel.profileState.collectAsState()
            val profileEvent = profileViewModel::onEvent
            ProfileScreen(
                navController = navController,
                authState = authState,
                profileEvent = profileEvent,
                profileState = profileState
            )
        }
        composable<ProfileSettingScreen> {
            val appSettingViewModel = hiltViewModel<AppSettingViewModel>()
            val authState by appSettingViewModel.authState.observeAsState(AuthState.Loading)
            val appSettingState1 by appSettingViewModel.appSettingState.collectAsState()
            val appSettingEvent1 = appSettingViewModel::onAppSettingEvent
            AppSettingScreen(
                navController = navController,
                authState = authState,
                appSettingState = appSettingState1,
                appSettingEvent = appSettingEvent1,
                appSettingStateTheme = appSettingState,
                appSettingEventTheme = appSettingEvent
            )
        }
        composable<ProfileDetailsScreen> {
            val profileDetailsViewModel = hiltViewModel<ProfileDetailsViewModel>()
            val appSettingState1 by profileDetailsViewModel.profileState.collectAsState()
            val profileDetailsEvent = profileDetailsViewModel::onEvent
            val profileAction by profileDetailsViewModel.profileActionState.observeAsState(initial = ProfileAction.Idle)
            val userEvent by profileDetailsViewModel.events.collectAsState(initial = null)
            ProfileDetailsScreen(
                navController = navController,
                profileState = appSettingState1,
                profileDetailsEvent = profileDetailsEvent,
                userEvent = userEvent
            )
        }
    }
}


@Serializable
data object Auth

@Serializable
data object Login

@Serializable
data object SignUp

@Serializable
data object ProfileSettingScreen

@Serializable
data object ProfileDetailsScreen
