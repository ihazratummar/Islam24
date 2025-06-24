package com.hazrat.islam24.core.presentation.qibla

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import com.hazrat.ui.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.domain.model.qiblaModels.compassList
import com.hazrat.islam24.core.presentation.common.BasicTopBar
import com.hazrat.islam24.core.presentation.common.PopupDialog
import com.hazrat.ui.theme.dimens
import com.hazrat.islam24.util.drawableToBitmap
import com.hazrat.islam24.util.hapticFeedbacks
import kotlin.math.abs

/**
 * @author Hazrat Ummar Shaikh
 */


/**
 * QiblaScreen is a composable function that displays a compass indicating the Qibla direction.
 * It also vibrates the device when the user faces the Qibla.
 *
 * @param navController The NavHostController for navigation.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    locationName: List<LocationDetailsEntity>,
    state: QiblaState,
    onBackClick: () -> Unit,
    isHapticFeedback: Boolean,
    authState: AuthState,
    qiblaEvent: (QiblaEvent) -> Unit,
    navigateToLogin: () -> Unit
) {

    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current


    val qiblaIcon = if (state.isFacingQibla) R.drawable.kaabaniddle else R.drawable.kaabaicon
    val qiblaIconBitmap =
        remember(state.isFacingQibla) { drawableToBitmap(context, qiblaIcon).asImageBitmap() }

    val composeModel = compassList.find { it.id == state.selectedCompassId }

    val needleBitmap = remember(state.selectedCompassId) {
        drawableToBitmap(
            context,
            composeModel?.compassNeedle ?: R.drawable.needles
        ).asImageBitmap()
    }

    val compassNeedleMiddle = composeModel?.compassMiddle?.let { painterResource(it) }


    val compassImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(composeModel?.compassImage)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        imageLoader = context.imageLoader
    )


    LaunchedEffect(state.isFacingQibla) {
        if (state.isFacingQibla) {
            hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.compass_screen_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.primaryContainer,
                BlendMode.Softlight
            )
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBar(
                modifier = Modifier,
                topBarTitle = stringResource(id = R.string.qibla),
                onBackClick = { onBackClick.invoke() },
                iconColor = Color.White,
                textColor = Color.White
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = dimens.size20)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .clip(
                        shape = RoundedCornerShape(dimens.size30)
                    )
                    .border(
                        dimens.size2,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(dimens.size30)
                    )
            ) {
                QiblaMapView(
                    modifier = Modifier
                        .fillMaxSize(),
                    qiblaLocation = LatLng(21.4225, 39.8262),
                    latitude = state.latitude,
                    longitude = state.longitude
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                            0.3f
                        )
                    ),
                    shape = RoundedCornerShape(dimens.size20)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Qibla Direction: ${state.qiblaDirection.toInt()}°",
                        style = MaterialTheme.typography.labelMedium.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Log.d("QiblaScreen", "QiblaScreen: ${state.latitude}")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(dimens.size10),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(dimens.size60)
                        .align(
                            Alignment.TopCenter
                        )
                )
                Text(
                    text = if (state.isFacingQibla) {
                        "You are now facing the Mecca"
                    } else if (state.qiblaDegreeDifference >= 0) {
                        "Rotate your device to ${state.qiblaDegreeDifference.toInt()}° right"
                    } else {
                        "Rotate your device to ${abs(state.qiblaDegreeDifference.toInt())}° left"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(
                        Alignment.BottomCenter
                    )
                )

                OrientationIndicator(
                    pitch = state.pitch,
                    roll = state.roll,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )

                val rotationAngle by animateFloatAsState(
                    targetValue = -state.currentDirection,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                )
                Image(
                    painter = compassImage,
                    contentDescription = "Background",
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .graphicsLayer(
                            rotationZ = rotationAngle,
                            transformOrigin = TransformOrigin.Center
                        )
                )

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val compassCenter = Offset(size.width / 2, size.height / 2)
                    val compassRadius = size.minDimension / 2.5f

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
                    rotate(
                        degrees = state.qiblaDirection - state.currentDirection,
                        pivot = Offset(
                            compassCenter.x,
                            compassCenter.y   // Rotate around the center of the compass
                        )
                    ) {
                        with(drawContext.canvas) {
                            save()
                            val scaleFactor = 0.3f
                            scale(
                                scale = scaleFactor,
                                pivot = Offset(
                                    compassCenter.x,
                                    compassCenter.y - compassRadius - needleBitmap.height / 4f
                                )
                            ) {
                                drawImage(
                                    image = qiblaIconBitmap,
                                    topLeft = Offset(
                                        compassCenter.x - qiblaIconBitmap.width / 2,
                                        compassCenter.y - compassRadius - needleBitmap.height / 4f
                                    ),
                                )
                            }
                            restore()
                        }

                    }
                }
                compassNeedleMiddle?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(dimens.size30)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            BottomCompassPreview(
                modifier = Modifier,
                onCompassClick = { id, isLoggedInRequired ->
                    Log.d("QiblaScreen", "Compass Clicked $id, $isLoggedInRequired")
                    if (!isLoggedInRequired) {
                        qiblaEvent(QiblaEvent.OnCompassClick(compassId = id))
                    } else {
                        when (authState) {
                            AuthState.Authenticated -> {
                                qiblaEvent(QiblaEvent.OnCompassClick(compassId = id))
                            }

                            else -> {
                                qiblaEvent(QiblaEvent.OnLoggedInRequiredCompassClick)
                            }
                        }
                    }
                }
            )
        }

        if (state.isLoggedInRequiredPopupVisible) {
            PopupDialog(
                modifier = Modifier,
                onDismissRequest = { qiblaEvent(QiblaEvent.OnLoggedInRequiredCompassClick) },
                title = "Login Required",
                text = "Login or Register to unlock more compass design",
                icon = R.drawable.alert,
                confirmButton = { navigateToLogin() }
            )
        }
    }
}

@Composable
fun OrientationIndicator(pitch: Float, roll: Float, modifier: Modifier = Modifier) {
    val circleSize = dimens.size80 // Size of the circle box
    val radius = with(LocalDensity.current) { circleSize.toPx() / 2 }
    val dotRadius = dimens.size8 // Size of the red dot

    Canvas(
        modifier = modifier
            .size(circleSize)
            .background(Color.Transparent, shape = CircleShape)
            .border(dimens.size3, Color.Gray, CircleShape)
    ) {
        // Normalize pitch and roll to keep the red dot within the circle
        val maxOffset = radius - dotRadius.toPx() * 2
        val xOffset = (roll / 45f).coerceIn(-1f, 1f) * maxOffset
        val yOffset = (-pitch / 45f).coerceIn(-1f, 1f) * maxOffset // Inverted for pitch

        // Draw the red dot
        drawCircle(
            color = Color.Red,
            radius = dotRadius.toPx(),
            center = Offset(
                x = center.x + xOffset,
                y = center.y + yOffset
            )
        )
    }
}
