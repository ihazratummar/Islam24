package com.hazrat.islam24.core.presentation.home.component

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
import com.hazrat.islam24.core.domain.model.ui_text_model.benefitsOfRecitingDataList
import com.hazrat.islam24.core.presentation.common.BasicTopBar
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
                .padding(horizontal = dimens.size20)
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBar(
                topBarTitle = stringResource(R.string.benefits_of_reciting_the_quran),
                onBackClick = {onBackClick()}
            )
            Spacer(Modifier.height(dimens.size20))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(benefitsOfRecitingDataList) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = dimens.size10),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimens.size10),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${it.number}",
                                modifier = Modifier.padding(dimens.size20),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.weight(1f))
                            Column(
                                modifier = Modifier.padding(dimens.size10),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(it.title),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Spacer(Modifier.height(dimens.size5))

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