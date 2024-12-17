package com.radio.station.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.radio.station.task.repository.RadioRepository
import com.radio.station.task.repository.RadioRepository_new

/*class RadioViewModelFactory (private val repository: RadioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RadioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/

class RadioViewModelFactory (private val repository: RadioRepository_new) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioViewModel_new::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RadioViewModel_new(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
