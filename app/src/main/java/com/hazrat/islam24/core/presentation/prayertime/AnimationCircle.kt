package com.hazrat.islam24.core.presentation.prayertime

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.util.DateUtil
import com.hazrat.islam24.util.DateUtil.convertLongToLocalTime
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun AnimationCircle(
    modifier: Modifier = Modifier,
    prayerTimeEntity: PrayerTimeEntity
) {

    val gregorianDay = prayerTimeEntity.gregorianDay.toInt()
    val hijriDay = prayerTimeEntity.hijriDay
    val prayerDay = gregorianDay == getCurrentDay()

    val sunrise = convertLongToLocalTime(prayerTimeEntity.sunriseTime)
    val sunset =convertLongToLocalTime(prayerTimeEntity.sunsetTime)

//    val sunrise = LocalTime.of(5, 0)
//    val sunset = LocalTime.of(18, 30)
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
        initialValue = 130f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000, // Duration for one complete cycle
                easing = FastOutSlowInEasing // Easing for smooth transition
            ),
            repeatMode = RepeatMode.Reverse // Reverses the animation on repeat
        ), label = ""
    )

    val shadowColor = Color(0x80000000) // Semi-transparent black for shadow
    val moonColors = listOf(
        Color(0xFFFAFAFA), // Very light gray for a soft glow
        Color(0xFFC0C0C0), // Light gray for the moon's surface
        Color(0xFFA9ADAD), // Medium gray for shading
        Color(0xFF99C9EE)  // Dark gray for deeper shadows
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
                    starAnimationSize = starAnimationSize
                )
            }
        }
    }

}

@Composable
private fun MyCards() {
    val list = 100
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(100.dp)) }
        items(list) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.9f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text = "Hello", modifier = Modifier.padding(20.dp))
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
    starAnimationSize: Float
) {
    // Adjust the moon's position based on the animatedFraction
    val moonXPosition = animatedFraction * width

    // Draw the moon and stars
    drawCircle(
        color = Color(0x4F8A8DFF),
        radius = animatedRadius * 2f,
        center = Offset(moonXPosition, sunYPosition * 0.25f)
    )
    drawCircle(
        color = Color(0x9AADBFFF),
        radius = animatedRadius * 1.7f,
        center = Offset(moonXPosition, sunYPosition * 0.25f)
    )
    drawCircle(
        color = Color(0xC4D8E8FF),
        radius = animatedRadius * 1.4f,
        center = Offset(moonXPosition, sunYPosition * 0.25f)
    )
    drawCircle(
        color = shadowColor,
        radius = animatedRadius,
        center = Offset(moonXPosition, sunYPosition * 0.25f)
    )
    drawCircle(
        brush = Brush.radialGradient(moonColors),
        radius = animatedRadius,
        center = Offset(moonXPosition, sunYPosition * 0.25f)
    )
    drawCircle(
        brush = Brush.radialGradient(moonColors),
        radius = animatedRadius * 0.7f,
        center = Offset(moonXPosition, sunYPosition * 0.25f)
    )

    // Draw stars
    drawStar(
        center = Offset(center.x, center.y - size.height / 2),
        size = starAnimationSize,
        color = Color.Yellow
    )
    drawStar(
        center = Offset(center.x + size.width / 4, center.y + size.height / 3),
        size = starAnimationSize - 20f,
        color = Color.Yellow.copy(0.6f)
    )
    drawStar(
        center = Offset(center.x - size.width / 4, center.y - size.height / 4),
        size = starAnimationSize,
        color = Color.Yellow
    )
    drawStar(
        center = Offset(center.x + size.width / 4, center.y - size.height / 4),
        size = starAnimationSize - 20f,
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