package com.hazrat.alQuran.ui.khatam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.quran.KhatamPlanModel
import com.hazrat.model.quran.KhatamStatus
import com.hazrat.ui.R
import com.hazrat.ui.common.LongText
import com.hazrat.ui.common.SurahNameProvider
import com.hazrat.ui.theme.dimens
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Composable
fun KhatamScreenContent(
    activePlan: KhatamPlanModel?,
    khatamHistory: List<KhatamPlanModel>,
    onStartNewPlanClick: () -> Unit,
    onContinueReadingClick: (surahNumber: Int, ayahNumber: Int) -> Unit,
    onEditPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            item {
                Spacer(modifier = Modifier.height(dimens.space8))
            }

            // 1. Active Khatam Plan Banner Card
            item {
                ActiveKhatamCard(
                    activePlan = activePlan,
                    onEditPlanClick = onEditPlanClick
                )
            }

            // 2. Former Plans History Section
            if (khatamHistory.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.khatam_former_plans),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = dimens.space12, bottom = dimens.space4)
                    )
                }

                items(khatamHistory, key = { it.id }) { historyPlan ->
                    FormerPlanCard(plan = historyPlan)
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimens.compCardMin)) // Bottom padding for fixed button bar
            }
        }

        // 3. Fixed Bottom Action Button Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = dimens.space8
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space16)
            ) {
                val hasActiveProgress = activePlan != null && activePlan.status == KhatamStatus.IN_PROGRESS

                Button(
                    onClick = {
                        if (hasActiveProgress) {
                            onContinueReadingClick(activePlan.lastReadSurahNumber, activePlan.lastReadAyahNumber)
                        } else {
                            onStartNewPlanClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.space48),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (hasActiveProgress) stringResource(R.string.khatam_continue_reading) else stringResource(R.string.khatam_start_new_plan),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveKhatamCard(
    activePlan: KhatamPlanModel?,
    onEditPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F382C),
            Color(0xFF1B5E4B),
            Color(0xFF0C2B22)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = dimens.divider,
                color = Color(0xFF00E676).copy(alpha = 0.3f),
                shape = RoundedCornerShape(dimens.cornerXl)
            ),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = gradientBrush)
                .padding(dimens.space20)
        ) {
            if (activePlan == null) {
                // Empty state when no plan is active
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.khatam_quran_title),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(dimens.space8))
                        Text(
                            text = stringResource(R.string.khatam_no_plan_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Box(
                        modifier = Modifier
                            .size(dimens.space56)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.book),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(dimens.iconLg)
                        )
                    }
                }
            } else {
                // Active Khatam plan present
                val formattedEndDate = remember(activePlan.targetEndDateTimestamp) {
                    Instant.ofEpochMilli(activePlan.targetEndDateTimestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }

                val daysLeft = remember(activePlan.targetEndDateTimestamp) {
                    val nowMillis = System.currentTimeMillis()
                    val diff = activePlan.targetEndDateTimestamp - nowMillis
                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    if (days < 0) 0 else days
                }

                val isExpired = activePlan.isExpired

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Row: Status Badge Tag + Title + Edit Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            // Status Pill Badge
                            val (badgeBg, badgeText, badgeLabel) = when {
                                activePlan.status == KhatamStatus.COMPLETED -> Triple(Color(0xFF00E676).copy(alpha = 0.25f), Color(0xFFB9F6CA), "Completed")
                                isExpired -> Triple(Color(0xFFE53935).copy(alpha = 0.3f), Color(0xFFFF8A80), "Expired")
                                else -> Triple(Color(0xFFD4AF37).copy(alpha = 0.3f), Color(0xFFFFD54F), "In Progress")
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(dimens.cornerSm))
                                    .background(badgeBg)
                                    .padding(horizontal = dimens.space8, vertical = dimens.space4)
                            ) {
                                Text(
                                    text = badgeLabel,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = badgeText
                                )
                            }

                            Text(
                                text = activePlan.title,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(dimens.avatarSm)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable { onEditPlanClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.editcontained),
                                contentDescription = "Edit Plan",
                                tint = Color.White,
                                modifier = Modifier.size(dimens.iconSm)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimens.space12))

                    // Date & Days Remaining metadata
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                        Text(
                            text = if (daysLeft > 0) "$daysLeft day left  •  Expiration Date: $formattedEndDate" else "Expiration Date: $formattedEndDate",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimens.space24))

                    // Progress Stat Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = String.format("%.1f%%", activePlan.progressPercentage),
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF00E676)
                        )
                        Text(
                            text = stringResource(R.string.khatam_reading_progress),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimens.space8))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { (activePlan.progressPercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimens.space12)
                            .clip(CircleShape),
                        color = Color(0xFF00E676),
                        trackColor = Color(0xFF0A261E)
                    )

                    Spacer(modifier = Modifier.height(dimens.space16))

                    // Current Reading Location Pill
                    val surahName = SurahNameProvider.getSurahName(activePlan.lastReadSurahNumber)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(dimens.cornerMd))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(horizontal = dimens.space12, vertical = dimens.space8)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.book),
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(dimens.iconSm)
                            )
                            LongText(
                                text = stringResource(R.string.khatam_current_position, surahName, activePlan.lastReadSurahNumber, activePlan.lastReadAyahNumber),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormerPlanCard(
    plan: KhatamPlanModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = dimens.divider,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(dimens.cornerLg)
            ),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(dimens.space4))
                val startDate = Instant.ofEpochMilli(plan.startDateTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val endDate = Instant.ofEpochMilli(plan.targetEndDateTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                val durationDays = TimeUnit.MILLISECONDS.toDays(
                    (plan.targetEndDateTimestamp - plan.startDateTimestamp).coerceAtLeast(0)
                ).coerceAtLeast(1)

                Text(
                    text = stringResource(R.string.khatam_history_duration, startDate, endDate, durationDays),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(dimens.space12))

            val (statusLabel, statusBg, statusText) = when (plan.displayStatus) {
                KhatamStatus.COMPLETED -> Triple(stringResource(R.string.khatam_completed_status), MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                KhatamStatus.EXPIRED -> Triple(stringResource(R.string.khatam_expired_status), MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                KhatamStatus.ENDED -> Triple(stringResource(R.string.khatam_ended_status), MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.onSurfaceVariant)
                KhatamStatus.IN_PROGRESS -> Triple(stringResource(R.string.khatam_in_progress_status), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerSm))
                    .background(statusBg)
                    .padding(horizontal = dimens.space8, vertical = dimens.space4)
            ) {
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = statusText
                )
            }
        }
    }
}
