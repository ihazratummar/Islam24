package com.hazrat.islam24.core.presentation.zakat


/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface ZakatEvent {

    data class OnPriceChange(val price: String) : ZakatEvent
    data class SubmitNisab(val price: String) : ZakatEvent

    data object OpenMoneyDialog : ZakatEvent
    data object OpenBankCashDialog : ZakatEvent
    data object OpenGoldDialog : ZakatEvent
    data object OpenSilverDialog : ZakatEvent
    data object OpenTradeAmountDialog : ZakatEvent
    data object OpenMonthCostDialog : ZakatEvent
    data object OpenDebtDialog : ZakatEvent


    data class OnMoneyValueChange(val price : String): ZakatEvent
    data class OnMoneySubmit(val price : String): ZakatEvent

    data class OnGoldPriceChange(val price : String): ZakatEvent
    data class OnSubmitGold(val price : String): ZakatEvent

    data class OnSilverPriceChange(val price : String): ZakatEvent
    data class OnSubmitSilver(val price : String): ZakatEvent

    data class OnTradeAmountPriceChange(val price : String): ZakatEvent
    data class OnSubmitTradeAmount(val price : String): ZakatEvent

    data class OnMonthCostPriceChange(val price : String): ZakatEvent
    data class OnSubmitMonthCost(val price : String): ZakatEvent

    data class OnDebtPriceChange(val price : String): ZakatEvent
    data class OnSubmitDebt(val price : String): ZakatEvent

    data object ResetAllState : ZakatEvent

    data object SaveZakat: ZakatEvent

    data object ToggleSortType: ZakatEvent

    data class DeleteZakat(val zakatId: Int): ZakatEvent
}