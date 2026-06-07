package com.hazrat.allahNames.ui.namesofallah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.common.OfflineCard
import com.hazrat.ui.theme.NotoNaskhFontFamily
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.getSystemLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesOfAllahScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    nameEntity: List<NameOfAllahData>,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.space32))
            BasicTopBar(
                modifier = Modifier,
                topBarTitle = stringResource(R.string.names),
                onBackClick = { onBackClick.invoke() }
            )
            LazyColumn(
                modifier = Modifier
            ) {
                if (nameEntity.isNotEmpty()) {
                    items(nameEntity) { name ->
                        NameCard(name = name)
                    }
                } else {
                    items(nameEntity) {
                        OfflineCard()
                    }
                }
            }
        }
    }
}


@Composable
fun NameCard(name: NameOfAllahData) {
    val systemLanguage = getSystemLanguage()
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimens.space8)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                expanded = !expanded
            }
            .padding(
                horizontal = dimens.space16,
                vertical = dimens.space2
            ),
        shape = RoundedCornerShape(dimens.cornerMd),
        colors = CardDefaults.cardColors(Color.Transparent),
        border = BorderStroke(
            dimens.divider, color = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                        .padding(
                            start = dimens.space4,
                            top = dimens.space20
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Text(
                        text = "${name.number}", style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(dimens.space4)
                ) {
                    Text(
                        text = when (systemLanguage) {
                            "bn" -> name.bnTransliteration
                            else -> name.transliteration
                        }, style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = when (systemLanguage) {
                            "bn" -> name.bnMeaning
                            else -> name.enMeaning
                        }, style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(dimens.space4))
                    Icon(
                        painter = if (expanded) painterResource(R.drawable.arrowup) else painterResource(R.drawable.down_arrow),
                        contentDescription = "Arrow",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.8f)
                        .padding(dimens.space4),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name.name,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = NotoNaskhFontFamily
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                HorizontalDivider(
                    thickness = dimens.divider,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = dimens.space32,
                            top = dimens.space20, end = dimens.space12,
                            bottom = dimens.space12
                        )
                ) {
                    Text(
                        text = "Ayat: ${name.found}",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(dimens.space4))
                    Text(
                        text = when (systemLanguage) {
                            "bn" -> name.bnDec ?: ""
                            else -> name.enDesc
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}