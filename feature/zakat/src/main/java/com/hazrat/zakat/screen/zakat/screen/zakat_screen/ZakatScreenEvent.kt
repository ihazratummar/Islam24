package com.hazrat.zakat.screen.zakat.screen.zakat_screen

import com.hazrat.zakat.screen.zakat.ZakatEvent

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-06-2025
 */

sealed interface ZakatScreenEvent {

    data object ToggleZakatDetailsPopUp : ZakatScreenEvent

    data class DeleteZakat(val zakatId: String): ZakatScreenEvent

    data object ToggleSortType: ZakatScreenEvent

}