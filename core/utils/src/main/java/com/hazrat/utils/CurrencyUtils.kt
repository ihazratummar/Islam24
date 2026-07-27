package com.hazrat.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Helper function to retrieve local currency symbol, with clean fallback to ₹ (Rupees).
 */
fun getDeviceCurrencySymbol(): String {
    return try {
        val locale = Locale.getDefault()
        val currency = Currency.getInstance(locale)
        val symbol = currency.getSymbol(locale)
        if (symbol == "INR" || locale.country == "IN") "₹"
        else if (symbol == "PKR" || locale.country == "PK") "Rs"
        else if (symbol == "$" && locale.country != "US") "₹"
        else symbol
    } catch (_: Exception) {
        "₹"
    }
}

/**
 * Formats a numeric value with comma separators and currency symbol.
 * Example: 4357.0 -> "₹4,357"
 */
fun formatCurrency(amount: Double): String {
    val symbol = getDeviceCurrencySymbol()
    return try {
        val format = NumberFormat.getNumberInstance(Locale.US)
        format.maximumFractionDigits = 0
        "$symbol${format.format(amount.toInt())}"
    } catch (_: Exception) {
        "$symbol${amount.toInt()}"
    }
}
