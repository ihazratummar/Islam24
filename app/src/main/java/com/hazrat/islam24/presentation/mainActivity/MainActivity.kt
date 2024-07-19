package com.hazrat.islam24.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hazrat.islam24.presentation.navigation.nvgraph.NavGraph
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.islam24.ui.theme.Islam24Theme
import com.hazrat.islam24.service.LocationHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//MainActivity.kt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var updateManager: UpdateManager

    @Inject
    lateinit var locationHandler: LocationHandler

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }
        setContent {
            Islam24Theme {
                NavGraph(startDestination = viewModel.startDestination.value)
            }
        }
        updateManager.checkForAppUpdates(this)
        locationHandler.showLocationPermissionDialog(this)
    }



    override fun onResume() {
        super.onResume()
        updateManager.onResume(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        updateManager.onDestroy()
    }
}