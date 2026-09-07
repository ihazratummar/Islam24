package com.hazrat.prayer.ui.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.Prayer
import com.hazrat.prayer.ui.component.GoldAccent
import com.hazrat.prayer.ui.component.PrayerIconWithBackground
import com.hazrat.prayer.ui.notification.component.AzanSoundBottomSheet
import com.hazrat.prayer.ui.notification.component.PreAlertBottomSheet
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.IslamicGridBackground
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.formatLocalizedDigits
import com.hazrat.utils.toLocalizedDigits

/**
 * Prayer Notifications Screen matching reference layout & pre-alert requirements.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerNotificationScreen(
    state: PrayerNotificationState,
    onEvent: (PrayerNotificationEvent) -> Unit,
    onBackClick: () -> Unit
) {
    IslamicGridBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.prayer_notifications_title),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = stringResource(R.string.prayer_enabled_count, state.enabledCount),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = customColors.secondaryText
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        BackIcon(onBackClick = onBackClick)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            contentWindowInsets = WindowInsets()
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = dimens.space16),
                verticalArrangement = Arrangement.spacedBy(dimens.space16)
            ) {
                // Master Notifications Control Card
                item {
                    MasterNotificationCard(
                        isAllActive = state.isAllActive,
                        onMasterToggle = { onEvent(PrayerNotificationEvent.ToggleMaster(it)) },
                        onEnableAll = { onEvent(PrayerNotificationEvent.ToggleMaster(true)) },
                        onDisableAll = { onEvent(PrayerNotificationEvent.ToggleMaster(false)) }
                    )
                }

                // 5 Accordion Prayer Cards (Fajr, Dhuhr, Asr, Maghrib, Isha)
                items(Prayer.entries) { prayer ->
                    val isEnabled = state.enabledPrayers[prayer]
                    val isExpanded = state.expandedPrayers.contains(prayer)
                    val preAlertOffset = state.preAlertOffsets[prayer] ?: 0
                    val reciterName = state.azanSounds[prayer] ?: "System Default"
                    val vibrationEnabled = state.vibrationStates[prayer] ?: true

                    val prayerType = when (prayer) {
                        Prayer.FAJR -> PrayerType.FAJR
                        Prayer.DHUHR -> PrayerType.DHUHR
                        Prayer.ASR -> PrayerType.ASR
                        Prayer.MAGHRIB -> PrayerType.MAGHRIB
                        Prayer.ISHA -> PrayerType.ISHA
                    }

                    PrayerNotificationAccordionCard(
                        prayerType = prayerType,
                        isEnabled = isEnabled!!,
                        isExpanded = isExpanded,
                        preAlertOffset = preAlertOffset,
                        reciterName = reciterName,
                        vibrationEnabled = vibrationEnabled,
                        onToggleNotification = { onEvent(PrayerNotificationEvent.TogglePrayer(prayer, !isEnabled)) },
                        onToggleExpand = { onEvent(PrayerNotificationEvent.ToggleAccordion(prayer)) },
                        onToggleVibration = { onEvent(PrayerNotificationEvent.ToggleVibration(prayer, !vibrationEnabled)) },
                        onOpenPreAlertSheet = { onEvent(PrayerNotificationEvent.OpenPreAlertSheet(prayer)) },
                        onOpenAzanSheet = { onEvent(PrayerNotificationEvent.OpenAzanSoundSheet(prayer)) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(dimens.space32))
                }
            }
        }
    }

    // Modal Bottom Sheet: Pre-Alert Time Selection
    state.activePreAlertSheetPrayer?.let { prayer ->
        val currentOffset = state.preAlertOffsets[prayer] ?: 0
        PreAlertBottomSheet(
            prayer = prayer,
            currentOffsetMinutes = currentOffset,
            onDismiss = { onEvent(PrayerNotificationEvent.DismissPreAlertSheet) },
            onConfirm = { newOffset ->
                onEvent(PrayerNotificationEvent.SetPreAlertOffset(prayer, newOffset))
            }
        )
    }

    // Modal Bottom Sheet: Azan Reciter Sound Selection
    state.activeAzanSoundSheetPrayer?.let { prayer ->
        val currentSound = state.azanSounds[prayer] ?: "Abdul Basit"
        AzanSoundBottomSheet(
            prayer = prayer,
            currentSelectedSound = currentSound,
            onDismiss = { onEvent(PrayerNotificationEvent.DismissAzanSoundSheet) },
            onConfirm = { newSound ->
                onEvent(PrayerNotificationEvent.SetAzanSound(prayer, newSound))
            }
        )
    }
}

/**
 * Master Control Card for enabling/disabling all notifications at once.
 */
@Composable
private fun MasterNotificationCard(
    isAllActive: Boolean,
    onMasterToggle: (Boolean) -> Unit,
    onEnableAll: () -> Unit,
    onDisableAll: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Master Switch Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.avatarMd)
                        .clip(CircleShape)
                        .background(if (isAllActive) GoldAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(if (isAllActive) R.drawable.notificationonn else R.drawable.bell_off),
                        contentDescription = null,
                        tint = if (isAllActive) GoldAccent else customColors.secondaryText,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAllActive) stringResource(R.string.prayer_all_notifications_active) else stringResource(R.string.prayer_all_notifications_muted),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isAllActive) stringResource(R.string.prayer_quick_mute_toggle) else stringResource(R.string.prayer_tap_unmute),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }

                Switch(
                    checked = isAllActive,
                    onCheckedChange = onMasterToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = GoldAccent
                    )
                )
            }

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Button(
                    onClick = onEnableAll,
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.space40),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent.copy(alpha = 0.2f),
                        contentColor = GoldAccent
                    ),
                    border = BorderStroke(dimens.divider, GoldAccent)
                ) {
                    Text(
                        text = stringResource(R.string.prayer_enable_all),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onDisableAll,
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.space40),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(R.string.prayer_disable_all),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

/**
 * Accordion Prayer Card with expandable reciter, vibration, and pre-alert details.
 */
@Composable
private fun PrayerNotificationAccordionCard(
    prayerType: PrayerType,
    isEnabled: Boolean,
    isExpanded: Boolean,
    preAlertOffset: Int,
    reciterName: String,
    vibrationEnabled: Boolean,
    onToggleNotification: () -> Unit,
    onToggleExpand: () -> Unit,
    onToggleVibration: () -> Unit,
    onOpenPreAlertSheet: () -> Unit,
    onOpenAzanSheet: () -> Unit
) {
    val offsetText = if (preAlertOffset > 0) {
        stringResource(R.string.prayer_min_before, preAlertOffset)
    } else {
        stringResource(R.string.prayer_at_prayer_time)
    }
    val summaryText = if (isEnabled) {
        "$reciterName · $offsetText"
    } else {
        stringResource(R.string.prayer_notifications_off)
    }

    Card(
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                // Prayer Icon Container
                PrayerIconWithBackground(
                    icon = prayerType.icon,
                    containerColor = prayerType.gradient
                )

                // Title & Summary
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(prayerType.nameRes),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = summaryText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isEnabled) GoldAccent else customColors.secondaryText
                    )
                }

                // Accordion Expand Icon (ONLY VISIBLE WHEN NOTIFICATION IS ON!)
                if (isEnabled) {
                    Box(
                        modifier = Modifier
                            .size(dimens.avatarMd)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            .clickable { onToggleExpand() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(if (isExpanded) R.drawable.arrowup else R.drawable.down_arrow),
                            contentDescription = "Expand Accordion",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }
                }

                // Bell Toggle Button
                val notificationColor = if (isEnabled) GoldAccent else customColors.secondaryText
                Box(
                    modifier = Modifier
                        .size(dimens.avatarMd)
                        .clip(CircleShape)
                        .background(notificationColor.copy(alpha = 0.15f))
                        .clickable { onToggleNotification() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(if (isEnabled) R.drawable.notificationonn else R.drawable.notificationoff),
                        contentDescription = "Toggle Prayer Alert",
                        tint = notificationColor,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }
            }

            // Expanded Options (ONLY RENDERED WHEN NOTIFICATION IS ON AND EXPANDED!)
            AnimatedVisibility(
                visible = isEnabled && isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space16)
                        .padding(bottom = dimens.space16),
                    verticalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    // Azan Sound Selection Row
                    AccordionSubItem(
                        iconRes = R.drawable.quran,
                        title = stringResource(R.string.prayer_azan_sound),
                        subtitle = reciterName,
                        onClick = onOpenAzanSheet
                    )

                    // Vibration Toggle Row
                    Card(
                        shape = RoundedCornerShape(dimens.cornerLg),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimens.space16, vertical = dimens.space12),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.volume),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(dimens.iconSm)
                            )
                            Text(
                                text = stringResource(R.string.prayer_vibration),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = vibrationEnabled,
                                onCheckedChange = { onToggleVibration() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black,
                                    checkedTrackColor = GoldAccent
                                )
                            )
                        }
                    }

                    // Pre-Alert Offset Selection Row
                    AccordionSubItem(
                        iconRes = R.drawable.calendar1,
                        title = stringResource(R.string.prayer_pre_alert_time),
                        subtitle = offsetText,
                        onClick = onOpenPreAlertSheet
                    )
                }
            }
        }
    }
}

/**
 * Clickable row sub-item used inside accordion options.
 */
@Composable
private fun AccordionSubItem(
    iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space16, vertical = dimens.space12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(dimens.iconSm)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = customColors.secondaryText
                )
            }

            Icon(
                painter = painterResource(R.drawable.arrowright),
                contentDescription = null,
                tint = customColors.secondaryText,
                modifier = Modifier.size(dimens.iconSm)
            )
        }
    }
}
