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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.hazrat.islam24.core.data.entity.AthkarDataEntity
import com.hazrat.ui.theme.Kitab
import com.hazrat.ui.theme.dimens
import com.hazrat.islam24.util.getSystemLanguage


@Composable
fun AdhkarCard(adhkars: AthkarDataEntity) {

    var expanded by remember {
        mutableStateOf(false)
    }
    val systemLanguage = getSystemLanguage()
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
                    text = "${adhkars.number}",
                    modifier = Modifier.padding(top = dimens.size20),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (adhkars.bismillah.isNotBlank()) {
                Text(
                    text = adhkars.bismillah,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.size10),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Kitab,
                    textAlign = TextAlign.Center,
                    letterSpacing = TextUnit.Unspecified
                )
            }
            Text(
                text = adhkars.arabicText,
                modifier = Modifier.padding(dimens.size10).fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Kitab,
                textAlign = TextAlign.Center,
                letterSpacing = TextUnit.Unspecified
            )
            Text(
                text = when(systemLanguage){
                    "bn" -> adhkars.bnTransliteration
                    else -> adhkars.enTransliteration
                } ,
                modifier = Modifier.padding(dimens.size10),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            AnimatedVisibility(visible = expanded) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimens.size1,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = when(systemLanguage){
                        "bn" -> adhkars.bnTranslation
                        else -> adhkars.enTranslation
                    },
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
