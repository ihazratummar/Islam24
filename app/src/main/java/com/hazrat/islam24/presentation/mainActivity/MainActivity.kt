package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.hazrat.islam24.domain.usecases.app_entry.ReadAppEntry
import com.hazrat.islam24.presentation.nvgraph.NavGraph
import com.hazrat.islam24.presentation.onboarding.OnBoardingScreen
import com.hazrat.islam24.presentation.onboarding.OnBoardingViewModel
import com.hazrat.islam24.ui.theme.DarkGreen
import com.hazrat.islam24.ui.theme.Islam24Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }


        setContent {
            Islam24Theme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkGreen)
                ) {
                    NavGraph(startDestination = viewModel.startDestination.value)
                }
            }
//            locationRepository.getCurrentLocation()


        }
    }

}