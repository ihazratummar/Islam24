package com.hazrat.tasbih.ui

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.TopAppBarTitle
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    viewModel: TasbihViewModel,
    onBackClick: () -> Unit = {}
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
                // Main Royal Tasbih Counter Card (Parent card NOT clickable)
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
                TodaySummaryCard(todayTotal = uiState.todayTotalDhikr)
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

            items(uiState.tasbihList) { tasbih ->
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

@Composable
private fun DhikrSelectorRow(
    tasbihList: List<Tasbih>,
    activeTasbih: Tasbih?,
    onSelect: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
    ) {
        items(tasbihList) { tasbih ->
            val isSelected = tasbih.id == activeTasbih?.id
            val bg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            val textCol = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerFull))
                    .background(bg)
                    .clickable { onSelect(tasbih.id) }
                    .padding(horizontal = dimens.space16, vertical = dimens.space8),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tasbih.transliteration,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = textCol,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun RoyalTasbihCard(
    tasbih: Tasbih,
    selectedTarget: Int,
    onCount: () -> Unit,
    onReset: () -> Unit,
    onUndo: () -> Unit,
    onTargetSelect: (Int) -> Unit
) {
    val progress = if (selectedTarget > 0) {
        (tasbih.currentCount % selectedTarget).toFloat() / selectedTarget.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(200),
        label = "ProgressAnimation"
    )

    val activeColor = customColors.accentColor
    val trackColor = MaterialTheme.colorScheme.outlineVariant
    val innerDiscBg = MaterialTheme.colorScheme.surface
    val innerOutlineColor = MaterialTheme.colorScheme.outline

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(dimens.elevation2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Arabic Title
            Text(
                text = tasbih.arabicText,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = customColors.accentColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dimens.space4))

            // Transliteration Subtitle
            Text(
                text = tasbih.transliteration,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(dimens.space24))

            // Royal Double Ring Counter Circle - ONLY CIRCLE IS CLICKABLE FOR COUNT
            Box(
                modifier = Modifier
                    .size(dimens.layoutXl)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onCount
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val outerStrokeWidth = 34f
                    val outerOffset = outerStrokeWidth / 2f
                    val outerDrawSize = Size(size.width - outerStrokeWidth, size.height - outerStrokeWidth)
                    val gap = 16f
                    val innerRadius = (size.width / 2f) - outerStrokeWidth - gap

                    // 1. Outer Dark/Neutral Ring Track
                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(outerOffset, outerOffset),
                        size = outerDrawSize,
                        style = Stroke(width = outerStrokeWidth)
                    )

                    // 2. Outer Progress Arc
                    if (animatedProgress > 0f) {
                        drawArc(
                            color = activeColor,
                            startAngle = -90f,
                            sweepAngle = 360f * animatedProgress,
                            useCenter = false,
                            topLeft = Offset(outerOffset, outerOffset),
                            size = outerDrawSize,
                            style = Stroke(width = outerStrokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // 3. Separate Inner Circle Disc Fill
                    drawCircle(
                        color = innerDiscBg,
                        radius = innerRadius
                    )

                    // 4. Inner Circle Outline Ring
                    drawCircle(
                        color = innerOutlineColor,
                        radius = innerRadius,
                        style = Stroke(width = 4f)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Big Counting Number Text
                    Text(
                        text = "${tasbih.currentCount}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "/ $selectedTarget",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(dimens.space2))
                    Text(
                        text = "tap to count",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space24))

            // Target Limit Chips Row (33, 99, 100, 500, 1000)
            val limits = listOf(33, 99, 100, 500, 1000)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                limits.forEach { limit ->
                    val isSelected = limit == selectedTarget
                    val chipBg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    val chipTextCol = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimens.cornerFull))
                            .background(chipBg)
                            .clickable { onTargetSelect(limit) }
                            .padding(horizontal = dimens.space16, vertical = dimens.space12),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$limit",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = chipTextCol,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space20))

            // Primary Count Button - CLICKABLE FOR COUNT
            Button(
                onClick = { onCount() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.compButton),
                shape = RoundedCornerShape(dimens.cornerLg),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.tap),
                    contentDescription = "Tap",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(dimens.iconSm)
                )
                Spacer(modifier = Modifier.width(dimens.space8))
                Text(
                    text = "Tap to Count",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            // Action Buttons: Reset & Undo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Button(
                    onClick = { onReset() },
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.compButton),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.refresh),
                        contentDescription = "Reset",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                    Spacer(modifier = Modifier.width(dimens.space8))
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { onUndo() },
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.compButton),
                    shape = RoundedCornerShape(dimens.cornerLg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.undo),
                        contentDescription = "Undo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                    Spacer(modifier = Modifier.width(dimens.space8))
                    Text(
                        text = "Undo",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TodaySummaryCard(todayTotal: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dimens.avatarMd)
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.zikir),
                        contentDescription = "Stats",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }
                Spacer(modifier = Modifier.width(dimens.space12))
                Column {
                    Text(
                        text = "Total Today",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "May Allah accept your dhikr",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Text(
                text = "$todayTotal",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = customColors.accentColor
            )
        }
    }
}

@Composable
private fun DhikrListItem(
    tasbih: Tasbih,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val cardBg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val borderMod = if (isSelected) Modifier.border(dimens.divider, MaterialTheme.colorScheme.primary, RoundedCornerShape(dimens.cornerLg)) else Modifier

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(borderMod)
            .clickable { onSelect() },
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = { onToggleFavorite() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "Favorite",
                        tint = if (tasbih.isFavorite) customColors.accentColor else customColors.secondaryText,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }
                Spacer(modifier = Modifier.width(dimens.space8))
                Column {
                    Text(
                        text = tasbih.transliteration,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = tasbih.arabicText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(dimens.iconMd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun AddCustomDhikrDialog(
    onDismiss: () -> Unit,
    onConfirm: (arabic: String, transliteration: String, translated: String, target: Int) -> Unit
) {
    var arabic by remember { mutableStateOf("") }
    var transliteration by remember { mutableStateOf("") }
    var translated by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("33") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        title = {
            Text(
                text = "Add Custom Dhikr",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimens.space8)) {
                OutlinedTextField(
                    value = transliteration,
                    onValueChange = { transliteration = it },
                    label = { Text("Name / Transliteration (e.g. Astaghfirullah)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = arabic,
                    onValueChange = { arabic = it },
                    label = { Text("Arabic Text (Optional)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { Text("Target Count (e.g. 33, 100)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (transliteration.isNotBlank()) {
                        val tInt = target.toIntOrNull() ?: 33
                        onConfirm(arabic, transliteration, translated, tInt)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
