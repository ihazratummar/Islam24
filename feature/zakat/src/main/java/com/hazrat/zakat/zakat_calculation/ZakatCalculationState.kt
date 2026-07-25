package com.hazrat.zakat.zakat_calculation

enum class ZakatTab {
    ASSETS,
    LIABILITIES
}

data class ZakatCalculationState(
    val silverPricePerGram: String = "",
    val cashAndBank: String = "",
    val gold: String = "",
    val silver: String = "",
    val investments: String = "",
    val businessAssets: String = "",
    val rentalProperty: String = "",
    val debtsAndLoans: String = "",
    val dueExpenses: String = "",
    val activeTab: ZakatTab = ZakatTab.ASSETS,
    val dueDate: String = "",
    val isReminderEnabled: Boolean = false
) {
    val isSilverPriceValid: Boolean
        get() = (silverPricePerGram.toDoubleOrNull() ?: 0.0) > 0.0

    val silverPriceVal: Double
        get() = silverPricePerGram.toDoubleOrNull() ?: 0.0

    val silverNisabThreshold: Double
        get() = 612.36 * silverPriceVal

    val cashAndBankVal: Double
        get() = cashAndBank.toDoubleOrNull() ?: 0.0

    val goldVal: Double
        get() = gold.toDoubleOrNull() ?: 0.0

    val silverVal: Double
        get() = silver.toDoubleOrNull() ?: 0.0

    val investmentsVal: Double
        get() = investments.toDoubleOrNull() ?: 0.0

    val businessAssetsVal: Double
        get() = businessAssets.toDoubleOrNull() ?: 0.0

    val rentalPropertyVal: Double
        get() = rentalProperty.toDoubleOrNull() ?: 0.0

    val debtsAndLoansVal: Double
        get() = debtsAndLoans.toDoubleOrNull() ?: 0.0

    val dueExpensesVal: Double
        get() = dueExpenses.toDoubleOrNull() ?: 0.0

    val totalAssets: Double
        get() = cashAndBankVal + goldVal + silverVal + investmentsVal + businessAssetsVal + rentalPropertyVal

    val totalLiabilities: Double
        get() = debtsAndLoansVal + dueExpensesVal

    val netAssets: Double
        get() = (totalAssets - totalLiabilities).coerceAtLeast(0.0)

    val isEligible: Boolean
        get() = isSilverPriceValid && netAssets >= silverNisabThreshold && silverNisabThreshold > 0.0

    val zakatPayable: Double
        get() = if (isEligible) netAssets * 0.025 else 0.0
}
