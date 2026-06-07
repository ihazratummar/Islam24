package com.hazrat.zakat.screen.zakat.screen.zakat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.DataStorePreference
import com.hazrat.model.DateType
import com.hazrat.zakat.domain.repository.ZakatRepository
import com.hazrat.zakat.domain.usecase.GetZakatDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-06-2025
 */


class ZakatScreenViewModel (
    private val repository: ZakatRepository,
    private val getZakatDetailsUseCase: GetZakatDetailsUseCase,
    private val dataStorePreference: DataStorePreference,
) : ViewModel() {

    private val _sortType = MutableStateFlow(dataStorePreference.getSortType())

    private val _zakatScreenState = MutableStateFlow(ZakatScreenState(zakatEntity = emptyList()))


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _zakats = combine(_sortType) { sortType ->
        sortType
    }.flatMapLatest { (sortType) ->
        when (sortType) {
            DateType.DATE_ASC -> repository.getZakatDetailsByDateAsc()
            DateType.DATE_DESC -> repository.getZakatDetailsByDateDesc()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val zakatState = combine(_zakatScreenState, _zakats) { state, zakats ->
        state.copy(
            zakatEntity = zakats,
            sortType = state.sortType
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ZakatScreenState(zakatEntity = emptyList())
    )


    fun getZakatDetails(id: String) {
        viewModelScope.launch {
            getZakatDetailsUseCase.invoke(id).collectLatest { zakat ->
                _zakatScreenState.update {
                    it.copy(
                        money = zakat.money.toString(),
                        gold = zakat.gold.toString(),
                        silver = zakat.silver.toString(),
                        tradeAmount = zakat.tradeAmount.toString(),
                        monthCost = zakat.monthCost.toString(),
                        debt = zakat.debt.toString(),
                        totalAsset = zakat.totalAsset,
                        zakatAmount = zakat.zakatAmount
                    )
                }
            }
        }
    }

    fun zakatScreenEvent(event: ZakatScreenEvent) {
        when (event) {
            is ZakatScreenEvent.DeleteZakat -> {

                viewModelScope.launch {
                    repository.deleteZakat(event.zakatId)
                }

            }

            ZakatScreenEvent.ToggleSortType -> {
                val newSortType =
                    if (_sortType.value == DateType.DATE_DESC) DateType.DATE_ASC else DateType.DATE_DESC
                _sortType.value = newSortType
                dataStorePreference.setSortType(newSortType)

            }

            ZakatScreenEvent.ToggleZakatDetailsPopUp -> {
                _zakatScreenState.update {
                    it.copy(
                        isZakatDetailsOpen = !it.isZakatDetailsOpen
                    )
                }
            }
        }
    }


}