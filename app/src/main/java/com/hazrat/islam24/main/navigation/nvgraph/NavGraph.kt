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
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.auth.presentation.forgetPassword.ForgetPasswordViewModel
import com.hazrat.islam24.auth.presentation.login.LoginViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileViewModel
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileDetailsViewModel
import com.hazrat.islam24.auth.presentation.signup.SingupViewModel
import com.hazrat.islam24.core.presentation.al_quran.QuranViewModel
import com.hazrat.islam24.core.presentation.home.HomeViewModel
import com.hazrat.islam24.core.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.core.presentation.qibla.QiblaViewModel
import com.hazrat.islam24.main.mainActivity.MainViewModel
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
    qiblaViewModel: QiblaViewModel,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    signUpViewModel: SingupViewModel,
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
                    qiblaViewModel = qiblaViewModel,
                    mainViewModel = mainViewModel,
                    homeViewModel = homeViewModel,
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