package com.hazrat.athkar.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.athkar.ui.azkar.AthkarItemState
import com.hazrat.ui.R
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.getSystemLanguage

@Composable
fun AthkarCard(
    athkarState: AthkarItemState,
    onCountClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val systemLanguage = getSystemLanguage()
    
    val item = athkarState.item
    val targetCount = item.repeatCount
    val currentCount = athkarState.currentCount
    val isCompleted = athkarState.isCompleted

    // Dynamic borders and colors to represent progress states elegantly
    val borderStrokeColor by animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        } else if (currentCount > 0) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
        },
        label = "borderColor"
    )

    val cardBgColor by animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "cardBgColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.space8, horizontal = dimens.space16)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(dimens.cornerLg),
        border = BorderStroke(dimens.divider, borderStrokeColor),
        elevation = CardDefaults.cardElevation(dimens.elevation1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16)
        ) {
            // Header Row: Reference or Item ID, Chevron, and individual reset button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimens.compChip)
                            .clip(CircleShape)
                            .clickable { onResetClick() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.refresh),
                            contentDescription = "Reset Count",
                            tint = if (currentCount > 0) customColors.accentColor else customColors.secondaryText.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(dimens.iconSm)
                                .align(Alignment.Center)
                        )
                    }
                    if (item.reference.isNotBlank()) {
                        Text(
                            text = item.reference.take(25) + if (item.reference.length > 25) "..." else "",
                            style = MaterialTheme.typography.labelSmall,
                            color = customColors.secondaryText
                        )
                    }
                }

                Icon(
                    painter = painterResource(
                        id = if (expanded) R.drawable.arrowup else R.drawable.down_arrow
                    ),
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = customColors.secondaryText,
                    modifier = Modifier.size(dimens.iconSm)
                )
            }

            Spacer(modifier = Modifier.height(dimens.space8))

            // Main Text Content: Large, beautiful Arabic matching Ayah screen styling
            Text(
                text = item.arabicText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.space12),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = ScheherazadeFontFamily,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Translateration in Default View
            val translitText = item.transliteration
            if (translitText.isNotBlank()) {
                Text(
                    text = translitText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = customColors.secondaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space8)
                )
            }

            // Expanded Details: Full Translation and full Reference
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(dimens.space12))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = dimens.divider,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
                    )
                    Spacer(modifier = Modifier.height(dimens.space12))
                    Text(
                        text = item.translation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (item.reference.isNotBlank()) {
                        Spacer(modifier = Modifier.height(dimens.space8))
                        Text(
                            text = "Reference: ${item.reference}",
                            style = MaterialTheme.typography.labelSmall,
                            color = customColors.secondaryText,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            // Counting Row: Circular Wheel Counter / Tap Interaction
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.avatarXl)
                        .clip(CircleShape)
                        .clickable {
                            if (!isCompleted) {
                                onCountClick()
                                val nextCount = currentCount + 1
                                if (nextCount >= targetCount) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                } else {
                                    haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Circle Progress Wheel Background
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f),
                        strokeWidth = dimens.space4,
                    )
                    // Active Progress Arc
                    CircularProgressIndicator(
                        progress = {
                            if (targetCount > 0) currentCount.toFloat() / targetCount.toFloat() else 0f
                        },
                        modifier = Modifier.fillMaxSize(),
                        color = if (isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                        strokeWidth = dimens.space4,
                    )

                    // Counter Text inside circular indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                painter = painterResource(id = R.drawable.check),
                                contentDescription = "Completed",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(dimens.iconLg)
                            )
                        } else {
                            Text(
                                text = "$currentCount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "/ $targetCount",
                                style = MaterialTheme.typography.labelSmall,
                                color = customColors.secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}
