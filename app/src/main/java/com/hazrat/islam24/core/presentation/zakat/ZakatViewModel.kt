package com.hazrat.islam24.core.presentation.zakat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.domain.model.zakat.NisabEntity
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import com.hazrat.islam24.util.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class ZakatViewModel @Inject constructor(
    private val repository: ZakatRepository,
    private val dataStorePreference: DataStorePreference
) : ViewModel() {


    private val _sortType = MutableStateFlow(dataStorePreference.getSortType())
    private val _state = MutableStateFlow(ZakatState(zakatEntity = emptyList()))

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _zakats = combine(_sortType) { sortType ->
        sortType
    }.flatMapLatest { (sortType) ->
        when (sortType) {
            DateType.DATE_ASC -> repository.getZakatDetailsByDateAsc()
            DateType.DATE_DESC -> repository.getZakatDetailsByDateDesc()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val zakatState = combine(_state, _zakats) { state, zakats ->
        state.copy(
            zakatEntity = zakats,
            sortType = state.sortType
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ZakatState(zakatEntity = emptyList())
    )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNisab().collectLatest {
                _state.update {
                    it.copy(
                        silverPrice = it.silverPrice
                    )
                }
            }
        }
    }

    // Function to recalculate total asset
    private fun recalculateTotalAsset() {
        val state = _state.value
        val totalAsset = calculateTotalAsset(
            handCash = state.money,
            bankCash = state.bankCash,
            gold = state.gold,
            silver = state.silver,
            tradeAmount = state.tradeAmount,
            monthCost = state.monthCost,
            debt = state.debt
        )

        _state.update {
            it.copy(
                totalAsset = totalAsset
            )
        }
    }

    fun event(event: ZakatEvent) {
        when (event) {
            is ZakatEvent.OnPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            silverPrice = event.price,
                            isSilverPriceValid = true
                        )
                    }
                    if (event.price.isNotBlank()) {
                        _state.update {
                            it.copy(
                                nisabAmount = calculateSilverPrice(event.price)
                            )
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isSilverPriceValid = false
                        )
                    }
                }
            }

            is ZakatEvent.SubmitNisab -> {
                viewModelScope.launch {
                    repository.insertNisab(
                        nisabEntity = NisabEntity(
                            silverPrice = event.price.toDouble()
                        )
                    )
                }
            }

            ZakatEvent.OpenBankCashDialog -> {
                _state.update {
                    it.copy(
                        isBankCashDialogOpen = !it.isBankCashDialogOpen
                    )
                }
            }

            ZakatEvent.OpenDebtDialog -> {
                _state.update {
                    it.copy(
                        isDebtDialogOpen = !it.isDebtDialogOpen
                    )
                }
            }

            ZakatEvent.OpenGoldDialog -> {
                _state.update {
                    it.copy(
                        isGoldDialogOpen = !it.isGoldDialogOpen
                    )
                }
            }

            ZakatEvent.OpenMoneyDialog -> {
                _state.update {
                    it.copy(
                        isMoneyDialogOpen = !it.isMoneyDialogOpen
                    )
                }

            }

            ZakatEvent.OpenMonthCostDialog -> {
                _state.update {
                    it.copy(
                        isMonthCostDialogOpen = !it.isMonthCostDialogOpen
                    )
                }
            }

            ZakatEvent.OpenSilverDialog -> {
                _state.update {
                    it.copy(
                        isSilverDialogOpen = !it.isSilverDialogOpen
                    )
                }
            }

            ZakatEvent.OpenTradeAmountDialog -> {
                _state.update {
                    it.copy(
                        isTradeAmountDialogOpen = !it.isTradeAmountDialogOpen
                    )
                }
            }

            is ZakatEvent.OnMoneyValueChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            money = event.price,
                            isHandCashPriceValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isHandCashPriceValid = false
                        )
                    }
                }
            }

            is ZakatEvent.OnMoneySubmit -> {
                _state.update {
                    it.copy(
                        money = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }


            is ZakatEvent.OnGoldPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            gold = event.price,
                            isGoldPriceValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isGoldPriceValid = false
                        )
                    }
                }
            }

            is ZakatEvent.OnSubmitGold -> {
                _state.update {
                    it.copy(
                        gold = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()

            }

            is ZakatEvent.OnSilverPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            silver = event.price,
                            isSilverValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isSilverValid = false
                        )
                    }
                }
            }

            is ZakatEvent.OnSubmitSilver -> {
                _state.update {
                    it.copy(
                        silver = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is ZakatEvent.OnSubmitTradeAmount -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            tradeAmount = event.price,
                            isTradeAmountValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isTradeAmountValid = false
                        )
                    }
                }
            }

            is ZakatEvent.OnTradeAmountPriceChange -> {
                _state.update {
                    it.copy(
                        tradeAmount = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is ZakatEvent.OnDebtPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            debt = event.price,
                            isDebtAmountValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isDebtAmountValid = false
                        )
                    }
                }
            }

            is ZakatEvent.OnSubmitDebt -> {
                _state.update {
                    it.copy(
                        debt = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is ZakatEvent.OnMonthCostPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            monthCost = event.price,
                            isMonthCostValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isMonthCostValid = false
                        )
                    }
                }
            }

            is ZakatEvent.OnSubmitMonthCost -> {
                _state.update {
                    it.copy(
                        monthCost = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()

            }

            ZakatEvent.ResetAllState -> {
                _state.update {
                    it.copy(
                        money = "0.0",
                        bankCash = "0.0",
                        gold = "0.0",
                        silver = "0.0",
                        tradeAmount = "0.0",
                        monthCost = "0.0",
                        debt = "0.0"
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is ZakatEvent.SaveZakat -> {
                viewModelScope.launch {
                    repository.insertZakat(
                        zakatEntity = ZakatEntity(
                            date = System.currentTimeMillis(),
                            totalAsset = _state.value.totalAsset,
                            money = _state.value.money.toDouble(),
                            gold = _state.value.gold.toDouble(),
                            silver = _state.value.silver.toDouble(),
                            tradeAmount = _state.value.tradeAmount.toDouble(),
                            monthCost = _state.value.monthCost.toDouble(),
                            debt = _state.value.debt.toDouble(),
                            zakatAmount = _state.value.zakatAmount,
                            )
                    )
                }
            }

            ZakatEvent.ToggleSortType -> {
                val newSortType =
                    if (_sortType.value == DateType.DATE_DESC) DateType.DATE_ASC else DateType.DATE_DESC
                _sortType.value = newSortType
                dataStorePreference.setSortType(newSortType)
            }

            is ZakatEvent.DeleteZakat -> {
                viewModelScope.launch {
                    repository.deleteZakat(event.zakatId)
                }
            }
        }
    }

    private fun zakatAmount() {
        val nisabAmount = _state.value.nisabAmount.toDoubleOrNull() ?: 0.0
        val totalAsset = _state.value.totalAsset

        if (totalAsset >= nisabAmount) {
            val zakatAmount = totalAsset / 40
            _state.update {
                it.copy(
                    zakatAmount = zakatAmount
                )
            }
        } else {
            _state.update {
                it.copy(
                    zakatAmount = 0.0
                )
            }
        }
    }


    private fun isValidNumber(input: String): Boolean {
        // Check if the input contains a comma
        return !input.contains(',')
    }
}

fun calculateSilverPrice(silverPrice: String?): String {
    val silverPriceOneTola = silverPrice?.toDoubleOrNull()?.times(11.6638)
    val silverNisabAmount = silverPriceOneTola?.times(52.50)
    val finalAmount = silverNisabAmount?.minus(1)
    val decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
    val decimalFormat = DecimalFormat("#.##", decimalFormatSymbols)
    return decimalFormat.format(finalAmount)
}


fun calculateTotalAsset(
    handCash: String = "",
    bankCash: String = "",
    gold: String = "",
    silver: String = "",
    tradeAmount: String = "",
    monthCost: String = "",
    debt: String = ""
): Double {
    val handCashValue = handCash.toDoubleOrNull() ?: 0.0
    val bankCashValue = bankCash.toDoubleOrNull() ?: 0.0
    val goldValue = gold.toDoubleOrNull() ?: 0.0
    val silverValue = silver.toDoubleOrNull() ?: 0.0
    val tradeAmountValue = tradeAmount.toDoubleOrNull() ?: 0.0
    val monthCostValue = monthCost.toDoubleOrNull() ?: 0.0
    val debtValue = debt.toDoubleOrNull() ?: 0.0

    return (handCashValue + bankCashValue + goldValue + silverValue + tradeAmountValue) -
            (monthCostValue + debtValue)
}

