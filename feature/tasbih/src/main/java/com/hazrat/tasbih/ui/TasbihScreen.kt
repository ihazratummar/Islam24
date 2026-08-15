package com.hazrat.tasbih.ui

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.tasbih.ui.component.AddCustomDhikrDialog
import com.hazrat.tasbih.ui.component.DhikrListItem
import com.hazrat.tasbih.ui.component.DhikrSelectorRow
import com.hazrat.tasbih.ui.component.RoyalTasbihCard
import com.hazrat.tasbih.ui.component.TodayDhikrSummaryCard
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.TopAppBarTitle
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Royal Islamic Tasbih Screen with Arabic calligraphy, custom dial graphics, and tactile audio/haptic feedback.
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    viewModel: TasbihViewModel,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is TasbihUiEffect.PerformHaptic -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                is TasbihUiEffect.PlaySoundClick -> {
                    try {
                        val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 60)
                        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 25)
                    } catch (_: Exception) {
                    }
                }
                is TasbihUiEffect.TargetReached -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { TopAppBarTitle(title = "Tasbih") },
                navigationIcon = { BackIcon(onBackClick = onBackClick) },
                actions = {
                    IconButton(onClick = { viewModel.processIntent(TasbihIntent.ToggleSound) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volume),
                            contentDescription = "Sound Toggle",
                            tint = if (uiState.isSoundEnabled) customColors.accentColor else customColors.secondaryText,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }
                    IconButton(onClick = { viewModel.processIntent(TasbihIntent.ToggleVibration) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.vibrate),
                            contentDescription = "Vibration Toggle",
                            tint = if (uiState.isVibrationEnabled) customColors.accentColor else customColors.secondaryText,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.space20)
        ) {
            item {
                Spacer(modifier = Modifier.height(dimens.space4))
                // Dhikr Horizontal Selector Bar
                DhikrSelectorRow(
                    tasbihList = uiState.tasbihList,
                    activeTasbih = uiState.activeTasbih,
                    onSelect = { viewModel.processIntent(TasbihIntent.SelectTasbih(it)) }
                )
                Spacer(modifier = Modifier.height(dimens.space16))
            }

            item {
                // Royal Tasbih Card with Scheherazade Arabic calligraphy and dial graphics
                uiState.activeTasbih?.let { tasbih ->
                    RoyalTasbihCard(
                        tasbih = tasbih,
                        selectedTarget = uiState.selectedTarget,
                        onCount = { viewModel.processIntent(TasbihIntent.IncrementCount) },
                        onReset = { viewModel.processIntent(TasbihIntent.ResetCount) },
                        onUndo = { viewModel.processIntent(TasbihIntent.UndoCount) },
                        onTargetSelect = { viewModel.processIntent(TasbihIntent.SetTargetLimit(it)) }
                    )
                }
                Spacer(modifier = Modifier.height(dimens.space20))
            }

            item {
                // Today Summary Card
                TodayDhikrSummaryCard(todayTotal = uiState.todayTotalDhikr)
                Spacer(modifier = Modifier.height(dimens.space24))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MORE DHIKR",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = customColors.secondaryText
                    )
                    TextButton(onClick = {
                        viewModel.processIntent(TasbihIntent.SetAddCustomDialogVisible(true))
                    }) {
                        Text(
                            text = "+ Add Custom",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = customColors.accentColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimens.space8))
            }

            items(
                items = uiState.tasbihList,
                key = { it.id }
            ) { tasbih ->
                DhikrListItem(
                    tasbih = tasbih,
                    isSelected = tasbih.id == uiState.activeTasbih?.id,
                    onSelect = { viewModel.processIntent(TasbihIntent.SelectTasbih(tasbih.id)) },
                    onToggleFavorite = {
                        viewModel.processIntent(
                            TasbihIntent.ToggleFavorite(
                                tasbih.id,
                                !tasbih.isFavorite
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(dimens.space8))
            }

            item {
                Spacer(modifier = Modifier.height(dimens.space32))
            }
        }

        if (uiState.showAddCustomDialog) {
            AddCustomDhikrDialog(
                onDismiss = { viewModel.processIntent(TasbihIntent.SetAddCustomDialogVisible(false)) },
                onConfirm = { arabic, transliteration, translated, target ->
                    viewModel.processIntent(
                        TasbihIntent.AddCustomTasbih(
                            arabicText = arabic,
                            transliteration = transliteration,
                            translatedName = translated,
                            defaultTarget = target
                        )
                    )
                }
            )
        }
    }
}
