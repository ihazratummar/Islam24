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
 * Strips U+06DF (Small High Rounded Zero) and converts sukun to comma-sukun.
 */
private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")       // Remove Small High Rounded Zero
        .replace('\u0652', '\u06e1') // Convert standard round sukun to comma sukun
        .replace("\uFEFF", "")       // Remove BOM
}

/**
 * Parses alquran.cloud tajweed bracket markup into a Compose AnnotatedString
 * with colored spans for each tajweed rule.
 *
 * Input:  "بِسْمِ [h:1[ٱ]للَّهِ"
 * Output: AnnotatedString with gray color on "ٱ"
 */
fun parseTajweedHtml(
    input: String,
    defaultColor: Color
): AnnotatedString {
    if (input.isBlank()) return AnnotatedString("")

    data class Segment(val text: String, val color: Color)

    val segments = mutableListOf<Segment>()
    var lastIndex = 0

    TAJWEED_BRACKET_REGEX.findAll(input).forEach { match ->
        // Plain text before this tajweed bracket
        if (match.range.first > lastIndex) {
            val plainText = input.substring(lastIndex, match.range.first).cleanUthmanic()
            if (plainText.isNotEmpty()) {
                segments.add(Segment(plainText, defaultColor))
            }
        }

        // Tajweed-colored text
        val identifier = match.groupValues[1]
        val content = match.groupValues[2].cleanUthmanic()
        val color = TajweedColors.getColor(identifier) ?: defaultColor

        if (content.isNotEmpty()) {
            segments.add(Segment(content, color))
        }

        lastIndex = match.range.last + 1
    }

    // Remaining plain text after the last tajweed bracket
    if (lastIndex < input.length) {
        val remaining = input.substring(lastIndex).cleanUthmanic()
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
