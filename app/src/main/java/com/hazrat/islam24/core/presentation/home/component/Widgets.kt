package com.hazrat.islam24.core.presentation.home.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import com.hazrat.ui.R
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.ui.theme.dimens


@Composable
fun LazyHorizontalManyIcons(
    onClick: (HomePageNavIcons) -> Unit
) {


    val navIcons = listOf(
        HomePageNavIcons.AsmaUlHusna,
        HomePageNavIcons.Calendar,
        HomePageNavIcons.Athkar,
        HomePageNavIcons.Qibla,
        HomePageNavIcons.Zakat,
        HomePageNavIcons.HajjLive,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
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
    val backgroundColor = if (isSystemInDarkTheme()) {
        iconData.backgroundColor.dark
    } else {
        iconData.backgroundColor.light
    }

    Column(
        modifier = Modifier.padding(vertical = dimens.size10, horizontal = dimens.size10),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(dimens.size80)
                .padding(dimens.size1)
                .combinedClickable(
                    onClick = { onClick() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .clip(CircleShape),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
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
                        .size(dimens.size60)
                        .padding(dimens.size1)
                )
            }
        }
        Text(
            text = stringResource(iconData.name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}


fun Modifier.shimmerEffect(
    animationSpeed: Int = 3000
): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationSpeed)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondaryContainer,
            ),
            start = Offset(startOffsetX, 0F),
            end = Offset(
                startOffsetX + size.width.toFloat(), size.height.toFloat()
            )
        ),
        shape = RoundedCornerShape(dimens.size10)
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
    val route: MainRoute,
    val backgroundColor: AppColor
) {
    data object AsmaUlHusna :
        HomePageNavIcons(
            icons = R.drawable.allah,
            name = R.string.names,
            route = MainRoute.NamesOfAllahScreen,
            backgroundColor = AppColor(
                dark = Color(0xFFE0F2F7),
                light = Color(0xFFA8D4FD)
            ) // Light blue shades
        )

    data object Calendar :
        HomePageNavIcons(
            icons = R.drawable.calendar,
            name = R.string.calendar,
            route = MainRoute.CalendarScreen,
            backgroundColor = AppColor(
                dark = Color(0xFFF8EBD0),
                light = Color(0xFFFAE194)
            ) // Yellow shades
        )

    data object Athkar :
        HomePageNavIcons(
            icons = R.drawable.zikir,
            name = R.string.athkar,
            route = MainRoute.AthkarScreen,
            backgroundColor = AppColor(
                dark = Color(0xFFDEFFDF),
                light = Color(0xFFCFFCAE)
            ) // Green shades
        )

    data object Qibla :
        HomePageNavIcons(
            icons = R.drawable.kaba,
            name = R.string.qibla,
            route = MainRoute.QiblaDirectionScreen,
            backgroundColor = AppColor(
                dark = Color(0xFFCFEDFF),
                light = Color(0xFFC6B8FF)
            ) // Light blue shades
        )

    data object Zakat :
        HomePageNavIcons(
            icons = R.drawable.zakat,
            name = R.string.zakat,
            route = MainRoute.ZakatScreen,
            backgroundColor = AppColor(
                dark = Color(0xFFFFE5BE),
                light = Color(0xFFE7B987)
            ) // Orange shades
        )

    data object HajjLive :
        HomePageNavIcons(
            icons = R.drawable.hajj_live,
            name = R.string.hajj_live,
            route = MainRoute.HajjLiveScreenRoute,
            backgroundColor = AppColor(
                dark = Color(0xFFC4FDB1),
                light = Color(0xFFFAFD95)
            ) // Orange shades
        )
}

data class AppColor(val light: Color, val dark: Color)