package com.hazrat.athkar.ui.azkar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toLocalizedDigits
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthkarScreen(
    uiState: AthkarUiState,
    onBackClick: () -> Unit,
    onCountClick: (itemId: Int, maxCount: Int) -> Unit,
    onResetItemClick: (itemId: Int) -> Unit,
    onResetAllClick: (categoryId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) } // 0 = Morning, 1 = Evening
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val activeAthkarList = if (selectedCategoryIndex == 0) uiState.morningAthkar else uiState.eveningAthkar
    val categoryId = if (selectedCategoryIndex == 0) 27 else 28

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { activeAthkarList.size }
    )
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Box {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerSm))
                                .clickable { isDropdownExpanded = true }
                                .padding(vertical = dimens.space4, horizontal = dimens.space4)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                            ) {
                                Text(
                                    text = if (selectedCategoryIndex == 0) stringResource(R.string.athkar_morning) else stringResource(R.string.athkar_evening),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.down_arrow),
                                    contentDescription = stringResource(R.string.athkar_select_category),
                                    tint = customColors.secondaryText,
                                    modifier = Modifier.size(dimens.iconXs)
                                )
                            }
                            if (activeAthkarList.isNotEmpty()) {
                                Text(
                                    text = "${(pagerState.currentPage + 1).toLocalizedDigits()} / ${activeAthkarList.size.toLocalizedDigits()}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = customColors.secondaryText
                                )
                            }
                        }

                        // Category Switcher Dropdown
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.athkar_morning)) },
                                onClick = {
                                    selectedCategoryIndex = 0
                                    isDropdownExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.athkar_evening)) },
                                onClick = {
                                    selectedCategoryIndex = 1
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                },
                navigationIcon = { BackIcon(onBackClick = onBackClick) },
                actions = {
                    IconButton(onClick = { onResetAllClick(categoryId) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.refresh),
                            contentDescription = stringResource(R.string.athkar_reset_all),
                            tint = customColors.secondaryText,
                            modifier = Modifier.size(dimens.iconMd)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            if (activeAthkarList.isNotEmpty()) {
                val currentItemState = activeAthkarList.getOrNull(pagerState.currentPage)
                val item = currentItemState?.item
                val currentCount = currentItemState?.currentCount ?: 0
                val targetCount = item?.repeatCount ?: 0

                // Sleek Floating Capsule Control Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space16, vertical = dimens.space16),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(dimens.cornerFull),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                        border = BorderStroke(dimens.divider, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                        shadowElevation = dimens.elevation3,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimens.space12, vertical = dimens.space8),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Main Free-Counter Button (Tap to count infinitely)
                            Surface(
                                shape = RoundedCornerShape(dimens.cornerFull),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(dimens.cornerFull))
                                    .clickable {
                                        if (item != null) {
                                            onCountClick(item.id, targetCount)
                                            haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = dimens.space20, vertical = dimens.space12),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                                ) {
                                    Text(
                                        text = "$currentCount",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }

                            // Right: Sleek Action Buttons (Previous, Reset Item, Next)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                            ) {
                                // Previous Button
                                Box(
                                    modifier = Modifier
                                        .size(dimens.compChip)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage > 0) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                            else Color.Transparent
                                        )
                                        .clickable(enabled = pagerState.currentPage > 0) {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow_left),
                                        contentDescription = stringResource(R.string.common_previous),
                                        tint = if (pagerState.currentPage > 0) MaterialTheme.colorScheme.onSurface else customColors.secondaryText.copy(alpha = 0.3f),
                                        modifier = Modifier.size(dimens.iconSm)
                                    )
                                }

                                // Reset Current Card Count Button
                                Box(
                                    modifier = Modifier
                                        .size(dimens.compChip)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                                        .clickable {
                                            if (item != null) {
                                                onResetItemClick(item.id)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.refresh),
                                        contentDescription = stringResource(R.string.athkar_reset_current),
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(dimens.iconSm)
                                    )
                                }

                                // Next Button
                                Box(
                                    modifier = Modifier
                                        .size(dimens.compChip)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage < activeAthkarList.size - 1) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                            else Color.Transparent
                                        )
                                        .clickable(enabled = pagerState.currentPage < activeAthkarList.size - 1) {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrowright),
                                        contentDescription = stringResource(R.string.common_next),
                                        tint = if (pagerState.currentPage < activeAthkarList.size - 1) MaterialTheme.colorScheme.onSurface else customColors.secondaryText.copy(alpha = 0.3f),
                                        modifier = Modifier.size(dimens.iconSm)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (activeAthkarList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.athkar_no_azkar),
                    style = MaterialTheme.typography.titleMedium,
                    color = customColors.secondaryText
                )
            }
        } else {
            // Main Full Screen Horizontal Page View
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Custom Islamic Architectural Pattern & Gradient Watermark Background
                AwesomeIslamicBackground(
                    modifier = Modifier.fillMaxSize()
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    val athkarState = activeAthkarList[pageIndex]
                    val item = athkarState.item

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = dimens.space24, vertical = dimens.space16),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(dimens.space16))

                        // Large Arabic Text in Scheherazade Font
                        Text(
                            text = item.arabicText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = ScheherazadeFontFamily,
                                color = MaterialTheme.colorScheme.onBackground,
                                textDirection = TextDirection.Rtl,
                                fontFeatureSettings = "cv62",
                                fontSize = 30.sp,
                                lineHeight = 60.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(dimens.space24))

                        val isBengali = LocalLocale.current.platformLocale.language == "bn"
                        val translitText = if (isBengali && !item.bnTransliteration.isNullOrBlank()) item.bnTransliteration!! else item.transliteration
                        val translationText = if (isBengali && !item.bnTranslation.isNullOrBlank()) item.bnTranslation!! else item.translation
                        val referenceText = if (isBengali && !item.bnReference.isNullOrBlank()) item.bnReference!! else item.reference

                        // Transliteration
                        if (translitText.isNotBlank()) {
                            Text(
                                text = translitText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.88f),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(dimens.space16))
                        }

                        // Translation
                        if (!translationText.isNullOrBlank()) {
                            Text(
                                text = translationText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = customColors.secondaryText,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(dimens.space24))
                        }

                        // Source Reference
                        if (!referenceText.isNullOrBlank()) {
                            Text(
                                text = if (isBengali) "উৎস: $referenceText" else "From: $referenceText",
                                style = MaterialTheme.typography.labelMedium,
                                color = customColors.secondaryText.copy(alpha = 0.7f),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(dimens.space64))
                    }
                }
            }
        }
    }
}

/**
 * Custom Canvas Islamic Geometry & Ambient Glow Background.
 * Renders elegant radial ambient lighting and geometric arch lines.
 */
@Composable
private fun AwesomeIslamicBackground(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    Box(modifier = modifier) {
        // Soft Radial Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerPoint = center
            val radius = size.minDimension * 0.75f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = if (isDark) 0.08f else 0.04f),
                        Color.Transparent
                    ),
                    center = centerPoint,
                    radius = radius
                ),
                radius = radius,
                center = centerPoint
            )

            // Subtitle Arch Geometric Line Art
            val path = Path().apply {
                val width = size.width
                val height = size.height
                moveTo(width * 0.2f, height * 0.35f)
                cubicTo(
                    width * 0.35f, height * 0.22f,
                    width * 0.65f, height * 0.22f,
                    width * 0.8f, height * 0.35f
                )
            }
            drawPath(
                path = path,
                color = primaryColor.copy(alpha = 0.06f),
                style = Stroke(width = 3f)
            )
        }
    }
}