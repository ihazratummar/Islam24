package com.hazrat.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hazrat.ui.R
import androidx.compose.material3.Typography

val Poppins = FontFamily(
    fonts = listOf(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_semibold, FontWeight.SemiBold)
    )
)

val AlQalam = FontFamily(
    fonts = listOf(
        Font(R.font.al_qalam)
    )
)

val Uthmani = FontFamily(
    fonts = listOf(
        Font(R.font.uthmani_main)
    )
)

val IndoPak = FontFamily(
    fonts = listOf(
        Font(R.font.uthmanic)
    )
)

val Hidaya = FontFamily(
    fonts = listOf(
        Font(R.font.hidayatullah)
    )
)



val Lafadz  = FontFamily(
    fonts = listOf(
        Font(R.font.lafadz)
    )
)

val Kitab  = FontFamily(
    fonts = listOf(
        Font(R.font.kitab)
    )
)


// Set of Material typography styles to start with
val CompactTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = Poppins,
        lineHeight = 45.sp
    ),
    displayMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = Poppins,
        lineHeight = 36.sp
    ),
    displaySmall = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp
    ),
    bodySmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp

    ),
    labelMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 8.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp
    )
)

val CompactMediumTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 40.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 65.sp
    ),
    displayMedium = TextStyle(
        fontSize = 37.5.sp, // 30.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 57.5.sp // 46.sp * 1.25
    ),
    displaySmall = TextStyle(
        fontSize = 35.sp, // 28.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 52.5.sp // 42.sp * 1.25
    ),
    headlineLarge = TextStyle(
        fontSize = 32.5.sp, // 26.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 47.5.sp // 38.sp * 1.25
    ),
    headlineMedium = TextStyle(
        fontSize = 30.sp, // 24.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 45.sp // 36.sp * 1.25
    ),
    headlineSmall = TextStyle(
        fontSize = 27.5.sp, // 22.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.5.sp // 34.sp * 1.25
    ),
    titleLarge = TextStyle(
        fontSize = 25.sp, // 20.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp // 32.sp * 1.25
    ),
    titleMedium = TextStyle(
        fontSize = 22.5.sp, // 18.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 37.5.sp // 30.sp * 1.25
    ),
    titleSmall = TextStyle(
        fontSize = 20.sp, // 16.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 35.sp // 28.sp * 1.25
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp, // 18.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 31.25.sp // 25.sp * 1.25
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp, // 16.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp // 24.sp * 1.25
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp, // 14.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 27.5.sp // 22.sp * 1.25
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp, // 16.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 27.5.sp // 22.sp * 1.25
    ),
    labelMedium = TextStyle(
        fontSize = 10.sp, // 14.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 25.sp // 20.sp * 1.25
    ),
    labelSmall = TextStyle(
        fontSize = 8.sp, // 12.sp * 1.25
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.5.sp // 18.sp * 1.25
    )

)


val CompactSmallTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 43.sp
    ),
    displayMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    displaySmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    titleLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp
    ),
    titleSmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp
    ),
    bodySmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 8.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp
    )
)


val MediumTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 34.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 50.sp
    ),
    displayMedium = TextStyle(
        fontSize = 30.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp
    ),
    displaySmall = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    titleSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 27.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 25.sp
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    labelMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    ),
    labelSmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    )
)


val ExpandedTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 40.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 56.sp
    ),
    displayMedium = TextStyle(
        fontSize = 36.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontSize = 32.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 34.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 50.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 32.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 30.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp
    ),
    titleLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp
    ),
    titleMedium = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    titleSmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 31.sp
    ),
    bodySmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    labelLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    labelMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp
    ),
    labelSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )
)