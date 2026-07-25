package com.hazrat.tasbih.domain.usecase

import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.tasbih.domain.repository.TasbihRepository
import kotlinx.coroutines.flow.Flow

class GetTasbihListUseCase(private val repository: TasbihRepository) {
    operator fun invoke(): Flow<List<Tasbih>> = repository.getAllTasbihs()
}

class GetTodayTotalDhikrUseCase(private val repository: TasbihRepository) {
    operator fun invoke(dateString: String): Flow<Int> = repository.getTodayTotalDhikr(dateString)
}

class IncrementTasbihUseCase(private val repository: TasbihRepository) {
    suspend operator fun invoke(id: Int, currentCount: Int, lifetimeCount: Int) {
        repository.incrementCount(id, currentCount, lifetimeCount)
    }
}

class UndoTasbihUseCase(private val repository: TasbihRepository) {
    suspend operator fun invoke(id: Int, currentCount: Int, lifetimeCount: Int) {
        repository.undoCount(id, currentCount, lifetimeCount)
    }
}

class ResetTasbihUseCase(private val repository: TasbihRepository) {
    suspend operator fun invoke(id: Int) {
        repository.resetCount(id)
    }
}

class ToggleFavoriteTasbihUseCase(private val repository: TasbihRepository) {
    suspend operator fun invoke(id: Int, isFavorite: Boolean) {
        repository.toggleFavorite(id, isFavorite)
    }
}

class AddCustomTasbihUseCase(private val repository: TasbihRepository) {
    suspend operator fun invoke(tasbih: Tasbih): Long {
        return repository.addCustomTasbih(tasbih)
    }
}
