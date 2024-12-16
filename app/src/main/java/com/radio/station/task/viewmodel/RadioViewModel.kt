package com.radio.station.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radio.station.task.model.radioStation
import com.radio.station.task.repository.RadioRepository
import kotlinx.coroutines.launch

class RadioViewModel(private val repo:RadioRepository): ViewModel() {
    private val _stations = MutableLiveData<List<Pair<radioStation, String>>>()
    val stations: LiveData<List<Pair<radioStation, String>>> get() = _stations
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun loadStations() {
        _loading.postValue(true)
        viewModelScope.launch {
            val data = repo.getStations()
            _stations.postValue(data)
            _loading.postValue(false)
        }
    }

}