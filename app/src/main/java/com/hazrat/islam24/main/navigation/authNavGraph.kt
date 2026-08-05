package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.hazrat.auth.ui.appSetting.AppSettingScreen
import com.hazrat.auth.ui.appSetting.ProfileViewModel
import com.hazrat.auth.ui.login.LoginScreen
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.policiesScreen.PoliciesScreen
import com.hazrat.auth.ui.policiesScreen.LegalScreens
import com.hazrat.auth.ui.support.SupportIslam24Screen
import com.hazrat.auth.ui.support.SupportViewModel
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

        composable<MainRoute.ProfileScreen> {
            val appSettingViewModel = koinViewModel<ProfileViewModel>()
            val appSettingEvent = appSettingViewModel::onAppSettingEvent
            val appSettingState by appSettingViewModel.appSettingState.collectAsState()
            AppSettingScreen(
                appSettingEvent = appSettingEvent,
                state = appSettingState,
                isHapticFeedback = isHapticFeedback,
                onPolicyClick = {
                    navController.navigate(PoliciesScreenRoute)
                },
                onAboutUsClick = { link, title ->
                    navController.navigate(LegalScreenRoute(link = link, title = title))
                },
                onSupportClick = {
                    navController.navigate(SupportIslam24Route)
                },
                onAuthClick = {
                    navController.navigate(Login)
                },
                effect = appSettingViewModel.effect
            )
        }

        composable<Login> {

            val viewModel = koinViewModel<LoginViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onTermsClick = {
                    navController.navigate(
                        LegalScreenRoute(
                            link = "https://islam24.app/terms",
                            title = "Terms of Service"
                        )
                    )
                },
                onPrivacyClick = {
                    navController.navigate(
                        LegalScreenRoute(
                            link = "https://islam24.app/privacy-policy",
                            title = "Privacy Policy"
                        )
                    )
                },
                effect = viewModel.effect,
                event = viewModel::event
            )
        }

        composable<PoliciesScreenRoute> {
            PoliciesScreen(
                onBackClick = { navController.popBackStack() },
                onPolicyClick = { link, title ->
                    navController.navigate(LegalScreenRoute(link = link, title = title))
                }
            )
        }

        composable<LegalScreenRoute> { navBackStack ->
            val route = navBackStack.toRoute<LegalScreenRoute>()
            LegalScreens(
                onBackClick = { navController.popBackStack() },
                url = route.link,
                title = route.title
            )
        }

        composable<SupportIslam24Route> {
            val viewModel = koinViewModel<SupportViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            SupportIslam24Screen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                supportEffect = viewModel.effect,
                uiState = state
            )
        }
    }
}

@Serializable
data object Auth

@Serializable
data object Login


@Serializable
data object ForgettingPassword

@Serializable
data object ProfileSettingScreen

@Serializable
data object ProfileDetailsScreen

@Serializable
data object PoliciesScreenRoute

@Serializable
data object SupportIslam24Route

@Serializable
data class LegalScreenRoute(val link: String, val title: String)