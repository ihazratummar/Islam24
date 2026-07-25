package com.hazrat.tasbih.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.AppDataStore
import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.tasbih.domain.usecase.AddCustomTasbihUseCase
import com.hazrat.tasbih.domain.usecase.GetTasbihListUseCase
import com.hazrat.tasbih.domain.usecase.GetTodayTotalDhikrUseCase
import com.hazrat.tasbih.domain.usecase.IncrementTasbihUseCase
import com.hazrat.tasbih.domain.usecase.ResetTasbihUseCase
import com.hazrat.tasbih.domain.usecase.ToggleFavoriteTasbihUseCase
import com.hazrat.tasbih.domain.usecase.UndoTasbihUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TasbihUiState(
    val activeTasbih: Tasbih? = null,
    val tasbihList: List<Tasbih> = emptyList(),
    val selectedTarget: Int = 33,
    val todayTotalDhikr: Int = 0,
    val isSoundEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true,
    val showAddCustomDialog: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface TasbihIntent {
    data class SelectTasbih(val id: Int) : TasbihIntent
    object IncrementCount : TasbihIntent
    object UndoCount : TasbihIntent
    object ResetCount : TasbihIntent
    data class SetTargetLimit(val target: Int) : TasbihIntent
    object ToggleSound : TasbihIntent
    object ToggleVibration : TasbihIntent
    data class ToggleFavorite(val id: Int, val isFavorite: Boolean) : TasbihIntent
    data class AddCustomTasbih(
        val arabicText: String,
        val transliteration: String,
        val translatedName: String,
        val defaultTarget: Int
    ) : TasbihIntent
    data class SetAddCustomDialogVisible(val visible: Boolean) : TasbihIntent
}

sealed interface TasbihUiEffect {
    object PerformHaptic : TasbihUiEffect
    object PlaySoundClick : TasbihUiEffect
    data class TargetReached(val tasbihName: String, val count: Int) : TasbihUiEffect
}

class TasbihViewModel(
    private val getTasbihListUseCase: GetTasbihListUseCase,
    private val getTodayTotalDhikrUseCase: GetTodayTotalDhikrUseCase,
    private val incrementTasbihUseCase: IncrementTasbihUseCase,
    private val undoTasbihUseCase: UndoTasbihUseCase,
    private val resetTasbihUseCase: ResetTasbihUseCase,
    private val toggleFavoriteTasbihUseCase: ToggleFavoriteTasbihUseCase,
    private val addCustomTasbihUseCase: AddCustomTasbihUseCase,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasbihUiState())
    val uiState: StateFlow<TasbihUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<TasbihUiEffect>()
    val uiEffect: SharedFlow<TasbihUiEffect> = _uiEffect.asSharedFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        loadTasbihTargetFromDataStore()
        loadTasbihs()
        loadTodayTotal()
    }

    fun processIntent(intent: TasbihIntent) {
        when (intent) {
            is TasbihIntent.SelectTasbih -> selectTasbih(intent.id)
            is TasbihIntent.IncrementCount -> incrementCount()
            is TasbihIntent.UndoCount -> undoCount()
            is TasbihIntent.ResetCount -> resetCount()
            is TasbihIntent.SetTargetLimit -> setTargetLimit(intent.target)
            is TasbihIntent.ToggleSound -> toggleSound()
            is TasbihIntent.ToggleVibration -> toggleVibration()
            is TasbihIntent.ToggleFavorite -> toggleFavorite(intent.id, intent.isFavorite)
            is TasbihIntent.AddCustomTasbih -> addCustomTasbih(
                intent.arabicText,
                intent.transliteration,
                intent.translatedName,
                intent.defaultTarget
            )
            is TasbihIntent.SetAddCustomDialogVisible -> setAddCustomDialogVisible(intent.visible)
        }
    }

    private fun loadTasbihTargetFromDataStore() {
        viewModelScope.launch {
            appDataStore.tasbihTarget.collect { savedTarget ->
                _uiState.update { it.copy(selectedTarget = savedTarget) }
            }
        }
    }

    private fun loadTasbihs() {
        viewModelScope.launch {
            getTasbihListUseCase().collect { list ->
                _uiState.update { state ->
                    val active = state.activeTasbih?.let { current ->
                        list.find { it.id == current.id }
                    } ?: list.firstOrNull()

                    state.copy(
                        tasbihList = list,
                        activeTasbih = active,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadTodayTotal() {
        val todayStr = dateFormat.format(Date())
        viewModelScope.launch {
            getTodayTotalDhikrUseCase(todayStr).collect { total ->
                _uiState.update { it.copy(todayTotalDhikr = total) }
            }
        }
    }

    private fun selectTasbih(id: Int) {
        val selected = _uiState.value.tasbihList.find { it.id == id } ?: return
        _uiState.update {
            it.copy(
                activeTasbih = selected
            )
        }
    }

    private fun incrementCount() {
        val active = _uiState.value.activeTasbih ?: return
        val currentTarget = _uiState.value.selectedTarget

        viewModelScope.launch {
            // Check if current count has already hit target threshold, reset back to 0 on tap if so
            if (active.currentCount >= currentTarget) {
                resetTasbihUseCase(active.id)
                _uiEffect.emit(TasbihUiEffect.PerformHaptic)
                return@launch
            }

            incrementTasbihUseCase(
                id = active.id,
                currentCount = active.currentCount,
                lifetimeCount = active.totalLifetimeCount
            )

            if (_uiState.value.isVibrationEnabled) {
                _uiEffect.emit(TasbihUiEffect.PerformHaptic)
            }
            if (_uiState.value.isSoundEnabled) {
                _uiEffect.emit(TasbihUiEffect.PlaySoundClick)
            }

            val newCount = active.currentCount + 1
            if (newCount == currentTarget) {
                _uiEffect.emit(
                    TasbihUiEffect.TargetReached(
                        tasbihName = active.transliteration,
                        count = newCount
                    )
                )
            }
        }
    }

    private fun undoCount() {
        val active = _uiState.value.activeTasbih ?: return
        viewModelScope.launch {
            undoTasbihUseCase(
                id = active.id,
                currentCount = active.currentCount,
                lifetimeCount = active.totalLifetimeCount
            )
        }
    }

    private fun resetCount() {
        val active = _uiState.value.activeTasbih ?: return
        viewModelScope.launch {
            resetTasbihUseCase(active.id)
        }
    }

    private fun setTargetLimit(target: Int) {
        _uiState.update { it.copy(selectedTarget = target) }
        viewModelScope.launch {
            appDataStore.saveTasbihTarget(target)
            // Option to reset count to 0 when changing target limit
            _uiState.value.activeTasbih?.let { active ->
                if (active.currentCount >= target) {
                    resetTasbihUseCase(active.id)
                }
            }
        }
    }

    private fun toggleSound() {
        _uiState.update { it.copy(isSoundEnabled = !it.isSoundEnabled) }
    }

    private fun toggleVibration() {
        _uiState.update { it.copy(isVibrationEnabled = !it.isVibrationEnabled) }
    }

    private fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteTasbihUseCase(id, isFavorite)
        }
    }

    private fun addCustomTasbih(
        arabicText: String,
        transliteration: String,
        translatedName: String,
        defaultTarget: Int
    ) {
        viewModelScope.launch {
            val newTasbih = Tasbih(
                arabicText = arabicText.ifBlank { transliteration },
                transliteration = transliteration,
                translatedName = translatedName.ifBlank { transliteration },
                defaultTarget = defaultTarget,
                isCustom = true
            )
            addCustomTasbihUseCase(newTasbih)
            setAddCustomDialogVisible(false)
        }
    }

    private fun setAddCustomDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAddCustomDialog = visible) }
    }
}
