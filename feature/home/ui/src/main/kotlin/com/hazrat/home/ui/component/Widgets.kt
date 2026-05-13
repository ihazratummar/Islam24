package com.hazrat.home.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.ui.R
import com.hazrat.ui.theme.EmeraldGreen
import com.hazrat.ui.theme.dimens
import kotlinx.serialization.Serializable

@Composable
fun LazyHorizontalManyIcons(
    onClick: (HomePageNavIcons) -> Unit
) {
    val navIcons = listOf(
        HomePageNavIcons.AsmaUlHusna,
        HomePageNavIcons.Calendar,
        HomePageNavIcons.Athkar,
        HomePageNavIcons.Qibla,
        HomePageNavIcons.Zakat
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navIcons.forEach {
                item {
                    HomeWidgetsIcons(
                        iconData = it,
                        onClick = { onClick(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeWidgetsIcons(
    iconData: HomePageNavIcons,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val containerColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else Color.White

    Column(
        modifier = Modifier.padding(vertical = dimens.size10, horizontal = dimens.size10),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(dimens.size80)
                .padding(dimens.size2)
                .border(
                    width = dimens.size1,
                    color = EmeraldGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(dimens.size20)
                )
                .combinedClickable(
                    onClick = { onClick() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .clip(RoundedCornerShape(dimens.size20)),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.size2)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconData.icons),
                    contentDescription = iconData.name.toString(),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(dimens.size35)
                        .padding(dimens.size1)
                )
            }
        }
        Text(
            text = stringResource(iconData.name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = dimens.size8)
        )
    }
}

sealed class HomePageNavIcons(
    val icons: Int,
    val name: Int,
    val route:HomeRoutes
) {
    data object AsmaUlHusna :
        HomePageNavIcons(
            icons = R.drawable.allah,
            name = R.string.names,
            route = HomeRoutes.NamesOfAllah
        )

    data object Calendar :
        HomePageNavIcons(
            icons = R.drawable.calendar,
            name = R.string.calendar,
            route = HomeRoutes.Calendar
        )

    data object Athkar :
        HomePageNavIcons(
            icons = R.drawable.zikir,
            name = R.string.athkar,
            route = HomeRoutes.Athkar
        )

    data object Qibla :
        HomePageNavIcons(
            icons = R.drawable.kaba,
            name = R.string.qibla,
            route = HomeRoutes.Qibla
        )

    data object Zakat :
        HomePageNavIcons(
            icons = R.drawable.zakat,
            name = R.string.zakat,
            route = HomeRoutes.Zakat
        )
}

@Serializable
sealed class HomeRoutes {
    @Serializable
    data object NamesOfAllah : HomeRoutes()
    @Serializable
    data object Calendar : HomeRoutes()
    @Serializable
    data object Athkar : HomeRoutes()
    @Serializable
    data object Qibla : HomeRoutes()
    @Serializable
    data object Zakat : HomeRoutes()
}
