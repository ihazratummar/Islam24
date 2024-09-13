package com.hazrat.islam24.core.presentation.zakat.screen

/**
 * @author Hazrat Ummar Shaikh
 */

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
fun GoldInfo(
    onBack: () -> Unit = {}
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Gold") },
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
                Text(
                    text = "Zakat on Gold should be calculated at 2.5% of the current market value of the gold you own as on the date of valuation. " +
                            "This applies to gold in all forms, whether it's jewelry, coins, or bars, provided it reaches or exceeds the Nisab threshold.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}
