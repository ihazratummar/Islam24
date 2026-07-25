package com.hazrat.zakat.zakat_calculation

sealed interface ZakatCalculationEvent {
    data class UpdateSilverPrice(val price: String) : ZakatCalculationEvent
    data class UpdateCashAndBank(val amount: String) : ZakatCalculationEvent
    data class UpdateGold(val amount: String) : ZakatCalculationEvent
    data class UpdateSilver(val amount: String) : ZakatCalculationEvent
    data class UpdateInvestments(val amount: String) : ZakatCalculationEvent
    data class UpdateBusinessAssets(val amount: String) : ZakatCalculationEvent
    data class UpdateRentalProperty(val amount: String) : ZakatCalculationEvent
    data class UpdateDebtsAndLoans(val amount: String) : ZakatCalculationEvent
    data class UpdateDueExpenses(val amount: String) : ZakatCalculationEvent
    data class SelectTab(val tab: ZakatTab) : ZakatCalculationEvent
    data class UpdateDueDate(val date: String) : ZakatCalculationEvent
    data class ToggleReminder(val enabled: Boolean) : ZakatCalculationEvent
    data object SaveZakatRecord : ZakatCalculationEvent
}
