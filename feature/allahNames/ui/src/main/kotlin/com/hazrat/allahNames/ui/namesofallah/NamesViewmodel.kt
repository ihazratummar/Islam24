package com.hazrat.allahNames.ui.namesofallah

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.allahNames.repository.NamesRepository
import com.hazrat.utils.network.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

class NamesViewmodel(
    private val namesRepository: NamesRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    /**
     * allah' names
     */
    private val _names = MutableStateFlow<List<NameOfAllahData>>(emptyList())
    val names = _names.asStateFlow()

    init {
        getNamesFromApi()
//        observeNamesFromDatabase()
    }

    private fun observeNamesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val allahNames = namesRepository.getAllahNamesFromDatabase()
            _names.value = allahNames
        }
    }

    private fun getNamesFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val networkStatus = connectivityObserver.observer().first()
            Log.d("NamesViewModel", "Network Status : $networkStatus")
            if (networkStatus == ConnectivityObserver.Status.Available) {
                val allahNames = namesRepository.getAllahNamesFromApi()
                _names.value = allahNames
            }
        }
    }
}