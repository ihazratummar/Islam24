package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.util.DateUtil.convertLongToLocalTime
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun PrayerTimeScreenAnimation(
    modifier: Modifier = Modifier,
    prayerTimeEntity: PrayerTimeEntity
) {

    val sunrise = convertLongToLocalTime(prayerTimeEntity.sunriseTime)
    val sunset =convertLongToLocalTime(prayerTimeEntity.sunsetTime)

    val currentTime = remember { mutableStateOf(LocalTime.now()) }

    val dayTotalMinutes = ChronoUnit.MINUTES.between(sunrise, sunset).toFloat()
    val minutesSinceSunrise = ChronoUnit.MINUTES.between(sunrise, currentTime.value).toFloat()
    val fractionOfDay = (minutesSinceSunrise / dayTotalMinutes).coerceIn(0f, 1f)


    val sunAnimatedFraction by animateFloatAsState(
        targetValue = fractionOfDay,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
    )
    val moonAnimatedFraction by animateFloatAsState(
        targetValue = fractionOfDay,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
    )

    // State to control the radius of the circle
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val sunAnimatedRadius by infiniteTransition.animateFloat(
        initialValue = 110f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 7000, // Duration for one complete cycle
                delayMillis = 2000,
                easing = FastOutSlowInEasing // Easing for smooth transition
            ),
            repeatMode = RepeatMode.Reverse // Reverses the animation on repeat
        ), label = ""
    )


    val isDayTIme = currentTime.value.isAfter(sunrise) && currentTime.value.isBefore(sunset)


    val starAnimationSize by infiniteTransition.animateFloat(
        initialValue = 70f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,

            ), label = ""
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val center = Offset(width / 2, height / 2)
            val sunYPosition = height * 0.25f
            val radius = sunAnimatedRadius
            val shadowColor = Color(0x80000000) // Semi-transparent black for shadow
            val moonColors = listOf(
                Color(0xFFFAFAFA),
                Color(0xFFC0C0C0),
                Color(0xFFA9ADAD),
                Color(0xFF99C9EE)
            )

            if (currentTime.value.isAfter(sunrise) && currentTime.value.isBefore(sunset)) {
                sunAnimation(
                    animatedRadius = sunAnimatedRadius,
                    animatedFraction = sunAnimatedFraction,
                    width = width,
                    sunYPosition = sunYPosition,
                    shadowColor = shadowColor
                )
            } else {
                moonAnimation(
                    animatedRadius = sunAnimatedRadius,
                    animatedFraction = moonAnimatedFraction,
                    width = width,
                    shadowColor = shadowColor,
                    moonColors = moonColors,
                    sunYPosition = sunYPosition,
                    starAnimationSize = starAnimationSize,
                    height = height
                )
            }
        }
    }

}

private fun DrawScope.moonAnimation(
    animatedRadius: Float,
    animatedFraction: Float,
    width: Float,
    shadowColor: Color,
    moonColors: List<Color>,
    sunYPosition: Float,
    starAnimationSize: Float,
    height: Float
) {
    // Adjust the moon's position based on the animatedFraction
    val moonXPosition = animatedFraction * width

    // Draw the moon and stars
    drawCircle(
        color = Color(0x4F8A8DFF),
        radius = animatedRadius * 2f,
        center = Offset(width / 2, height/ 4)
    )
    drawCircle(
        color = Color(0x9AADBFFF),
        radius = animatedRadius * 1.7f,
        center = Offset(width / 2, height/ 4)
    )
    drawCircle(
        color = Color(0xC4D8E8FF),
        radius = animatedRadius * 1.4f,
        center = Offset(width / 2, height/ 4)
    )
    drawCircle(
        color = shadowColor,
        radius = animatedRadius,
        center = Offset(width / 2, height/ 4)
    )
    drawCircle(
        brush = Brush.radialGradient(moonColors),
        radius = animatedRadius,
        center = Offset(width / 2, height/ 4)
    )
    drawCircle(
        brush = Brush.radialGradient(moonColors),
        radius = animatedRadius * 0.7f,
        center = Offset(width / 2, height/ 4)
    )
    drawCircle(
        color = Color(0xC4D8E8FF), // Color for the masking circle
        radius = animatedRadius * 0.8f,
        center = Offset(
            width / 2 + animatedRadius * 0.3f,
            height / 4.3f
        ) // Offset to create the crescent shape
    )

    // Draw stars
    drawStar(
        center = Offset(center.x, center.y - size.height / 2),
        size = starAnimationSize- 60f,
        color = Color.Yellow
    )
    drawStar(
        center = Offset(center.x + size.width / 4, center.y + size.height / 3),
        size = starAnimationSize - 40f,
        color = Color.Yellow.copy(0.6f)
    )
    drawStar(
        center = Offset(center.x - size.width / 4, center.y - size.height / 4),
        size = starAnimationSize - 60f,
        color = Color(0xC4A8DAFC)
    )
    drawStar(
        center = Offset(center.x + size.width / 4, center.y - size.height / 4),
        size = starAnimationSize - 50f,
        color = Color.Yellow
    )
    drawStar(
        center = Offset(size.width , center.y - size.height / 4),
        size = starAnimationSize - 40f,
        color = Color(0xC4A8DAFC)
    )
    drawStar(
        center = Offset(0f , center.y - size.height / 4),
        size = starAnimationSize - 50f,
        color = Color.Yellow.copy(0.4f)
    )
    drawStar(
        center = Offset(width* 0.3f, center.y - size.height / 4),
        size = starAnimationSize - 50f,
        color = Color.Yellow
    )
    drawStar(
        center = Offset(width* 0.3f, center.y - size.height-20f),
        size = starAnimationSize - 30f,
        color = Color(0xC4A8DAFC)
    )
    drawStar(
        center = Offset(40f, center.y - size.height-20f),
        size = starAnimationSize - 30f,
        color = Color.Yellow
    )
    drawStar(
        center = Offset(100f, center.y - size.height /4),
        size = starAnimationSize - 30f,
        color = Color.Yellow
    )
}

private fun DrawScope.drawStar(center: Offset, size: Float, color: Color) {
    val path = Path().apply {
        fillType = PathFillType.NonZero

        // Define the points for the star
        val points = arrayOf(
            Offset(center.x, center.y - size / 2),
            Offset(center.x + size / 5, center.y - size / 5),
            Offset(center.x + size / 2, center.y),
            Offset(center.x + size / 5, center.y + size / 5),
            Offset(center.x, center.y + size / 2),
            Offset(center.x - size / 5, center.y + size / 5),
            Offset(center.x - size / 2, center.y),
            Offset(center.x - size / 5, center.y - size / 5)
        )

        // Move to the first point
        moveTo(points[0].x, points[0].y)

        // Draw lines between the points to create the star
        for (i in 1 until points.size) {
            lineTo(points[i].x, points[i].y)
        }

        // Close the path
        close()
    }

    drawPath(
        path = path,
        color = color
    )
}


private fun DrawScope.sunAnimation(
    animatedRadius: Float,
    animatedFraction: Float,
    width: Float,
    sunYPosition: Float,
    shadowColor: Color
) {
    val sunXPosition = animatedFraction * width
    drawCircle(
        color = Color(0x4FF7CA75),
        radius = animatedRadius * 2f,
        center = Offset(sunXPosition, sunYPosition + 10f)
    )
    drawCircle(
        color = Color(0x9AF7CA75),
        radius = animatedRadius * 1.7f,
        center = Offset(sunXPosition, sunYPosition + 10f)
    )
    drawCircle(
        color = Color(0xC4FCDC9E),
        radius = animatedRadius * 1.4f,
        center = Offset(sunXPosition, sunYPosition + 10f)
    )
    drawCircle(
        color = shadowColor,
        radius = animatedRadius,
        center = Offset(sunXPosition, sunYPosition + 10f)
    )
    drawCircle(
        color = Color(0xFFEBAE3A),
        radius = animatedRadius,
        center = Offset(sunXPosition, sunYPosition)
    )
}