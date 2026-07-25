package com.hazrat.zakat.zakat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.notification.ZakatAlarmScheduler
import com.hazrat.zakat.domain.repository.ZakatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ZakatListViewModel(
    private val zakatRepository: ZakatRepository,
    private val zakatAlarmScheduler: ZakatAlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZakatListState())
    val uiState = _uiState.asStateFlow()

    init {
        getZakatList()
    }

    private fun getZakatList() {
        viewModelScope.launch {
            zakatRepository.getZakatList().collectLatest { list ->
                _uiState.update { it.copy(zakatEntityList = list) }
            }
        }
    }

    fun getZakatDetails(id: String) {
        viewModelScope.launch {
            zakatRepository.getZakatDetails(id).collectLatest { entity ->
                _uiState.update {
                    it.copy(
                        totalAsset = entity.totalAsset,
                        zakatAmount = entity.zakatAmount,
                        gold = entity.gold.toString(),
                        silver = entity.silver.toString(),
                        money = entity.money.toString(),
                        debt = entity.debt.toString(),
                        tradeAmount = entity.tradeAmount.toString(),
                        monthCost = entity.monthCost.toString(),
                        date = entity.date.toString()
                    )
                }
            }
        }
    }

    fun onEvent(event: ZakatListEvent) {
        when (event) {
            is ZakatListEvent.DeleteZakat -> {
                deleteZakat(event.id)
            }
            ZakatListEvent.ToggleZakatDetailsPopUp -> {
                _uiState.update { it.copy(isZakatDetailsOpen = !it.isZakatDetailsOpen) }
            }
        }
    }

    private fun deleteZakat(id: String) {
        viewModelScope.launch {
            zakatAlarmScheduler.cancelZakatReminder(id)
            zakatRepository.deleteZakat(id)
        }
    }
}
