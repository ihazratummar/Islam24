package com.hazrat.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * All values are multiples of 4dp — Google Material 3 base grid.
 *
 * Naming convention:
 *   space*   → padding, margin, gap
 *   icon*    → icon sizes
 *   avatar*  → avatar / profile image sizes
 *   comp*    → interactive component heights (button, input, bar)
 *   fab*     → floating action button sizes
 *   layout*  → large layout containers (cards, headers, images)
 *
 * Window class multipliers (applied in Theme.kt via WindowSizeClass):
 *   CompactSmall  0.75×   phones < 360dp width
 *   Compact       1.00×   standard phones
 *   Medium        1.50×   foldables / small tablets
 *   Expanded      2.00×   tablets ≥840dp / desktop
 */
data class Dimens(

    // ── Spacing (padding / margin / gap) ──────────────────────────────
    val space2: Dp = 0.dp,   // hairline / divider inset
    val space4: Dp = 0.dp,   // tight inset (chip padding)
    val space8: Dp = 0.dp,   // small inset (icon padding)
    val space12: Dp = 0.dp,  // list item vertical padding
    val space16: Dp = 0.dp,  // standard content padding  ← most common
    val space20: Dp = 0.dp,  // card internal padding
    val space24: Dp = 0.dp,  // section / dialog padding
    val space32: Dp = 0.dp,  // large gap between sections
    val space48: Dp = 0.dp,  // screen horizontal edge padding
    val space56: Dp = 0.dp,  // screen horizontal edge padding
    val space64: Dp = 0.dp,  // xl gap / hero section spacing

    // ── Icon sizes ────────────────────────────────────────────────────
    val iconSm: Dp = 0.dp,   // 16dp — dense / trailing icons
    val iconMd: Dp = 0.dp,   // 24dp — standard (M3 default)
    val iconLg: Dp = 0.dp,   // 32dp — large decorative icon
    val iconXl: Dp = 0.dp,   // 48dp — xl / FAB inner icon

    // ── Avatar / image sizes ──────────────────────────────────────────
    val avatarSm: Dp = 0.dp, // 32dp — comment / chip avatar
    val avatarMd: Dp = 0.dp, // 40dp — list item avatar
    val avatarLg: Dp = 0.dp, // 56dp — profile header avatar
    val avatarXl: Dp = 0.dp, // 96dp — profile page avatar

    // ── Interactive component heights ─────────────────────────────────
    val compButton: Dp = 0.dp,   // 40dp — button (M3 standard)
    val compInput: Dp = 0.dp,    // 56dp — text field (M3 standard)
    val compListItem: Dp = 0.dp, // 56dp — single-line list item
    val compChip: Dp = 0.dp,     // 32dp — filter / assist chip
    val compTopBar: Dp = 0.dp,   // 64dp — top app bar
    val compNavBar: Dp = 0.dp,   // 80dp — bottom nav bar height
    val compNavRail: Dp = 0.dp,  // 80dp — nav rail width
    val compCard: Dp = 0.dp,     // 80dp — card minimum height

    // ── FAB sizes ─────────────────────────────────────────────────────
    val fabSm: Dp = 0.dp,    // 40dp — small FAB
    val fabMd: Dp = 0.dp,    // 56dp — standard FAB (M3)
    val fabLg: Dp = 0.dp,    // 96dp — large FAB

    // ── Layout (Large containers / heights) ──────────────────────────
    val layoutXs:  Dp = 0.dp,  // 96dp
    val layoutSm:  Dp = 0.dp,  // 120dp
    val layoutMd:  Dp = 0.dp,  // 160dp
    val layoutLg:  Dp = 0.dp,  // 200dp
    val layoutXl:  Dp = 0.dp,  // 240dp
    val layoutXxl: Dp = 0.dp,  // 296dp

    // ── Structural ────────────────────────────────────────────────────
    val divider: Dp = 0.dp,      // 1dp — divider / border
    val elevation1: Dp = 0.dp,   // 1dp — tonal elevation level 1
    val elevation2: Dp = 0.dp,   // 3dp — tonal elevation level 2
    val elevation3: Dp = 0.dp,   // 6dp — tonal elevation level 3
    val cornerSm: Dp = 0.dp,     // 4dp — small corner radius
    val cornerMd: Dp = 0.dp,     // 12dp — medium corner radius
    val cornerLg: Dp = 0.dp,     // 16dp — large corner radius (card)
    val cornerXl: Dp = 0.dp,     // 28dp — extra large / bottom sheet
    val cornerFull: Dp = 0.dp,   // 50dp — fully rounded (FAB, chip)

)

// ── Compact Small  (phones < 360dp) — 0.75× ──────────────────────────
val CompactSmallDimens = Dimens(
    space2 = 2.dp, space4 = 3.dp, space8 = 6.dp, space12 = 9.dp,
    space16 = 12.dp, space20 = 15.dp, space24 = 18.dp,
    space32 = 24.dp, space48 = 36.dp, space56 = 42.dp, space64 = 48.dp,

    iconSm = 12.dp, iconMd = 18.dp, iconLg = 24.dp, iconXl = 36.dp,

    avatarSm = 24.dp, avatarMd = 30.dp, avatarLg = 42.dp, avatarXl = 72.dp,

    compButton = 30.dp, compInput = 42.dp, compListItem = 42.dp,
    compChip = 24.dp, compTopBar = 48.dp, compNavBar = 60.dp,
    compNavRail = 60.dp, compCard = 60.dp,

    fabSm = 30.dp, fabMd = 42.dp, fabLg = 72.dp,

    layoutXs = 72.dp, layoutSm = 90.dp, layoutMd = 120.dp,
    layoutLg = 150.dp, layoutXl = 180.dp, layoutXxl = 222.dp,

    divider = 1.dp, elevation1 = 1.dp, elevation2 = 3.dp, elevation3 = 6.dp,
    cornerSm = 4.dp, cornerMd = 8.dp, cornerLg = 12.dp,
    cornerXl = 20.dp, cornerFull = 50.dp,
)

// ── Compact  (standard phones) — 1.0× ────────────────────────────────
val CompactDimens = Dimens(
    space2 = 2.dp, space4 = 4.dp, space8 = 8.dp, space12 = 12.dp,
    space16 = 16.dp, space20 = 20.dp, space24 = 24.dp,
    space32 = 32.dp, space48 = 48.dp, space56 = 56.dp, space64 = 64.dp,

    iconSm = 16.dp, iconMd = 24.dp, iconLg = 32.dp, iconXl = 48.dp,

    avatarSm = 32.dp, avatarMd = 40.dp, avatarLg = 56.dp, avatarXl = 96.dp,

    compButton = 40.dp, compInput = 56.dp, compListItem = 56.dp,
    compChip = 32.dp, compTopBar = 64.dp, compNavBar = 80.dp,
    compNavRail = 80.dp, compCard = 80.dp,

    fabSm = 40.dp, fabMd = 56.dp, fabLg = 96.dp,

    layoutXs = 96.dp, layoutSm = 120.dp, layoutMd = 160.dp,
    layoutLg = 200.dp, layoutXl = 240.dp, layoutXxl = 296.dp,

    divider = 1.dp, elevation1 = 1.dp, elevation2 = 3.dp, elevation3 = 6.dp,
    cornerSm = 4.dp, cornerMd = 12.dp, cornerLg = 16.dp,
    cornerXl = 28.dp, cornerFull = 50.dp,
)

// ── Medium  (foldables / small tablets) — 1.5× ───────────────────────
val MediumDimens = Dimens(
    space2 = 2.dp,
    space4 = 6.dp,
    space8 = 12.dp,
    space12 = 18.dp,
    space16 = 24.dp,
    space20 = 30.dp,
    space24 = 36.dp,
    space32 = 48.dp,
    space48 = 60.dp,
    space56 = 70.dp,
    space64 = 96.dp,

    iconSm = 24.dp, iconMd = 36.dp, iconLg = 48.dp, iconXl = 72.dp,

    avatarSm = 48.dp, avatarMd = 60.dp, avatarLg = 84.dp, avatarXl = 144.dp,

    compButton = 60.dp, compInput = 84.dp, compListItem = 84.dp,
    compChip = 48.dp, compTopBar = 96.dp, compNavBar = 120.dp,
    compNavRail = 120.dp, compCard = 120.dp,

    fabSm = 60.dp, fabMd = 84.dp, fabLg = 144.dp,

    layoutXs = 144.dp, layoutSm = 180.dp, layoutMd = 240.dp,
    layoutLg = 300.dp, layoutXl = 360.dp, layoutXxl = 444.dp,

    divider = 1.dp, elevation1 = 1.dp, elevation2 = 3.dp, elevation3 = 6.dp,
    cornerSm = 4.dp, cornerMd = 12.dp, cornerLg = 16.dp,
    cornerXl = 28.dp, cornerFull = 50.dp,
)

// ── Expanded  (tablets ≥840dp / desktop) — 2.0× ──────────────────────
val ExpandedDimens = Dimens(
    space2 = 2.dp, space4 = 8.dp, space8 = 16.dp, space12 = 24.dp,
    space16 = 32.dp, space20 = 40.dp, space24 = 48.dp,
    space32 = 64.dp, space48 = 96.dp, space56 = 112.dp, space64 = 128.dp,

    iconSm = 32.dp, iconMd = 48.dp, iconLg = 64.dp, iconXl = 96.dp,

    avatarSm = 64.dp, avatarMd = 80.dp, avatarLg = 112.dp, avatarXl = 192.dp,

    compButton = 80.dp, compInput = 112.dp, compListItem = 112.dp,
    compChip = 64.dp, compTopBar = 128.dp, compNavBar = 160.dp,
    compNavRail = 160.dp, compCard = 160.dp,

    fabSm = 80.dp, fabMd = 112.dp, fabLg = 192.dp,

    layoutXs = 192.dp, layoutSm = 240.dp, layoutMd = 320.dp,
    layoutLg = 400.dp, layoutXl = 480.dp, layoutXxl = 592.dp,

    divider = 1.dp, elevation1 = 1.dp, elevation2 = 3.dp, elevation3 = 6.dp,
    cornerSm = 4.dp, cornerMd = 12.dp, cornerLg = 16.dp,
    cornerXl = 28.dp, cornerFull = 50.dp,
)
