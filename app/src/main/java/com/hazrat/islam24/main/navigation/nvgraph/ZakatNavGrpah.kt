package com.hazrat.islam24.main.navigation.nvgraph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.core.presentation.zakat.screen.CalculationScreen
import com.hazrat.islam24.core.presentation.zakat.screen.NisabScreen
import com.hazrat.islam24.core.presentation.zakat.screen.ZakatDetails
import com.hazrat.islam24.core.presentation.zakat.screen.ZakatScreen
import com.hazrat.islam24.main.navigation.MainRoute
import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 */

fun NavGraphBuilder.zakatNavGraph(
    navController: NavController,
    zakatViewModel: ZakatViewModel
) {
    navigation<Zakat>(MainRoute.ZakatScreen) {
        composable<MainRoute.ZakatScreen> {
            val zakatState by zakatViewModel.zakatState.collectAsState()
            ZakatScreen(
                zakatState = zakatState,
                zakatEvent = zakatViewModel::event,
                onNewAddClick = {
                    navController.navigate(NisabScreen)
                },
                openZakat = { id ->
                    navController.navigate(ZakatDetailsScreen(zakatId = id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<NisabScreen> {
            val zakatState by zakatViewModel.zakatState.collectAsState()
            NisabScreen(
                zakatState = zakatState,
                zakatEvent = zakatViewModel::event,
                onSubmit = {
                    navController.navigate(CalculationScreen)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<ZakatDetailsScreen> { backStackEntry ->
            val zakatState by zakatViewModel.zakatState.collectAsState()
            val zakatId = backStackEntry.toRoute<ZakatDetailsScreen>()
            val zakat = zakatState.zakatEntity.find { it.id == zakatId.zakatId }
            if (zakat != null) {
                ZakatDetails(
                    zakatEntity = zakat,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable<CalculationScreen> {
            val zakatState by zakatViewModel.zakatState.collectAsState()
            CalculationScreen(
                zakatState = zakatState,
                zakatEvent = zakatViewModel::event,
                onSaveClick = {
                    navController.navigate(Zakat){
                        popUpTo(MainRoute.ZakatScreen){
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}


@Serializable
data object Zakat


@Serializable
data class ZakatDetailsScreen(val zakatId: String)

@Serializable
data object NisabScreen

@Serializable
data object CalculationScreen

