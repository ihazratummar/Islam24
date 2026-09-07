package com.hazrat.utils

import java.util.Locale

/**
 * Utility helper for formatting numbers and string digits into localized language representation
 * (e.g., Bengali digits ০, ১, ২... for 'bn').
 * @author Hazrat Ummar Shaikh
 */
object NumberUtils {

    private val BENGALI_DIGITS = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    /**
     * Converts ASCII digits (0-9) in [input] to localized digits for the active or given [locale].
     */
    fun formatDigits(input: String, locale: Locale = Locale.getDefault()): String {
        if (locale.language.equals("bn", ignoreCase = true)) {
            val sb = StringBuilder(input.length)
            for (ch in input) {
                if (ch in '0'..'9') {
                    sb.append(BENGALI_DIGITS[ch - '0'])
                } else {
                    sb.append(ch)
                }
            }
            return sb.toString()
        }
        return input
    }
}

/**
 * Formats this integer's digits into localized language representation (e.g. Bengali digits for 'bn').
 */
fun Int.toLocalizedDigits(locale: Locale = Locale.getDefault()): String {
    return NumberUtils.formatDigits(this.toString(), locale)
}

/**
 * Formats any ASCII digits within this string into localized language representation.
 */
fun String.formatLocalizedDigits(locale: Locale = Locale.getDefault()): String {
    return NumberUtils.formatDigits(this, locale)
}

fun String.toLocalizedDigits(locale: Locale = Locale.getDefault()): String {
    return NumberUtils.formatDigits(this, locale)
}
