package com.hazrat.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renders an ultra-premium Islamic 8-Pointed Star (Rub el Hizb) geometric lattice pattern
 * with ambient radial glow vignettes for a luxury feel.
 *
 * @author Hazrat Ummar Shaikh
 */
@Composable
fun IslamicGridBackground(
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
    accentNodeColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
    content: @Composable () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    val ambientGlow = if (isDark) {
        Brush.radialGradient(
            colors = listOf(
                Color(0xFF0E3832),
                Color(0xFF0A2622),
                MaterialTheme.colorScheme.background
            ),
            radius = 1400f
        )
    } else {
        Brush.radialGradient(
            colors = listOf(
                Color(0xFFE8F5E9),
                Color(0xFFF1F8E9),
                MaterialTheme.colorScheme.background
            ),
            radius = 1400f
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ambientGlow)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tileSize = 96.dp.toPx()
            val starRadius = 24.dp.toPx()
            val strokeWidth = 1.2.dp.toPx()

            val width = size.width
            val height = size.height

            val cols = (width / tileSize).toInt() + 2
            val rows = (height / tileSize).toInt() + 2

            for (r in -1..rows) {
                for (c in -1..cols) {
                    val cx = c * tileSize + (if (r % 2 != 0) tileSize / 2f else 0f)
                    val cy = r * tileSize

                    // 1. Draw 8-pointed Rub el Hizb Star
                    val starPath = Path()
                    for (i in 0 until 16) {
                        val radius = if (i % 2 == 0) starRadius else starRadius * 0.5f
                        val angle = Math.toRadians((i * 22.5) - 90)
                        val x = cx + (radius * cos(angle)).toFloat()
                        val y = cy + (radius * sin(angle)).toFloat()
                        if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
                    }
                    starPath.close()

                    drawPath(
                        path = starPath,
                        color = lineColor,
                        style = Stroke(width = strokeWidth)
                    )

                    // 2. Center Accent Node
                    drawCircle(
                        color = accentNodeColor,
                        radius = 2.5.dp.toPx(),
                        center = Offset(cx, cy)
                    )

                    // 3. Interlocking Diagonal Lattice Connectors
                    val connectRadius = tileSize * 0.35f
                    for (angleDeg in listOf(45, 135, 225, 315)) {
                        val angle = Math.toRadians(angleDeg.toDouble())
                        val endX = cx + (connectRadius * cos(angle)).toFloat()
                        val endY = cy + (connectRadius * sin(angle)).toFloat()
                        drawLine(
                            color = lineColor.copy(alpha = lineColor.alpha * 0.7f),
                            start = Offset(cx, cy),
                            end = Offset(endX, endY),
                            strokeWidth = strokeWidth * 0.8f
                        )
                    }
                }
            }
        }

        content()
    }
}
