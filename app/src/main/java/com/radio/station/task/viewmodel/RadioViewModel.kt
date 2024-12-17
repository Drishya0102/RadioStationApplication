package com.radio.station.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radio.station.task.model.radioStation
import com.radio.station.task.repository.RadioRepository
import kotlinx.coroutines.launch

class RadioViewModel(private val repo:RadioRepository): ViewModel() {
    //private val _stations = MutableLiveData<List<Pair<radioStation, String>>>()
    private val _stations = MutableLiveData<List<radioStation>>()
   // val stations: LiveData<List<Pair<radioStation, String>>> get() = _stations
   val stations: LiveData<List<radioStation>> = _stations

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun loadStations() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
            /*val data = repo.getStations()
            _stations.postValue(data)
            _loading.postValue(false)*/
            val stationlist = repo.getStations()
            val updatedStations  = stationlist.map { station ->
                //station to repo.getStationAvailability(station.stationuuid)
                station.copy(url = "Available")
            }
            _stations.postValue(updatedStations)
                _loading.postValue(false)
            } catch (e: Exception) {
                // Handle error if the station fetching fails
                _stations.postValue(emptyList()) // Optional: Return an empty list on error
            }
        }
    }

}