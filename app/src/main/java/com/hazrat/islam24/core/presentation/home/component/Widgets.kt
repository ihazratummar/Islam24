package com.hazrat.islam24.core.presentation.home.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.hazrat.islam24.R
import com.hazrat.islam24.main.navigation.AthkarScreen
import com.hazrat.islam24.main.navigation.CalendarScreen
import com.hazrat.islam24.main.navigation.NamesOfAllahScreen
import com.hazrat.islam24.main.navigation.QiblaDirectionScreen
import com.hazrat.islam24.main.navigation.nvgraph.Zakat
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.Dimens
import okhttp3.internal.immutableListOf

@Composable
fun LazyVerticalGridCardIcons(navController: NavController) {

    val icons = immutableListOf(
        R.drawable.allah_logo,
        R.drawable.calendar,
        R.drawable.athkar,
        R.drawable.qibla,
        R.drawable.zakat
    )

    val names = immutableListOf(
        stringResource(R.string.names),
        stringResource(R.string.calendar),
        stringResource(R.string.athkar),
        stringResource(id = R.string.qibla),
        stringResource(id = R.string.zakat)
    )

    val onClickLabel = immutableListOf(
        stringResource(R.string.names),
        stringResource(R.string.calendar),
        stringResource(R.string.athkar),
        stringResource(id = R.string.qibla),
        stringResource(id = R.string.zakat)
    )

    Box(modifier = Modifier
        .height(dimens.size200)
        .fillMaxWidth()) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.Center
        ) {
            items(names.size) { index ->
                Column(
                    modifier = Modifier.padding(vertical = dimens.size10),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .width(dimens.size60)
                            .height(dimens.size60)
                            .padding(dimens.size1)
                            .clickable(
                                onClick = {
                                    when (index) {
                                        0 -> navController.navigate(NamesOfAllahScreen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = false
                                        }

                                        1 -> navController.navigate(CalendarScreen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }

                                        2 -> navController.navigate(AthkarScreen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }

                                        3 -> navController.navigate(QiblaDirectionScreen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        4-> navController.navigate(Zakat){
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                onClickLabel = onClickLabel[index]
                            )
                            .clip(RoundedCornerShape(dimens.size8)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = icons[index]),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimens.size5)
                        )
                    }
                    Text(
                        names[index],
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }
        }
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
                Color(0xD71F581F),
                Color(0xFA277006),
                Color(0xD71F581F),
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
