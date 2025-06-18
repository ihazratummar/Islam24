package com.hazrat.ui.theme

import androidx.compose.ui.graphics.Color

// Royal Green Theme Colors
 val royalGreenLight = Color(0xFF15382C) // Example of royal green
 val royalGreenPrimary = Color(0xFF3ABFAB) // Darker shade for dark mode
 val onPrimary = Color(0xFF061C16) // Darker shade for dark mode

//light colors
val primaryLight = royalGreenPrimary
val onPrimaryLight = Color.White
val primaryContainerLight = Color(0xFF8AC2BC) // Lighter green
val onPrimaryContainerLight = Color(0xFF1B5E58) // Dark green


val secondaryLight = Color(0xFF6AD1B4) // Soft green
val onSecondaryLight = Color.White
val secondaryContainerLight = Color(0xFFB3E8DD) // Very light green
val onSecondaryContainerLight = Color(0xFF153B37) // Medium green

val backgroundLight = Color(0xFFE8F6F3)
val onBackgroundLight = Color(0xFF0F302D)
val surfaceLight = Color(0xFFEFF5F3)
val onSurfaceLight = Color(0xFF183F3A)


//dark colors
val primaryDark = royalGreenPrimary
val onPrimaryDark =onPrimary
val primaryContainerDark = Color(0xFF21504E)
val onPrimaryContainerDark = Color(0xFFA5D6D0)

val secondaryDark = Color(0xFFB2CBC8)
val onSecondaryDark = Color(0xFF000000)
val secondaryContainerDark = Color(0xFF1A4848) /*Color(0xFF124436) */
val onSecondaryContainerDark = Color(0xFFEAFFF7)

val backgroundDark = Color(0xFF002A29) /* Color(0xFF071A16)*/
val onBackgroundDark = Color(0xFFF9FDFB)
val surfaceDark = Color(0xFF003231)
val onSurfaceDark = Color(0xFFE1E4DA)


// Universal Colors
val errorLight = Color(0xFFD32F2F) // Brighter error color in light mode
val onErrorLight = Color.White // Keeping white for contrast
val errorContainerLight = Color(0xFFFFCDD2) // Softer background for light mode
val onErrorContainerLight = Color(0xFF370B0B) // Darker shade for contrast

val errorDark = Color(0xFFE57373) // Lighter error color in dark mode
val onErrorDark = Color(0xFFE0E0E0) // Very light gray for contrast
val errorContainerDark = Color(0xFFB71C1C) // Muted error background for dark mode
val onErrorContainerDark = Color.White // Keeping white for contrast


// Surface and Container Colors
val surfaceVariantLight = Color(0xFFDDEFEF) // Softer greenish tone derived from the backgroundLight
val onSurfaceVariantLight = Color(0xFF164E4A) // Matches the onPrimaryContainerLight for text
val outlineLight = Color(0xFFB3D4D1) // Lighter greenish-gray for outlines
val outlineVariantLight = Color(0xFF88AAA8) // Slightly darker variant for subtle contrast
val scrimLight = Color(0xFF0F302B) // Matches onBackgroundLight for shadowing

// Inverse Colors for Contrast
val inverseSurfaceLight = Color(0xFF164E4A) // Matches onPrimaryContainerLight for dark accents
val inverseOnSurfaceLight = Color(0xFFE8F6F3) // Uses backgroundLight for contrast

// Surface Containers
val surfaceDimLight = Color(0xFFE8F6F3) // Same as backgroundLight for dimmed surfaces
val surfaceBrightLight = Color(0xFFECF8F6) // Slightly brighter variant (closer to the background)
val surfaceContainerLowestLight = Color(0xFFEDF7F5) // Very close to the background, for subtle elevation
val surfaceContainerLowLight = Color(0xFFF0F8F6) // Softer than previous
val surfaceContainerLight = Color(0xFFF3F9F7) // Slightly brighter than containerLowLight
val surfaceContainerHighLight = Color(0xFFF1F7F6) // Close to surfaceLight
val surfaceContainerHighestLight = Color(0xFFEFF5F4) // Lightest tone for highest elevations



// Surface and Container Colors
val surfaceVariantDark = Color(0xFF214844) // Derived from royalGreenLight for dark surfaces
val onSurfaceVariantDark = Color(0xFFB2E8DE) // Matches primaryContainerLight for text
val outlineDark = Color(0xFF567D7A) // Muted greenish-gray for outlines
val outlineVariantDark = Color(0xFF3C5F5D) // Slightly darker green for contrast
val scrimDark = Color(0xFF061C16) // Matches onPrimary for subtle shadows

// Inverse Colors for Contrast
val inverseSurfaceDark = Color(0xFFE8F6F3) // Matches backgroundLight for contrast
val inverseOnSurfaceDark = Color(0xFF164E4A) // Matches onPrimaryContainerLight for dark accents

// Surface Containers
val surfaceDimDark = Color(0xFF0F302B) // Matches onBackgroundLight
val surfaceBrightDark = Color(0xFF1A403C) // Slightly brighter for elevated elements
val surfaceContainerLowestDark = Color(0xFF163531) // Slightly darker than surfaceBrightDark
val surfaceContainerLowDark = Color(0xFF204844) // Matches surfaceVariantDark
val surfaceContainerDark = Color(0xFF2A5854) // Slightly lighter than surfaceVariantDark
val surfaceContainerHighDark = Color(0xFF335C58) // Softer tone for higher elevations
val surfaceContainerHighestDark = Color(0xFF3E6C68) // Lightest for highest elevations


// tertiary for dark/light elevations
val tertiaryLight = Color(0xFF386569)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFBCEBEE)
val onTertiaryContainerLight = Color(0xFF002022)

val tertiaryDark = Color(0xFFA0CFD2)
val onTertiaryDark = Color(0xFF00373A)
val tertiaryContainerDark = Color(0xFF1E4D51)
val onTertiaryContainerDark = Color(0xFFBCEBEE)

