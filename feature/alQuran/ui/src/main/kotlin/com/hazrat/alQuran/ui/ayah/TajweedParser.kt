package com.hazrat.alQuran.ui.ayah

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Tajweed color coding parser for alquran.cloud API format.
 *
 * Parses bracket-based tajweed markup:
 *   [h:1[ٱ]   -> Hamzat ul Wasl (gray)
 *   [f:16[نف] -> Ikhfa (purple)
 *   [n[ـٰ]    -> Madda Normal (blue)
 *
 * Color scheme from: https://alquran.cloud/tajweed-guide
 * Colors brightened for dark mode visibility.
 *
 * @author hazratummar
 */

/**
 * Official alquran.cloud tajweed color scheme.
 * Reference: https://alquran.cloud/tajweed-guide
 *
 * Original (light-mode) colors listed in comments.
 * Active colors are brightened for dark (#272727) backgrounds.
 */
object TajweedColors {

    /**
     * Maps single-letter identifiers from alquran.cloud API to colors.
     *
     * Format: [identifier:optional_number[text]
     * The identifier is always a single lowercase letter.
     */
    private val idToColor = mapOf(
        // Gray: Silent / non-pronounced
        "h" to Color(0xFFAAAAAA),   // ham_wasl  - Hamzat ul Wasl        (original #AAAAAA)
        "s" to Color(0xFFAAAAAA),   // slnt      - Silent                (original #AAAAAA)
        "l" to Color(0xFFAAAAAA),   // slnt      - Lam Shamsiyyah        (original #AAAAAA)

        // Blue: Madd (Prolongation)
        "n" to Color(0xFF537FFF),   // madda_normal      - 2 Vowels      (original #537FFF)
        "p" to Color(0xFF4050FF),   // madda_permissible - 2,4,6 Vowels  (original #4050FF)
        "m" to Color(0xFF5A7FFF),   // madda_necessary   - 6 Vowels      (original #000EBC → brightened)
        "o" to Color(0xFF4466DD),   // madda_obligatory  - 4-5 Vowels    (original #2144C1 → brightened)

        // Red: Qalqalah (Echo)
        "q" to Color(0xFFFF4444),   // qlq - Qalqalah                   (original #DD0008 → brightened)

        // Purple: Ikhfa (Concealment)
        "f" to Color(0xFFCC44FF),   // ikhf      - Ikhfa                (original #9400A8 → brightened)
        "c" to Color(0xFFEE55DD),   // ikhf_shfw - Ikhfa Shafawi        (original #D500B7 → brightened)

        // Green/Teal: Idgham (Merging)
        "a" to Color(0xFF22CC99),   // idgh_ghn  - Idgham w/ Ghunnah    (original #169777 → brightened)
        "u" to Color(0xFF22BB44),   // idgh_w_ghn - Idgham w/o Ghunnah  (original #169200 → brightened)
        "w" to Color(0xFF77DD22),   // idghm_shfw - Idgham Shafawi      (original #58B800 → brightened)
        "d" to Color(0xFFBBBBBB),   // idgh_mus  - Idgham Mutajanisayn  (original #A1A1A1 → brightened)

        // Cyan: Iqlab (Conversion)
        "i" to Color(0xFF44DDFF),   // iqlb - Iqlab                     (original #26BFFD → brightened)

        // Orange: Ghunnah (Nasalization)
        "g" to Color(0xFFFF9933),   // ghn - Ghunnah: 2 Vowels          (original #FF7E1E → brightened)
    )

    fun getColor(identifier: String): Color? = idToColor[identifier]
}

/**
 * Regex for alquran.cloud tajweed bracket format.
 *
 * Matches patterns like:
 *   [h:1[ٱ]       -> identifier="h", content="ٱ"
 *   [f:16[نف]     -> identifier="f", content="نف"
 *   [n[ـٰ]        -> identifier="n", content="ـٰ"
 *   [g[مّ]        -> identifier="g", content="مّ"
 *
 * Pattern: \[ (letter) (?::digits)? \[ (content) \]
 */
private val TAJWEED_BRACKET_REGEX =
    """\[([a-z])(?::?\d*)\[([^\]]*)\]""".toRegex()

/**
 * Strips Tatweel (U+0640), Small High Rounded Zero (U+06DF), and converts sukun for Uthmani scripts.
 * Works with uthamnic_new.otf font.
 */
fun String.cleanUthmanic(): String {
    return this
        .replace("\u0640", "")       // Remove Tatweel (kashida U+0640)
        .replace("\u25CC", "")       // Remove Dotted Circle (U+25CC)
        .replace("\u06DF", "")       // Remove Small High Rounded Zero
        .replace('\u0652', '\u06e1') // Convert standard round sukun to comma sukun
        .replace("\uFEFF", "")       // Remove BOM
}

/**
 * Transforms Madani Uthmani Rasm into authentic IndoPak orthography:
 * 1. Converts Alef Wasla (U+0671 ٱ) -> Plain Alef (U+0627 ا)
 * 2. Converts Medina Head-of-Khah Sukun (U+06E1 ۡ) -> Round Sukun/Jazm (U+0652 ْ)
 * 3. Converts Dagger Alef (U+0670 ٰ) to Full Alef (U+0627 ا) for IndoPak Rasm words:
 *    - صِرَٰطَ -> صِرَاطَ
 *    - مَٰلِكِ -> مَالِكِ
 *    - كِتَٰبُ -> كِتَابُ
 *    - عَالَمِينَ -> عَالَمِينَ
 * 4. Removes small floating symbols (U+06E5 ۥ, U+06E6 ۦ, U+06DF ۟, U+06E0 ۠, U+FEFF, U+0640)
 */
fun String.cleanIndoPak(): String {
    var text = this
        .replace('\u0671', '\u0627') // Alef Wasla (ٱ) -> Plain Alef (ا)
        .replace('\u06E1', '\u0652') // Medina sukun (ۡ) -> Round Sukun (ْ)
        .replace("\u0640", "")       // Tatweel
        .replace("\u06DF", "")       // Small High Rounded Zero
        .replace("\u06E0", "")       // Small High Upright Rectangular Zero
        .replace("\u06E5", "")       // Small High Waw
        .replace("\u06E6", "")       // Small High Yeh
        .replace("\uFEFF", "")       // BOM

    // Protect words that keep Dagger Alef in IndoPak orthography
    val protectedRahman = "__RAHMAN__"
    val protectedAllah = "__ALLAH__"
    val protectedIlah = "__ILAH__"

    text = text
        .replace("الرَّحْمَٰنِ", protectedRahman)
        .replace("الرَّحْمٰنِ", protectedRahman)
        .replace("اللَّٰهِ", protectedAllah)
        .replace("اللّٰهِ", protectedAllah)
        .replace("إِلَٰهَ", protectedIlah)
        .replace("إِلٰهَ", protectedIlah)

    // Convert Dagger Alef to Full Alef for IndoPak script (e.g. صِرَاطَ, مَالِكِ, الْكِتَابُ, عَالَمِينَ)
    text = text.replace("""([ابتثجحخدذرزسشصضطظعغفقكلمنهوي])\u064E?\u0670""".toRegex(), "$1َا")

    // Restore protected words with authentic IndoPak Dagger Alef
    text = text
        .replace(protectedRahman, "الرَّحْمٰنِ")
        .replace(protectedAllah, "اللّٰهِ")
        .replace(protectedIlah, "إِلٰهَ")

    return text
}

/**
 * Formats Quranic text for specific font orthographies.
 */
fun String.formatForFont(fontType: String): String {
    return if (fontType == "INDOPAK") this.cleanIndoPak() else this.cleanUthmanic()
}

/**
 * Strips all tajweed bracket markup [x:12[text]] and returns clean text.
 */
fun String.stripTajweedMarkup(): String {
    return TAJWEED_BRACKET_REGEX.replace(this) { match ->
        match.groupValues[2]
    }
}

/**
 * Parses alquran.cloud tajweed bracket markup into a Compose AnnotatedString
 * with colored spans for each tajweed rule.
 *
 * Input:  "بِسْمِ [h:1[ٱ]للَّهِ"
 * Output: AnnotatedString with gray color on "ٱ" or "ا" based on fontType.
 * If enableTajweedColor is false, returns plain uncolored text formatted for fontType.
 */
fun parseTajweedHtml(
    input: String,
    defaultColor: Color,
    fontType: String = "SCHEHERAZADE",
    enableTajweedColor: Boolean = true
): AnnotatedString {
    if (input.isBlank()) return AnnotatedString("")

    if (!enableTajweedColor) {
        val plainCleanText = input.stripTajweedMarkup().formatForFont(fontType)
        return AnnotatedString(plainCleanText)
    }

    data class Segment(val text: String, val color: Color)

    val segments = mutableListOf<Segment>()
    var lastIndex = 0

    TAJWEED_BRACKET_REGEX.findAll(input).forEach { match ->
        // Plain text before this tajweed bracket
        if (match.range.first > lastIndex) {
            val plainText = input.substring(lastIndex, match.range.first).formatForFont(fontType)
            if (plainText.isNotEmpty()) {
                segments.add(Segment(plainText, defaultColor))
            }
        }

        // Tajweed-colored text
        val identifier = match.groupValues[1]
        val content = match.groupValues[2].formatForFont(fontType)
        val color = TajweedColors.getColor(identifier) ?: defaultColor

        if (content.isNotEmpty()) {
            segments.add(Segment(content, color))
        }

        lastIndex = match.range.last + 1
    }

    // Remaining plain text after the last tajweed bracket
    if (lastIndex < input.length) {
        val remaining = input.substring(lastIndex).formatForFont(fontType)
        if (remaining.isNotEmpty()) {
            segments.add(Segment(remaining, defaultColor))
        }
    }

    // Build AnnotatedString with colored spans (color-only, preserves ligatures)
    return buildAnnotatedString {
        segments.forEach { segment ->
            withStyle(SpanStyle(color = segment.color)) {
                append(segment.text)
            }
        }
    }
}
