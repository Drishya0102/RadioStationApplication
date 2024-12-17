package com.radio.station.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radio.station.task.model.radioStation
import com.radio.station.task.repository.LazyRadioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LazyRadioViewModel(private val repository: LazyRadioRepository) : ViewModel() {

    private val _stations = MutableStateFlow<List<radioStation>>(emptyList())
    val stations: StateFlow<List<radioStation>> get() = _stations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        // Load the full station list on initialization
        viewModelScope.launch {
            _isLoading.value = true
            repository.fetchAllStations()
            loadMoreStations()
            _isLoading.value = false
        }
    }

    fun loadMoreStations() {
        if (_isLoading.value || !repository.hasMoreStations()) return

        viewModelScope.launch {
            _isLoading.value = true
            val newStations = repository.getNextStations()
            _stations.value = _stations.value + newStations // Append to the existing list
            _isLoading.value = false
        }
    }
}
