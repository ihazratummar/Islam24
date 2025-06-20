package com.hazrat.zakat.screen.zakat.screen.zakat_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.hazrat.zakat.screen.zakat.screen.CalculateItems

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatDetails(
    modifier: Modifier = Modifier,
    zakatState: ZakatScreenState
) {


    val listDetailsItem = listOf(
        ZakatDetailsItem(
            text = stringResource(R.string.money),
            amount = zakatState.money
        ),
        ZakatDetailsItem(
            text = stringResource(R.string.gold),
            amount = zakatState.gold
        ),
        ZakatDetailsItem(
            text = stringResource(R.string.silver),
            amount = zakatState.silver
        ),
        ZakatDetailsItem(
            text = stringResource(R.string.trade_amount),
            amount = zakatState.tradeAmount
        ),
        ZakatDetailsItem(
            text = stringResource(R.string.monthly_living_cost),
            amount = zakatState.monthCost,
            fontColor = MaterialTheme.colorScheme.error
        ),
        ZakatDetailsItem(
            text = stringResource(R.string.total_debt_on_you),
            amount = zakatState.debt,
            fontColor = MaterialTheme.colorScheme.error
        ),
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimens.size15)
            ) {
                items(listDetailsItem) {
                    CalculateItems(
                        text = it.text,
                        amount = it.amount,
                        fontColor = it.fontColor ?: MaterialTheme.colorScheme.onBackground
                    )
                }

                item {

                    Spacer(Modifier.height(dimens.size15))
                    HorizontalDivider()
                    Spacer(Modifier.height(dimens.size15))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(dimens.size15))
                            .padding(dimens.size20)
                            ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total: ${zakatState.totalAsset}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Text(
                            text = "Zakat: ${zakatState.zakatAmount}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

            }
        }
    }
}

data class ZakatDetailsItem(
    val text: String,
    val amount: String,
    val fontColor: Color? = null
)