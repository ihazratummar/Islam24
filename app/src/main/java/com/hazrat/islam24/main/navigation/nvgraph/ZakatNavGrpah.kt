package com.hazrat.islam24.main.navigation.nvgraph

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.home.ui.component.HomeRoutes
import com.hazrat.zakat.zakat_calculation.ZakatCalculationScreen
import com.hazrat.zakat.zakat_calculation.ZakatCalculationViewModel
import com.hazrat.zakat.zakat_list.ZakatListScreen
import com.hazrat.zakat.zakat_list.ZakatListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

/**
 * @author Hazrat Ummar Shaikh
 */

fun NavGraphBuilder.zakatNavGraph(
    navController: NavController
) {
    navigation<Zakat>(HomeRoutes.Zakat) {
        composable<HomeRoutes.Zakat> {
            val zakatListViewModel: ZakatListViewModel = koinViewModel()
            val uiState by zakatListViewModel.uiState.collectAsStateWithLifecycle()
            ZakatListScreen(
                uiState = uiState,
                onEvent = zakatListViewModel::onEvent,
                onNewAddClick = {
                    navController.navigate(CalculationScreen)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                getZakatDetails = {
                    zakatListViewModel.getZakatDetails(it)
                }
            )
        }

        composable<CalculationScreen> {
            val zakatCalculationViewModel: ZakatCalculationViewModel = koinViewModel()
            val uiState by zakatCalculationViewModel.uiState.collectAsStateWithLifecycle()
            ZakatCalculationScreen(
                uiState = uiState,
                onEvent = zakatCalculationViewModel::onEvent,
                onSaveClick = {
                    navController.popBackStack()
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
data object CalculationScreen
