package com.hazrat.islam24.presentation.namesofallah

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.domain.model.namesofallah.Data
import com.hazrat.islam24.domain.repository.NamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NamesViewModel @Inject constructor(
    private val repository: NamesRepository
) : ViewModel() {

    private val _names = MutableLiveData<List<Data>>()
    val names: LiveData<List<Data>> get() = _names


    init {
        viewModelScope.launch {
            _names.value  = repository.getAllNames()
        }

        names.observeForever { names ->
            names?.let {
                Log.d("Totalnames", "Names - ${names}")
            }
        }
    }

}