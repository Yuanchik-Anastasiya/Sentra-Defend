package com.yuanchik.sentradefend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yuanchik.sentradefend.entity.ScanResultEntity
import com.yuanchik.sentradefend.repository.ScanHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ScanHistoryRepository) : ViewModel() {

    val scanResults: Flow<PagingData<ScanResultEntity>> =
        repository.getPagedResults()
            .cachedIn(viewModelScope)

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearScanResults()
        }
    }
}