package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.model.AuthState
import com.hazrat.auth.ui.appSetting.AppSettingScreen
import com.hazrat.auth.ui.appSetting.AppSettingViewModel
import com.hazrat.auth.ui.forgetPassword.ForgetPassword
import com.hazrat.auth.ui.forgetPassword.ForgetPasswordViewModel
import com.hazrat.auth.ui.login.AuthLoginScreen
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.policiesScreen.PoliciesScreen
import com.hazrat.auth.ui.policiesScreen.PrivacyPolicyScreen
import com.hazrat.auth.ui.profileScreen.ProfileScreen
import com.hazrat.auth.ui.profileScreen.ProfileViewModel
import com.hazrat.auth.ui.profiledetails.ProfileDetailsScreen
import com.hazrat.auth.ui.profiledetails.ProfileDetailsViewModel
import com.hazrat.auth.ui.signup.AuthSignupScreen
import com.hazrat.auth.ui.signup.SignUpViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

/**
 * @author Hazrat Ummar Shaikh
 */

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    isHapticFeedback: Boolean = false
) {
    navigation<Auth>(startDestination = MainRoute.ProfileScreen) {
        composable<Login> {
            val loginViewModel = koinViewModel <LoginViewModel>()
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
                        popUpTo(Login)
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
            val signUpViewModel = koinViewModel <SignUpViewModel>()
            val signUpState by signUpViewModel.state.collectAsState()
            val signUpEvent = signUpViewModel::onEvent
            val authState by signUpViewModel.authState.observeAsState(initial = AuthState.Loading)
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
            val profileViewModel = koinViewModel <ProfileViewModel>()

            val authState by profileViewModel.authState.observeAsState(AuthState.Loading)
            val profileState by profileViewModel.profileState.collectAsState()
            ProfileScreen(
                authState = authState,
                profileState = profileState,
                onSettingClick = {
                    navController.navigate(ProfileSettingScreen) {
                        popUpTo(ProfileSettingScreen) {
                            inclusive = true
                            saveState = false
                        }
                        launchSingleTop = true
                    }
                },
                navigateToLogin = { navController.navigate(Login) },
                navigateToProfileDetails = {
                    navController.navigate(ProfileDetailsScreen) {
                        popUpTo(ProfileDetailsScreen) {
                            inclusive = true
                            saveState = true
                        }
                    }
                }
            )
        }
        composable<ProfileSettingScreen> {
            val appSettingViewModel = koinViewModel <AppSettingViewModel>()
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
                onBackClick = {navController.navigateUp()},
            )
        }
        composable<ProfileDetailsScreen> {
            val profileDetailsViewModel = koinViewModel<ProfileDetailsViewModel>()
            val appSettingState1 by profileDetailsViewModel.profileState.collectAsState()
            val profileDetailsEvent = profileDetailsViewModel::onEvent
            val userEvent by profileDetailsViewModel.events.collectAsState(initial = null)
            ProfileDetailsScreen(
                onBackClick = { navController.popBackStack() },
                profileState = appSettingState1,
                profileDetailsEvent = profileDetailsEvent,
                userEvent = userEvent,
                isHapticFeedback = isHapticFeedback
            )
        }
        composable<ForgettingPassword> {
            val forgetPasswordViewModel = koinViewModel <ForgetPasswordViewModel>()
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