package com.hazrat.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.hazrat.ui.theme.dimens


/**
 * @author hazratummar
 * Created on 23/01/26
 */

fun Modifier.profileCardShimmerEffect(): Modifier = composed {
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
