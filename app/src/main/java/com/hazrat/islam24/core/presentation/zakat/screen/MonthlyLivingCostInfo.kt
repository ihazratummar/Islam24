package com.hazrat.islam24.core.presentation.zakat.screen

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyLivingCostInfo(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Living Cost") },
                navigationIcon = { IconButton(onClick = { onBack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null )
                } }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                // Title
                Text(
                    text = "What is Monthly Living Cost?",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Main definition
                Text(
                    text = "Monthly Living Cost refers to the total expenses you need to cover essential needs and live comfortably within a month. These are the basic expenses you should consider when calculating Zakat. Below are some key points to help you understand what to include:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Bullet points for key aspects
            item {
                Text(
                    text = "Key Points to Consider:",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Housing expenses
            item {
                BulletPoint(text = "Housing Expenses: Rent/mortgage, utilities, home maintenance.")
            }

            // Food and groceries
            item {
                BulletPoint(text = "Food & Groceries: Monthly food and household supplies.")
            }

            // Transportation
            item {
                BulletPoint(text = "Transportation: Costs like fuel, car payments, or public transport.")
            }

            // Healthcare
            item {
                BulletPoint(text = "Healthcare: Health insurance, medications, and medical visits.")
            }

            // Education
            item {
                BulletPoint(text = "Education: Tuition fees, school supplies for children or yourself.")
            }

            // Communication
            item {
                BulletPoint(text = "Communication & Internet: Costs for phone, internet, and communication services.")
            }

            // Miscellaneous
            item {
                BulletPoint(text = "Miscellaneous: Essential recurring costs like child or elderly care.")
            }

            // Additional Note
            item {
                Text(
                    text = "Note: Exclude luxury expenses such as vacations, luxury goods, or excessive entertainment.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
