package com.hazrat.islam24.main.navigation.nvgraph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.hazrat.islam24.core.presentation.zakat.ZakatViewModel
import com.hazrat.islam24.core.presentation.zakat.screen.CalculationScreen
import com.hazrat.islam24.core.presentation.zakat.screen.GoldInfo
import com.hazrat.islam24.core.presentation.zakat.screen.MoneyInfo
import com.hazrat.islam24.core.presentation.zakat.screen.MonthlyLivingCostInfo
import com.hazrat.islam24.core.presentation.zakat.screen.NisabScreen
import com.hazrat.islam24.core.presentation.zakat.screen.SilverInfo
import com.hazrat.islam24.core.presentation.zakat.screen.TotalDebtInfo
import com.hazrat.islam24.core.presentation.zakat.screen.ZakatDetails
import com.hazrat.islam24.core.presentation.zakat.screen.ZakatScreen
import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 */

fun NavGraphBuilder.zakatNavGraph(
    navController: NavController,
    zakatViewModel: ZakatViewModel
) {
    navigation<Zakat>(ZakatScreen) {
        composable<ZakatScreen> {
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
                    },
                    moneyInfoNav = {
                        navController.navigate(MoneyInfoScreen)
                    },
                    goldInfoNav = {
                        navController.navigate(GoldInfoScreen)
                    },
                    silverInfoNav = {
                        navController.navigate(SilverInfoScreen)
                    },
                    monthInfoNav = {
                        navController.navigate(MonthlyLivingCostInfoScreen)
                    },
                    totalDebtInfoNav = {
                        navController.navigate(TotalDebtInfoScreen)
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
                        popUpTo(ZakatScreen){
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                moneyInfoNav = {
                    navController.navigate(MoneyInfoScreen)
                },
                goldInfoNav = {
                    navController.navigate(GoldInfoScreen)
                },
                silverInfoNav = {
                    navController.navigate(SilverInfoScreen)
                },
                monthInfoNav = {
                    navController.navigate(MonthlyLivingCostInfoScreen)
                },
                totalDebtInfoNav = {
                    navController.navigate(TotalDebtInfoScreen)
                }
            )
        }

        composable<MonthlyLivingCostInfoScreen> {
            MonthlyLivingCostInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<TotalDebtInfoScreen> {
            TotalDebtInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<GoldInfoScreen> {
            GoldInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<SilverInfoScreen> {
            SilverInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<MoneyInfoScreen> {
            MoneyInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}


@Serializable
data object Zakat

@Serializable
data object ZakatScreen

@Serializable
data class ZakatDetailsScreen(val zakatId: Int)

@Serializable
data object NisabScreen

@Serializable
data object CalculationScreen

@Serializable
data object MonthlyLivingCostInfoScreen

@Serializable
data object TotalDebtInfoScreen

@Serializable
data object GoldInfoScreen

@Serializable
data object SilverInfoScreen

@Serializable
data object MoneyInfoScreen
