package com.hazrat.islam24.core.presentation.zakat.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import com.hazrat.islam24.core.presentation.zakat.ZakatEvent
import com.hazrat.islam24.util.DateUtil.getDateFromLong

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatDetails(
    modifier: Modifier = Modifier,
    zakatEntity: ZakatEntity,
    onBackClick:() -> Unit ={},
    moneyInfoNav:() -> Unit,
    goldInfoNav:() -> Unit,
    silverInfoNav:() -> Unit,
    monthInfoNav:() -> Unit,
    totalDebtInfoNav:() -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = getDateFromLong(zakatEntity.date)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                CalculateItems(
                    text = "Money",
                    amount = "${zakatEntity.money}",
                    infoNavigation = {
                        moneyInfoNav()
                    }
                )
                CalculateItems(
                    text = "Gold",
                    amount = "${zakatEntity.gold}",
                    infoNavigation = { goldInfoNav() }
                )
                CalculateItems(
                    text = "Silver",
                    amount = "${zakatEntity.silver}",
                    infoNavigation = { silverInfoNav() }
                )
                CalculateItems(
                    text = "Trade Amount",
                    amount = "${zakatEntity.tradeAmount}",
                )
                CalculateItems(
                    text = "Monthly Living Cost",
                    amount = "${zakatEntity.monthCost}",
                    infoNavigation = { monthInfoNav() },
                    fontColor = MaterialTheme.colorScheme.error
                )
                CalculateItems(
                    text = "Total Debt on you",
                    amount = "${zakatEntity.debt}",
                    infoNavigation = { totalDebtInfoNav() },
                    fontColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}