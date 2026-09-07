package com.hazrat.alQuran.ui.khatam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetDatePickerSheet(
    initialTargetTimestamp: Long? = null,
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Preset target durations in days: 7, 15, 30, 60, 90 days
    val dayOptions = listOf(7, 15, 30, 60, 90, 180)
    var selectedDaysIndex by remember { mutableIntStateOf(2) } // default 30 days

    val targetLocalDate = remember(selectedDaysIndex) {
        LocalDate.now().plusDays(dayOptions[selectedDaysIndex].toLong())
    }
    val targetTimestamp = remember(targetLocalDate) {
        targetLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.khatam_target_end_date),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = stringResource(R.string.common_dismiss),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = stringResource(R.string.khatam_target_date_format, targetLocalDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimens.space20))

            Text(
                text = stringResource(R.string.khatam_select_duration),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(dimens.space12))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                dayOptions.chunked(3).forEach { rowDays ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        rowDays.forEach { days ->
                            val index = dayOptions.indexOf(days)
                            val isSelected = selectedDaysIndex == index
                            val chipBg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest
                            val chipTextColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(dimens.cornerMd))
                                    .background(chipBg)
                                    .clickable { selectedDaysIndex = index }
                                    .padding(vertical = dimens.space12),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.khatam_days, days),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = chipTextColor
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space24))

            Button(
                onClick = { onDateSelected(targetTimestamp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space48),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.khatam_continue),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingCompletedSheet(
    onDismissRequest: () -> Unit,
    onStartNow: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.khatam_setting_completed),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = stringResource(R.string.common_dismiss),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space24))

            Box(
                modifier = Modifier
                    .size(dimens.compCardMin)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.book),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(dimens.iconXl)
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = stringResource(R.string.khatam_go_read_quran),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dimens.space8))

            Text(
                text = stringResource(R.string.khatam_blessing),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dimens.space32))

            Button(
                onClick = onStartNow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space48),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.khatam_start_now),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReadingPlanSheet(
    onDismissRequest: () -> Unit,
    onEditTargetDateClick: () -> Unit,
    onResetPlanClick: () -> Unit,
    onEndPlanClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.khatam_edit_plan),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = stringResource(R.string.common_dismiss),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space24))

            OutlinedButton(
                onClick = {
                    onDismissRequest()
                    onEditTargetDateClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space48),
                shape = RoundedCornerShape(dimens.cornerLg)
            ) {
                Text(
                    text = stringResource(R.string.khatam_edit_end_date),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Button(
                onClick = {
                    onDismissRequest()
                    onResetPlanClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space48),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.khatam_reset_plan),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = stringResource(R.string.khatam_end_plan),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .clickable {
                        onDismissRequest()
                        onEndPlanClick()
                    }
                    .padding(vertical = dimens.space12, horizontal = dimens.space16)
            )
        }
    }
}
