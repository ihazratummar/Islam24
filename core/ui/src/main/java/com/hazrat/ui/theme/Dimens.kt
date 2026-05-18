package com.hazrat.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Industry-standard design tokens for Islam 24.
 * Defines spatial systems, component dimensions, and layout constraints.
 */
data class Dimens(
    // Spacing System (8dp grid)
    val space2: Dp = 2.dp,
    val space4: Dp = 4.dp,
    val space8: Dp = 8.dp,
    val space12: Dp = 12.dp,
    val space16: Dp = 16.dp,
    val space20: Dp = 20.dp,
    val space24: Dp = 24.dp,
    val space32: Dp = 32.dp,
    val space40: Dp = 40.dp,
    val space48: Dp = 48.dp,
    val space56: Dp = 56.dp,
    val space64: Dp = 64.dp,

    // Iconography
    val iconSx: Dp = 12.dp,
    val iconXs: Dp = 16.dp,
    val iconSm: Dp = 20.dp,
    val iconMd: Dp = 24.dp,
    val iconLg: Dp = 32.dp,
    val iconXl: Dp = 48.dp,

    // Avatars & Imagery
    val avatarSm: Dp = 32.dp,
    val avatarMd: Dp = 40.dp,
    val avatarLg: Dp = 56.dp,
    val avatarXl: Dp = 96.dp,

    // Component Dimensions
    val compButton: Dp = 48.dp,
    val compInput: Dp = 56.dp,
    val compListItem: Dp = 56.dp,
    val compChip: Dp = 32.dp,
    val compTopBar: Dp = 64.dp,
    val compNavBar: Dp = 80.dp,
    val compNavRail: Dp = 80.dp,
    val compCardMin: Dp = 80.dp,

    // FAB Dimensions
    val fabSm: Dp = 40.dp,
    val fabMd: Dp = 56.dp,
    val fabLg: Dp = 96.dp,

    // Layout Containers
    val layoutXs: Dp = 96.dp,
    val layoutSm: Dp = 120.dp,
    val layoutMd: Dp = 160.dp,
    val layoutLg: Dp = 200.dp,
    val layoutXl: Dp = 240.dp,
    val layoutXxl: Dp = 296.dp,

    // Structural Elements
    val divider: Dp = 1.dp,
    val elevation1: Dp = 1.dp,
    val elevation2: Dp = 3.dp,
    val elevation3: Dp = 6.dp,

    // Corner Radius System
    val cornerXs: Dp = 4.dp,
    val cornerSm: Dp = 8.dp,
    val cornerMd: Dp = 12.dp,
    val cornerLg: Dp = 16.dp,
    val cornerXl: Dp = 28.dp,
    val cornerFull: Dp = 999.dp,
)

/**
 * Standard phone dimensions (Compact Window Size Class).
 */
val CompactDimens = Dimens()

/**
 * Foldables and small tablets (Medium Window Size Class).
 */
val MediumDimens = CompactDimens.copy(
    space20 = 24.dp,
    space24 = 28.dp,
    space32 = 40.dp,
    space48 = 64.dp,
    space56 = 72.dp,
    space64 = 96.dp,
    layoutXs = 120.dp,
    layoutSm = 160.dp,
    layoutMd = 220.dp,
    layoutLg = 300.dp,
    layoutXl = 380.dp,
    layoutXxl = 460.dp,
    compTopBar = 72.dp,
    compNavBar = 88.dp,
)

/**
 * Large tablets and desktops (Expanded Window Size Class).
 */
val ExpandedDimens = CompactDimens.copy(
    space20 = 32.dp,
    space24 = 40.dp,
    space32 = 56.dp,
    space48 = 80.dp,
    space56 = 96.dp,
    space64 = 128.dp,
    layoutXs = 160.dp,
    layoutSm = 220.dp,
    layoutMd = 320.dp,
    layoutLg = 420.dp,
    layoutXl = 520.dp,
    layoutXxl = 640.dp,
    compTopBar = 80.dp,
    compNavBar = 96.dp,
    compNavRail = 96.dp,
)
