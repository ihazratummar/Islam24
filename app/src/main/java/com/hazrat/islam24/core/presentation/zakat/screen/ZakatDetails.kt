package com.hazrat.islam24.core.presentation.zakat.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import com.hazrat.islam24.R
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import com.hazrat.islam24.core.presentation.common.BasicTopBar
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.DateUtil.getDateFromLong

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatDetails(
    modifier: Modifier = Modifier,
    zakatEntity: ZakatEntity,
    onBackClick: () -> Unit = {},
    moneyInfoNav: () -> Unit,
    goldInfoNav: () -> Unit,
    silverInfoNav: () -> Unit,
    monthInfoNav: () -> Unit,
    totalDebtInfoNav: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBar(
                onBackClick = {
                    onBackClick.invoke()
                }

            )
            LazyColumn(
                modifier = modifier
                    .fillMaxSize().padding(horizontal = dimens.size15)
            ) {
                item {
                    CalculateItems(
                        text = stringResource(R.string.money),
                        amount = "${zakatEntity.money}",
                        infoNavigation = {
                            moneyInfoNav()
                        }
                    )
                    CalculateItems(
                        text = stringResource(R.string.gold),
                        amount = "${zakatEntity.gold}",
                        infoNavigation = { goldInfoNav() }
                    )
                    CalculateItems(
                        text = stringResource(R.string.silver),
                        amount = "${zakatEntity.silver}",
                        infoNavigation = { silverInfoNav() }
                    )
                    CalculateItems(
                        text = stringResource(R.string.trade_amount),
                        amount = "${zakatEntity.tradeAmount}",
                    )
                    CalculateItems(
                        text = stringResource(R.string.monthly_living_cost),
                        amount = "${zakatEntity.monthCost}",
                        infoNavigation = { monthInfoNav() },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                    CalculateItems(
                        text = stringResource(R.string.total_debt_on_you),
                        amount = "${zakatEntity.debt}",
                        infoNavigation = { totalDebtInfoNav() },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

}