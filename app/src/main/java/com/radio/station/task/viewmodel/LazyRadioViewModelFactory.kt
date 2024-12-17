package com.radio.station.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.radio.station.task.api.RadioApiServices
import com.radio.station.task.repository.LazyRadioRepository

class LazyRadioViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LazyRadioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LazyRadioViewModel(LazyRadioRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
