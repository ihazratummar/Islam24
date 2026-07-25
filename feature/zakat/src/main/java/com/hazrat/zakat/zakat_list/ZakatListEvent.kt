package com.hazrat.zakat.zakat_list

sealed interface ZakatListEvent {
    data class DeleteZakat(val id: String) : ZakatListEvent
    data object ToggleZakatDetailsPopUp : ZakatListEvent
}
