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

  /*  fun loadStations() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
            *//*val data = repo.getStations()
            _stations.postValue(data)
            _loading.postValue(false)*//*
            val stationlist = repo.getAllStations()
            val updatedStations  = stationlist.map { station ->
                try {
                    // Fetch the availability for the current station
                    val availability = repo.getStationAvailability(station.stationuuid)
                    // Get the most recent availability status, fallback to "Unknown" in case of error
                    if(availability.size==0)
                    {
                        station.copy(url = "unAvailable")
                    }
                    else {
                        val latestStatus = repo.getLatestAvailability(availability)
                        station.copy(url = latestStatus)
                    }
                } catch (e: Exception) {
                    // In case of error, set status to "Unknown"
                    station.copy(url = "unAvailable")
                }
                //val availability = repo.getStationAvailability(station.stationuuid)
                //station.copy(url = "Available")
            }
                if(updatedStations.isEmpty())
                {
                    _stations.postValue(emptyList()) // Post an empty list in case of error
                    _loading.postValue(false)
                }
                else {
                    // Post the updated list of stations to LiveData
                    _stations.postValue(updatedStations)
                    _loading.postValue(false)
                }

            //_stations.postValue(updatedStations)
            //_loading.postValue(false)
            } catch (e: Exception) {
                // Handle error if the station fetching fails
                _stations.postValue(emptyList()) // Optional: Return an empty list on error
            }
        }
    }*/


    fun loadStations() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val stationList = repo.getStationsWithAvailability()
                _stations.value = stationList
                _loading.postValue(false)
            } catch (e: Exception) {
                // Handle error if the station fetching fails
                _stations.postValue(emptyList()) // Optional: Return an empty list on error
                _loading.postValue(false)
            }
        }
    }
}