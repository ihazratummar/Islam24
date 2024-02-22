package com.hazrat.islam24.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hazrat.islam24.R


val Poppins = FontFamily(
    fonts = listOf(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_semibold, FontWeight.SemiBold)
    )
)

val Hidayat = FontFamily(
    fonts = listOf(
        Font(R.font.hidayatullah)
    )
)


// Set of Material typography styles to start with
val CompactTypography = Typography(
    displaySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 32.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 13.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp,
    ),
)
val CompactMediumTypography  = Typography(
    displaySmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 30.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 17.sp,
    ),
)

val CompactSmallTypography  = Typography(
    displaySmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 8.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 7.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 13.sp,
    ),
)

val MediumTypography  = Typography(
    displaySmall = TextStyle(
        fontSize = 30.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 38.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 54.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 20.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 27.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 19.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 25.sp,
    ),
)


val ExpandedTypography   = Typography(
    displaySmall = TextStyle(
        fontSize = 34.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 42.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 58.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 31.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 26.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 23.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        lineHeight = 29.sp,
    ),
)