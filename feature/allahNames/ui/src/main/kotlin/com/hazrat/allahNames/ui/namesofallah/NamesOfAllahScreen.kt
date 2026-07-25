package com.hazrat.allahNames.ui.namesofallah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.ui.R
import com.hazrat.ui.common.AsmaulHusnaTopAppBar
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.common.OfflineCard
import com.hazrat.ui.theme.Islam24Theme
import com.hazrat.ui.theme.NotoNaskhFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.getSystemLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesOfAllahScreen(
    onBackClick: () -> Unit = {},
    nameEntity: List<NameOfAllahData>,
) {

    Scaffold(
        topBar = {
            AsmaulHusnaTopAppBar(
                onBackClick = onBackClick
            )
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->


        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space20)
        ) {
            if (nameEntity.isNotEmpty()) {
                val chunkedNames = nameEntity.chunked(2)
                items(chunkedNames) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        rowItems.forEach { data ->
                            NameCard(
                                name = data,
                                modifier = Modifier
                                    .padding(dimens.space4)
                                    .weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

            } else {
                items(nameEntity) {
                    OfflineCard()
                }
            }
        }
    }
}

@Composable
private fun HeroNameCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = customColors.secondCardColor
        ),
        shape = RoundedCornerShape(dimens.cornerMd)
    ) {
        Row(
            modifier = Modifier
                .padding(dimens.space12)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.premium_circle),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(dimens.layoutXl)
                )
                Icon(
                    painter = painterResource(R.drawable._01_ar_rahmaan),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(dimens.layoutXs)
                )
            }

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(R.string.home_ar_rahman),
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(

                    text = stringResource(R.string.home_the_most_merciful),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun NameCard(
    modifier: Modifier,
    name: NameOfAllahData
) {
    val systemLanguage = getSystemLanguage()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.cornerMd),
        colors = CardDefaults.cardColors(Color.Transparent),
        border = BorderStroke(
            width = dimens.space2 / 2.5f,
            color = MaterialTheme.colorScheme.onBackground.copy(0.1f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = name.number.toString(),
                modifier = Modifier.padding(dimens.space8).align(Alignment.TopEnd)
            )
            Column(
                modifier = Modifier.padding(dimens.space12).fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val iconRes = getNameOfAllahIcon(name.number)
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.name_ring),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(dimens.layoutXs)
                    )
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(dimens.avatarLg)
                    )
                }

                Text(
                    text = name.transliteration,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.basicMarquee(
                        repeatDelayMillis = 3000,
                        initialDelayMillis = 500,
                        animationMode = MarqueeAnimationMode.Immediately,
                        velocity = 20.dp
                    )
                )
                Text(
                    text = name.enMeaning,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.basicMarquee(
                        repeatDelayMillis = 3000,
                        initialDelayMillis = 500,
                        animationMode = MarqueeAnimationMode.Immediately,
                        velocity = 20.dp
                    )
                )
            }
        }
    }
}