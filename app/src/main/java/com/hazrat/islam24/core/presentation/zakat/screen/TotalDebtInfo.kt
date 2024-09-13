package com.hazrat.islam24.core.presentation.zakat.screen

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalDebtInfo(
    onBack: () -> Unit = {}
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Debt") },
                navigationIcon = { IconButton(onClick = { onBack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null )
                } }
            )
        }
    ){ innderpadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innderpadding)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            item {
                // Main definition
                Text(
                    text = "Total Debt on You refers to the total amount you owe to others, including personal loans, credit card debt, mortgages, or any other financial obligations. " +
                            "When calculating Zakat, it is essential to consider this amount, as it can be deducted from your total assets. This ensures that Zakat is only paid on the net wealth that you truly own after accounting for your debts.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Additional explanation
                Text(
                    text = "If you have any outstanding loans or debts, reduce the total amount of debt from your assets before calculating Zakat. This provides an accurate picture of your net wealth, ensuring that Zakat is only paid on the wealth free from obligations.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}
