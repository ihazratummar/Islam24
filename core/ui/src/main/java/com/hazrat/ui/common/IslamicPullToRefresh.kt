package com.hazrat.ui.common

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.hazrat.ui.theme.dimens

/**
 * Premium Islamic Pull-To-Refresh container.
 *
 * Features an iconic **Islamic Crescent Moon & Star (Hilal & Najm)** in gold
 * surrounded by a smooth spinning emerald arc ring.
 *
 * @author Hazrat Ummar Shaikh
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    content: @Composable BoxScope.() -> Unit
) {
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        indicator = {
            IslamicPullToRefreshIndicator(
                state = state,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IslamicPullToRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val containerBg = if (isDark) {
        Color(0xFF142428)
    } else {
        Color(0xFFFFFFFF)
    }

    PullToRefreshDefaults.IndicatorBox(
        state = state,
        isRefreshing = isRefreshing,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = containerBg,
        elevation = dimens.elevation2
    ) {
        Box(
            modifier = Modifier.size(dimens.space48),
            contentAlignment = Alignment.Center
        ) {
            if (isRefreshing) {
                RefreshingCrescentAnimation()
            } else {
                PullCrescentAnimation(distanceFraction = state.distanceFraction)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// ── PULL PHASE: Crescent fills & rotates with drag ────────
// ═══════════════════════════════════════════════════════════

@Composable
private fun PullCrescentAnimation(distanceFraction: Float) {
    val progress = distanceFraction.coerceIn(0f, 1f)
    val primaryColor = MaterialTheme.colorScheme.primary
    val isDark = isSystemInDarkTheme()
    val goldColor = if (isDark) Color(0xFFF1D470) else Color(0xFFB5942F)
    val bgColor = if (isDark) Color(0xFF142428) else Color(0xFFFFFFFF)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val ringRadius = size.minDimension * 0.38f
        val arcStroke = size.minDimension * 0.08f

        // Outer progress arc track
        drawCircle(
            color = primaryColor.copy(alpha = 0.15f),
            radius = ringRadius,
            center = Offset(cx, cy),
            style = Stroke(width = arcStroke)
        )

        // Progress arc
        drawArc(
            color = primaryColor,
            startAngle = -90f,
            sweepAngle = progress * 360f,
            useCenter = false,
            style = Stroke(width = arcStroke, cap = StrokeCap.Round),
            topLeft = Offset(cx - ringRadius, cy - ringRadius),
            size = androidx.compose.ui.geometry.Size(ringRadius * 2, ringRadius * 2)
        )

        // Central Crescent Moon & 5-pointed Islamic Star
        val moonRadius = ringRadius * 0.5f
        val moonX = cx - moonRadius * 0.15f
        val moonY = cy

        // Moon outer circle
        drawCircle(
            color = goldColor.copy(alpha = 0.4f + progress * 0.6f),
            radius = moonRadius,
            center = Offset(moonX, moonY)
        )
        // Moon inner cutout
        drawCircle(
            color = bgColor,
            radius = moonRadius * 0.8f,
            center = Offset(moonX + moonRadius * 0.45f, moonY - moonRadius * 0.35f)
        )

        // 5-Pointed Star beside the Crescent
        if (progress > 0.4f) {
            val starProgress = ((progress - 0.4f) / 0.6f).coerceIn(0f, 1f)
            val starX = cx + moonRadius * 0.55f
            val starY = cy - moonRadius * 0.35f
            val starR = moonRadius * 0.35f * starProgress

            drawFivePointedStar(
                cx = starX,
                cy = starY,
                radius = starR,
                color = goldColor.copy(alpha = starProgress)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════
// ── REFRESH PHASE: Spinning Arc with Pulsing Crescent ──────
// ═══════════════════════════════════════════════════════════

@Composable
private fun RefreshingCrescentAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "crescentRefresh")

    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringRotation"
    )

    val moonPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "moonPulse"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val isDark = isSystemInDarkTheme()
    val goldColor = if (isDark) Color(0xFFF1D470) else Color(0xFFB5942F)
    val bgColor = if (isDark) Color(0xFF142428) else Color(0xFFFFFFFF)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val ringRadius = size.minDimension * 0.38f
        val arcStroke = size.minDimension * 0.08f

        // Track ring
        drawCircle(
            color = primaryColor.copy(alpha = 0.15f),
            radius = ringRadius,
            center = Offset(cx, cy),
            style = Stroke(width = arcStroke)
        )

        // Spinning Arc
        rotate(degrees = ringRotation, pivot = Offset(cx, cy)) {
            drawArc(
                color = primaryColor,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = arcStroke, cap = StrokeCap.Round),
                topLeft = Offset(cx - ringRadius, cy - ringRadius),
                size = androidx.compose.ui.geometry.Size(ringRadius * 2, ringRadius * 2)
            )
        }

        // Center Crescent & Star (Pulsing)
        val moonRadius = ringRadius * 0.48f * moonPulse
        val moonX = cx - moonRadius * 0.15f
        val moonY = cy

        // Moon Body
        drawCircle(
            color = goldColor,
            radius = moonRadius,
            center = Offset(moonX, moonY)
        )
        // Moon Cutout
        drawCircle(
            color = bgColor,
            radius = moonRadius * 0.8f,
            center = Offset(moonX + moonRadius * 0.45f, moonY - moonRadius * 0.35f)
        )

        // 5-Pointed Star
        val starX = cx + moonRadius * 0.55f
        val starY = cy - moonRadius * 0.35f
        val starR = moonRadius * 0.35f

        drawFivePointedStar(
            cx = starX,
            cy = starY,
            radius = starR,
            color = goldColor
        )
    }
}

/**
 * Draws a clean, classic 5-pointed Islamic Star.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFivePointedStar(
    cx: Float,
    cy: Float,
    radius: Float,
    color: Color
) {
    if (radius <= 0f) return
    val path = Path()
    val innerRadius = radius * 0.4f
    for (i in 0 until 10) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angleDeg = i * 36.0 - 90.0
        val angleRad = angleDeg * Math.PI / 180.0
        val x = (cx + r * kotlin.math.cos(angleRad)).toFloat()
        val y = (cy + r * kotlin.math.sin(angleRad)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path = path, color = color, style = Fill)
}
