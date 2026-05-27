package com.hazrat.alQuran.ui.ayah

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Tajweed color coding parser.
 * Parses quran.com API v4 `text_uthmani_tajweed` HTML markup
 * and converts it to Jetpack Compose AnnotatedString with colored spans.
 *
 * @author hazratummar
 */

/**
 * Standard Tajweed color scheme matching quran.com / Muslim Pro.
 */
object TajweedColors {
    // ============================================================
    // Exact quran.com DARK MODE colors
    // Extracted from: quran.com/_next/static/css/53aa552c789492f1.css
    // ============================================================

    // Gray: Edgham category (silent / non-pronounced)  -> quran.com dark-edgham: #999
    val hamzatWasl = Color(0xFF999999)
    val laamShamsiyah = Color(0xFF999999)
    val silent = Color(0xFF999999)

    // Green: Ekhfa category (concealment)              -> quran.com dark-ekhfa: #26b55d
    val ikhfa = Color(0xFF26B55D)
    val ikhfaShafawi = Color(0xFF26B55D)

    // Gray: Edgham category (merging)                  -> quran.com dark-edgham: #999
    val idghamGhunnah = Color(0xFF999999)
    val idghamNoGhunnah = Color(0xFF999999)
    val idghamShafawi = Color(0xFF999999)
    val idghamMutajanisayn = Color(0xFF999999)
    val idghamMutaqaribayn = Color(0xFF999999)

    // Orange: Ghunnah                                  -> quran.com dark-mad-2-4-6: #ff8e3b
    val ghunnah = Color(0xFFFF8E3B)

    // Cyan: Qalqalah                                   -> quran.com dark-qalqala: #00deff
    val iqlab = Color(0xFF3C84D5) // tafkhim blue
    val qalqalah = Color(0xFF00DEFF)

    // Madd gradient (from quran.com dark mode):
    val maddNormal = Color(0xFFFFC1E0)       // dark-mad-2:     #ffc1e0 (pink)
    val maddPermissible = Color(0xFFFF8E3B)  // dark-mad-2-4-6: #ff8e3b (orange)
    val maddObligatory = Color(0xFFFF5E8E)   // dark-mad-4-5:   #ff5e8e (rose)
    val maddNecessary = Color(0xFFE30000)     // dark-mad-6:     #e30000 (red)

    /**
     * Maps CSS class names from quran.com API v4 to colors.
     * These are the EXACT 17 class names used in our database.
     */
    private val classToColor = mapOf(
        // Gray: Silent / Non-pronounced
        "ham_wasl" to hamzatWasl,                       // 13252 occurrences
        "laam_shamsiyah" to laamShamsiyah,               // 2608 occurrences
        "slnt" to silent,                                // 4159 occurrences

        // Orange: Ghunnah (Nasalization)
        "ghunnah" to ghunnah,                            // 4923 occurrences

        // Purple: Ikhfa (Concealment)
        "ikhafa" to ikhfa,                               // 5291 occurrences
        "ikhafa_shafawi" to ikhfaShafawi,                // 496 occurrences

        // Green/Teal: Idgham (Merging)
        "idgham_ghunnah" to idghamGhunnah,               // 3933 occurrences
        "idgham_wo_ghunnah" to idghamNoGhunnah,           // 1036 occurrences
        "idgham_shafawi" to idghamShafawi,               // 832 occurrences
        "idgham_mutajanisayn" to idghamMutajanisayn,     // 58 occurrences
        "idgham_mutaqaribayn" to idghamMutaqaribayn,     // 13 occurrences

        // Light Blue: Iqlab (Conversion)
        "iqlab" to iqlab,                                // 562 occurrences

        // Red: Qalqalah (Echo)
        "qalaqah" to qalqalah,                           // 3834 occurrences

        // Blue gradient: Madd (Prolongation)
        "madda_normal" to maddNormal,                    // 9028 occurrences
        "madda_permissible" to maddPermissible,          // 4543 occurrences
        "madda_obligatory" to maddObligatory,            // 5166 occurrences
        "madda_necessary" to maddNecessary,              // 143 occurrences
    )

    fun getColor(className: String): Color? = classToColor[className]
}

/**
 * Regex patterns for parsing quran.com tajweed HTML markup.
 */
private val TAJWEED_TAG_REGEX =
    """<tajweed\s+class=(["\']?)([^"'\s>]+)\1[^>]*>(.*?)</tajweed>""".toRegex()

private val SPAN_END_TAG_REGEX =
    """<span\s+class=(["\']?)end\1[^>]*>(.*?)</span>""".toRegex()

private val ANY_HTML_TAG_REGEX = """<[^>]+>""".toRegex()

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
 * Parses quran.com tajweed HTML markup into a Compose AnnotatedString
 * with colored spans for each tajweed rule.
 *
 * Input format: "بِسْمِ <tajweed class=ham_wasl>ٱ</tajweed>للَّهِ"
 * Output: AnnotatedString with gray color on "ٱ"
 *
 * Also keeps <span class=end>٢</span> verse-end markers as plain text.
 */
fun parseTajweedHtml(
    input: String,
    defaultColor: Color
): AnnotatedString {
    if (input.isBlank()) return AnnotatedString("")

    // Step 1: Extract verse-end markers and wrap with decorative ۝ (U+06DD)
    var processed = SPAN_END_TAG_REGEX.replace(input) { match ->
        val verseNum = match.groupValues[2]
        " \u06DD${verseNum} " // U+06DD wraps the number in a decorative ayah marker
    }

    // Step 2: Parse tajweed tags into segments
    data class Segment(val text: String, val color: Color)

    val segments = mutableListOf<Segment>()
    var lastIndex = 0

    TAJWEED_TAG_REGEX.findAll(processed).forEach { match ->
        // Plain text before this tajweed tag
        if (match.range.first > lastIndex) {
            val plainText = processed.substring(lastIndex, match.range.first)
            val cleaned = ANY_HTML_TAG_REGEX.replace(plainText, "").cleanUthmanic()
            if (cleaned.isNotEmpty()) {
                segments.add(Segment(cleaned, defaultColor))
            }
        }

        // Tajweed-colored text
        val className = match.groupValues[2]
        val content = match.groupValues[3].cleanUthmanic()
        val color = TajweedColors.getColor(className) ?: defaultColor

        if (content.isNotEmpty()) {
            segments.add(Segment(content, color))
        }

        lastIndex = match.range.last + 1
    }

    // Remaining plain text after the last tajweed tag
    if (lastIndex < processed.length) {
        val remaining = processed.substring(lastIndex)
        val cleaned = ANY_HTML_TAG_REGEX.replace(remaining, "").cleanUthmanic()
        if (cleaned.isNotEmpty()) {
            segments.add(Segment(cleaned, defaultColor))
        }
    }

    // Step 3: Build AnnotatedString with colored spans
    return buildAnnotatedString {
        segments.forEach { segment ->
            withStyle(SpanStyle(color = segment.color)) {
                append(segment.text)
            }
        }
    }
}
