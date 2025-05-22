package com.yuanchik.sentradefend.presentation.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yuanchik.sentradefend.presentation.viewmodel.HistoryViewModel
import com.yuanchik.sentradefend.repository.ScanHistoryRepository

class HistoryViewModelFactory(
    private val repository: ScanHistoryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}