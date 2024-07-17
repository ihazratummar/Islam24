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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.adhkarscreen.model.morningAthkars
import com.hazrat.islam24.R
import com.hazrat.islam24.core.domain.model.athkar.EveningAkhtarData
import com.hazrat.islam24.core.domain.model.athkar.eveningAkhtar
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun EveningContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(com.hazrat.islam24.core.domain.model.athkar.eveningAkhtar) { athkar ->
            AdhkarEveningCard(adhkars = athkar)
        }
    }
}

@Composable
fun AdhkarEveningCard(adhkars: com.hazrat.islam24.core.domain.model.athkar.EveningAkhtarData) {


    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.size10)
            .clickable {
                expanded = !expanded
            },
        colors = CardDefaults.cardColors(colorResource(id = R.color.background_color)),
        elevation = CardDefaults.outlinedCardElevation(MaterialTheme.dimens.size2),
        border = BorderStroke(MaterialTheme.dimens.size1, color = colorResource(id = R.color.primary))
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
                    modifier = Modifier.padding(top = MaterialTheme.dimens.size20),
                    style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.text)
                )
            }
            Text(
                text = adhkars.transliteration,
                modifier = Modifier.padding(MaterialTheme.dimens.size10),
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.text)
            )

            AnimatedVisibility(visible = expanded) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = MaterialTheme.dimens.size1,
                    color = colorResource(id = R.color.primary)
                )
                Text(
                    text = adhkars.translation,
                    modifier = Modifier.padding(MaterialTheme.dimens.size10),
                    style = MaterialTheme.typography.bodyMedium,
                    color =colorResource(id = R.color.text)
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
                    border = BorderStroke(MaterialTheme.dimens.size1, color = colorResource(id = R.color.primary)),
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size80)
                        .padding(bottom = MaterialTheme.dimens.size10),
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
                            color = colorResource(id = R.color.text)
                        )
                    }
                }
            }
        }
    }

}


