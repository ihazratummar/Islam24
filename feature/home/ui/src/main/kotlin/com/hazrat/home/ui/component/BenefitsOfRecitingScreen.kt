package com.hazrat.home.ui.component

import androidx.annotation.StringRes
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 11-12-2024
 */

@Composable
fun BenefitsOfRecitingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.space20)
        ) {
            Spacer(Modifier.height(dimens.space48))
            BasicTopBar(
                topBarTitle = stringResource(R.string.benefits_of_reciting_the_quran),
                onBackClick = {onBackClick()}
            )
            Spacer(Modifier.height(dimens.space20))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(benefitsOfRecitingDataList) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = dimens.space12),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimens.space12),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${it.number}",
                                modifier = Modifier.padding(dimens.space20),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.weight(1f))
                            Column(
                                modifier = Modifier.padding(dimens.space12),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(it.title),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Spacer(Modifier.height(dimens.space4))

                                Text(
                                    text = stringResource(it.description),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Normal,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


data class BenefitsOfRecitingData(
    val number: Int,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
)


val benefitsOfRecitingDataList = listOf(
    BenefitsOfRecitingData(
        number = 1,
        title = R.string.the_prevalence_of_scientific_miracles,
        description = R.string.the_prevalence_of_scientific_miracles_description
    ),
    BenefitsOfRecitingData(
        number = 2,
        title = R.string.InspirationforScientificResearch,
        description = R.string.InspirationforScientificResearchDescription
    ),
    BenefitsOfRecitingData(
        number = 3,
        title = R.string.integration_of_faith_and_science,
        description = R.string.integration_of_faith_and_science_description
    ),
    BenefitsOfRecitingData(
        number = 4,
        title = R.string.stress_reduction,
        description = R.string.stress_reduction_description
    ),
    BenefitsOfRecitingData(
        number = 5,
        title =  R.string.cognitive_enhancement,
        description = R.string.cognitive_enhancement_description
    ),
    BenefitsOfRecitingData(
        number = 6,
        title = R.string.emotional_well_being,
        description = R.string.emotional_well_being_description
    ),
    BenefitsOfRecitingData(
        number = 7,
        title =  R.string.enhanced_recitation,
        description = R.string.enhanced_recitation_description
    ),
    BenefitsOfRecitingData(
        number = 8,
        title = R.string.clearer_understanding,
        description = R.string.clearer_understanding_description
    ),
)
