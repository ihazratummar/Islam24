package com.hazrat.zakat.zakat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.formatCurrency
import com.hazrat.utils.getDeviceCurrencySymbol

@Composable
fun ZakatDetailsBottomSheet(
    modifier: Modifier = Modifier,
    uiState: ZakatListState
) {
    val currencySymbol = getDeviceCurrencySymbol()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.space24)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.space40)
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.zakat),
                    contentDescription = "Zakat Details",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(dimens.iconMd)
                )
            }

            Spacer(modifier = Modifier.width(dimens.space12))

            Column {
                Text(
                    text = stringResource(R.string.zakat_details_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.zakat_details_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.space16))

        DetailRow(label = stringResource(R.string.zakat_cash_savings_title), value = "$currencySymbol${uiState.money}")
        DetailRow(label = stringResource(R.string.zakat_gold_value_title), value = "$currencySymbol${uiState.gold}")
        DetailRow(label = stringResource(R.string.zakat_silver_value_title), value = "$currencySymbol${uiState.silver}")
        DetailRow(label = stringResource(R.string.zakat_business_inventory_title), value = "$currencySymbol${uiState.tradeAmount}")
        DetailRow(label = stringResource(R.string.zakat_monthly_expenses_title), value = "$currencySymbol${uiState.monthCost}")
        DetailRow(label = stringResource(R.string.zakat_outstanding_debts_title), value = "$currencySymbol${uiState.debt}")

        Spacer(modifier = Modifier.height(dimens.space12))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = dimens.divider
        )
        Spacer(modifier = Modifier.height(dimens.space12))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.zakat_net_assets),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatCurrency(uiState.totalAsset),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.zakat_payable),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatCurrency(uiState.zakatAmount),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = customColors.accentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.space24))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.space4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
