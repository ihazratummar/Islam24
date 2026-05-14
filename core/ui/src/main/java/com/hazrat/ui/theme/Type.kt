package com.hazrat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hazrat.ui.R

/**
 * @author Hazrat Ummar Shaikh
 * Industry Grade Typography based on DESIGN_SPEC.md
 */

// ========== UI FONT (Inter) ==========
//val InterFontFamily = FontFamily(
//    Font(R.font.inter_regular, FontWeight.Normal),
//    Font(R.font.inter_medium, FontWeight.Medium),
//    Font(R.font.inter_semibold, FontWeight.SemiBold),
//    Font(R.font.inter_bold, FontWeight.Bold)
//)


val AppleGothic = FontFamily(
    Font(R.font.apple_gothic, FontWeight.Normal)
)

// ========== QURANIC FONTS ==========
val AmiriFontFamily = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

val ScheherazadeFontFamily = FontFamily(
    Font(R.font.scheherazade_new_regular, FontWeight.Normal),
    Font(R.font.scheherazade_new_bold, FontWeight.Bold)
)

val NotoNaskhFontFamily = FontFamily(
    Font(R.font.noto_naskh_arabic_regular, FontWeight.Normal),
    Font(R.font.noto_naskh_arabic_medium, FontWeight.Medium),
    Font(R.font.noto_naskh_arabic_bold, FontWeight.Bold)
)

// Material 3 Typography Mapping from DESIGN_SPEC.md
val CompactTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = AppleGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// Adaptive Typography for large screen sizes
val MediumTypography = CompactTypography.copy(
    displayLarge = CompactTypography.displayLarge.copy(fontSize = 40.sp),
    displayMedium = CompactTypography.displayMedium.copy(fontSize = 36.sp),
    bodyLarge = CompactTypography.bodyLarge.copy(fontSize = 18.sp)
)

val ExpandedTypography = MediumTypography.copy(
    displayLarge = CompactTypography.displayLarge.copy(fontSize = 48.sp),
    displayMedium = CompactTypography.displayMedium.copy(fontSize = 42.sp),
    bodyLarge = CompactTypography.bodyLarge.copy(fontSize = 20.sp)
)

// Compatibility for small devices
val CompactSmallTypography = CompactTypography.copy(
    displayLarge = CompactTypography.displayLarge.copy(fontSize = 32.sp),
    displayMedium = CompactTypography.displayMedium.copy(fontSize = 28.sp),
    displaySmall = CompactTypography.displaySmall.copy(fontSize = 22.sp)
)

val CompactMediumTypography = CompactTypography

// Industry Grade Special Tokens (DESIGN_SPEC.md 1.3 & 1.4)
object IslamicTypography {
    val ArabicLg = TextStyle(fontFamily = AmiriFontFamily, fontSize = 24.sp, lineHeight = 36.sp)
    val ArabicMd = TextStyle(fontFamily = AmiriFontFamily, fontSize = 20.sp, lineHeight = 32.sp)
    val ArabicSm = TextStyle(fontFamily = AmiriFontFamily, fontSize = 18.sp, lineHeight = 28.sp)
    val ArabicXs = TextStyle(fontFamily = AmiriFontFamily, fontSize = 16.sp, lineHeight = 24.sp)
    
    val CounterXl = TextStyle(fontFamily = AppleGothic, fontWeight = FontWeight.Bold, fontSize = 48.sp, lineHeight = 56.sp, letterSpacing = (-0.5).sp)
    val CounterLg = TextStyle(fontFamily = AppleGothic, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 44.sp, letterSpacing = (-0.5).sp)
    val CounterMd = TextStyle(fontFamily = AppleGothic, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp)
}
