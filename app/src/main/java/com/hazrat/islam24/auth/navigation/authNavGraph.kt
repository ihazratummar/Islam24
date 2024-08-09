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
import com.hazrat.islam24.auth.AuthViewModel
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingEvent
import com.hazrat.islam24.auth.presentation.login.AuthLoginScreen
import com.hazrat.islam24.auth.presentation.login.LoginViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileScreen
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileViewModel
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingScreen
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingState
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.auth.presentation.signup.AuthSignupScreen
import com.hazrat.islam24.auth.presentation.signup.SingupViewModel
import com.hazrat.islam24.main.navigation.ProfileScreen
import com.hazrat.islam24.main.navigation.nvgraph.NavGraph
import com.hazrat.islam24.ui.theme.Islam24Theme
import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 */

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    appSettingState: AppSettingState,
    appSettingEvent : (AppSettingEvent) -> Unit
) {
    navigation<Auth>(startDestination = ProfileScreen) {
        composable<Login> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val loginState by loginViewModel.loginState.collectAsState()
            val loginEvent = loginViewModel::onEvent
            val authState by loginViewModel.authState.observeAsState(initial = AuthState.Loading)
            AuthLoginScreen(
                navController = navController,
                state = loginState,
                loginEvent = loginEvent,
                authState = authState
            )
        }
        composable<SignUp> {
            val singUpViewModel = hiltViewModel<SingupViewModel>()
            val signUpState by singUpViewModel.state.collectAsState()
            val signUpEvent = singUpViewModel::onEvent
            val authState by singUpViewModel.authState.observeAsState(initial = AuthState.Loading)
            AuthSignupScreen(
                navController = navController,
                signUpState = signUpState,
                onEvent = signUpEvent,
                authState = authState
            )
        }
        composable<ProfileScreen> {
            val authViewModel = hiltViewModel<AuthViewModel>()
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            val authState by authViewModel.authState.observeAsState(AuthState.Loading)
            val authEvent = authViewModel::onEvent
            val profileEvent = profileViewModel::onEvent
            ProfileScreen(
                navController = navController,
                state = authState,
                authEvent = authEvent,
                profileEvent = profileEvent
            )
        }
        composable<ProfileSettingScreen> {
            val authViewModel = hiltViewModel<AuthViewModel>()
            val authState by authViewModel.authState.observeAsState(AuthState.Loading)
            val authEvent = authViewModel::onEvent
//            val appSettingViewModel = hiltViewModel<AppSettingViewModel>()
//            val appSettingState1 by appSettingViewModel.appSettingState.collectAsState()
//            val appSettingEvent1 = appSettingViewModel::onAppSettingEvent
            AppSettingScreen(
                navController = navController,
                state = authState,
                authEvent = authEvent,
                appSettingState = appSettingState,
                appSettingEvent = appSettingEvent
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
