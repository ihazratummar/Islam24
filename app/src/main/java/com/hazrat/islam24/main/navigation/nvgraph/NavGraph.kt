package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hazrat.auth.ui.appSetting.AppSettingViewModel
import com.hazrat.auth.ui.forgetPassword.ForgetPasswordViewModel
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.profileScreen.ProfileViewModel
import com.hazrat.auth.ui.profiledetails.ProfileDetailsViewModel
import com.hazrat.auth.ui.signup.SignUpViewModel
import com.hazrat.alQuran.ui.QuranViewModel
import com.hazrat.prayer.ui.PrayerTimeViewModel
import com.hazrat.islam24.main.navigation.AppNavigator
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.zakat.screen.zakat.ZakatViewModel
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    zakatViewModel: ZakatViewModel,
    quranViewModel: QuranViewModel,
    prayerTimeViewModel : PrayerTimeViewModel,
    appSettingViewModel : AppSettingViewModel,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    profileDetailsViewModel: ProfileDetailsViewModel,
    forgetPasswordViewModel: ForgetPasswordViewModel,
    isHapticFeedback : Boolean = false
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RootNav,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation<RootNav>(startDestination = MainRoute.HomeScreen) {
            composable<MainRoute.HomeScreen> {
                AppNavigator(
                    zakatViewModel = zakatViewModel,
                    quranViewModel = quranViewModel,
                    prayerTimeViewModel = prayerTimeViewModel,
                    appSettingViewModel = appSettingViewModel,
                    isHapticFeedback = isHapticFeedback,
                    profileViewModel = profileViewModel,
                    loginViewModel = loginViewModel,
                    signUpViewModel = signUpViewModel,
                    profileDetailsViewModel = profileDetailsViewModel,
                    forgetPasswordViewModel = forgetPasswordViewModel
                )
            }
        }
    }
}

@Serializable
data object RootNav