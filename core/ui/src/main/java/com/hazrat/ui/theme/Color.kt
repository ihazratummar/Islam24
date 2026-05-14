package com.hazrat.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Modern Spiritual Palette (Premium)
val DeepPineGreen = Color(0xFF0F3D3E)
val EmeraldGreen = Color(0xFF1E726D)
val SpiritualGold = Color(0xFFD4AF37)
val SoftCream = Color(0xFFFDFBF7)


//// New Colors ///


val Primary10 = Color(0xFF021416)
val Primary20 = Color(0xFF04282C)
val Primary30 = Color(0xFF074145)
val Primary40 = Color(0xFF0A5A5E)
val Primary50 = Color(0xFF0D7377)
val Primary60 = Color(0xFF149285)
val Primary70 = Color(0xFF26A490)
val Primary80 = Color(0xFF4DB5A9)
val Primary90 = Color(0xFFB3E0D9)
val Primary95 = Color(0xFFE6F5F3)
val Primary99 = Color(0xFFF5FBFA)

// ========== SECONDARY (Gold) ==========
val Secondary10 = Color(0xFF594317)
val Secondary20 = Color(0xFF785E1F)
val Secondary30 = Color(0xFF967927)
val Secondary40 = Color(0xFFB5942F)
val Secondary50 = Color(0xFFD4AF37)
val Secondary60 = Color(0xFFEDC848)
val Secondary70 = Color(0xFFF1D470)
val Secondary80 = Color(0xFFF5E098)
val Secondary90 = Color(0xFFF9ECC0)
val Secondary95 = Color(0xFFFDF8E8)
val Secondary99 = Color(0xFFFFFDF7)

// ========== TERTIARY (Terracotta — warm accent) ==========
val Tertiary10 = Color(0xFF410E0B)
val Tertiary20 = Color(0xFF612C22)
val Tertiary30 = Color(0xFF7D3B2E)
val Tertiary40 = Color(0xFF9A4E3F)
val Tertiary50 = Color(0xFFB85C3C)
val Tertiary60 = Color(0xFFC4784A)
val Tertiary70 = Color(0xFFD48F60)
val Tertiary80 = Color(0xFFE2A87A)
val Tertiary90 = Color(0xFFF5CFB0)
val Tertiary95 = Color(0xFFFFF0E4)
val Tertiary99 = Color(0xFFFFFBF8)

// ========== NEUTRAL (Surface tones) ==========
val Neutral0 = Color(0xFF000000)
val Neutral4 = Color(0xFF0A1618)
val Neutral6 = Color(0xFF0F1B1E)
val Neutral10 = Color(0xFF151E20)
val Neutral12 = Color(0xFF1A2E32)
val Neutral17 = Color(0xFF223A3E)
val Neutral20 = Color(0xFF2A4548)
val Neutral22 = Color(0xFF2F4D50)
val Neutral30 = Color(0xFF456467)
val Neutral40 = Color(0xFF5D7D80)
val Neutral50 = Color(0xFF789799)
val Neutral60 = Color(0xFF93B1B3)
val Neutral70 = Color(0xFFAEC9CB)
val Neutral80 = Color(0xFFC9E1E3)
val Neutral87 = Color(0xFFDFF0F1)
val Neutral90 = Color(0xFFE8F5F6)
val Neutral92 = Color(0xFFEEF5F5)
val Neutral94 = Color(0xFFF2F8F8)
val Neutral95 = Color(0xFFF5FAFA)
val Neutral96 = Color(0xFFF8FAF9)
val Neutral98 = Color(0xFFFAFDFC)
val Neutral99 = Color(0xFFFFFFFF)

// ========== NEUTRAL VARIANT (Muted surface tones) ==========
val NeutralVariant30 = Color(0xFF3B4D4F)
val NeutralVariant50 = Color(0xFF6B8284)
val NeutralVariant60 = Color(0xFF849B9D)
val NeutralVariant80 = Color(0xFFB3D0D3)
val NeutralVariant90 = Color(0xFFD6EBED)

// ========== ERROR ==========
val Error10 = Color(0xFF410E0B)
val Error20 = Color(0xFF601410)
val Error30 = Color(0xFF8C1D18)
val Error40 = Color(0xFFB3261E)
val Error50 = Color(0xFFD9382E)
val Error60 = Color(0xFFE46962)
val Error70 = Color(0xFFEC928E)
val Error80 = Color(0xFFF2B8B5)
val Error90 = Color(0xFFF9DEDC)
val Error95 = Color(0xFFFCECEB)
val Error99 = Color(0xFFFFF9F9)


// ---------- Shared Prayer Gradients ----------

val FajrGradient = listOf(
    Color(0xFF5B4FD7),
    Color(0xFF7B5CF1)
)

val SunriseGradient = listOf(
    Color(0xFFF5A623),
    Color(0xFFF8B13B)
)

val DhuhrGradient = listOf(
    Color(0xFF2BB7E8),
    Color(0xFF3CC8F5)
)

val AsrGradient = listOf(
    Color(0xFFF39C12),
    Color(0xFFF7A623)
)

val MaghribGradient = listOf(
    Color(0xFFFF6B57),
    Color(0xFFFF8A4D)
)

val IshaGradient = listOf(
    Color(0xFF4B56B7),
    Color(0xFF2F3E73)
)

// ---------- Buttons  ----------
val ButtonColorDark = Color(0xFF10b981)

// ---------- App Gradients ----------

data class CustomColors(
    val prayerCard: List<Color>,

    val fajrGradient: List<Color>,
    val sunriseGradient: List<Color>,
    val dhuhrGradient: List<Color>,
    val asrGradient: List<Color>,
    val maghribGradient: List<Color>,
    val ishaGradient: List<Color>,

    val emerald: Color,
    val buttonColor : Color

)

val LocalCustomColors = compositionLocalOf {
    CustomColors(
        prayerCard = emptyList(),

        fajrGradient = emptyList(),
        sunriseGradient = emptyList(),
        dhuhrGradient = emptyList(),
        asrGradient = emptyList(),
        maghribGradient = emptyList(),
        ishaGradient = emptyList(),
        emerald = Color(0xFF95f0c6),
        buttonColor = ButtonColorDark
    )
}


// ---------- Dark Theme ----------

val DarkCustomColors = CustomColors(

    prayerCard = listOf(
        Color(0xFF0D5153),
        Color(0xFF0F5A59),
        Color(0xFF1B6058),
        Color(0xFF27665B)
    ),

    fajrGradient = FajrGradient,
    sunriseGradient = SunriseGradient,
    dhuhrGradient = DhuhrGradient,
    asrGradient = AsrGradient,
    maghribGradient = MaghribGradient,
    ishaGradient = IshaGradient,
    emerald = Color(0xFF95f0c6),
    buttonColor = ButtonColorDark
)


// ---------- Light Theme ----------

val LightCustomColors = CustomColors(

    prayerCard = listOf(
        Primary50,
        Primary40,
        Primary30
    ),

    fajrGradient = FajrGradient,
    sunriseGradient = SunriseGradient,
    dhuhrGradient = DhuhrGradient,
    asrGradient = AsrGradient,
    maghribGradient = MaghribGradient,
    ishaGradient = IshaGradient,
    emerald = Color(0xFFA7F5D0),
    buttonColor = ButtonColorDark
)