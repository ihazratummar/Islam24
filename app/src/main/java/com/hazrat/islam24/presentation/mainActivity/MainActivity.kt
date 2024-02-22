package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hazrat.islam24.presentation.nvgraph.NavGraph
import com.hazrat.islam24.ui.theme.DarkGreen
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.util.LocationHandler
import com.hazrat.islam24.domain.repository.location.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//MainActivity.kt

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    @Inject
    lateinit var locationRepository: LocationRepository
    private lateinit var locationHandler: LocationHandler

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.hashCode(), Color.Transparent.hashCode()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.hashCode(), Color.Transparent.hashCode()
            )
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }
        locationHandler = LocationHandler(this, locationRepository)
        setContent {
            Islam24Theme {
                NavGraph(startDestination = viewModel.startDestination.value)
            }
        }
    }
}