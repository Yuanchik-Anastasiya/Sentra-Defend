package com.yuanchik.sentradefend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yuanchik.sentradefend.repository.ScanHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val repository: ScanHistoryRepository) : ViewModel() {

    val scanResults = repository.allResults
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

class HistoryViewModelFactory(private val repository: ScanHistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
