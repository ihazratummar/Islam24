package com.hazrat.tasbih.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toLocalizedDigits
import kotlin.math.cos
import kotlin.math.sin

/**
 * Royal Islamic Radial Dial Counter with 33 Misbaha jewel bead markers, sweep gradient arc, and tactile spring bounce.
 * @author hazratummar
 */
@Composable
fun TasbihDialCounter(
    currentCount: Int,
    targetLimit: Int,
    onCount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (targetLimit > 0) {
        (currentCount % targetLimit).toFloat() / targetLimit.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "TasbihProgress"
    )

    // Tap scale bounce animation
    val scaleAnim = remember { Animatable(1f) }
    LaunchedEffect(currentCount) {
        if (currentCount > 0) {
            scaleAnim.animateTo(
                targetValue = 0.93f,
                animationSpec = tween(durationMillis = 50)
            )
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.4f, stiffness = 450f)
            )
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val accentColor = customColors.accentColor
    val trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
    val innerDiscBg = MaterialTheme.colorScheme.surfaceContainerLow
    val surfaceColor = MaterialTheme.colorScheme.surface
    val innerOutlineColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    val beadInactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
    val beadActiveColor = accentColor

    Box(
        modifier = modifier
            .size(dimens.layoutXl)
            .scale(scaleAnim.value)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCount
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 32f
            val halfStroke = strokeWidth / 2f
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val centerOffset = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = (size.width / 2f) - halfStroke
            val innerRadius = outerRadius - strokeWidth - 14f

            // 1. Outer Delicate Bezel Ring
            drawCircle(
                color = accentColor.copy(alpha = 0.15f),
                radius = size.width / 2f - 2f,
                center = centerOffset,
                style = Stroke(width = 2f)
            )

            // 2. Background Track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(halfStroke, halfStroke),
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )

            // 3. Active Luminous Gradient Arc
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to primaryColor,
                        0.5f to accentColor,
                        1.0f to primaryColor,
                        center = centerOffset
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = Offset(halfStroke, halfStroke),
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // 4. 33 Radial Misbaha Jewel Bead Markers
            val totalBeadMarkers = 33
            val baseBeadRadius = 5f
            for (i in 0 until totalBeadMarkers) {
                val angleDeg = (i.toFloat() / totalBeadMarkers) * 360f - 90f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val beadX = (centerOffset.x + outerRadius * cos(angleRad)).toFloat()
                val beadY = (centerOffset.y + outerRadius * sin(angleRad)).toFloat()

                val beadFraction = i.toFloat() / totalBeadMarkers.toFloat()
                val isBeadPassed = animatedProgress >= beadFraction

                // Bead Outer Halo if passed
                if (isBeadPassed) {
                    drawCircle(
                        color = beadActiveColor.copy(alpha = 0.35f),
                        radius = baseBeadRadius * 1.8f,
                        center = Offset(beadX, beadY)
                    )
                }

                // Bead Body
                drawCircle(
                    color = if (isBeadPassed) beadActiveColor else beadInactiveColor,
                    radius = if (isBeadPassed) baseBeadRadius * 1.25f else baseBeadRadius,
                    center = Offset(beadX, beadY)
                )
            }

            // 5. Central Counter Disc Fill
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        innerDiscBg,
                        surfaceColor
                    ),
                    center = centerOffset,
                    radius = innerRadius
                ),
                radius = innerRadius,
                center = centerOffset
            )

            // 6. Concentric Inner Gold Outline Bezel
            drawCircle(
                color = innerOutlineColor,
                radius = innerRadius,
                center = centerOffset,
                style = Stroke(width = 3f)
            )
        }

        // Center Digital Display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentCount.toLocalizedDigits(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "/ ${targetLimit.toLocalizedDigits()}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(dimens.space4))
            Text(
                text = stringResource(com.hazrat.ui.R.string.tasbih_tap_to_count_lower),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
            )
        }
    }
}
