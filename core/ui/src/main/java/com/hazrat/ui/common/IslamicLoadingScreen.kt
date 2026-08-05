package com.hazrat.ui.common

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.ui.R
import com.hazrat.ui.theme.Islam24Theme
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.dimens
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Industry-grade Islamic loading screen with animated geometric rosette,
 * orbiting crescent accent, floating particles, and customizable text.
 *
 * This composable can be called from any UI module as a premium full-screen
 * loading experience. All text is customizable — no hardcoded Arabic text.
 *
 * @param modifier Modifier for the root container
 * @param title Optional primary title text (e.g., Arabic calligraphy). Pass null to hide.
 * @param subtitle Optional subtitle text below the title. Defaults to "Please wait…"
 *
 * @author Hazrat Ummar Shaikh
 */
@Composable
fun IslamicLoadingScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String = stringResource(R.string.loading_please_wait),
) {
    val isDark = isSystemInDarkTheme()

    val bgGradient = if (isDark) {
        Brush.radialGradient(
            colors = listOf(
                Color(0xFF0E3832),
                Color(0xFF0A2622),
                Color(0xFF071517)
            ),
            radius = 1800f
        )
    } else {
        Brush.radialGradient(
            colors = listOf(
                Color(0xFFE8F5E9),
                Color(0xFFF1F8E9),
                Color(0xFFFAFAFA)
            ),
            radius = 1800f
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient),
        contentAlignment = Alignment.Center
    ) {
        // ── Floating particles layer (behind the rosette) ──
        FloatingParticles(isDark = isDark)

        // ── Main content column ──
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = dimens.space32)
        ) {
            // ── Rosette + Crescent ──
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(dimens.layoutXl)
            ) {
                IslamicRosetteAnimation(isDark = isDark)
                OrbitingCrescentAccent(isDark = isDark)
            }

            Spacer(modifier = Modifier.height(dimens.space48))

            // ── Text section ──
            LoadingTextSection(
                title = title,
                subtitle = subtitle,
                isDark = isDark
            )
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ── ROSETTE: Canvas-drawn 8-fold Islamic geometric star ──
// ═══════════════════════════════════════════════════════════

@Composable
private fun IslamicRosetteAnimation(
    isDark: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rosette")

    // Path-tracing draw progress (draws the rosette stroke-by-stroke)
    val drawProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseOutCubic),
            repeatMode = RepeatMode.Restart
        ),
        label = "drawProgress"
    )

    // Breathing scale pulse
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    // Glow alpha pulse
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // Slow rotation for the entire rosette
    val rosetteRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rosetteRotation"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val baseRadius = size.minDimension * 0.42f * breathScale

        rotate(degrees = rosetteRotation, pivot = Offset(cx, cy)) {
            // Layer 1: Outer glow ring
            drawCircle(
                color = primaryColor.copy(alpha = glowAlpha * 0.4f),
                radius = baseRadius * 1.15f,
                center = Offset(cx, cy)
            )

            // Layer 2: Outer octagonal frame
            drawOctagonFrame(
                cx = cx, cy = cy,
                radius = baseRadius,
                color = primaryColor.copy(alpha = 0.3f + glowAlpha * 0.3f),
                strokeWidth = 2f,
                progress = drawProgress
            )

            // Layer 3: 8-pointed star (two overlapping rotated squares)
            drawEightPointedStar(
                cx = cx, cy = cy,
                outerRadius = baseRadius * 0.85f,
                innerRadius = baseRadius * 0.35f,
                color = primaryColor.copy(alpha = 0.5f + glowAlpha * 0.3f),
                strokeWidth = 2.5f,
                progress = drawProgress
            )

            // Layer 4: Inner 8-pointed star (smaller, secondary color)
            drawEightPointedStar(
                cx = cx, cy = cy,
                outerRadius = baseRadius * 0.55f,
                innerRadius = baseRadius * 0.22f,
                color = secondaryColor.copy(alpha = 0.4f + glowAlpha * 0.2f),
                strokeWidth = 1.8f,
                progress = drawProgress
            )

            // Layer 5: Inner octagonal frame
            drawOctagonFrame(
                cx = cx, cy = cy,
                radius = baseRadius * 0.45f,
                color = tertiaryColor.copy(alpha = 0.25f + glowAlpha * 0.2f),
                strokeWidth = 1.5f,
                progress = drawProgress
            )

            // Layer 6: Central filled circle with glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = glowAlpha * 0.8f),
                        primaryColor.copy(alpha = glowAlpha * 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = baseRadius * 0.25f
                ),
                radius = baseRadius * 0.25f,
                center = Offset(cx, cy)
            )

            // Layer 7: Central solid dot
            drawCircle(
                color = primaryColor.copy(alpha = 0.6f + glowAlpha * 0.3f),
                radius = baseRadius * 0.06f,
                center = Offset(cx, cy),
                style = Fill
            )

            // Layer 8: Interlocking diamond connectors (adds depth)
            drawDiamondConnectors(
                cx = cx, cy = cy,
                radius = baseRadius * 0.7f,
                color = primaryColor.copy(alpha = 0.15f + glowAlpha * 0.15f),
                strokeWidth = 1.2f,
                progress = drawProgress
            )
        }
    }
}

/**
 * Draws a regular octagon frame at the given center.
 */
private fun DrawScope.drawOctagonFrame(
    cx: Float, cy: Float,
    radius: Float,
    color: Color,
    strokeWidth: Float,
    progress: Float
) {
    val path = Path()
    val totalPoints = 8
    val visiblePoints = (totalPoints * progress).toInt().coerceAtLeast(1)

    for (i in 0..visiblePoints) {
        val angleDeg = (i * 45.0) - 90.0
        val angleRad = angleDeg * PI / 180.0
        val x = cx + (radius * cos(angleRad)).toFloat()
        val y = cy + (radius * sin(angleRad)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    if (progress >= 1f) path.close()

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

/**
 * Draws an authentic Islamic Rub el Hizb (8-pointed star formed by two overlapping squares rotated 45°).
 */
private fun DrawScope.drawEightPointedStar(
    cx: Float, cy: Float,
    outerRadius: Float,
    innerRadius: Float,
    color: Color,
    strokeWidth: Float,
    progress: Float
) {
    // Square 1: Upright (0°, 90°, 180°, 270°)
    val path1 = Path()
    val sq1Angles = listOf(0.0, 90.0, 180.0, 270.0)
    for (i in 0 until 4) {
        val angleRad = (sq1Angles[i] - 90.0) * PI / 180.0
        val x = cx + (outerRadius * cos(angleRad)).toFloat()
        val y = cy + (outerRadius * sin(angleRad)).toFloat()
        if (i == 0) path1.moveTo(x, y) else path1.lineTo(x, y)
    }
    path1.close()

    drawPath(
        path = path1,
        color = color,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Miter)
    )

    // Square 2: Rotated 45° (45°, 135°, 225°, 315°)
    if (progress > 0.2f) {
        val path2 = Path()
        val sq2Angles = listOf(45.0, 135.0, 225.0, 315.0)
        for (i in 0 until 4) {
            val angleRad = (sq2Angles[i] - 90.0) * PI / 180.0
            val x = cx + (outerRadius * cos(angleRad)).toFloat()
            val y = cy + (outerRadius * sin(angleRad)).toFloat()
            if (i == 0) path2.moveTo(x, y) else path2.lineTo(x, y)
        }
        path2.close()

        drawPath(
            path = path2,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Miter)
        )
    }
}

/**
 * Draws interlocking diamond connector lines between star vertices for geometric depth.
 */
private fun DrawScope.drawDiamondConnectors(
    cx: Float, cy: Float,
    radius: Float,
    color: Color,
    strokeWidth: Float,
    progress: Float
) {
    val visibleConnectors = (8 * progress).toInt()
    for (i in 0 until visibleConnectors) {
        val angleDeg = (i * 45.0) - 22.5
        val angleRad = angleDeg * PI / 180.0
        val innerR = radius * 0.3f
        val outerR = radius

        val startX = cx + (innerR * cos(angleRad)).toFloat()
        val startY = cy + (innerR * sin(angleRad)).toFloat()
        val endX = cx + (outerR * cos(angleRad)).toFloat()
        val endY = cy + (outerR * sin(angleRad)).toFloat()

        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}


// ═══════════════════════════════════════════════════════════
// ── ORBITING CRESCENT: Small crescent moon accent ────────
// ═══════════════════════════════════════════════════════════

@Composable
private fun OrbitingCrescentAccent(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "crescent")

    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitAngle"
    )

    val crescentGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crescentGlow"
    )

    val crescentColor = if (isDark) {
        Color(0xFFD4AF37) // Gold for dark mode
    } else {
        Color(0xFFB5942F) // Deeper gold for light mode
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val orbitRadius = size.minDimension * 0.48f

        val angleRad = orbitAngle * PI.toFloat() / 180f
        val crescentX = cx + orbitRadius * cos(angleRad)
        val crescentY = cy + orbitRadius * sin(angleRad)

        // Crescent moon (outer circle minus offset inner circle)
        val moonRadius = size.minDimension * 0.03f

        // Glow behind the crescent
        drawCircle(
            color = crescentColor.copy(alpha = crescentGlow * 0.3f),
            radius = moonRadius * 2.5f,
            center = Offset(crescentX, crescentY)
        )

        // Outer moon circle
        drawCircle(
            color = crescentColor.copy(alpha = crescentGlow),
            radius = moonRadius,
            center = Offset(crescentX, crescentY)
        )

        // Inner cutout (creates crescent shape)
        val cutoutOffset = moonRadius * 0.45f
        drawCircle(
            color = if (isDark) Color(0xFF0A2622) else Color(0xFFF1F8E9),
            radius = moonRadius * 0.8f,
            center = Offset(crescentX + cutoutOffset, crescentY - cutoutOffset)
        )

        // Small star near the crescent
        val starDist = moonRadius * 2.2f
        val starAngle = angleRad + 0.3f
        val starX = cx + (orbitRadius + starDist * 0.5f) * cos(starAngle)
        val starY = cy + (orbitRadius + starDist * 0.5f) * sin(starAngle)
        drawCircle(
            color = crescentColor.copy(alpha = crescentGlow * 0.7f),
            radius = moonRadius * 0.2f,
            center = Offset(starX, starY)
        )
    }
}


// ═══════════════════════════════════════════════════════════
// ── FLOATING PARTICLES: Ambient radial particle system ───
// ═══════════════════════════════════════════════════════════

private data class ParticleSpec(
    val angleDeg: Float,
    val speed: Float,      // 0..1 normalized
    val size: Float,       // particle radius
    val delayMs: Int,      // animation stagger
    val durationMs: Int
)

@Composable
private fun FloatingParticles(isDark: Boolean) {
    val particles = remember {
        List(20) { i ->
            ParticleSpec(
                angleDeg = (i * 18f) + (i * 7.3f) % 360f,
                speed = 0.6f + (i % 5) * 0.1f,
                size = 1.5f + (i % 4) * 0.8f,
                delayMs = (i * 200) % 3000,
                durationMs = 3000 + (i % 4) * 500
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")

    val particleProgresses = particles.mapIndexed { index, spec ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = spec.durationMs,
                    delayMillis = spec.delayMs,
                    easing = EaseOutCubic
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "particle_$index"
        )
    }

    val particleColor = if (isDark) {
        Color(0xFF95F0C6) // emerald
    } else {
        Color(0xFF0D7377) // primary teal
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxOrbitRadius = size.minDimension * 0.55f

        particles.forEachIndexed { index, spec ->
            val progress by particleProgresses[index]
            val angleRad = spec.angleDeg * PI.toFloat() / 180f
            val currentRadius = maxOrbitRadius * 0.15f + maxOrbitRadius * progress * spec.speed
            val alpha = (1f - progress) * 0.6f

            if (alpha > 0.02f) {
                val px = cx + currentRadius * cos(angleRad)
                val py = cy + currentRadius * sin(angleRad)

                // Glow
                drawCircle(
                    color = particleColor.copy(alpha = alpha * 0.3f),
                    radius = spec.size * 3f,
                    center = Offset(px, py)
                )
                // Core
                drawCircle(
                    color = particleColor.copy(alpha = alpha),
                    radius = spec.size,
                    center = Offset(px, py)
                )
            }
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ── LOADING TEXT SECTION: Customizable with shimmer ──────
// ═══════════════════════════════════════════════════════════

@Composable
private fun LoadingTextSection(
    title: String?,
    subtitle: String,
    isDark: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "textShimmer")

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val baseTextColor = if (isDark) {
        Color(0xCCFFFFFF) // WhiteMutedText
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    }

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            baseTextColor.copy(alpha = 0.4f),
            baseTextColor,
            baseTextColor.copy(alpha = 0.4f)
        ),
        start = Offset(shimmerOffset * 300f, 0f),
        end = Offset(shimmerOffset * 300f + 200f, 0f)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space8)
    ) {
        // Optional title (e.g., Arabic calligraphy like Bismillah, or any text)
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = shimmerBrush,
                    fontFamily = ScheherazadeFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dimens.space4))
        }

        // Subtitle
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = baseTextColor.copy(alpha = 0.6f)
            ),
            textAlign = TextAlign.Center
        )
    }
}

// ═══════════════════════════════════════════════════════════
// ══ VARIANT 2: COMPACT SPINNING INDICATOR ════════════════
// ═══════════════════════════════════════════════════════════

/**
 * Size presets for [IslamicLoadingIndicator].
 */
enum class IslamicIndicatorSize {
    /** 24dp — for inline use inside buttons, list items */
    Small,
    /** 40dp — standard replacement for CircularProgressIndicator */
    Medium,
    /** 64dp — prominent standalone indicator */
    Large
}

/**
 * A compact spinning Islamic rosette indicator — a premium replacement
 * for [androidx.compose.material3.CircularProgressIndicator].
 *
 * Renders a spinning 8-pointed star with breathing glow. Use this anywhere
 * you'd normally use a circular spinner.
 *
 * @param modifier Modifier for the indicator container
 * @param size Preset size ([IslamicIndicatorSize.Small], [IslamicIndicatorSize.Medium], [IslamicIndicatorSize.Large])
 *
 * Usage:
 * ```
 * IslamicLoadingIndicator()                          // Medium (40dp)
 * IslamicLoadingIndicator(size = IslamicIndicatorSize.Small)  // 24dp
 * IslamicLoadingIndicator(size = IslamicIndicatorSize.Large)  // 64dp
 * ```
 */
@Composable
fun IslamicLoadingIndicator(
    modifier: Modifier = Modifier,
    size: IslamicIndicatorSize = IslamicIndicatorSize.Medium
) {
    val dimension = when (size) {
        IslamicIndicatorSize.Small -> dimens.space24
        IslamicIndicatorSize.Medium -> dimens.space40
        IslamicIndicatorSize.Large -> dimens.space64
    }

    val infiniteTransition = rememberInfiniteTransition(label = "indicator")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "indicatorRotation"
    )

    val isDark = isSystemInDarkTheme()
    val primaryColor = MaterialTheme.colorScheme.primary
    val goldColor = if (isDark) Color(0xFFF1D470) else Color(0xFFB5942F)
    val bgColor = if (isDark) Color(0xFF142428) else Color(0xFFFFFFFF)

    Canvas(modifier = modifier.size(dimension)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val ringRadius = this.size.minDimension * 0.4f
        val arcStroke = this.size.minDimension * 0.08f

        // Track Ring
        drawCircle(
            color = primaryColor.copy(alpha = 0.15f),
            radius = ringRadius,
            center = Offset(cx, cy),
            style = Stroke(width = arcStroke)
        )

        // Spinning Arc Ring
        rotate(degrees = rotation, pivot = Offset(cx, cy)) {
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

        // Center Crescent & 5-Pointed Star (Hilal & Najm)
        val moonRadius = ringRadius * 0.45f
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

        // 5-Pointed Star beside the Crescent
        if (size != IslamicIndicatorSize.Small) {
            val starX = cx + moonRadius * 0.55f
            val starY = cy - moonRadius * 0.35f
            val starR = moonRadius * 0.35f

            val starPath = Path()
            val innerR = starR * 0.4f
            for (i in 0 until 10) {
                val r = if (i % 2 == 0) starR else innerR
                val angleDeg = i * 36.0 - 90.0
                val angleRad = angleDeg * Math.PI / 180.0
                val x = (starX + r * kotlin.math.cos(angleRad)).toFloat()
                val y = (starY + r * kotlin.math.sin(angleRad)).toFloat()
                if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
            }
            starPath.close()
            drawPath(path = starPath, color = goldColor, style = Fill)
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ══ VARIANT 3: LOADING OVERLAY ═══════════════════════════
// ═══════════════════════════════════════════════════════════

/**
 * A semi-transparent Islamic loading overlay placed on top of existing content.
 *
 * Shows a centered [IslamicLoadingIndicator] with optional text over a dimmed
 * scrim. Blocks touch interaction on underlying content.
 *
 * @param isLoading When true, the overlay is visible
 * @param modifier Modifier for the overlay container
 * @param loadingText Optional text below the spinner
 * @param content The underlying content
 *
 * Usage:
 * ```
 * IslamicLoadingOverlay(isLoading = uiState.isLoading) {
 *     // Your normal screen content here
 *     MyScreenContent()
 * }
 * ```
 */
@Composable
fun IslamicLoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    loadingText: String? = null,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        if (isLoading) {
            val isDark = isSystemInDarkTheme()
            val scrimColor = if (isDark) {
                Color.Black.copy(alpha = 0.6f)
            } else {
                Color.White.copy(alpha = 0.7f)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.space16)
                ) {
                    IslamicLoadingIndicator(size = IslamicIndicatorSize.Large)

                    if (loadingText != null) {
                        Text(
                            text = loadingText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ══ VARIANT 4: REFRESH INDICATOR (Spinning Ring) ═════════
// ═══════════════════════════════════════════════════════════

/**
 * A spinning ring with an Islamic crescent accent — ideal for
 * refresh indicators, pull-to-refresh headers, or inline "loading more" states.
 *
 * Features a partial arc (like a Material spinner) but styled with Islamic
 * geometric endpoints and an orbiting crescent moon.
 *
 * @param modifier Modifier for the indicator
 * @param showText Whether to show "Refreshing…" text below
 *
 * Usage:
 * ```
 * IslamicRefreshIndicator()
 * IslamicRefreshIndicator(showText = true)
 * ```
 */
@Composable
fun IslamicRefreshIndicator(
    modifier: Modifier = Modifier,
    showText: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "refresh")

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 30f,
        targetValue = 270f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweepAngle"
    )

    val startAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "startAngle"
    )

    val crescentOrbit by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "crescentOrbit"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val isDark = isSystemInDarkTheme()
    val crescentColor = if (isDark) Color(0xFFD4AF37) else Color(0xFFB5942F)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space8),
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.size(dimens.space48)) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val ringRadius = size.minDimension * 0.38f
            val arcStroke = size.minDimension * 0.06f

            // Track ring (faint background)
            drawCircle(
                color = primaryColor.copy(alpha = 0.1f),
                radius = ringRadius,
                center = Offset(cx, cy),
                style = Stroke(width = arcStroke, cap = StrokeCap.Round)
            )

            // Active arc sweep
            drawArc(
                color = primaryColor.copy(alpha = 0.8f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = arcStroke, cap = StrokeCap.Round),
                topLeft = Offset(cx - ringRadius, cy - ringRadius),
                size = androidx.compose.ui.geometry.Size(ringRadius * 2, ringRadius * 2)
            )

            // Orbiting crescent at the leading edge of the arc
            val crescentAngleRad = crescentOrbit * PI.toFloat() / 180f
            val orbitR = ringRadius * 0.6f
            val moonX = cx + orbitR * cos(crescentAngleRad)
            val moonY = cy + orbitR * sin(crescentAngleRad)
            val moonRadius = size.minDimension * 0.06f

            drawCircle(
                color = crescentColor.copy(alpha = 0.8f),
                radius = moonRadius,
                center = Offset(moonX, moonY)
            )
            drawCircle(
                color = if (isDark) Color(0xFF071517) else Color(0xFFFAFAFA),
                radius = moonRadius * 0.7f,
                center = Offset(moonX + moonRadius * 0.35f, moonY - moonRadius * 0.35f)
            )
        }

        if (showText) {
            Text(
                text = stringResource(R.string.loading_please_wait),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ══ VARIANT 5: CASCADING PULSE DOTS ══════════════════════
// ═══════════════════════════════════════════════════════════

/**
 * Three cascading pulse dots with an Islamic emerald glow — perfect for
 * inline "typing...", "loading more...", or chat-style loading indicators.
 *
 * Each dot pulses with a staggered delay, creating a wave effect.
 *
 * @param modifier Modifier for the dots row
 *
 * Usage:
 * ```
 * Row(verticalAlignment = Alignment.CenterVertically) {
 *     Text("Loading")
 *     Spacer(Modifier.width(dimens.space8))
 *     IslamicLoadingDots()
 * }
 * ```
 */
@Composable
fun IslamicLoadingDots(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val dotScales = (0..2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    delayMillis = index * 180,
                    easing = EaseInOutSine
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dotScale_$index"
        )
    }

    val dotAlphas = (0..2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    delayMillis = index * 180,
                    easing = EaseInOutSine
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dotAlpha_$index"
        )
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier.size(
            width = dimens.space48,
            height = dimens.space16
        )
    ) {
        val cy = size.height / 2f
        val dotRadius = size.height * 0.18f
        val spacing = size.width / 4f

        for (i in 0..2) {
            val scale by dotScales[i]
            val alpha by dotAlphas[i]
            val cx = spacing * (i + 1)

            // Glow
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.25f),
                radius = dotRadius * scale * 2f,
                center = Offset(cx, cy)
            )
            // Core dot
            drawCircle(
                color = primaryColor.copy(alpha = alpha),
                radius = dotRadius * scale,
                center = Offset(cx, cy)
            )
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ══ VARIANT 6: GEOMETRIC PROGRESS BAR ════════════════════
// ═══════════════════════════════════════════════════════════

/**
 * A horizontal loading bar with an Islamic geometric pattern overlay —
 * a premium replacement for [androidx.compose.material3.LinearProgressIndicator].
 *
 * Features an indeterminate sweeping gradient with subtle star-pattern
 * texture drawn across the bar.
 *
 * @param modifier Modifier for the bar container
 *
 * Usage:
 * ```
 * IslamicLoadingBar(modifier = Modifier.fillMaxWidth().padding(horizontal = dimens.space16))
 * ```
 */
@Composable
fun IslamicLoadingBar(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "progressBar")

    val sweepOffset by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweepOffset"
    )

    val shimmerPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPhase"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.space4)
    ) {
        val barWidth = size.width
        val barHeight = size.height

        // Track background
        drawRoundRect(
            color = trackColor.copy(alpha = 0.4f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(barHeight / 2f),
            size = size
        )

        // Sweep gradient (the active indicator)
        val sweepStart = (sweepOffset * barWidth).coerceIn(0f, barWidth)
        val sweepEnd = ((sweepOffset + 0.35f) * barWidth).coerceIn(0f, barWidth)
        val sweepWidth = sweepEnd - sweepStart

        if (sweepWidth > 0f) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.1f),
                        primaryColor.copy(alpha = 0.8f),
                        primaryColor,
                        primaryColor.copy(alpha = 0.8f),
                        primaryColor.copy(alpha = 0.1f)
                    ),
                    startX = sweepStart,
                    endX = sweepEnd
                ),
                topLeft = Offset(sweepStart, 0f),
                size = androidx.compose.ui.geometry.Size(sweepWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barHeight / 2f)
            )
        }

        // Geometric star texture overlay (subtle repeating mini stars)
        val starSpacing = barHeight * 6f
        val numStars = (barWidth / starSpacing).toInt() + 1
        val textureAlpha = 0.08f + shimmerPhase * 0.06f

        for (i in 0 until numStars) {
            val starCx = i * starSpacing + (shimmerPhase * starSpacing)
            if (starCx in 0f..barWidth) {
                val starRadius = barHeight * 0.3f
                // Mini 4-pointed star
                val starPath = Path()
                for (j in 0 until 8) {
                    val r = if (j % 2 == 0) starRadius else starRadius * 0.4f
                    val angle = (j * 45.0 - 90.0) * PI / 180.0
                    val sx = starCx + (r * cos(angle)).toFloat()
                    val sy = barHeight / 2f + (r * sin(angle)).toFloat()
                    if (j == 0) starPath.moveTo(sx, sy) else starPath.lineTo(sx, sy)
                }
                starPath.close()
                drawPath(
                    path = starPath,
                    color = primaryColor.copy(alpha = textureAlpha),
                    style = Fill
                )
            }
        }
    }
}


// ═══════════════════════════════════════════════════════════
// ── PREVIEWS ─────────────────────────────────────────────
// ═══════════════════════════════════════════════════════════

@Preview(showBackground = true, backgroundColor = 0xFF071517)
@Composable
private fun IslamicLoadingScreenDarkPreview() {
    Islam24Theme(darkTheme = true) {
        IslamicLoadingScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
@Composable
private fun IslamicLoadingScreenLightPreview() {
    Islam24Theme(darkTheme = false) {
        IslamicLoadingScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF071517)
@Composable
private fun IslamicLoadingScreenWithTitlePreview() {
    Islam24Theme(darkTheme = true) {
        IslamicLoadingScreen(
            title = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",
            subtitle = "Preparing your experience"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF071517, widthDp = 300, heightDp = 100)
@Composable
private fun IslamicLoadingIndicatorPreview() {
    Islam24Theme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimens.space24),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IslamicLoadingIndicator(size = IslamicIndicatorSize.Small)
                IslamicLoadingIndicator(size = IslamicIndicatorSize.Medium)
                IslamicLoadingIndicator(size = IslamicIndicatorSize.Large)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA, widthDp = 300, heightDp = 100)
@Composable
private fun IslamicLoadingIndicatorLightPreview() {
    Islam24Theme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimens.space24),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IslamicLoadingIndicator(size = IslamicIndicatorSize.Small)
                IslamicLoadingIndicator(size = IslamicIndicatorSize.Medium)
                IslamicLoadingIndicator(size = IslamicIndicatorSize.Large)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF071517, widthDp = 300, heightDp = 120)
@Composable
private fun IslamicRefreshIndicatorPreview() {
    Islam24Theme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IslamicRefreshIndicator(showText = true)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF071517, widthDp = 200, heightDp = 60)
@Composable
private fun IslamicLoadingDotsPreview() {
    Islam24Theme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IslamicLoadingDots()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF071517, widthDp = 300, heightDp = 40)
@Composable
private fun IslamicLoadingBarPreview() {
    Islam24Theme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IslamicLoadingBar(
                modifier = Modifier.padding(horizontal = dimens.space16)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA, widthDp = 300, heightDp = 40)
@Composable
private fun IslamicLoadingBarLightPreview() {
    Islam24Theme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IslamicLoadingBar(
                modifier = Modifier.padding(horizontal = dimens.space16)
            )
        }
    }
}
