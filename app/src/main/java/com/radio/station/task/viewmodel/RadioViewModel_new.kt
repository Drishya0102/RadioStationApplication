package com.radio.station.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radio.station.task.model.radioStation
import com.radio.station.task.repository.RadioRepository_new
import kotlinx.coroutines.launch

class RadioViewModel_new(private val radioRepository: RadioRepository_new) : ViewModel() {

    private val _stationsLiveData = MutableLiveData<List<radioStation>>()
    val stationsLiveData: LiveData<List<radioStation>> = _stationsLiveData


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    // Load stations and their availability
    fun loadStations() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                // Fetch stations and their availability from the repository
                val stationsWithStatus = radioRepository.getStationsWithAvailability()
                if(stationsWithStatus.isEmpty())
                {
                    _stationsLiveData.postValue(emptyList()) // Post an empty list in case of error
                    _loading.postValue(false)
                }
                else {
                    // Post the updated list of stations to LiveData
                    _stationsLiveData.postValue(stationsWithStatus)
                    _loading.postValue(false)
                }

            } catch (e: Exception) {
                // Handle error (e.g., network issues)
                _stationsLiveData.postValue(emptyList()) // Post an empty list in case of error
                _loading.postValue(false)
            }
        }
    }
}