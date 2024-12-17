package com.radio.station.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radio.station.task.model.StationWithStatus
import com.radio.station.task.repository.RadioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RadioViewModel(private val repo:RadioRepository): ViewModel() {

    private val _stations = MutableStateFlow<List<StationWithStatus>>(emptyList())
    val stations: StateFlow<List<StationWithStatus>> = _stations

    // Loading state
    private val _isLoading = MutableStateFlow(false) // This will track loading state
    val isLoading: StateFlow<Boolean> = _isLoading


    // Fetch and process stations
    fun loadStations() {
        _isLoading.value = true // Start loading
        viewModelScope.launch {
            try {
                val stationsList = repo.getStations()
                // Batch the station availability requests for better performance
                val stationIds = stationsList.map { it.stationuuid }
                val availabilityMap = repo.getAvailability(stationIds)

                val stationsWithStatus = stationsList.map { station ->
                    StationWithStatus(
                        name = station.name,
                        status = availabilityMap[station.stationuuid] ?: "offline",
                        lang = station.language
                    )
                }

                _stations.value = stationsWithStatus
                /*val statusList = stationsList.map { station ->
                    val status = repo.getAvailability(station.stationuuid)
                    StationWithStatus(station.name, status, station.language)
                }
                _stations.value = statusList*/
            } catch (e: Exception) {
                // Handle error (e.g., show a message to the user)
            } finally {
                _isLoading.value = false // Stop loading
        }
        }
    }

    //working
    /* private val _stations = MutableLiveData<List<Pair<radioStation, String>>>()
    val stations: LiveData<List<Pair<radioStation, String>>> get() = _stations

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun loadStations() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val data = repo.getStations()
                _stations.postValue(data)
            } catch (e: Exception) {
                _stations.postValue(emptyList())
            } finally {
                _loading.postValue(false)
            }
        }
    }*/

    /*fun loadStations() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val data = repo.getStations()
                _stations.postValue(data)
            } catch (e: Exception) {
                _stations.postValue(emptyList())
            } finally {
                _loading.postValue(false)
            }
        }
    }*/


    /*
       //working
        fun loadStations() {
            _loading.value = true
            viewModelScope.launch {
                try {
                    val stationsList = repo.getStations()
                    val stationAvailabilityPairs = stationsList.map { station ->
                        // Fetch availability for each station
                        val availability = repo.getStationAvailability(station.stationuuid)
                        station to availability
                    }
                    _stations.postValue(stationAvailabilityPairs)
                } catch (e: Exception) {
                    _stations.postValue(emptyList()) // Handle errors gracefully
                }
                _loading.value = false
            }
        }
    */


}