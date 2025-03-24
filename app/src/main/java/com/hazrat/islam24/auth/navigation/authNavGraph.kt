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
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingScreen
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.auth.presentation.forgetPassword.ForgetPassword
import com.hazrat.islam24.auth.presentation.forgetPassword.ForgetPasswordViewModel
import com.hazrat.islam24.auth.presentation.login.AuthLoginScreen
import com.hazrat.islam24.auth.presentation.login.LoginViewModel
import com.hazrat.islam24.auth.presentation.policiesScreen.PoliciesScreen
import com.hazrat.islam24.auth.presentation.policiesScreen.PrivacyPolicyScreen
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileScreen
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileViewModel
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
    appSettingViewModel: AppSettingViewModel,
    isHapticFeedback: Boolean = false
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
                },
                navigateToForgotPassword = {
                    navController.navigate(ForgettingPassword)
                },
                isHapticFeedback = isHapticFeedback
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
                    navController.navigate(Login) {
                        popUpTo(SignUp) {
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
                },
                isHapticFeedback = isHapticFeedback
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
                profileState = profileState,
                isHapticFeedback = isHapticFeedback
            )
        }
        composable<ProfileSettingScreen> {
            val authState by appSettingViewModel.authState.observeAsState(AuthState.Loading)
            val appSettingEvent = appSettingViewModel::onAppSettingEvent
            val appSettingState by appSettingViewModel.appSettingState.collectAsState()
            AppSettingScreen(
                authState = authState,
                appSettingEvent = appSettingEvent,
                appSettingState = appSettingState,
                isHapticFeedback = isHapticFeedback,
                onPolicyClick = {
                    navController.navigate(PoliciesScreenRoute)
                },
                onBackClick = {navController.navigateUp()}
            )
        }
        composable<ProfileDetailsScreen> {
            val profileDetailsViewModel = hiltViewModel<ProfileDetailsViewModel>()
            val appSettingState1 by profileDetailsViewModel.profileState.collectAsState()
            val profileDetailsEvent = profileDetailsViewModel::onEvent
            val userEvent by profileDetailsViewModel.events.collectAsState(initial = null)
            ProfileDetailsScreen(
                navController = navController,
                profileState = appSettingState1,
                profileDetailsEvent = profileDetailsEvent,
                userEvent = userEvent,
                isHapticFeedback = isHapticFeedback
            )
        }
        composable<ForgettingPassword> {
            val forgetPasswordViewModel = hiltViewModel<ForgetPasswordViewModel>()
            val forgetPasswordState by forgetPasswordViewModel.forgetPasswordState.collectAsState()
            val channelEvent by forgetPasswordViewModel.events.collectAsState(initial = null)
            ForgetPassword(
                forgetPasswordState = forgetPasswordState,
                forgetPasswordEvent = forgetPasswordViewModel::onEvent,
                onBackClick = { navController.popBackStack() },
                channelEvent = channelEvent,
                navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(ForgettingPassword) {
                            inclusive = true
                            saveState = false
                        }
                        launchSingleTop = true
                    }
                },
                isHapticFeedback = isHapticFeedback
            )
        }

        composable <PoliciesScreenRoute>{
            PoliciesScreen(
                onBackClick = { navController.popBackStack() },
                onPolicyClick = {navController.navigate(PrivacyPolicyScreenRoute)}
            )
        }

        composable <PrivacyPolicyScreenRoute>{
            PrivacyPolicyScreen(
                onBackClick = { navController.popBackStack() },
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
data object ForgettingPassword


@Serializable
data object ProfileSettingScreen

@Serializable
data object ProfileDetailsScreen

@Serializable
data object PoliciesScreenRoute


@Serializable
data object PrivacyPolicyScreenRoute