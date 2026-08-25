package com.hazrat.qibla.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.drawableToBitmap
import com.hazrat.utils.hapticFeedbacks

/**
 * Pure Compose Qibla Screen strictly following SOLID and Clean Architecture.
 * Interacts exclusively with ViewModel via QiblaState and sealed QiblaEvent.
 */
@Composable
fun QiblaScreen(
    state: QiblaState,
    onBackClick: () -> Unit = {},
    qiblaEvent: (QiblaEvent) -> Unit = {},
    navigateToTasbih: () -> Unit = {},
    isHapticFeedback: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    val composeModel = compassList.find { it.id == state.selectedCompassId } ?: compassList.first()

    // Qibla icon: gold kaabaniddle when facing Qibla, kaabaicon when not facing
    val qiblaIcon = if (state.isFacingQibla) R.drawable.kaabaniddle else R.drawable.kaabaicon
    val qiblaIconBitmap = remember(state.isFacingQibla) {
        drawableToBitmap(context, qiblaIcon).asImageBitmap()
    }

    val needleBitmap = remember(state.selectedCompassId) {
        drawableToBitmap(
            context,
            composeModel.compassNeedle
        ).asImageBitmap()
    }

    val compassNeedleMiddle = composeModel.compassMiddle.let { painterResource(it) }

    val compassImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(composeModel.compassImage)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        imageLoader = context.imageLoader
    )

    DisposableEffect(Unit) {
        qiblaEvent(QiblaEvent.StartSensorsAndLocation)
        onDispose {
            qiblaEvent(QiblaEvent.StopSensorsAndLocation)
        }
    }

    LaunchedEffect(state.isFacingQibla) {
        if (state.isFacingQibla) {
            hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BasicTopBar(
                topBarTitle = stringResource(id = R.string.nav_qibla),
                onBackClick = onBackClick
            )
        },
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(dimens.space4))

                // Top Status Badges Row (Location, Accuracy, Degrees)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space16),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location Badge
                    StatusPillBadge(
                        iconText = if (state.isLocationEnabled) "📍" else "⚠️",
                        label = if (state.isLocationEnabled) {
                            if (state.locationName.isNotBlank()) state.locationName else "Location"
                        } else {
                            "Location Off"
                        },
                        iconColor = if (state.isLocationEnabled) Color.Unspecified else Color(0xFFFA716A),
                        onClick = if (!state.isLocationEnabled) {
                            {
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                context.startActivity(intent)
                            }
                        } else null
                    )

                    // Accuracy Badge (ONLY shown when Location is Enabled to eliminate duplicate error badge!)
                    if (state.isLocationEnabled) {
                        val (accText, accColor, isAccClickable) = when (state.sensorAccuracy) {
                            3 -> Triple("High accuracy", Color(0xFF4CAF50), false)
                            2 -> Triple("Medium accuracy", Color(0xFFFFB752), false)
                            1 -> Triple("Calibrate Compass", Color(0xFFFF8E00), true)
                            else -> Triple("Calibrate Compass", Color(0xFFFA716A), true)
                        }

                        StatusPillBadge(
                            iconText = if (state.sensorAccuracy >= 2) "✔" else "⚠️",
                            label = accText,
                            iconColor = accColor,
                            onClick = if (isAccClickable) {
                                { qiblaEvent(QiblaEvent.ToggleCalibrationDialog(true)) }
                            } else null
                        )
                    }

                    // Qibla Degree Badge
                    StatusPillBadge(
                        iconText = "🧭",
                        label = if (!state.isLocationEnabled) "--" else if (state.isQiblaCalculated) "${state.qiblaDirection.toInt()}°" else "..."
                    )
                }

                // Location Off Alert Banner
                if (!state.isLocationEnabled) {
                    Card(
                        shape = RoundedCornerShape(dimens.cornerLg),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFA716A).copy(alpha = 0.15f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space16)
                            .padding(top = dimens.space8)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimens.space12, vertical = dimens.space8),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                        ) {
                            Text(text = "⚠️", style = MaterialTheme.typography.titleMedium)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Location Services Disabled",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFFA716A)
                                )
                                Text(
                                    text = "Turn on location services to compute accurate Qibla direction.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = customColors.secondaryText
                                )
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(dimens.cornerMd),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA716A)),
                                modifier = Modifier.height(dimens.space32)
                            ) {
                                Text(text = "Enable", color = Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(dimens.space8))

                // Central Compass Engine Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(horizontal = dimens.space16),
                    contentAlignment = Alignment.Center
                ) {
                    // Top North Indicator Line
                    Box(
                        modifier = Modifier
                            .size(width = dimens.divider * 3, height = dimens.space20)
                            .clip(RoundedCornerShape(dimens.cornerXs))
                            .background(
                                if (state.isFacingQibla) Color(0xFF4CAF50)
                                else MaterialTheme.colorScheme.primary
                            )
                            .align(Alignment.TopCenter)
                    )

                    // Rotation Angle (Smooth GPU animation)
                    val rotationAngle by animateFloatAsState(
                        targetValue = -state.currentDirection,
                        animationSpec = tween(
                            durationMillis = 150,
                            easing = LinearOutSlowInEasing
                        ),
                        label = "CompassRotation"
                    )

                    // Compass Dial Background (Original 0.8f Scale)
                    Image(
                        painter = compassImage,
                        contentDescription = stringResource(R.string.prayer_background),
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .graphicsLayer(
                                rotationZ = rotationAngle,
                                transformOrigin = TransformOrigin.Center
                            )
                    )

                    // Needle & Kaaba Icon Canvas (Exact Reference Implementation)
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val compassCenter = Offset(size.width / 2, size.height / 2)
                        val compassRadius = size.minDimension / 2.5f

                        if (state.isQiblaCalculated) {
                            // 1. Rotate & Draw Needle from Center
                            rotate(
                                degrees = state.qiblaDirection - state.currentDirection,
                                pivot = compassCenter
                            ) {
                                val needleStartY = compassCenter.y - needleBitmap.height / 1.1f
                                drawImage(
                                    image = needleBitmap,
                                    topLeft = Offset(
                                        compassCenter.x - needleBitmap.width / 2f,
                                        needleStartY
                                    )
                                )
                            }

                            // 2. Rotate & Draw Kaaba Icon (Switches to gold kaabaniddle when facing Qibla)
                            rotate(
                                degrees = state.qiblaDirection - state.currentDirection,
                                pivot = compassCenter
                            ) {
                                with(drawContext.canvas) {
                                    save()
                                    val scaleFactor = 0.3f
                                    val iconY = compassCenter.y - compassRadius - needleBitmap.height / 4f
                                    scale(
                                        scale = scaleFactor,
                                        pivot = Offset(
                                            compassCenter.x,
                                            iconY
                                        )
                                    ) {
                                        drawImage(
                                            image = qiblaIconBitmap,
                                            topLeft = Offset(
                                                compassCenter.x - qiblaIconBitmap.width / 2,
                                                iconY
                                            )
                                        )
                                    }
                                    restore()
                                }
                            }
                        }
                    }

                    // Needle Middle Center Cap (Centered in Box)
                    Image(
                        painter = compassNeedleMiddle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimens.iconLg)
                            .align(Alignment.Center)
                    )
                }

                // Guidance Status Text
                val guidanceText = when {
                    !state.isLocationEnabled -> "Enable location services"
                    !state.isQiblaCalculated -> "Locating Qibla..."
                    state.isFacingQibla -> "Facing Qibla"
                    state.qiblaDegreeDifference > 0 -> "Turn to your right"
                    else -> "Turn to your left"
                }

                Text(
                    text = guidanceText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (state.isFacingQibla) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = dimens.space8)
                )

                // Tasbih Recommendation Card (Compact)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space16)
                        .clip(RoundedCornerShape(dimens.cornerLg))
                        .clickable { navigateToTasbih() },
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space12, vertical = dimens.space8),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.tasbih),
                            contentDescription = "Tasbih",
                            modifier = Modifier.size(dimens.avatarLg)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Go to the Tasbih feature",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Find solace in dhikr",
                                style = MaterialTheme.typography.bodySmall,
                                color = customColors.secondaryText
                            )
                        }
                    }
                }

                Spacer(Modifier.height(dimens.space8))

                // Bottom Compass Selector Row (Smaller circular compass previews)
                BottomCompassPreview(
                    selectedCompassId = state.selectedCompassId,
                    onCompassClick = { compassId ->
                        qiblaEvent(QiblaEvent.OnCompassClick(compassId))
                    },
                    modifier = Modifier.padding(bottom = dimens.space8)
                )
            }
        }
    }

    // Figure-8 Compass Calibration Dialog Modal
    if (state.isCalibrationDialogVisible) {
        CompassCalibrationDialog(
            sensorAccuracy = state.sensorAccuracy,
            onDismiss = { qiblaEvent(QiblaEvent.ToggleCalibrationDialog(false)) }
        )
    }
}

/**
 * Pill status badge composable used for top location, accuracy, and degree info.
 */
@Composable
private fun StatusPillBadge(
    iconText: String,
    label: String,
    iconColor: Color? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerFull))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .border(
                width = dimens.divider,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.cornerFull)
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = dimens.space12, vertical = dimens.space4)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space4)
        ) {
            Text(
                text = iconText,
                style = MaterialTheme.typography.labelMedium,
                color = iconColor ?: MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
