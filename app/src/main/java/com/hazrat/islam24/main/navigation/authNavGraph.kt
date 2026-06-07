package com.hazrat.islam24.main.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.hazrat.auth.ui.appSetting.AppSettingScreen
import com.hazrat.auth.ui.appSetting.AppSettingViewModel
import com.hazrat.auth.ui.policiesScreen.PoliciesScreen
import com.hazrat.auth.ui.policiesScreen.LegalScreens
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
            val appSettingViewModel = koinViewModel<AppSettingViewModel>()
            val appSettingEvent = appSettingViewModel::onAppSettingEvent
            val appSettingState by appSettingViewModel.appSettingState.collectAsState()
            AppSettingScreen(
                appSettingEvent = appSettingEvent,
                appSettingState = appSettingState,
                isHapticFeedback = isHapticFeedback,
                onPolicyClick = {
                    navController.navigate(PoliciesScreenRoute)
                },
                onAboutUsClick = { link, title ->
                    navController.navigate(LegalScreenRoute(link = link, title = title))
                }
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

        composable<LegalScreenRoute> {navBackStack ->

            val route = navBackStack.toRoute<LegalScreenRoute>()
            LegalScreens(
                onBackClick = { navController.popBackStack() },
                url = route.link,
                title = route.title
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
data class LegalScreenRoute(val link: String, val title: String)