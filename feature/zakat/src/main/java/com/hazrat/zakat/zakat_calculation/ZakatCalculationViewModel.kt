package com.hazrat.zakat.zakat_calculation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.database.entity.zakat.ZakatEntity
import com.hazrat.notification.ZakatAlarmScheduler
import com.hazrat.zakat.domain.repository.ZakatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ZakatCalculationViewModel(
    private val zakatRepository: ZakatRepository,
    private val zakatAlarmScheduler: ZakatAlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZakatCalculationState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ZakatCalculationEvent) {
        when (event) {
            is ZakatCalculationEvent.UpdateSilverPrice -> {
                _uiState.update { it.copy(silverPricePerGram = event.price) }
            }
            is ZakatCalculationEvent.UpdateCashAndBank -> {
                _uiState.update { it.copy(cashAndBank = event.amount) }
            }
            is ZakatCalculationEvent.UpdateGold -> {
                _uiState.update { it.copy(gold = event.amount) }
            }
            is ZakatCalculationEvent.UpdateSilver -> {
                _uiState.update { it.copy(silver = event.amount) }
            }
            is ZakatCalculationEvent.UpdateInvestments -> {
                _uiState.update { it.copy(investments = event.amount) }
            }
            is ZakatCalculationEvent.UpdateBusinessAssets -> {
                _uiState.update { it.copy(businessAssets = event.amount) }
            }
            is ZakatCalculationEvent.UpdateRentalProperty -> {
                _uiState.update { it.copy(rentalProperty = event.amount) }
            }
            is ZakatCalculationEvent.UpdateDebtsAndLoans -> {
                _uiState.update { it.copy(debtsAndLoans = event.amount) }
            }
            is ZakatCalculationEvent.UpdateDueExpenses -> {
                _uiState.update { it.copy(dueExpenses = event.amount) }
            }
            is ZakatCalculationEvent.SelectTab -> {
                _uiState.update { it.copy(activeTab = event.tab) }
            }
            is ZakatCalculationEvent.UpdateDueDate -> {
                _uiState.update { it.copy(dueDate = event.date) }
            }
            is ZakatCalculationEvent.ToggleReminder -> {
                _uiState.update { it.copy(isReminderEnabled = event.enabled) }
            }
            ZakatCalculationEvent.SaveZakatRecord -> {
                saveRecord()
            }
        }
    }

    private fun saveRecord() {
        val currentState = _uiState.value
        val entityId = UUID.randomUUID().toString()
        val entity = ZakatEntity(
            id = entityId,
            date = System.currentTimeMillis(),
            money = currentState.cashAndBankVal,
            gold = currentState.goldVal,
            silver = currentState.silverVal,
            tradeAmount = currentState.businessAssetsVal,
            monthCost = currentState.dueExpensesVal,
            debt = currentState.debtsAndLoansVal,
            totalAsset = currentState.netAssets,
            zakatAmount = currentState.zakatPayable
        )
        viewModelScope.launch {
            zakatRepository.insertZakat(entity)
            if (currentState.isReminderEnabled) {
                // 354 lunar days in millis minus 3 days advance notice
                val hawlMillis = 354L * 24L * 60L * 60L * 1000L - (3L * 24L * 60L * 60L * 1000L)
                val triggerTimeMillis = System.currentTimeMillis() + hawlMillis
                zakatAlarmScheduler.scheduleZakatReminder(
                    zakatId = entityId,
                    triggerTimeMillis = triggerTimeMillis,
                    zakatAmount = currentState.zakatPayable
                )
            }
        }
    }
}
