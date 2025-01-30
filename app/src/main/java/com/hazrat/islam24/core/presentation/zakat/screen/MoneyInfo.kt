package com.hazrat.islam24.core.presentation.zakat.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.common.BasicTopBar
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyInfo(
    onBack: () -> Unit = {}
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBar(
                topBarTitle = stringResource(id = R.string.money),
                onBackClick = {
                    onBack.invoke()
                }

            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                item {

                    // Main description
                    Text(
                        text = stringResource(R.string.moneyinfo),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Bullet Points for different types of money
                    Text(
                        text = stringResource(R.string.hand_cash_local_currency),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.bank_cash_savings_and_current_accounts),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.international_currency_foreign_money_in_your_possession),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.digital_currency_e_g_cryptocurrencies_e_wallets),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.loans_given_money_owed_to_you_by_others),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Conclusion
                    Text(
                        text = stringResource(R.string.moneyinfo1),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}
