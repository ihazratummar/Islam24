package com.hazrat.prayer.ui.notification.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.Prayer
import com.hazrat.prayer.ui.component.GoldAccent
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

data class PreAlertOption(
    val label: String,
    val minutes: Int
)

val defaultPreAlertOptions = listOf(
    PreAlertOption("At prayer time", 0),
    PreAlertOption("5 min before", 5),
    PreAlertOption("10 min before", 10),
    PreAlertOption("15 min before", 15),
    PreAlertOption("30 min before", 30)
)

/**
 * Pre-Alert Time Selection Bottom Sheet matching Screenshot 2.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreAlertBottomSheet(
    prayer: Prayer,
    currentOffsetMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var selectedMinutes by remember(currentOffsetMinutes) { mutableIntStateOf(currentOffsetMinutes) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(topStart = dimens.cornerXl, topEnd = dimens.cornerXl)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space20, vertical = dimens.space16),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Drag Handle Indicator
            Box(
                modifier = Modifier
                    .width(dimens.space32)
                    .height(dimens.space4)
                    .clip(RoundedCornerShape(dimens.cornerFull))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    .align(Alignment.CenterHorizontally)
            )

            // Header Title & Subtitle
            Column(verticalArrangement = Arrangement.spacedBy(dimens.space4)) {
                Text(
                    text = "Pre-Alert Time",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "When to notify you before ${prayer.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = customColors.secondaryText
                )
            }

            Spacer(modifier = Modifier.height(dimens.space4))

            // 2-Column Options Grid
            val chunkedOptions = defaultPreAlertOptions.chunked(2)
            chunkedOptions.forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    rowOptions.forEach { option ->
                        val isSelected = (selectedMinutes == option.minutes)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(dimens.space48)
                                .clip(RoundedCornerShape(dimens.cornerMd))
                                .clickable { selectedMinutes = option.minutes },
                            shape = RoundedCornerShape(dimens.cornerMd),
                            color = if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            border = if (!isSelected) BorderStroke(dimens.divider, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)) else null
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    if (rowOptions.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            // Done Action Button
            Button(
                onClick = {
                    onConfirm(selectedMinutes)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space48),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))
        }
    }
}
