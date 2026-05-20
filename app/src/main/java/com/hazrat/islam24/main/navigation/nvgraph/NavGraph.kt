package com.hazrat.islam24.main.navigation.nvgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hazrat.islam24.main.navigation.AppNavigator
import com.hazrat.islam24.main.navigation.MainRoute
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    isHapticFeedback: Boolean = false
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootNav,
        // ✅ Keep simple — this only wraps AppNavigator
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
    ) {
        navigation<RootNav>(startDestination = MainRoute.HomeScreen) {
            composable<MainRoute.HomeScreen> {
                AppNavigator(
                    isHapticFeedback = isHapticFeedback
                )
            }
        }
    }
}

@Serializable
data object RootNav