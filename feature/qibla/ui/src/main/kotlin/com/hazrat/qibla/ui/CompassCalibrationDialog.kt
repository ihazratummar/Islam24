package com.hazrat.qibla.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

private val GoldAccent = Color(0xFFE5A93C)

/**
 * Figure-8 Compass Calibration Dialog.
 * Instructs the user to wave the device in a figure-8 pattern to calibrate hardware magnetometer.
 */
@Composable
fun CompassCalibrationDialog(
    sensorAccuracy: Int,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Figure8Transition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val accuracyText = when (sensorAccuracy) {
        3 -> "High Accuracy 🟢"
        2 -> "Medium Accuracy 🟡"
        1 -> "Low Accuracy 🟠"
        else -> "Unreliable 🔴"
    }

    val accuracyColor = when (sensorAccuracy) {
        3 -> Color(0xFF4CAF50)
        2 -> Color(0xFFFFB752)
        1 -> Color(0xFFFF8E00)
        else -> Color(0xFFFA716A)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(dimens.cornerXl),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimens.space16)
            ) {
                // Top Title & Icon
                Text(
                    text = "Calibrate Compass",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Dynamic Animated Figure-8 Indicator Graphic ♾️
                Box(
                    modifier = Modifier
                        .size(dimens.space48 * 2)
                        .clip(CircleShape)
                        .background(GoldAccent.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "♾️",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = rotationAngle
                        }
                    )
                }

                // Instructions text
                Text(
                    text = "Wave your phone in a smooth Figure-8 motion in the air to calibrate the magnetic sensor for precise Qibla direction.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = customColors.secondaryText,
                    textAlign = TextAlign.Center
                )

                // Current Accuracy Pill
                Card(
                    shape = RoundedCornerShape(dimens.cornerMd),
                    colors = CardDefaults.cardColors(
                        containerColor = accuracyColor.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = dimens.space16, vertical = dimens.space8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status: $accuracyText",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = accuracyColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimens.space8))

                // Done Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.space48),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
