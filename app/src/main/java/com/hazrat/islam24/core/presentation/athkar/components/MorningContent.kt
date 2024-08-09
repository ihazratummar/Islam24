package com.hazrat.islam24.core.presentation.athkar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.islam24.core.domain.model.athkar.MorningAkhtarData
import com.hazrat.islam24.core.domain.model.athkar.morningAthkars
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun MorningContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(morningAthkars) { athkar ->
            AdhkarCard(adhkars = athkar)
        }
    }
}

@Composable
fun AdhkarCard(adhkars: MorningAkhtarData) {


    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.size10)
            .clickable {
                expanded = !expanded
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        elevation = CardDefaults.outlinedCardElevation(dimens.size2),
        border = BorderStroke(dimens.size1, color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${adhkars.number}-${morningAthkars.size - 1}",
                    modifier = Modifier.padding(top = dimens.size20),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = adhkars.transliteration,
                modifier = Modifier.padding(dimens.size10),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            AnimatedVisibility(visible = expanded) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimens.size1,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = adhkars.translation,
                    modifier = Modifier.padding(dimens.size10),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(dimens.size1, MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(dimens.size80)
                        .padding(bottom = dimens.size10),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "X${adhkars.count}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
