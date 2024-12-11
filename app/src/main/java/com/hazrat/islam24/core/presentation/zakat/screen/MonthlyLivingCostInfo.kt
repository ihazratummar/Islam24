package com.hazrat.islam24.core.presentation.zakat.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
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
fun MonthlyLivingCostInfo(
    onBack: () -> Unit
) {


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size30))
            BasicTopBar(
                topBarTitle = stringResource(id = R.string.monthly_living_cost),
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
                    // Main definition
                    Text(
                        text = stringResource(R.string.monthlyinfo),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Bullet points for key aspects
                item {
                    Text(
                        text = stringResource(R.string.key_points_to_consider),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Housing expenses
                item {
                    BulletPoint(text = stringResource(R.string.housing_expenses_rent_mortgage_utilities_home_maintenance))
                }

                // Food and groceries
                item {
                    BulletPoint(text = stringResource(R.string.food_groceries_monthly_food_and_household_supplies))
                }

                // Transportation
                item {
                    BulletPoint(text = stringResource(R.string.transportation_costs_like_fuel_car_payments_or_public_transport))
                }

                // Healthcare
                item {
                    BulletPoint(text = stringResource(R.string.healthcare_health_insurance_medications_and_medical_visits))
                }

                // Education
                item {
                    BulletPoint(text = stringResource(R.string.education_tuition_fees_school_supplies_for_children_or_yourself))
                }

                // Communication
                item {
                    BulletPoint(text = stringResource(R.string.communication_internet_costs_for_phone_internet_and_communication_services))
                }

                // Miscellaneous
                item {
                    BulletPoint(text = stringResource(R.string.miscellaneous_essential_recurring_costs_like_child_or_elderly_care))
                }

                // Additional Note
                item {
                    Text(
                        text = stringResource(R.string.note_exclude_luxury_expenses_such_as_vacations_luxury_goods_or_excessive_entertainment),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
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
