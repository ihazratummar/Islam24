package com.hazrat.islam24.main.navigation.nvgraph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.zakat.screen.zakat.ZakatViewModel
import com.hazrat.zakat.screen.zakat.screen.CalculationScreen
import com.hazrat.zakat.screen.zakat.screen.NisabScreen
import com.hazrat.zakat.screen.zakat.screen.zakat_screen.ZakatScreen
import com.hazrat.zakat.screen.zakat.screen.zakat_screen.ZakatScreenViewModel
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
            val zakatScreenViewModel: ZakatScreenViewModel = hiltViewModel()
            val zakatScreenState by zakatScreenViewModel.zakatState.collectAsState()
            ZakatScreen(
                zakatScreenState =zakatScreenState ,
                zakatScreenEvent = zakatScreenViewModel::zakatScreenEvent,
                onNewAddClick = {
                    navController.navigate(NisabScreen)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                getZakatDetails = {
                    zakatScreenViewModel.getZakatDetails(it)
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
data object NisabScreen

@Serializable
data object CalculationScreen

