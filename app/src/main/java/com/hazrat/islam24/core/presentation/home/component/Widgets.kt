package com.hazrat.islam24.core.presentation.home.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import com.hazrat.islam24.R
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.islam24.ui.theme.dimens


@Composable
fun LazyVerticalGridCardIcons(
    onClick: (HomePageNavIcons) -> Unit
) {
    val navIcons = listOf(
        HomePageNavIcons.AsmaUlHusna,
        HomePageNavIcons.Calendar,
        HomePageNavIcons.Athkar,
        HomePageNavIcons.Qibla,
        HomePageNavIcons.Zakat,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.size200),
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.Center
        ) {
            navIcons.forEach {
                item {
                    HomeWidgetsIcons(
                        icons = it.icons,
                        names = it.name,
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
    icons: Int,
    names: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = dimens.size10),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(dimens.size60)
                .padding(dimens.size1)
                .combinedClickable(
                    onClick = { onClick() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .clip(RoundedCornerShape(dimens.size8)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Icon(
                painter = painterResource(icons),
                contentDescription = names.toString(),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimens.size60)
                    .padding(dimens.size1)
            )
        }
        Text(
            text = stringResource(names),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xEE155242),
                Color(0xFD258971),
                Color(0xE9155242),

                ),
            start = Offset(startOffsetX, 0F),
            end = Offset(
                startOffsetX + size.width.toFloat(), size.height.toFloat()
            )
        ),
        shape = RoundedCornerShape(dimens.size30)
    ).onGloballyPositioned {
        size = it.size
    }
}


fun Modifier.generalShimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0x8FBDBDBD), // Light grey
                Color(0x75E0E0E0), // Medium grey
                Color(0x8BBDBDBD)  // Light grey
            ),
            start = Offset(startOffsetX, 0F),
            end = Offset(
                startOffsetX + size.width.toFloat(), size.height.toFloat()
            )
        ),
        shape = RoundedCornerShape(dimens.size30)
    ).onGloballyPositioned {
        size = it.size
    }
}


sealed class HomePageNavIcons(
    val icons: Int,
    val name: Int,
    val route:  MainRoute
) {
    data object AsmaUlHusna :
        HomePageNavIcons(icons = R.drawable.allah_logo, name = R.string.names, route = MainRoute.NamesOfAllahScreen)

    data object Calendar :
        HomePageNavIcons(icons = R.drawable.calendaricon, name = R.string.calendar,  route = MainRoute.CalendarScreen)

    data object Athkar :
        HomePageNavIcons(icons = R.drawable.athkar, name = R.string.athkar,  route = MainRoute.AthkarScreen)

    data object Qibla :
        HomePageNavIcons(icons = R.drawable.qibla, name = R.string.qibla, route = MainRoute.QiblaDirectionScreen)

    data object Zakat :
        HomePageNavIcons(icons = R.drawable.zakat, name = R.string.zakat, route = MainRoute.ZakatScreen)
}