package com.radio.station.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.radio.station.task.repository.RadioRepository

class RadioViewModelFactory (private val repository: RadioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RadioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
